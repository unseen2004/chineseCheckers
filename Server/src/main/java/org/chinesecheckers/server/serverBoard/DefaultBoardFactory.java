package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.PlayerColor;

public class DefaultBoardFactory implements BoardFactory {
    protected static final int COLUMNS = 13;
    protected static final int ROWS = 17;

    @Override
    public Board createBoard(int numberOfPlayers) throws GameException {
        validatePlayerCount(numberOfPlayers);
        Board board = new DefaultBoard(COLUMNS, ROWS);
        configurePlayers(board, numberOfPlayers);
        initializeCentralCells(board);
        return board;
    }

    protected void configurePlayers(Board board, int numberOfPlayers) throws GameException {
        switch (numberOfPlayers) {
            case 1:
                setupSinglePlayer(board);
                break;
            case 2:
                setupTwoPlayers(board);
                break;
            case 3:
                setupThreePlayers(board);
                break;
            case 4:
                setupFourPlayers(board);
                break;
            case 6:
                setupSixPlayers(board);
                break;
        }
    }

    private void setupSinglePlayer(Board board) throws GameException {
        setPlayer(board, PlayerColor.RED, getRedPositions(), true);
    }

    private void setupTwoPlayers(Board board) throws GameException {
        setPlayer(board, PlayerColor.RED, getRedPositions(), true);
        setPlayer(board, PlayerColor.GREEN, getGreenPositions(), true);
    }

    private void setupThreePlayers(Board board) throws GameException {
        setPlayer(board, PlayerColor.RED, getRedPositions(), true);
        setPlayer(board, PlayerColor.BLUE, getBluePositions(), true);
        setPlayer(board, PlayerColor.YELLOW, getYellowPositions(), true);
    }

    private void setupFourPlayers(Board board) throws GameException {
        setPlayer(board, PlayerColor.YELLOW, getYellowPositions(), true);
        setPlayer(board, PlayerColor.ORANGE, getOrangePositions(), true);
        setPlayer(board, PlayerColor.VIOLET, getVioletPositions(), true);
        setPlayer(board, PlayerColor.BLUE, getBluePositions(), true);
    }

    private void setupSixPlayers(Board board) throws GameException {
        setPlayer(board, PlayerColor.GREEN, getGreenPositions(), true);
        setPlayer(board, PlayerColor.RED, getRedPositions(), true);
        setPlayer(board, PlayerColor.BLUE, getBluePositions(), true);
        setPlayer(board, PlayerColor.VIOLET, getVioletPositions(), true);
        setPlayer(board, PlayerColor.YELLOW, getYellowPositions(), true);
        setPlayer(board, PlayerColor.ORANGE, getOrangePositions(), true);
    }

    protected void setPlayer(Board board, PlayerColor color, int[][] positions, boolean active) throws GameException {
        for (int[] pos : positions) {
            Cell cell = createCell(color, active);
            board.setCell(pos[0], pos[1], cell);
        }
    }

    protected Cell createCell(PlayerColor color, boolean active) {
        return new Cell(
                active ? color : PlayerColor.NONE,
                color,
                getTargetColor(color),
                true,
                false
        );
    }

    protected PlayerColor getTargetColor(PlayerColor color) {
        switch (color) {
            case GREEN: return PlayerColor.RED;
            case RED: return PlayerColor.GREEN;
            case BLUE: return PlayerColor.ORANGE;
            case VIOLET: return PlayerColor.YELLOW;
            case YELLOW: return PlayerColor.VIOLET;
            case ORANGE: return PlayerColor.BLUE;
            default: return PlayerColor.NONE;
        }
    }

    protected void initializeCentralCells(Board board) throws GameException {
        int[] beginnings = {5, 4, 4, 3, 3, 3, 4, 4, 5};
        int[] endings = {9, 9, 10, 10, 11, 10, 10, 9, 9};

        for (int j = 5; j <= 13; j++) {
            int index = j - 5;
            for (int i = beginnings[index]; i <= endings[index]; i++) {
                board.setCell(i, j, new Cell(
                        PlayerColor.NONE,
                        PlayerColor.NONE,
                        PlayerColor.NONE,
                        true,
                        false
                ));
            }
        }
    }

    protected int[][] getGreenPositions() {
        return new int[][]{
                {7, 1}, {6, 2}, {7, 2}, {6, 3}, {8, 3},
                {7, 3}, {5, 4}, {6, 4}, {7, 4}, {8, 4}
        };
    }

    protected int[][] getRedPositions() {
        return new int[][]{
                {7, 17}, {6, 16}, {7, 16}, {6, 15}, {7, 15},
                {8, 15}, {5, 14}, {6, 14}, {7, 14}, {8, 14}
        };
    }

    protected int[][] getBluePositions() {
        return new int[][]{
                {1, 5}, {2, 5}, {3, 5}, {4, 5}, {1, 6},
                {2, 6}, {3, 6}, {2, 7}, {3, 7}, {2, 8}
        };
    }

    protected int[][] getVioletPositions() {
        return new int[][]{
                {2, 10}, {2, 11}, {3, 11}, {1, 12}, {2, 12},
                {3, 12}, {1, 13}, {2, 13}, {3, 13}, {4, 13}
        };
    }

    protected int[][] getYellowPositions() {
        return new int[][]{
                {10, 5}, {11, 5}, {12, 5}, {13, 5}, {10, 6},
                {11, 6}, {12, 6}, {11, 7}, {12, 7}, {11, 8}
        };
    }

    protected int[][] getOrangePositions() {
        return new int[][]{
                {11, 10}, {11, 11}, {12, 11}, {10, 12}, {11, 12},
                {12, 12}, {10, 13}, {11, 13}, {12, 13}, {13, 13}
        };
    }
}