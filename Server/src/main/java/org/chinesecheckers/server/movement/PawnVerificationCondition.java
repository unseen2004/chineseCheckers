package org.chinesecheckers.server.movement;

/**
 * Represents a condition to verify the validity of a pawn move in the Chinese Checkers game.
 */
public class PawnVerificationCondition implements MoveValidationCondition {

    private int m_currentX;
    private int m_currentY;
    private int m_previousX;
    private int m_previousY;

    /**
     * Verifies if the pawn move is valid based on the current and previous coordinates.
     *
     * @return true if the move is valid, otherwise false
     */
    @Override
    public boolean verify() {
        return m_currentX != m_previousX || m_currentY != m_previousY;
    }

    /**
     * Sets the current coordinates of the pawn.
     *
     * @param x the current x-coordinate
     * @param y the current y-coordinate
     */
    public void setCurrentXY(int x, int y) {
        this.m_currentX = x;
        this.m_currentY = y;
    }

    /**
     * Sets the previous coordinates of the pawn.
     *
     * @param x the previous x-coordinate
     * @param y the previous y-coordinate
     */
    public void setPreviousXY(int x, int y) {
        this.m_previousX = x;
        this.m_previousY = y;
    }
}