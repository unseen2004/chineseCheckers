// Board.java
package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.Coord;
import org.chinesecheckers.common.PlayerColor;
import java.util.ArrayList;
import java.util.List;

public abstract class Board {
    private final int columns;
    private final int rows;
    protected final Cell[][] cells;

    public Board(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        this.cells = new Cell[columns + 1][rows + 1];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int x = 1; x <= columns; x++) {
            for (int y = 1; y <= rows; y++) {
                cells[x][y] = new Cell();
            }
        }
    }

    public void addPiece(int x, int y, PlayerColor color) throws GameException {
        validateCoordinates(x, y);
        if (!cells[x][y].isPlayable()) {
            throw new GameException("Cell is not playable");
        }
        cells[x][y].setCurrentColor(color);
    }

    public void removePiece(int x, int y) throws GameException {
        validateCoordinates(x, y);
        if (!cells[x][y].isPlayable()) {
            throw new GameException("Cell is not playable");
        }
        cells[x][y].setCurrentColor(PlayerColor.NONE);
    }

    public PlayerColor getColor(int x, int y) throws GameException {
        validateCoordinates(x, y);
        return cells[x][y].getCurrentColor();
    }

    public Cell getCell(int x, int y) throws GameException {
        validateCoordinates(x, y);
        return cells[x][y];
    }

    public void setCell(int x, int y, Cell cell) throws GameException {
        validateCoordinates(x, y);
        cells[x][y] = cell;
    }

    public Cell getField(int x, int y) {
        if (x < 1 || x > columns || y < 1 || y > rows) {
            return null;
        }
        return cells[x][y];
    }

    private void validateCoordinates(int x, int y) throws GameException {
        if (x < 1 || x > columns || y < 1 || y > rows) {
            throw new GameException("Invalid coordinates: " + x + ", " + y);
        }
    }

    public List<Coord> getNearbyCells(int x, int y) {
        List<Coord> coords = new ArrayList<>();
        int[][] offsets = {
                {-1, 0}, {1, 0}, // Horizontal
                {y % 2 == 0 ? 0 : -1, -1}, {y % 2 == 0 ? 1 : 0, -1}, // Upper
                {y % 2 == 0 ? 0 : -1, 1}, {y % 2 == 0 ? 1 : 0, 1}, // Lower
                {-2, 0}, {2, 0}, // Long horizontal
                {-1, -2}, {1, -2}, // Upper jumps
                {-1, 2}, {1, 2} // Lower jumps
        };

        for (int[] offset : offsets) {
            int newX = x + offset[0];
            int newY = y + offset[1];
            if (isValidCoordinate(newX, newY)) {
                coords.add(new Coord(newX, newY));
            }
        }
        return coords;
    }

    protected boolean isValidCoordinate(int x, int y) {
        return x >= 1 && x <= columns && y >= 1 && y <= rows;
    }

    public abstract String getAsString();
    public abstract boolean isWinner(PlayerColor color);

    public int getColumns() { return columns; }
    public int getRows() { return rows; }

    protected abstract void initializeSpecialCells();
}