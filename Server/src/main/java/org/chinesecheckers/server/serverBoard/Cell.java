package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.PlayerColor;

/**
 * Represents a cell on the game board.
 */
public class Cell {

    private PlayerColor m_currentColor;
    private PlayerColor m_nativeColor;
    private PlayerColor m_targetColor;
    private boolean m_playable;
    private boolean m_isKing;
    private boolean m_special; // Add this field
    private double m_alpha; // Add this field

    /**
     * Constructs an empty Cell.
     */
    public Cell() {
    }

    /**
     * Constructs a Cell with the specified properties.
     *
     * @param m_currentColor the current color of the cell
     * @param m_nativeColor the native color of the cell
     * @param m_targetColor the target color of the cell
     * @param m_playable whether the cell is playable
     * @param m_isKing whether the cell is a king
     */
    public Cell(PlayerColor m_currentColor, PlayerColor m_nativeColor, PlayerColor m_targetColor, boolean m_playable, boolean m_isKing) {
        this.m_currentColor = m_currentColor;
        this.m_nativeColor = m_nativeColor;
        this.m_targetColor = m_targetColor;
        this.m_playable = m_playable;
        this.m_isKing = m_isKing;
        this.m_special = false; // Initialize the special field
        this.m_alpha = 1.0; // Initialize the alpha value to fully opaque
    }

    /**
     * Gets the alpha value of the cell.
     *
     * @return the alpha value
     */
    public double getM_alpha() {
        return m_alpha;
    }

    /**
     * Sets the alpha value of the cell.
     *
     * @param m_alpha the alpha value to set
     */
    public void setM_alpha(double m_alpha) {
        this.m_alpha = m_alpha;
    }

    /**
     * Gets the current color of the cell.
     *
     * @return the current color
     */
    public PlayerColor getCurrentColor() {
        return m_currentColor;
    }

    /**
     * Sets the current color of the cell.
     *
     * @param m_currentColor the current color to set
     */
    void setCurrentColor(PlayerColor m_currentColor) {
        this.m_currentColor = m_currentColor;
    }

    /**
     * Gets the native color of the cell.
     *
     * @return the native color
     */
    PlayerColor getNativeColor() {
        return m_nativeColor;
    }

    /**
     * Gets the target color of the cell.
     *
     * @return the target color
     */
    public PlayerColor getTargetColor() {
        return m_targetColor;
    }

    /**
     * Checks if the cell is playable.
     *
     * @return true if the cell is playable, otherwise false
     */
    public boolean isPlayable() {
        return m_playable;
    }

    /**
     * Checks if the cell is a king.
     *
     * @return true if the cell is a king, otherwise false
     */
    public boolean isKing() {
        return m_isKing;
    }

    /**
     * Sets the cell as a king.
     *
     * @param m_isKing true to set the cell as a king, otherwise false
     */
    public void setKing(boolean m_isKing) {
        this.m_isKing = m_isKing;
    }

    /**
     * Sets the cell as special.
     *
     * @param special true to set the cell as special, otherwise false
     */
    public void setSpecial(boolean special) {
        this.m_special = special;
    }

    /**
     * Checks if the cell is special.
     *
     * @return true if the cell is special, otherwise false
     */
    public boolean isSpecial() {
        return m_special;
    }

    /**
     * Sets the native color of the cell.
     *
     * @param nativeColor the native color to set
     */
    public void setNativeColor(PlayerColor nativeColor) {
        this.m_nativeColor = nativeColor;
    }
}