package org.chinesecheckers.server.player;

import org.chinesecheckers.common.Colors;
import org.chinesecheckers.common.PlayerColor;

import java.net.Socket;

/**
 * Represents a player entity in the Chinese Checkers game.
 */
public class PlayerEntity extends Player {
    private final CommunicationManager m_communicationManager;

    /**
     * Constructs a PlayerEntity with the specified socket and player color.
     *
     * @param socket the socket for communication
     * @param color the player color
     * @throws Exception if an error occurs while setting up the communication manager
     */
    public PlayerEntity(Socket socket, PlayerColor color) throws Exception {
        this.color = Colors.valueOf(color.name());
        m_communicationManager = new CommunicationManager(socket);
    }

    /**
     * Sends a command to the player.
     *
     * @param command the command to send
     */
    @Override
    public void sendCommand(String command) {
        m_communicationManager.writeLine(command);
    }

    /**
     * Reads the response from the player.
     *
     * @return the response as a string
     * @throws PlayerLeftException if the player has left the game
     */
    @Override
    public String readResponse() throws PlayerLeftException {
        try {
            return m_communicationManager.readLine();
        } catch (Exception ignored) {
            throw new PlayerLeftException(color.toString());
        }
    }
}