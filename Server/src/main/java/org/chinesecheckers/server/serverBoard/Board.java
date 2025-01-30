package org.chinesecheckers.server.serverBoard;


import org.chinesecheckers.common.Coord;
import org.chinesecheckers.common.PlayerColor;

import java.util.ArrayList;
import java.util.List;


public abstract class Board {
    int columns, rows;
    Cell[][] cells;

    public void addPiece(int x, int y, PlayerColor color) throws IllegalCellException {
        if (cells[x][y].isPlayable()) {
            cells[x][y].setCurrentColor(color);
        } else {
            throw new IllegalCellException();
        }
    }


    public void removePiece(int x, int y) throws IllegalCellException {
        if (cells[x][y].isPlayable()) {
            cells[x][y].setCurrentColor(PlayerColor.NONE);
        } else {
            throw new IllegalCellException();
        }
    }

    public PlayerColor getColor(int x, int y) throws IllegalCellException {
        if (cells[x][y].isPlayable()) {
            return cells[x][y].getCurrentColor();
        } else {
            throw new IllegalCellException();
        }
    }


    public abstract String getAsString();

    public abstract boolean isWinner(PlayerColor color);

    public Cell getField(int x, int y) {
        if (x < 1 || y < 1 || x > columns || y > rows) {
            return null;
        } else {
            return cells[x][y];
        }
    }

    public void setField(int x, int y, Cell f) {
        if (x < 1 || y < 1 || x > columns || y > rows) {
            throw new NullPointerException();
        } else {
            cells[x][y] = f;
        }
    }


    public List<Coord> getNearbyCells(int x, int y) {
        List<Coord> coords = new ArrayList<>();


        coords.add(new Coord(x - 1, y));
        coords.add(new Coord(x + 1, y));
        coords.add(new Coord((y % 2 == 0 ? x : x - 1), y - 1));
        coords.add(new Coord((y % 2 == 0 ? x + 1 : x), y - 1));
        coords.add(new Coord((y % 2 == 0 ? x : x - 1), y + 1));
        coords.add(new Coord((y % 2 == 0 ? x + 1 : x), y + 1));

        coords.add(new Coord(x - 2, y));
        coords.add(new Coord(x + 2, y));
        coords.add(new Coord(x - 1, y - 2));
        coords.add(new Coord(x + 1, y - 2));
        coords.add(new Coord(x - 1, y + 2));
        coords.add(new Coord(x + 1, y + 2));

        return coords;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
}