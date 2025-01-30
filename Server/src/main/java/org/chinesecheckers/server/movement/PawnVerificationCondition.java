package org.chinesecheckers.server.movement;


public class PawnVerificationCondition implements MoveValidationCondition {

    private int m_currentX;
    private int m_currentY;
    private int m_previousX;
    private int m_previousY;


    @Override
    public boolean verify() {
        return m_currentX != m_previousX || m_currentY != m_previousY;
    }

    public void setCurrentXY(int x, int y) {
        this.m_currentX = x;
        this.m_currentY = y;
    }

    public void setPreviousXY(int x, int y) {
        this.m_previousX = x;
        this.m_previousY = y;
    }


}