package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.PlayerColor;

public class DiamondBoard extends DefaultBoard {

    public DiamondBoard(int columns, int rows) {
        super(columns, rows);
    }

    @Override
    protected void appendCellState(StringBuilder sb, Cell cell, int x, int y) {
        if (sb.length() > 0) sb.append(" ");
        if (cell.isKing()) {
            sb.append("KING_");
        }
        sb.append(cell.getCurrentColor().name())
                .append(" ")
                .append(x)
                .append(" ")
                .append(y);
    }

    @Override
    public boolean isWinner(PlayerColor color) {
        int kingCount = 0;
        int targetCount = 0;

        for (int x = 1; x <= getColumns(); x++) {
            for (int y = 1; y <= getRows(); y++) {
                Cell cell = cells[x][y];
                if (cell.isPlayable() && cell.getCurrentColor() == color) {
                    // Diamond variant requires all kings in target zone
                    if (!cell.isKing()) return false;
                    if (cell.getCurrentColor() != cell.getTargetColor()) {
                        return false;
                    }
                    targetCount++;
                }
            }
        }
        return targetCount >= 3; // Diamond-specific win condition
    }

    @Override
    protected void initializeSpecialCells() {
        // Diamond pattern initialization
        int centerX = getColumns() / 2 + 1;
        int centerY = getRows() / 2 + 1;

        for (int y = centerY - 2; y <= centerY + 2; y++) {
            int width = 2 - Math.abs(centerY - y);
            for (int x = centerX - width; x <= centerX + width; x++) {
                if (isValidCoordinate(x, y)) {
                    cells[x][y].setSpecial(true);
                }
            }
        }
    }
}