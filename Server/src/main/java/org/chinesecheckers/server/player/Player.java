package org.chinesecheckers.server.player;

import org.chinesecheckers.common.Colors;
import org.chinesecheckers.common.PlayerColor;

/**
 * Represents an abstract player in the Chinese Checkers game.
 */
public abstract class Player {
    Colors color;
    private boolean m_finished;

    /**
     * Sends a command to the player.
     *
     * @param command the command to send
     */
    public abstract void sendCommand(String command);

    /**
     * Reads the response from the player.
     *
     * @return the response as a string
     * @throws PlayerLeftException if the player has left the game
     */
    public abstract String readResponse() throws PlayerLeftException;

    /**
     * Gets the color of the player.
     *
     * @return the player color
     */
    public PlayerColor getColor() {
        return PlayerColor.valueOf(color.name());
    }

    /**
     * Checks if the player has finished the game.
     *
     * @return true if the player has finished, otherwise false
     */
    public boolean isFinished() {
        return m_finished;
    }

    /**
     * Sets the finished status of the player.
     *
     * @param status the finished status to set
     */
    public void setFinished(boolean status) {
        this.m_finished = status;
    }
}