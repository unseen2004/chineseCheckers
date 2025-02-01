package org.chinesecheckers.client.board;

import org.chinesecheckers.common.Colors;
import org.chinesecheckers.common.GridCoordinate;

import java.util.List;

/**
 * Represents the game board for Chinese Checkers.
 */
public class Board {
    private final List<Cell> m_cells;
    private Cell m_selectedCell;

    /**
     * Constructs a Board with the specified list of cells.
     *
     * @param cells the list of cells on the board
     */
    public Board(List<Cell> cells) {
        this.m_cells = cells;
    }

    /**
     * Removes all pieces from the board by setting their color to NONE.
     */
    public void removeAllPieces() {
        for (Cell cell : m_cells) {
            if (!cell.isDisabled()) cell.setM_color(Colors.NONE);
        }
    }

    /**
     * Gets the coordinates of the currently selected cell.
     *
     * @return the coordinates of the selected cell, or null if no cell is selected
     */
    public GridCoordinate getCoordinateOfSelectedCell() {
        if (m_selectedCell != null) {
            int x = m_selectedCell.getM_x();
            int y = m_selectedCell.getM_y();
            return new GridCoordinate(x, y);
        } else {
            return null;
        }
    }

    /**
     * Selects the cell at the specified coordinates.
     *
     * @param x the x-coordinate of the cell to select
     * @param y the y-coordinate of the cell to select
     */
    public void selectCell(int x, int y) {
        deselectAllCells();
        m_selectedCell = getCell(x, y);

        if (m_selectedCell != null) {
            m_selectedCell.setSelected(true);
        }
    }

    /**
     * Deselects all cells and unmarks all jump targets.
     */
    public void deselectCells() {
        deselectAllCells();
        unmarkAllJumpTargets();
        m_selectedCell = null;
    }

    /**
     * Deselects all cells on the board.
     */
    private void deselectAllCells() {
        for (Cell cell : m_cells)
            cell.setSelected(false);
    }

    /**
     * Unmarks all cells as possible jump targets.
     */
    public void unmarkAllJumpTargets() {
        for (Cell cell : m_cells) {
            cell.markAsPossibleJumps(false);
        }
    }

    /**
     * Adds a piece of the specified color to the cell at the given coordinates.
     *
     * @param x     the x-coordinate of the cell
     * @param y     the y-coordinate of the cell
     * @param color the color of the piece to add
     */
    public void addPiece(int x, int y, Colors color) {
        Cell cell = getCell(x, y);
        if (cell != null) cell.setM_color(color);
    }

    /**
     * Checks if the cell at the specified coordinates is empty.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @return true if the cell is empty, false otherwise
     */
    public boolean isCellEmpty(int x, int y) {
        Cell cell = getCell(x, y);
        if (cell != null) return cell.getM_color() == Colors.NONE;
        else return true;
    }

    /**
     * Gets the color of the piece in the cell at the specified coordinates.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @return the color of the piece in the cell, or NONE if the cell is empty
     */
    public Colors getColor(int x, int y) {
        Cell cell = getCell(x, y);
        if (cell != null) return cell.getM_color();
        else return Colors.NONE;
    }

    /**
     * Marks the cell at the specified coordinates as a possible jump target.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
    public void markFieldAsPossibleJumps(int x, int y) {
        Cell cell = getCell(x, y);
        if (cell != null) {
            cell.markAsPossibleJumps(true);
        }
    }

    /**
     * Gets the cell at the specified coordinates.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @return the cell at the specified coordinates, or null if no cell exists at those coordinates
     */
    private Cell getCell(int x, int y) {
        for (Cell cell : m_cells) {
            if (cell.getM_x() == x && cell.getM_y() == y) return cell;
        }
        return null;
    }
}