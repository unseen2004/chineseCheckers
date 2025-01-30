package org.chinesecheckers.server.movement;

import org.chinesecheckers.common.Coord;
import org.chinesecheckers.common.PlayerColor;
import org.chinesecheckers.server.serverBoard.Board;
import org.chinesecheckers.server.serverBoard.BoardFactory;
import org.chinesecheckers.server.serverBoard.DefaultBoard;
import org.chinesecheckers.server.serverBoard.IllegalCellException;

import java.util.ArrayList;
import java.util.List;


public class GameHandler {
    private final MovementStrategy m_movementStrategy;
    private final BoardFactory m_boardFactory;
    private Board m_board;

    public GameHandler(MovementStrategy ms, BoardFactory bf) {
        m_board = new DefaultBoard();
        m_movementStrategy = ms;
        m_boardFactory = bf;
    }

    public void initializeBoard(int numberOfPlayers) {
        m_board = m_boardFactory.createBoard(numberOfPlayers);
    }

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


    public int verifyMove(int x1, int y1, int x2, int y2, MoveValidationCondition[] moveValidationConditions) {
        return m_movementStrategy.verifyMove(m_board, x1, y1, x2, y2, moveValidationConditions);
    }

    public void makeMove(int x1, int y1, int x2, int y2) {
        m_board = m_movementStrategy.makeMove(m_board, x1, y1, x2, y2);
    }

    public PlayerColor getColorAtCell(int x, int y) {
        try {
            return m_board.getColor(x, y);
        } catch (IllegalCellException ufexc) {
            return null;
        }
    }

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


    public boolean isWinner(PlayerColor color) {
        return m_board.isWinner(color);
    }


    public String getBoardAsString() {
        return m_board.getAsString();
    }

    public Board getBoard() {
        return m_board;
    }

}