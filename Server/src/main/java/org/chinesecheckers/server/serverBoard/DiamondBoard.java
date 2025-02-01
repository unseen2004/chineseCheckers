package org.chinesecheckers.server.serverBoard;

/**
 * Represents a diamond-shaped game board for Chinese Checkers.
 */
public class DiamondBoard extends DefaultBoard {

    /**
     * Constructs a DiamondBoard with the specified number of columns and rows.
     *
     * @param columns the number of columns
     * @param rows the number of rows
     */
    public DiamondBoard(int columns, int rows) {
        super(columns, rows);
    }
}