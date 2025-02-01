package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.PlayerColor;

/**
 * Factory class for creating the default game board for Chinese Checkers.
 */
public class DefaultBoardFactory implements BoardFactory {
    protected static final int COLUMNS = 13;
    protected static final int ROWS = 17;

    /**
     * Creates a game board for the specified number of players.
     *
     * @param numberOfPlayers the number of players
     * @return the created game board
     * @throws GameException if the number of players is invalid
     */
    @Override
    public Board createBoard(int numberOfPlayers) throws GameException {
        validatePlayerCount(numberOfPlayers);
        Board board = new DefaultBoard(COLUMNS, ROWS);
        configurePlayers(board, numberOfPlayers);
        initializeCentralCells(board);
        return board;
    }

    /**
     * Configures the players on the board based on the number of players.
     *
     * @param board           the game board
     * @param numberOfPlayers the number of players
     * @throws GameException if the number of players is invalid
     */
    protected void configurePlayers(Board board, int numberOfPlayers) throws GameException {
        switch (numberOfPlayers) {
            case 1 -> setupSinglePlayer(board);
            case 2 -> setupTwoPlayers(board);
            case 3 -> setupThreePlayers(board);
            case 4 -> setupFourPlayers(board);
            case 6 -> setupSixPlayers(board);
        }
    }

    /**
     * Sets up the board for a single player.
     *
     * @param board the game board
     * @throws GameException if an error occurs while setting up the player
     */
    private void setupSinglePlayer(Board board) throws GameException {
        setPlayer(board, PlayerColor.RED, getRedPositions(), true);
    }

    /**
     * Sets up the board for two players.
     *
     * @param board the game board
     * @throws GameException if an error occurs while setting up the players
     */
    private void setupTwoPlayers(Board board) throws GameException {
        setPlayer(board, PlayerColor.RED, getRedPositions(), true);
        setPlayer(board, PlayerColor.GREEN, getGreenPositions(), true);
    }

    /**
     * Sets up the board for three players.
     *
     * @param board the game board
     * @throws GameException if an error occurs while setting up the players
     */
    private void setupThreePlayers(Board board) throws GameException {
        setPlayer(board, PlayerColor.RED, getRedPositions(), true);
        setPlayer(board, PlayerColor.BLUE, getBluePositions(), true);
        setPlayer(board, PlayerColor.YELLOW, getYellowPositions(), true);
    }

    /**
     * Sets up the board for four players.
     *
     * @param board the game board
     * @throws GameException if an error occurs while setting up the players
     */
    private void setupFourPlayers(Board board) throws GameException {
        setPlayer(board, PlayerColor.YELLOW, getYellowPositions(), true);
        setPlayer(board, PlayerColor.ORANGE, getOrangePositions(), true);
        setPlayer(board, PlayerColor.VIOLET, getVioletPositions(), true);
        setPlayer(board, PlayerColor.BLUE, getBluePositions(), true);
    }

    /**
     * Sets up the board for six players.
     *
     * @param board the game board
     * @throws GameException if an error occurs while setting up the players
     */
    private void setupSixPlayers(Board board) throws GameException {
        setPlayer(board, PlayerColor.GREEN, getGreenPositions(), true);
        setPlayer(board, PlayerColor.RED, getRedPositions(), true);
        setPlayer(board, PlayerColor.BLUE, getBluePositions(), true);
        setPlayer(board, PlayerColor.VIOLET, getVioletPositions(), true);
        setPlayer(board, PlayerColor.YELLOW, getYellowPositions(), true);
        setPlayer(board, PlayerColor.ORANGE, getOrangePositions(), true);
    }

    /**
     * Sets the player on the board with the specified color and positions.
     *
     * @param board     the game board
     * @param color     the player color
     * @param positions the positions of the player's pieces
     * @param active    whether the player is active
     * @throws GameException if an error occurs while setting the player
     */
    protected void setPlayer(Board board, PlayerColor color, int[][] positions, boolean active) throws GameException {
        for (int[] pos : positions) {
            Cell cell = createCell(color, active);
            board.setCell(pos[0], pos[1], cell);
        }
    }

    /**
     * Creates a cell with the specified color and active status.
     *
     * @param color  the player color
     * @param active whether the cell is active
     * @return the created cell
     */
    protected Cell createCell(PlayerColor color, boolean active) {
        return new Cell(active ? color : PlayerColor.NONE, color, getTargetColor(color), true, false);
    }

    /**
     * Gets the target color for the specified player color.
     *
     * @param color the player color
     * @return the target color
     */
    protected PlayerColor getTargetColor(PlayerColor color) {
        switch (color) {
            case GREEN -> {
                return PlayerColor.RED;
            }
            case RED -> {
                return PlayerColor.GREEN;
            }
            case BLUE -> {
                return PlayerColor.ORANGE;
            }
            case VIOLET -> {
                return PlayerColor.YELLOW;
            }
            case YELLOW -> {
                return PlayerColor.VIOLET;
            }
            case ORANGE -> {
                return PlayerColor.BLUE;
            }
            default -> {
                return PlayerColor.NONE;
            }
        }
    }

    /**
     * Initializes the central cells on the board.
     *
     * @param board the game board
     * @throws GameException if an error occurs while initializing the cells
     */
    protected void initializeCentralCells(Board board) throws GameException {
        int[] beginnings = {5, 4, 4, 3, 3, 3, 4, 4, 5};
        int[] endings = {9, 9, 10, 10, 11, 10, 10, 9, 9};

        for (int j = 5; j <= 13; j++) {
            int index = j - 5;
            for (int i = beginnings[index]; i <= endings[index]; i++) {
                board.setCell(i, j, new Cell(PlayerColor.NONE, PlayerColor.NONE, PlayerColor.NONE, true, false));
            }
        }
    }

    /**
     * Gets the positions for the green player.
     *
     * @return the positions for the green player
     */
    protected int[][] getGreenPositions() {
        return new int[][]{{7, 1}, {6, 2}, {7, 2}, {6, 3}, {8, 3}, {7, 3}, {5, 4}, {6, 4}, {7, 4}, {8, 4}};
    }

    /**
     * Gets the positions for the red player.
     *
     * @return the positions for the red player
     */
    protected int[][] getRedPositions() {
        return new int[][]{{7, 17}, {6, 16}, {7, 16}, {6, 15}, {7, 15}, {8, 15}, {5, 14}, {6, 14}, {7, 14}, {8, 14}};
    }

    /**
     * Gets the positions for the blue player.
     *
     * @return the positions for the blue player
     */
    protected int[][] getBluePositions() {
        return new int[][]{{1, 5}, {2, 5}, {3, 5}, {4, 5}, {1, 6}, {2, 6}, {3, 6}, {2, 7}, {3, 7}, {2, 8}};
    }

    /**
     * Gets the positions for the violet player.
     *
     * @return the positions for the violet player
     */
    protected int[][] getVioletPositions() {
        return new int[][]{{2, 10}, {2, 11}, {3, 11}, {1, 12}, {2, 12}, {3, 12}, {1, 13}, {2, 13}, {3, 13}, {4, 13}};
    }

    /**
     * Gets the positions for the yellow player.
     *
     * @return the positions for the yellow player
     */
    protected int[][] getYellowPositions() {
        return new int[][]{{10, 5}, {11, 5}, {12, 5}, {13, 5}, {10, 6}, {11, 6}, {12, 6}, {11, 7}, {12, 7}, {11, 8}};
    }

    /**
     * Gets the positions for the orange player.
     *
     * @return the positions for the orange player
     */
    protected int[][] getOrangePositions() {
        return new int[][]{{11, 10}, {11, 11}, {12, 11}, {10, 12}, {11, 12}, {12, 12}, {10, 13}, {11, 13}, {12, 13}, {13, 13}};
    }
}