package org.chinesecheckers.server.player;

import org.chinesecheckers.common.*;
import org.chinesecheckers.server.movement.GameHandler;
import org.chinesecheckers.server.movement.JumpVerificationCondition;
import org.chinesecheckers.server.movement.MoveValidationCondition;
import org.chinesecheckers.server.movement.PawnVerificationCondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Represents a bot player in the Chinese Checkers game.
 */
public class Bot extends Player {
    private final GameHandler m_gameHandler;
    private final JumpVerificationCondition m_jumpVerificationCondition;
    private final PawnVerificationCondition m_pawnVerificationCondition;
    private final MoveValidationCondition[] m_verifyConditions;
    private final int m_sleepDuration;
    List<Move> moves;
    boolean strayMode = false;
    private GridCoordinate m_target;
    private int m_skipCount;

    /**
     * Constructs a Bot with the specified player color, game handler, and sleep duration.
     *
     * @param color         the player color
     * @param gameHandler   the game handler
     * @param m_sleepDuration the sleep duration in milliseconds
     */
    public Bot(PlayerColor color, GameHandler gameHandler, int m_sleepDuration) {
        this.m_gameHandler = gameHandler;
        this.m_sleepDuration = m_sleepDuration;
        moves = new ArrayList<>();
        this.color = Colors.valueOf(color.name());
        setTarget();
        m_jumpVerificationCondition = new JumpVerificationCondition(0);
        m_pawnVerificationCondition = new PawnVerificationCondition();
        m_verifyConditions = new MoveValidationCondition[]{m_jumpVerificationCondition, m_pawnVerificationCondition};
    }

    /**
     * Sends a command to the bot.
     *
     * @param command the command to send
     */
    @Override
    public void sendCommand(String command) {
        ClientMessage[] responses = ClientMessage.getResponses(command);
        executeResponses(responses);
    }

    /**
     * Reads the response from the bot.
     *
     * @return the response as a string
     */
    @Override
    public String readResponse() {
        try {
            TimeUnit.MILLISECONDS.sleep(m_sleepDuration);
        } catch (InterruptedException ex) {
            throw new RuntimeException();
        }
        if (moves.isEmpty() || bestMove().getValue() <= 0) {
            m_skipCount++;
            if (m_skipCount > 1) {
                activateStrayMode();
            }
            return "SKIP";
        } else {
            m_skipCount = 0;
            return makeMoveCommand(bestMove());
        }
    }

    /**
     * Lists all possible moves for the bot.
     */
    void listMoves() {
        List<Coord> possibleMoves;
        moves.clear();
        for (int i = 1; i <= m_gameHandler.getBoard().getColumns(); i++) {
            for (int j = 1; j <= m_gameHandler.getBoard().getRows(); j++) {
                if (m_gameHandler.getBoard().getField(i, j).getCurrentColor() == PlayerColor.valueOf(color.name())) {
                    if (!strayMode || m_gameHandler.getBoard().getField(i, j).getTargetColor() != PlayerColor.valueOf(color.name())) {
                        m_pawnVerificationCondition.setCurrentXY(i, j);
                        possibleMoves = m_gameHandler.getPossibleMovesForCell(i, j, m_verifyConditions);
                        for (Coord temp : possibleMoves) {
                            moves.add(new Move(new GridCoordinate(i, j), new GridCoordinate(temp.getX(), temp.getY())));
                        }
                    }
                }
            }
        }
        System.out.println("Bot " + color + " listed moves: " + moves);
    }

    /**
     * Evaluates all possible moves for the bot.
     */
    void evaluateMoves() {
        if (moves.isEmpty()) {
            return;
        }
        Double prevDistance, currDistance;
        for (Move temp : moves) {
            prevDistance = calcDistance(temp.from, m_target);
            currDistance = calcDistance(temp.to, m_target);
            temp.setValue(prevDistance - currDistance);
        }
        moves.sort(Collections.reverseOrder());
        updateVerifyConditions(bestMove());
        System.out.println("Bot " + color + " evaluated moves: " + moves);
    }

    /**
     * Gets the best move for the bot.
     *
     * @return the best move
     */
    Move bestMove() {
        return !moves.isEmpty() ? moves.getFirst() : null;
    }

    /**
     * Calculates the distance between two coordinates.
     *
     * @param c1 the first coordinate
     * @param c2 the second coordinate
     * @return the distance between the coordinates
     */
    Double calcDistance(GridCoordinate c1, GridCoordinate c2) {
        return Math.sqrt(Math.pow(c1.getX() - c2.getX(), 2) + Math.pow(c1.getY() - c2.getY(), 2));
    }

    /**
     * Creates a move command string.
     *
     * @param move the move to create the command for
     * @return the move command string
     */
    String makeMoveCommand(Move move) {
        return "MOVE " + move.from.getX() + " " + move.from.getY() + " " + move.to.getX() + " " + move.to.getY();
    }

    /**
     * Updates the verification conditions based on the specified move.
     *
     * @param move the move to update the conditions for
     */
    void updateVerifyConditions(Move move) {
        resetVerifyConditions();
        m_pawnVerificationCondition.setCurrentXY(move.to.getX(), move.to.getY());
        m_jumpVerificationCondition.setStatus(m_gameHandler.verifyMove(move.from.getX(), move.from.getY(), move.to.getX(), move.to.getY(), m_verifyConditions));
        m_pawnVerificationCondition.setPreviousXY(move.to.getX(), move.to.getY());
    }

    /**
     * Resets the verification conditions.
     */
    void resetVerifyConditions() {
        m_jumpVerificationCondition.setStatus(0);
        m_pawnVerificationCondition.setCurrentXY(0, 0);
        m_pawnVerificationCondition.setPreviousXY(0, 0);
    }

    /**
     * Executes the specified responses.
     *
     * @param responses the responses to execute
     */
    void executeResponses(ClientMessage[] responses) {
        for (ClientMessage response : responses) {
            executeResponse(response);
        }
    }

    /**
     * Executes the specified response.
     *
     * @param response the response to execute
     */
    void executeResponse(ClientMessage response) {
        switch (response.getCode()) {
            case "YOU" -> {
                resetVerifyConditions();
                listMoves();
                evaluateMoves();
            }
            case "OK" -> {
                listMoves();
                evaluateMoves();
            }
        }
    }

    /**
     * Activates the stray mode for the bot.
     */
    void activateStrayMode() {
        strayMode = true;
        setStrayTarget();
    }

    /**
     * Sets the stray target for the bot.
     */
    void setStrayTarget() {
        for (int i = 1; i <= m_gameHandler.getBoard().getColumns(); i++) {
            for (int j = 1; j <= m_gameHandler.getBoard().getRows(); j++) {
                if (m_gameHandler.getBoard().getField(i, j).getTargetColor() == PlayerColor.valueOf(color.name()) && m_gameHandler.getBoard().getField(i, j).getCurrentColor() == PlayerColor.NONE) {
                    m_target = new GridCoordinate(i, j);
                }
            }
        }
    }

    /**
     * Sets the target for the bot based on its color.
     */
    void setTarget() {
        switch (color) {
            case RED -> m_target = new GridCoordinate(7, 1);
            case GREEN -> m_target = new GridCoordinate(7, 17);
            case BLUE -> m_target = new GridCoordinate(13, 13);
            case ORANGE -> m_target = new GridCoordinate(1, 5);
            case VIOLET -> m_target = new GridCoordinate(13, 5);
            case YELLOW -> m_target = new GridCoordinate(1, 13);
        }
    }

    /**
     * Represents a move in the Chinese Checkers game.
     */
    static class Move implements Comparable<Move> {
        GridCoordinate from, to;
        Double value;

        /**
         * Constructs a Move with the specified from and to coordinates.
         *
         * @param from the starting coordinate
         * @param to   the ending coordinate
         */
        Move(GridCoordinate from, GridCoordinate to) {
            this.from = from;
            this.to = to;
        }

        /**
         * Compares this move to another move based on their values.
         *
         * @param o the other move to compare to
         * @return a negative integer, zero, or a positive integer as this move is less than, equal to, or greater than the specified move
         */
        @Override
        public int compareTo(Move o) {
            return this.value.compareTo(o.value);
        }

        /**
         * Gets the value of the move.
         *
         * @return the value of the move
         */
        Double getValue() {
            return value;
        }

        /**
         * Sets the value of the move.
         *
         * @param value the value to set
         */
        void setValue(Double value) {
            this.value = value;
        }

        /**
         * Returns a string representation of the move.
         *
         * @return a string representation of the move
         */
        @Override
        public String toString() {
            return "Move{" + "from=" + from + ", to=" + to + ", value=" + value + '}';
        }
    }
}