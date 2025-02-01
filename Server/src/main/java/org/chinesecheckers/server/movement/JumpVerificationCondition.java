package org.chinesecheckers.server.movement;

/**
 * Represents a condition to verify the validity of a jump move in the Chinese Checkers game.
 */
public class JumpVerificationCondition implements MoveValidationCondition {

    private int m_status;

    /**
     * Constructs a JumpVerificationCondition with the specified status.
     *
     * @param m_status the status of the jump condition
     */
    public JumpVerificationCondition(int m_status) {
        this.m_status = m_status;
    }

    /**
     * Verifies if the jump move is valid based on the status.
     *
     * @return true if the jump move is valid, otherwise false
     */
    public boolean verify() {
        return m_status != 2;
    }

    /**
     * Sets the status of the jump condition.
     *
     * @param m_status the new status of the jump condition
     */
    public void setStatus(int m_status) {
        this.m_status = m_status;
    }
}