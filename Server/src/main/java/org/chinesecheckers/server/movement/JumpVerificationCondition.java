package org.chinesecheckers.server.movement;


public class JumpVerificationCondition implements MoveValidationCondition {

    private int m_status;

    public JumpVerificationCondition(int m_status) {
        this.m_status = m_status;
    }


    public boolean verify() {
        return m_status != 2;
    }

    public void setStatus(int m_status) {
        this.m_status = m_status;
    }

}