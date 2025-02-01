package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.PlayerColor;

public class DefaultBoard extends Board {

    public DefaultBoard(int columns, int rows) {
        super(columns, rows);
    }

    @Override
    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        for (int x = 1; x <= getColumns(); x++) {
            for (int y = 1; y <= getRows(); y++) {
                Cell cell = cells[x][y];
                if (cell.isPlayable() && cell.getCurrentColor() != PlayerColor.NONE) {
                    appendCellState(sb, cell, x, y);
                }
            }
        }
        return sb.toString();
    }

    protected void appendCellState(StringBuilder sb, Cell cell, int x, int y) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(cell.getCurrentColor().name())
                .append(" ")
                .append(x)
                .append(" ")
                .append(y);
    }

    @Override
    public boolean isWinner(PlayerColor color) {
        for (int x = 1; x <= getColumns(); x++) {
            for (int y = 1; y <= getRows(); y++) {
                Cell cell = cells[x][y];
                if (cell.isPlayable() &&
                        cell.getCurrentColor() == color &&
                        cell.getCurrentColor() != cell.getTargetColor()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void initializeSpecialCells() {
        // Default board doesn't have special cell initialization
    }
}