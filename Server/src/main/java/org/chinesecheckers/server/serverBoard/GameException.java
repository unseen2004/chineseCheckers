package org.chinesecheckers.server.serverBoard;

/**
 * Exception thrown when there is an error in the game.
 */
public class GameException extends Exception {

    /**
     * Constructs a new GameException with the specified detail message.
     *
     * @param message the detail message
     */
    public GameException(String message) {
        super(message);
    }
}