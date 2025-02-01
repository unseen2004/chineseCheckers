package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.PlayerColor;

public class DiamondBoardFactory extends DefaultBoardFactory {
    @Override
    public Board createBoard(int numberOfPlayers) throws GameException {
        validatePlayerCount(numberOfPlayers);
        Board board = new DiamondBoard(COLUMNS, ROWS);
        configurePlayers(board, numberOfPlayers);
        initializeCentralCells(board);
        return board;
    }

    @Override
    protected void setPlayer(Board board, PlayerColor color, int[][] positions, boolean active) throws GameException {
        for (int[] pos : positions) {
            Cell cell = createCell(color, active);
            cell.setNativeColor(PlayerColor.RED); // Set the edge color to red
            board.setCell(pos[0], pos[1], cell);
        }
    }
}