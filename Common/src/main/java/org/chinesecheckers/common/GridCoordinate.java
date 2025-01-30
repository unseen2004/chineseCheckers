package org.chinesecheckers.common;

import java.util.Objects;


public class GridCoordinate {
    private final int m_x;
    private final int m_y;

    public GridCoordinate(int m_x, int m_y) {
        this.m_x = m_x;
        this.m_y = m_y;
    }

    public int getX() {
        return m_x;
    }

    public int getY() {
        return m_y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GridCoordinate c) {
            return m_x == c.m_x && m_y == c.m_y;
        } else return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_x, m_y);
    }
}