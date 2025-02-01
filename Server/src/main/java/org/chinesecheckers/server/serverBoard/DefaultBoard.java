package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.PlayerColor;

/**
 * Represents the default game board for Chinese Checkers.
 */
public class DefaultBoard extends Board {

    /**
     * Constructs a DefaultBoard with the specified number of columns and rows.
     *
     * @param columns the number of columns
     * @param rows    the number of rows
     */
    public DefaultBoard(int columns, int rows) {
        super(columns, rows);
    }

    /**
     * Gets the board as a string representation.
     *
     * @return the board as a string
     */
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

    /**
     * Appends the state of a cell to the string builder.
     *
     * @param sb   the string builder
     * @param cell the cell
     * @param x    the x-coordinate of the cell
     * @param y    the y-coordinate of the cell
     */
    protected void appendCellState(StringBuilder sb, Cell cell, int x, int y) {
        if (sb.length() > 0) sb.append(" ");
        sb.append(cell.getCurrentColor().name()).append(" ").append(x).append(" ").append(y);
    }

    /**
     * Checks if the specified player color is the winner.
     *
     * @param color the player color
     * @return true if the player color is the winner, otherwise false
     */
    @Override
    public boolean isWinner(PlayerColor color) {
        for (int x = 1; x <= getColumns(); x++) {
            for (int y = 1; y <= getRows(); y++) {
                Cell cell = cells[x][y];
                if (cell.isPlayable() && cell.getCurrentColor() == color && cell.getCurrentColor() != cell.getTargetColor()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Initializes special cells on the board.
     * Default board doesn't have special cell initialization.
     */
    @Override
    protected void initializeSpecialCells() {
        // Default board doesn't have special cell initialization
    }
}