package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.PlayerColor;

public class DiamondBoardFactory extends DefaultBoardFactory {
    @Override
    public Board createBoard(int numberOfPlayers) throws GameException {
        validatePlayerCount(numberOfPlayers);
        Board board = new DiamondBoard(getColumns(), getRows());
        configurePlayers(board, numberOfPlayers);
        initializeCentralCells(board);
        return board;
    }

    @Override
    protected void setPlayer(Board board, PlayerColor color, int[][] positions, boolean active) throws GameException {
        for (int i = 0; i < positions.length; i++) {
            int[] pos = positions[i];
            boolean isKing = (i == 0);
            Cell cell = new Cell(
                    active ? color : PlayerColor.NONE,
                    color,
                    getTargetColor(color),
                    true,
                    isKing
            );
            board.setCell(pos[0], pos[1], cell);
        }
    }

    @Override
    protected PlayerColor getTargetColor(PlayerColor color) {
        // Diamond-specific target colors
        return switch (color) {
            case RED -> PlayerColor.VIOLET;
            case GREEN -> PlayerColor.ORANGE;
            case BLUE -> PlayerColor.YELLOW;
            case VIOLET -> PlayerColor.RED;
            case YELLOW -> PlayerColor.BLUE;
            case ORANGE -> PlayerColor.GREEN;
            default -> PlayerColor.NONE;
        };
    }

    protected int getColumns() {
        return 13; // or any other value specific to DiamondBoard
    }

    protected int getRows() {
        return 17; // or any other value specific to DiamondBoard
    }
}