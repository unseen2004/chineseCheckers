package org.chinesecheckers.common;

import java.util.Objects;

/**
 * Represents a coordinate on the grid.
 */
public class GridCoordinate {
    private final int m_x;
    private final int m_y;

    /**
     * Constructs a GridCoordinate with the specified x and y values.
     *
     * @param m_x the x-coordinate
     * @param m_y the y-coordinate
     */
    public GridCoordinate(int m_x, int m_y) {
        this.m_x = m_x;
        this.m_y = m_y;
    }

    /**
     * Gets the x-coordinate.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return m_x;
    }

    /**
     * Gets the y-coordinate.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return m_y;
    }

    /**
     * Checks if this GridCoordinate is equal to another object.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof GridCoordinate c) {
            return m_x == c.m_x && m_y == c.m_y;
        } else return false;
    }

    /**
     * Returns the hash code of this GridCoordinate.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(m_x, m_y);
    }
}