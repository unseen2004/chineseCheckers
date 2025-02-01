package org.chinesecheckers.server.serverBoard;

/**
 * Exception thrown when an illegal operation is performed on a cell.
 */
public class IllegalCellException extends Exception {

    /**
     * Constructs a new IllegalCellException with no detail message.
     */
    public IllegalCellException() {
        super();
    }

    /**
     * Constructs a new IllegalCellException with the specified detail message.
     *
     * @param message the detail message
     */
    public IllegalCellException(String message) {
        super(message);
    }
}