package org.chinesecheckers.server.player;

/**
 * Exception thrown when a player leaves the game.
 */
public class PlayerLeftException extends Exception {

    /**
     * Constructs a PlayerLeftException with the specified detail message.
     *
     * @param msg the detail message
     */
    public PlayerLeftException(String msg) {
        super(msg);
    }
}