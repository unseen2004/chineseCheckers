package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.PlayerColor;

public class Cell {

    private PlayerColor m_currentColor;
    private PlayerColor m_nativeColor;
    private PlayerColor m_targetColor;
    private boolean m_playable;
    private boolean m_isKing;
    private boolean m_special; // Add this field

    public Cell() {
    }

    public Cell(PlayerColor m_currentColor, PlayerColor m_nativeColor, PlayerColor m_targetColor, boolean m_playable, boolean m_isKing) {
        this.m_currentColor = m_currentColor;
        this.m_nativeColor = m_nativeColor;
        this.m_targetColor = m_targetColor;
        this.m_playable = m_playable;
        this.m_isKing = m_isKing;
        this.m_special = false; // Initialize the special field
    }

    public PlayerColor getCurrentColor() {
        return m_currentColor;
    }

    void setCurrentColor(PlayerColor m_currentColor) {
        this.m_currentColor = m_currentColor;
    }

    PlayerColor getNativeColor() {
        return m_nativeColor;
    }

    public PlayerColor getTargetColor() {
        return m_targetColor;
    }

    public boolean isPlayable() {
        return m_playable;
    }

    public boolean isKing() {
        return m_isKing;
    }

    public void setKing(boolean m_isKing) {
        this.m_isKing = m_isKing;
    }

    public void setSpecial(boolean special) {
        this.m_special = special;
    }

    public boolean isSpecial() {
        return m_special;
    }

    public void setNativeColor(PlayerColor nativeColor) {
        this.m_nativeColor = nativeColor;
    }
}