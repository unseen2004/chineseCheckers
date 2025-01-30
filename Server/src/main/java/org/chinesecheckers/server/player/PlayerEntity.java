package org.chinesecheckers.server.player;

import org.chinesecheckers.common.Colors;
import org.chinesecheckers.common.PlayerColor;

import java.net.Socket;


public class PlayerEntity extends Player {
    private final CommunicationManager m_communicationManager;

    public PlayerEntity(Socket socket, PlayerColor color) throws Exception {
        this.color = Colors.valueOf(color.name());
        m_communicationManager = new CommunicationManager(socket);
    }

    @Override
    public void sendCommand(String command) {
        m_communicationManager.writeLine(command);
    }

    @Override
    public String readResponse() throws PlayerLeftException {
        try {
            return m_communicationManager.readLine();
        } catch (Exception ignored) {
            throw new PlayerLeftException(color.toString());
        }
    }
}