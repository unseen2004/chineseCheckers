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
        switch (pos[0] + "," + pos[1]) {
            case "6,2":
            case "12,15":
            case "12,13":
            case "8,15":
            case "2,13":
            case "1,6":
                Cell cell = createCell(color.YELLOW, active);
                cell.setKing(true);
                board.setCell(pos[0], pos[1], cell);
                break;
            default:
                Cell defaultCell = createCell(color, active);
                board.setCell(pos[0], pos[1], defaultCell);
                break;
        }
    }
}
}