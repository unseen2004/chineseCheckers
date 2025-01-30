package org.chinesecheckers.client.main;

import javafx.application.Platform;
import org.chinesecheckers.client.board.Board;
import org.chinesecheckers.common.ClientMessage;
import org.chinesecheckers.common.Colors;
import org.chinesecheckers.common.GridCoordinate;

import java.util.function.Consumer;

class Player {
    private final Board m_board;
    private Colors m_color;
    private final ServerConnectionHandler m_serverConnectionHandler;
    private final Consumer<Boolean> m_blockGUI;
    private final Consumer<String> m_printSuccess;
    private final Consumer<String> m_printAlert;
    private final Consumer<String> m_printError;

    private boolean m_isTurn;
    private boolean m_isFinished;

    Player(ServerConnectionHandler cm, Board m_board, Consumer<Boolean> m_blockGUI, Consumer<String> m_printSuccess, Consumer<String> m_printAlert, Consumer<String> m_printError) {
        this.m_serverConnectionHandler = cm;
        this.m_board = m_board;
        this.m_color = Colors.RED;
        this.m_blockGUI = m_blockGUI;

        this.m_printSuccess = m_printSuccess;
        this.m_printAlert = m_printAlert;
        this.m_printError = m_printError;
    }

    void startMatch() {
        m_isTurn = false;
        m_isFinished = false;
        m_printSuccess.accept("Connected, waiting.");

        blockGUIandReadResponses();
    }

    void handleClickOnCell(int x, int y) {
        boolean isOwnCell = !m_board.isCellEmpty(x, y) && m_board.getColor(x, y).equals(m_color);
        if (isOwnCell) {
            selectOwnCell(x, y);
        } else {
            clickCell(x, y);
        }
    }

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

    private void clickCell(int x, int y) {
        boolean clickedEmptyCell = m_board.isCellEmpty(x, y);
        boolean isSomeCellSelected = m_board.getCoordinateOfSelectedCell() != null;
        if (clickedEmptyCell && isSomeCellSelected) {
            sendMoveRequest(x, y);
        } else {
            m_board.deselectCells();
        }
    }

    private void sendMoveRequest(int x, int y) {
        moveSelected(x, y);
        m_board.deselectCells();

        blockGUIandReadResponses();
    }

    private void moveSelected(int destX, int destY) {
        GridCoordinate selected = m_board.getCoordinateOfSelectedCell();

        int fromX = selected.getX();
        int fromY = selected.getY();

        sendMoveRequest(fromX, fromY, destX, destY);
    }

    private void sendMoveRequest(int fromX, int fromY, int toX, int toY) {
        String msg = "MOVE " + fromX + " " + fromY + " " + toX + " " + toY;
        m_serverConnectionHandler.writeLine(msg);
    }

    private void sendSkipRequest() {
        m_serverConnectionHandler.writeLine("SKIP");
    }

    private void blockGUIandReadResponses() {
        m_blockGUI.accept(true);
        Thread thread = new Thread(this::readResponses);
        thread.setDaemon(true);
        thread.start();
    }

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

    private void waitForResponseAndExecute() throws Exception {
        String line = m_serverConnectionHandler.readLine();

        ClientMessage[] response = ClientMessage.getResponses(line);

        executeResponses(response);
    }

    private void executeResponses(ClientMessage[] respons) {
        for (ClientMessage clientMessage : respons) {
            executeResponse(clientMessage);
        }
    }

    private void executeResponse(ClientMessage clientMessage) {
        switch (clientMessage.getCode()) {
            case "WELCOME":
                executeWelcomeResponse(clientMessage);
                m_isTurn = false;
                m_printAlert.accept("You are: " + m_color.toString() + " wait for your turn");
                break;
            case "YOU":
                m_printSuccess.accept("(" + m_color.toString() + ") Your turn");
                m_isTurn = true;
                break;
            case "BOARD":
                Platform.runLater(() -> loadBoard(clientMessage));
                break;
            case "CLUES":
                Platform.runLater(() -> loadMoves(clientMessage));
                break;
            case "END":
                m_printSuccess.accept("You are " + clientMessage.getNumbers()[0]);
                m_isTurn = false;
                m_isFinished = true;
                break;
            case "OK":
                break;
            case "NOK":
                break;
            case "STOP":
                m_printAlert.accept("Wait");
                m_isTurn = false;
                break;
            case "ERROR":
                executeErrorResponse(clientMessage);
        }
    }

    private void executeErrorResponse(ClientMessage clientMessage) {
        String errorMessage = String.join(" ", clientMessage.getWords());
        throw new RuntimeException("End" + errorMessage);
    }

    private void askServerForMoves(int x, int y) {
        m_serverConnectionHandler.writeLine("CLUES " + x + " " + y);
    }

    private void executeWelcomeResponse(ClientMessage clientMessage) {
        boolean incorrectWelcomeMessage = !clientMessage.getCode().equals("WELCOME") || clientMessage.getWords().length != 1;

        if (incorrectWelcomeMessage) {
            System.err.println("Error");
        }

        readPlayerColorFromResponse(clientMessage);
    }

    private void readPlayerColorFromResponse(ClientMessage welcomeClientMessage) {
        m_color = Colors.valueOf(welcomeClientMessage.getWords()[0]);
    }

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