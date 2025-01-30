package org.chinesecheckers.server.player;

import org.chinesecheckers.common.Colors;
import org.chinesecheckers.common.PlayerColor;


public abstract class Player {
    Colors color;
    private boolean m_finished;


    public abstract void sendCommand(String command);


    public abstract String readResponse() throws PlayerLeftException;

    public PlayerColor getColor() {
        return PlayerColor.valueOf(color.name());
    }

    public boolean isFinished() {
        return m_finished;
    }

    public void setFinished(boolean status) {
        this.m_finished = status;
    }
}