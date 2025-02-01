package org.chinesecheckers.client.main;

import javafx.application.Platform;
import org.chinesecheckers.client.board.Board;
import org.chinesecheckers.common.ClientMessage;
import org.chinesecheckers.common.Colors;
import org.chinesecheckers.common.GridCoordinate;

import java.util.function.Consumer;

/**
 * Represents a player in the Chinese Checkers game.
 */
class Player {
    private final Board m_board;
    private final ServerConnectionHandler m_serverConnectionHandler;
    private final Consumer<Boolean> m_blockGUI;
    private final Consumer<String> m_printSuccess;
    private final Consumer<String> m_printAlert;
    private final Consumer<String> m_printError;
    private Colors m_color;
    private boolean m_isTurn;
    private boolean m_isFinished;

    /**
     * Constructs a Player with the specified parameters.
     *
     * @param cm             the server connection handler
     * @param m_board        the game board
     * @param m_blockGUI     the consumer to block/unblock the GUI
     * @param m_printSuccess the consumer to print success messages
     * @param m_printAlert   the consumer to print alert messages
     * @param m_printError   the consumer to print error messages
     */
    Player(ServerConnectionHandler cm, Board m_board, Consumer<Boolean> m_blockGUI, Consumer<String> m_printSuccess, Consumer<String> m_printAlert, Consumer<String> m_printError) {
        this.m_serverConnectionHandler = cm;
        this.m_board = m_board;
        this.m_color = Colors.RED;
        this.m_blockGUI = m_blockGUI;
        this.m_printSuccess = m_printSuccess;
        this.m_printAlert = m_printAlert;
        this.m_printError = m_printError;
    }

    /**
     * Starts the match for the player.
     */
    void startMatch() {
        m_isTurn = false;
        m_isFinished = false;
        m_printSuccess.accept("Connected, waiting.");
        blockGUIandReadResponses();
    }

    /**
     * Handles the click on a cell.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
    void handleClickOnCell(int x, int y) {
        boolean isOwnCell = !m_board.isCellEmpty(x, y) && m_board.getColor(x, y).equals(m_color);
        if (isOwnCell) {
            selectOwnCell(x, y);
        } else {
            clickCell(x, y);
        }
    }

    /**
     * Selects the player's own cell.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
    private void selectOwnCell(int x, int y) {
        GridCoordinate selected = m_board.getCoordinateOfSelectedCell();
        boolean clickedSelectedCell = selected != null && selected.getX() == x && selected.getY() == y;

        if (clickedSelectedCell) {
            m_board.deselectCells();
            sendSkipRequest();
        } else {
            m_board.selectCell(x, y);
            askServerForMoves(x, y);
        }

        blockGUIandReadResponses();
    }

    /**
     * Handles the click on a cell that is not the player's own.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
    private void clickCell(int x, int y) {
        boolean clickedEmptyCell = m_board.isCellEmpty(x, y);
        boolean isSomeCellSelected = m_board.getCoordinateOfSelectedCell() != null;
        if (clickedEmptyCell && isSomeCellSelected) {
            sendMoveRequest(x, y);
        } else {
            m_board.deselectCells();
        }
    }

    /**
     * Sends a move request to the server.
     *
     * @param x the x-coordinate of the destination cell
     * @param y the y-coordinate of the destination cell
     */
    private void sendMoveRequest(int x, int y) {
        moveSelected(x, y);
        m_board.deselectCells();
        blockGUIandReadResponses();
    }

    /**
     * Moves the selected piece to the specified destination.
     *
     * @param destX the x-coordinate of the destination cell
     * @param destY the y-coordinate of the destination cell
     */
    private void moveSelected(int destX, int destY) {
        GridCoordinate selected = m_board.getCoordinateOfSelectedCell();
        int fromX = selected.getX();
        int fromY = selected.getY();
        sendMoveRequest(fromX, fromY, destX, destY);
    }

    /**
     * Sends a move request to the server with the specified coordinates.
     *
     * @param fromX the x-coordinate of the source cell
     * @param fromY the y-coordinate of the source cell
     * @param toX   the x-coordinate of the destination cell
     * @param toY   the y-coordinate of the destination cell
     */
    private void sendMoveRequest(int fromX, int fromY, int toX, int toY) {
        String msg = "MOVE " + fromX + " " + fromY + " " + toX + " " + toY;
        m_serverConnectionHandler.writeLine(msg);
    }

    /**
     * Sends a skip request to the server.
     */
    private void sendSkipRequest() {
        m_serverConnectionHandler.writeLine("SKIP");
    }

    /**
     * Blocks the GUI and starts reading responses from the server.
     */
    private void blockGUIandReadResponses() {
        m_blockGUI.accept(true);
        Thread thread = new Thread(this::readResponses);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Reads responses from the server.
     */
    private void readResponses() {
        do {
            try {
                waitForResponseAndExecute();
            } catch (Exception e) {
                if (!m_isFinished) m_printError.accept(e.getMessage());
                return;
            }
        } while (!m_isTurn);
        m_blockGUI.accept(false);
    }

    /**
     * Waits for a response from the server and executes it.
     *
     * @throws Exception if an error occurs while reading the response
     */
    private void waitForResponseAndExecute() throws Exception {
        String line = m_serverConnectionHandler.readLine();
        ClientMessage[] response = ClientMessage.getResponses(line);
        executeResponses(response);
    }

    /**
     * Executes the responses from the server.
     *
     * @param responses the responses from the server
     */
    private void executeResponses(ClientMessage[] responses) {
        for (ClientMessage clientMessage : responses) {
            executeResponse(clientMessage);
        }
    }

    /**
     * Executes a single response from the server.
     *
     * @param clientMessage the response from the server
     */
    private void executeResponse(ClientMessage clientMessage) {
        switch (clientMessage.getCode()) {
            case "WELCOME" -> {
                executeWelcomeResponse(clientMessage);
                m_isTurn = false;
                m_printAlert.accept("You are: " + m_color.toString() + " wait for your turn");
            }
            case "YOU" -> {
                m_printSuccess.accept("(" + m_color.toString() + ") Your turn");
                m_isTurn = true;
            }
            case "BOARD" -> Platform.runLater(() -> loadBoard(clientMessage));
            case "CLUES" -> Platform.runLater(() -> loadMoves(clientMessage));
            case "END" -> {
                m_printSuccess.accept("You are " + clientMessage.getNumbers()[0]);
                m_isTurn = false;
                m_isFinished = true;
            }
            case "OK" -> {
            }
            case "NOK" -> {
            }
            case "STOP" -> {
                m_printAlert.accept("Wait");
                m_isTurn = false;
            }
            case "ERROR" -> executeErrorResponse(clientMessage);
        }
    }

    /**
     * Executes an error response from the server.
     *
     * @param clientMessage the error response from the server
     */
    private void executeErrorResponse(ClientMessage clientMessage) {
        String errorMessage = String.join(" ", clientMessage.getWords());
        throw new RuntimeException("End" + errorMessage);
    }

    /**
     * Asks the server for possible moves from the specified cell.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
    private void askServerForMoves(int x, int y) {
        m_serverConnectionHandler.writeLine("CLUES " + x + " " + y);
    }

    /**
     * Executes the welcome response from the server.
     *
     * @param clientMessage the welcome response from the server
     */
    private void executeWelcomeResponse(ClientMessage clientMessage) {
        boolean incorrectWelcomeMessage = !clientMessage.getCode().equals("WELCOME") || clientMessage.getWords().length != 1;
        if (incorrectWelcomeMessage) {
            System.err.println("Error");
        }
        readPlayerColorFromResponse(clientMessage);
    }

    /**
     * Reads the player's color from the welcome response.
     *
     * @param welcomeClientMessage the welcome response from the server
     */
    private void readPlayerColorFromResponse(ClientMessage welcomeClientMessage) {
        m_color = Colors.valueOf(welcomeClientMessage.getWords()[0]);
    }

    /**
     * Loads the board state from the server response.
     *
     * @param clientMessage the server response containing the board state
     */
    private void loadBoard(ClientMessage clientMessage) {
        if (clientMessage.getCode().equals("BOARD")) {
            m_board.removeAllPieces();
            int coordNum = 0;
            for (String word : clientMessage.getWords()) {
                Colors color = Colors.valueOf(word);
                int x = clientMessage.getNumbers()[coordNum];
                int y = clientMessage.getNumbers()[coordNum + 1];
                m_board.addPiece(x, y, color);
                coordNum += 2;
            }
        }
    }

    /**
     * Loads the possible moves from the server response.
     *
     * @param clientMessage the server response containing the possible moves
     */
    private void loadMoves(ClientMessage clientMessage) {
        if (clientMessage.getCode().equals("CLUES")) {
            m_board.unmarkAllJumpTargets();
            int[] numbers = clientMessage.getNumbers();
            for (int n : numbers) {
                System.out.print(n + " ");
            }
            System.out.println();
            for (int i = 0; i < numbers.length - 1; i += 2) {
                int x = numbers[i];
                int y = numbers[i + 1];
                m_board.markFieldAsPossibleJumps(x, y);
            }
        }
    }
}