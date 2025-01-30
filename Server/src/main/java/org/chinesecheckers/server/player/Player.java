package org.chinesecheckers.server.player;

import org.chinesecheckers.common.PlayerColor;


public abstract class Player {
    PlayerColor color;
    private boolean m_finished;


    public abstract void sendCommand(String command);


    public abstract String readResponse() throws PlayerLeftException;

    public PlayerColor getColor() {
        return color;
    }

    public boolean isFinished() {
        return m_finished;
    }

    public void setFinished(boolean status) {
        this.m_finished = status;
    }
}