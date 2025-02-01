package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.Coord;
import org.chinesecheckers.common.PlayerColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board for Chinese Checkers.
 */
public abstract class Board {
    protected final Cell[][] cells;
    private final int columns;
    private final int rows;

    /**
     * Constructs a Board with the specified number of columns and rows.
     *
     * @param columns the number of columns
     * @param rows    the number of rows
     */
    public Board(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        this.cells = new Cell[columns + 1][rows + 1];
        initializeBoard();
    }

    /**
     * Initializes the board by creating playable cells.
     */
    private void initializeBoard() {
        for (int x = 1; x <= columns; x++) {
            for (int y = 1; y <= rows; y++) {
                cells[x][y] = new Cell();
            }
        }
    }

    /**
     * Adds a piece to the specified cell.
     *
     * @param x     the x-coordinate of the cell
     * @param y     the y-coordinate of the cell
     * @param color the color of the piece
     * @throws GameException if the cell is not playable or coordinates are invalid
     */
    public void addPiece(int x, int y, PlayerColor color) throws GameException {
        validateCoordinates(x, y);
        if (!cells[x][y].isPlayable()) {
            throw new GameException("Cell is not playable");
        }
        cells[x][y].setCurrentColor(color);
    }

    /**
     * Removes a piece from the specified cell.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @throws GameException if the cell is not playable or coordinates are invalid
     */
    public void removePiece(int x, int y) throws GameException {
        validateCoordinates(x, y);
        if (!cells[x][y].isPlayable()) {
            throw new GameException("Cell is not playable");
        }
        cells[x][y].setCurrentColor(PlayerColor.NONE);
    }

    /**
     * Gets the color of the piece at the specified cell.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @return the color of the piece
     * @throws GameException if coordinates are invalid
     */
    public PlayerColor getColor(int x, int y) throws GameException {
        validateCoordinates(x, y);
        return cells[x][y].getCurrentColor();
    }

    /**
     * Gets the cell at the specified coordinates.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @return the cell
     * @throws GameException if coordinates are invalid
     */
    public Cell getCell(int x, int y) throws GameException {
        validateCoordinates(x, y);
        return cells[x][y];
    }

    /**
     * Sets the cell at the specified coordinates.
     *
     * @param x    the x-coordinate of the cell
     * @param y    the y-coordinate of the cell
     * @param cell the cell to set
     * @throws GameException if coordinates are invalid
     */
    public void setCell(int x, int y, Cell cell) throws GameException {
        validateCoordinates(x, y);
        cells[x][y] = cell;
    }

    /**
     * Gets the cell at the specified coordinates without validation.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     * @return the cell, or null if coordinates are out of bounds
     */
    public Cell getField(int x, int y) {
        if (x < 1 || x > columns || y < 1 || y > rows) {
            return null;
        }
        return cells[x][y];
    }

    /**
     * Validates the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @throws GameException if coordinates are out of bounds
     */
    private void validateCoordinates(int x, int y) throws GameException {
        if (x < 1 || x > columns || y < 1 || y > rows) {
            throw new GameException("Invalid coordinates: " + x + ", " + y);
        }
    }

    /**
     * Gets the nearby cells for the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return a list of nearby coordinates
     */
    public List<Coord> getNearbyCells(int x, int y) {
        List<Coord> coords = new ArrayList<>();
        int[][] offsets = {{-1, 0}, {1, 0}, // Horizontal
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

    /**
     * Checks if the specified coordinates are valid.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the coordinates are valid, otherwise false
     */
    protected boolean isValidCoordinate(int x, int y) {
        return x >= 1 && x <= columns && y >= 1 && y <= rows;
    }

    /**
     * Gets the board as a string representation.
     *
     * @return the board as a string
     */
    public abstract String getAsString();

    /**
     * Checks if the specified player color is the winner.
     *
     * @param color the player color
     * @return true if the player color is the winner, otherwise false
     */
    public abstract boolean isWinner(PlayerColor color);

    /**
     * Gets the number of columns on the board.
     *
     * @return the number of columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Gets the number of rows on the board.
     *
     * @return the number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Initializes special cells on the board.
     */
    protected abstract void initializeSpecialCells();
}