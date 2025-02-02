package org.chinesecheckers.server.movement;

import org.chinesecheckers.common.Coord;
import org.chinesecheckers.common.PlayerColor;
import org.chinesecheckers.server.serverBoard.Board;
import org.chinesecheckers.server.serverBoard.BoardFactory;
import org.chinesecheckers.server.serverBoard.DefaultBoard;
import org.chinesecheckers.server.serverBoard.GameException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the game logic and interactions with the board and movement strategies.
 */
@Component
public class GameHandler {
    private MovementStrategy m_movementStrategy;
    private BoardFactory m_boardFactory;
    private Board m_board;

    /**
     * Constructs a GameHandler with the specified movement strategy and board factory.
     *
     * @param ms the movement strategy
     * @param bf the board factory
     * @throws GameException if an error occurs while creating the default board
     */
    public GameHandler(@Qualifier("defaultMovementStrategy") MovementStrategy ms, BoardFactory bf) throws GameException {
        m_board = new DefaultBoard(13, 17); // Provide the required parameters
        m_movementStrategy = ms;
        m_boardFactory = bf;
    }

    /**
     * Initializes the game handler with the specified board factory, movement strategy, and number of players.
     *
     * @param boardFactory     the board factory
     * @param movementStrategy the movement strategy
     * @param numberOfPlayers  the number of players
     * @throws GameException if an error occurs while creating the board
     */
    public void initialize(BoardFactory boardFactory, MovementStrategy movementStrategy, int numberOfPlayers) throws GameException {
        this.m_boardFactory = boardFactory;
        this.m_movementStrategy = movementStrategy;
        this.m_board = m_boardFactory.createBoard(numberOfPlayers);
    }

    /**
     * Gets the possible colors for the specified number of players.
     *
     * @param numberOfPlayers the number of players
     * @return an array of possible player colors
     */
    public PlayerColor[] getPossibleColorsForPlayers(int numberOfPlayers) {
        return switch (numberOfPlayers) {
            case 1 -> new PlayerColor[]{PlayerColor.RED};
            case 2 -> new PlayerColor[]{PlayerColor.RED, PlayerColor.GREEN};
            case 3 -> new PlayerColor[]{PlayerColor.RED, PlayerColor.BLUE, PlayerColor.YELLOW};
            case 4 -> new PlayerColor[]{PlayerColor.BLUE, PlayerColor.YELLOW, PlayerColor.VIOLET, PlayerColor.ORANGE};
            case 6 ->
                    new PlayerColor[]{PlayerColor.RED, PlayerColor.GREEN, PlayerColor.BLUE, PlayerColor.ORANGE, PlayerColor.YELLOW, PlayerColor.VIOLET};
            default -> throw new RuntimeException("Error number of players mismatch: " + numberOfPlayers);
        };
    }

    /**
     * Verifies the validity of a move on the board.
     *
     * @param x1                       the starting x-coordinate
     * @param y1                       the starting y-coordinate
     * @param x2                       the ending x-coordinate
     * @param y2                       the ending y-coordinate
     * @param moveValidationConditions the conditions to validate the move
     * @return the distance of the move if valid, otherwise 0
     */
    public int verifyMove(int x1, int y1, int x2, int y2, MoveValidationCondition[] moveValidationConditions) {
        return m_movementStrategy.verifyMove(m_board, x1, y1, x2, y2, moveValidationConditions);
    }

    /**
     * Makes a move on the board.
     *
     * @param x1 the starting x-coordinate
     * @param y1 the starting y-coordinate
     * @param x2 the ending x-coordinate
     * @param y2 the ending y-coordinate
     */
    public void makeMove(int x1, int y1, int x2, int y2) {
        m_board = m_movementStrategy.makeMove(m_board, x1, y1, x2, y2);
    }

    /**
     * Gets the color of the piece at the specified cell.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the color of the piece at the cell
     */
    public PlayerColor getColorAtCell(int x, int y) {
        try {
            return m_board.getColor(x, y);
        } catch (GameException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the possible moves for the piece at the specified cell.
     *
     * @param x                        the x-coordinate
     * @param y                        the y-coordinate
     * @param moveValidationConditions the conditions to validate the move
     * @return a list of possible moves
     */
    public List<Coord> getPossibleMovesForCell(int x, int y, MoveValidationCondition[] moveValidationConditions) {
        int result;
        List<Coord> possibleMoves = new ArrayList<>();
        List<Coord> nearbyCoords = m_board.getNearbyCells(x, y);
        for (Coord coord : nearbyCoords) {
            result = verifyMove(x, y, coord.getX(), coord.getY(), moveValidationConditions);
            if (result != 0) possibleMoves.add(coord);
        }

        return possibleMoves;
    }

    /**
     * Checks if the specified player color has won the game.
     *
     * @param color the player color
     * @return true if the player has won, otherwise false
     */
    public boolean isWinner(PlayerColor color) {
        return m_board.isWinner(color);
    }

    /**
     * Gets the board as a string representation.
     *
     * @return the board as a string
     */
    public String getBoardAsString() {
        return m_board.getAsString();
    }

    /**
     * Gets the current game board.
     *
     * @return the game board
     */
    public Board getBoard() {
        return m_board;
    }
}