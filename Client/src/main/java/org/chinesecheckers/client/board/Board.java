package org.chinesecheckers.client.board;

import org.chinesecheckers.common.Colors;
import org.chinesecheckers.common.GridCoordinate;

import java.util.List;


public class Board {
    private final List<Cell> m_cells;
    private Cell m_selectedCell;

    public Board(List<Cell> cells) {
        this.m_cells = cells;
    }

    public void removeAllPieces() {
        for (Cell cell : m_cells) {
            if (!cell.isDisabled()) cell.setColor(Colors.NONE);
        }
    }

    public GridCoordinate getCoordinateOfSelectedCell() {
        if (m_selectedCell != null) {
            int x = m_selectedCell.getX();
            int y = m_selectedCell.getY();
            return new GridCoordinate(x, y);
        } else {
            return null;
        }
    }


    public void selectCell(int x, int y) {
        deselectAllCells();
        m_selectedCell = getCell(x, y);

        if (m_selectedCell != null) {
            m_selectedCell.setSelected(true);
        }
    }

    public void deselectCells() {
        deselectAllCells();
        unmarkAllJumpTargets();
        m_selectedCell = null;
    }

    private void deselectAllCells() {
        for (Cell cell : m_cells)
            cell.setSelected(false);
    }

    public void unmarkAllJumpTargets() {
        for (Cell cell : m_cells) {
            cell.markAsPossibleJumps(false);
        }
    }

    public void addPiece(int x, int y, Colors color) {
        Cell cell = getCell(x, y);
        if (cell != null) cell.setColor(color);
    }

    public boolean isCellEmpty(int x, int y) {
        Cell cell = getCell(x, y);
        if (cell != null) return cell.getColor() == Colors.NONE;
        else return true;
    }


    public Colors getColor(int x, int y) {
        Cell cell = getCell(x, y);
        if (cell != null) return cell.getColor();
        else return Colors.NONE;
    }


    public void markFieldAsPossibleJumps(int x, int y) {
        Cell cell = getCell(x, y);
        if (cell != null) {
            cell.markAsPossibleJumps(true);
        }
    }


    private Cell getCell(int x, int y) {
        for (Cell cell : m_cells) {
            if (cell.getX() == x && cell.getY() == y) return cell;
        }
        return null;
    }
}