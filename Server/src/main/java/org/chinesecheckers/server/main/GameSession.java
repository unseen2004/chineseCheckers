// GameSession.java
package org.chinesecheckers.server.main;

import org.chinesecheckers.server.model.Game;
import org.chinesecheckers.server.model.Move;
import org.chinesecheckers.server.repository.GameRepository;
import org.chinesecheckers.server.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.chinesecheckers.common.Coord;
import org.chinesecheckers.common.PlayerColor;
import org.chinesecheckers.common.Response;
import org.chinesecheckers.common.ResponseInterpreter;
import org.chinesecheckers.server.movement.*;
import org.chinesecheckers.server.player.Bot;
import org.chinesecheckers.server.player.Player;
import org.chinesecheckers.server.player.PlayerEntity;
import org.chinesecheckers.server.player.PlayerLeftException;
import org.chinesecheckers.server.serverBoard.BoardFactory;
import org.chinesecheckers.server.serverBoard.DefaultBoardFactory;
import org.chinesecheckers.server.serverBoard.DiamondBoardFactory;
import org.chinesecheckers.server.movement.GameHandler;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Component
class GameSession {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MoveRepository moveRepository;

    private Game currentGame;

    @Autowired
    private GameHandler m_gameHandler;
    private PlayerColor[] m_availableColors;
    private List<Player> m_players;
    private JumpVerificationCondition m_jumpStatus;
    private PawnVerificationCondition m_previousPawn;
    private MoveValidationCondition[] m_conditions;
    private int m_moveDistance = 0;

    private boolean m_turnFinished;
    private int m_place;

    @Autowired
    GameSession(GameHandler gameHandler) {
        this.m_gameHandler = gameHandler;
        this.m_players = new ArrayList<>();
        this.m_turnFinished = true;
        this.m_place = 1;
        this.m_availableColors = new PlayerColor[0]; // Initialize with an empty array
    }

    void initialize(List<Socket> playerSockets, String gameMode, int numberOfBots, int sleepDuration) throws Exception {
        m_players.clear(); // Clear the players list at the beginning

        BoardFactory boardFactory;
        MovementStrategy movementStrategy;

        if ("diamond".equalsIgnoreCase(gameMode)) {
            boardFactory = new DiamondBoardFactory();
            movementStrategy = new DiamondMovementStrategy();
        } else {
            boardFactory = new DefaultBoardFactory();
            movementStrategy = new DefaultMovementStrategy();
        }

        int numberOfPlayers = playerSockets.size();
        int total = numberOfPlayers + numberOfBots;

        m_gameHandler.initialize(boardFactory, movementStrategy, total);
        m_availableColors = m_gameHandler.getPossibleColorsForPlayers(total);

        addPlayers(playerSockets);
        addBots(numberOfBots, playerSockets.size(), sleepDuration);

        // Initialize and save the game
        currentGame = new Game();
        currentGame.setMode(gameMode);
        gameRepository.save(currentGame);
    }

    private void addPlayers(List<Socket> playerSockets) throws Exception {
        int numberOfPlayers = playerSockets.size();

        for (int i = 0; i < numberOfPlayers; i++) {
            m_players.add(new PlayerEntity(playerSockets.get(i), m_availableColors[i]));
        }
    }

    private void addBots(int numberOfBots, int colorIndex, int sleepDuration) {
        for (int i = colorIndex; i < numberOfBots + colorIndex; i++) {
            System.out.print("Added bot");
            m_players.add(new Bot(m_availableColors[i], m_gameHandler, sleepDuration));
        }
    }

    void start() {
        try {
            sendWelcomeToPlayers();
            sendStartBoardToPlayers();
            play();
        } catch (PlayerLeftException e) {
            endMatchWithError("Someone left the game");
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
            e.printStackTrace();
            endMatchWithError("Connection lost");
        }
    }

    private void sendWelcomeToPlayers() {
        for (int i = 0; i < m_players.size(); i++) {
            m_players.get(i).sendCommand("WELCOME " + m_availableColors[i]);
        }
    }

    private void sendStartBoardToPlayers() {
        String command = "START@BOARD " + m_gameHandler.getBoardAsString();
        sendToAll(command);
    }

    private void play() throws Exception {
        int indexOfPlayerHavingTurn = 0;
        do {
            playTurnForPlayer(m_players.get(indexOfPlayerHavingTurn));
            indexOfPlayerHavingTurn = getNextPlayerIndex(indexOfPlayerHavingTurn);
        } while (!allPlayersFinished());
        System.out.println("Match ended");
    }

    private void playTurnForPlayer(Player player) throws PlayerLeftException {
        setDefaultSettingsForNewTurn();
        player.sendCommand("YOU");
        System.out.println("Starts: " + player.getColor().toString());
        readResponsesAndExecute(player);
    }

    private void setDefaultSettingsForNewTurn() {
        m_jumpStatus = new JumpVerificationCondition(0);
        m_previousPawn = new PawnVerificationCondition();
        m_conditions = new MoveValidationCondition[]{m_jumpStatus, m_previousPawn};

        m_turnFinished = false;
    }

    private void readResponsesAndExecute(Player player) throws PlayerLeftException {
        do {
            Response response = readResponseFromPlayer(player);
            executeResponse(player, response);
        } while (!m_turnFinished);
    }

    private Response readResponseFromPlayer(Player player) throws PlayerLeftException {
        String line = player.readResponse();
        if (line == null) {
            throw new PlayerLeftException("Player " + player.getColor().toString() + " left the game.");
        }
        Response[] responses = ResponseInterpreter.getResponses(line);
        if (responses.length != 1) {
            System.err.println("Error " + player.getColor().toString());
        }
        return responses[0];
    }

    private void executeResponse(Player player, Response response) {
        String responseType = response.getCode();
        switch (responseType) {
            case "SKIP":
                sendStopAndFinishTurn(player);
                break;
            case "CLUES":
                executeMovesResponse(player, response);
                break;
            case "MOVE":
                executeMoveResponse(player, response);
                break;
            default:
                sendNokAndPrintIncorrectResponse(player);
        }
    }

    private void sendStopAndFinishTurn(Player player) {
        player.sendCommand("STOP");
        m_turnFinished = true;
    }

    private void executeMovesResponse(Player player, Response response) {
        boolean correctCluesResponse = response.getCode().equals("CLUES") && response.getNumbers().length == 2;
        if (correctCluesResponse) {
            int x = response.getNumbers()[0];
            int y = response.getNumbers()[1];
            sendMoves(player, x, y);
        } else {
            sendNokAndPrintIncorrectResponse(player);
        }
    }

    private void sendMoves(Player player, int x, int y) {
        ((PawnVerificationCondition) m_conditions[1]).setCurrentXY(x, y);

        List<Coord> possibleMoves = m_gameHandler.getPossibleMovesForCell(x, y, m_conditions);
        String command = getMovesCommand(possibleMoves);

        player.sendCommand(command);
    }

    private void executeMoveResponse(Player player, Response response) {
        boolean correctMoveResponse = response.getCode().equals("MOVE") && response.getNumbers().length == 4;
        if (correctMoveResponse) {
            int fromX = response.getNumbers()[0];
            int fromY = response.getNumbers()[1];
            int toX = response.getNumbers()[2];
            int toY = response.getNumbers()[3];
            verifyMoveAndExecute(player, fromX, fromY, toX, toY);
        } else {
            sendNokAndPrintIncorrectResponse(player);
        }
    }

    private void verifyMoveAndExecute(Player player, int fromX, int fromY, int toX, int toY) {
        m_previousPawn.setCurrentXY(fromX, fromY);

        m_moveDistance = m_gameHandler.verifyMove(fromX, fromY, toX, toY, m_conditions);
        if (m_moveDistance == 0) {
            player.sendCommand("NOK");
        } else {
            makeMove(player, fromX, fromY, toX, toY);
        }
    }

    private void makeMove(Player player, int fromX, int fromY, int toX, int toY) {
        m_jumpStatus.setStatus(m_moveDistance);
        m_previousPawn.setPreviousXY(toX, toY);
        m_gameHandler.makeMove(fromX, fromY, toX, toY);

        Move move = new Move();
        move.setFromX(fromX);
        move.setFromY(fromY);
        move.setToX(toX);
        move.setToY(toY);
        move.setGame(currentGame);
        moveRepository.save(move);

        boolean playerFinished = m_gameHandler.isWinner(player.getColor());

        if (playerFinished) {
            makePlayerFinishedAndSendResponses(player);
            m_turnFinished = true;
        } else if (m_moveDistance == 1) {
            sendResponsesAfterShortJump(player);
            m_turnFinished = true;
        } else {
            sendResponsesAfterLongJump(player);
        }
    }

    private void makePlayerFinishedAndSendResponses(Player player) {
        System.out.println("Player " + player.getColor().toString() + " ended on " + m_place);
        player.sendCommand("END " + m_place);
        player.setFinished(true);
        m_place++;
        sendToAll("BOARD " + m_gameHandler.getBoardAsString());
    }

    private void sendResponsesAfterShortJump(Player player) {
        player.sendCommand("OK@STOP");
        sendToAll("BOARD " + m_gameHandler.getBoardAsString());
    }

    private void sendResponsesAfterLongJump(Player player) {
        String command = "OK@BOARD " + m_gameHandler.getBoardAsString();
        player.sendCommand(command);

        String msg = "BOARD " + m_gameHandler.getBoardAsString();
        sendToAllExceptOne(msg, player);
    }

    private void sendNokAndPrintIncorrectResponse(Player player) {
        System.err.println("Error " + player.getColor().toString());
        player.sendCommand("NOK");
    }

    private boolean allPlayersFinished() {
        for (Player player : m_players) {
            if (!player.isFinished()) return false;
        }
        return true;
    }

    private int getNextPlayerIndex(int activePlayer) {
        int nextPlayer = activePlayer;
        boolean nextPlayerFinished;
        do {
            nextPlayer++;
            if (nextPlayer >= m_players.size()) {
                nextPlayer = 0;
            }

            nextPlayerFinished = m_players.get(nextPlayer).isFinished();

            if (nextPlayerFinished) {
                int howManyFinished = 0;
                for (Player p : m_players) {
                    if (p.isFinished()) howManyFinished++;
                }
                if (howManyFinished == m_players.size()) {
                    return -1;
                }
            }
        } while (nextPlayerFinished);

        return nextPlayer;
    }

    private void sendToAll(String command) {
        for (Player player : m_players) {
            player.sendCommand(command);
        }
    }

    private void sendToAllExceptOne(String command, Player excluded) {
        for (Player player : m_players) {
            if (player != excluded) player.sendCommand(command);
        }
    }

    private String getMovesCommand(List<Coord> possibleMoves) {
        StringBuilder sb = new StringBuilder();
        for (Coord c : possibleMoves) {
            if (!sb.toString().isEmpty()) {
                sb.append(" ");
            }
            sb.append(c.getX()).append(" ").append(c.getY());
        }
        return "CLUES " + sb;
    }

    private void endMatchWithError(String message) {
        for (Player player : m_players) {
            try {
                player.sendCommand("ERROR " + message);
            } catch (Exception ignored) {
            }
        }

        m_players.clear();
        m_players = null;
    }

    // New method to replay moves
    void replayMoves(Long gameId) {
        List<Move> moves = moveRepository.findMovesByGameId(gameId);
        for (Move move : moves) {
            m_gameHandler.makeMove(move.getFromX(), move.getFromY(), move.getToX(), move.getToY());
            sendToAll("BOARD " + m_gameHandler.getBoardAsString());
            try {
                Thread.sleep(1000); // Add delay to simulate real-time replay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}