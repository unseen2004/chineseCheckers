package org.chinesecheckers.client.board;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import org.chinesecheckers.common.Colors;

/**
 * Represents a cell on the Chinese Checkers board.
 */
public class Cell {
    private final int m_x;
    private final int m_y;
    private final Circle m_circle;
    private Colors m_color = Colors.NONE;
    private double m_alpha; // Add this field

    /**
     * Constructs a Cell with the specified coordinates and circle.
     *
     * @param m_x      the x-coordinate of the cell
     * @param m_y      the y-coordinate of the cell
     * @param m_circle the circle representing the cell
     */
    public Cell(int m_x, int m_y, Circle m_circle) {
        this.m_x = m_x;
        this.m_y = m_y;
        this.m_circle = m_circle;
        this.m_alpha = 1.0; // Initialize the alpha value to fully opaque
    }

    /**
     * Gets the x-coordinate of the cell.
     *
     * @return the x-coordinate of the cell
     */
    public int getM_x() {
        return this.m_x;
    }

    /**
     * Gets the y-coordinate of the cell.
     *
     * @return the y-coordinate of the cell
     */
    public int getM_y() {
        return this.m_y;
    }

    /**
     * Gets the color of the cell.
     *
     * @return the color of the cell
     */
    Colors getM_color() {
        return m_color;
    }

    /**
     * Sets the color of the cell.
     *
     * @param m_color the color to set
     */
    void setM_color(Colors m_color) {
        this.m_color = m_color;
        Color fillColor = switch (m_color) {
            case NONE -> Color.WHITE;
            case RED -> Color.RED;
            case GREEN -> Color.GREEN;
            case YELLOW -> Color.YELLOW;
            case BLUE -> Color.BLUE;
            case ORANGE -> Color.ORANGE;
            case VIOLET -> Color.VIOLET;
        };
        fillColor = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), m_alpha); // Set alpha
        m_circle.setFill(fillColor);
    }

    /**
     * Gets the alpha (transparency) value of the cell.
     *
     * @return the alpha value of the cell
     */
    public double getM_alpha() {
        return m_alpha;
    }

    /**
     * Sets the alpha (transparency) value of the cell.
     *
     * @param m_alpha the alpha value to set
     */
    public void setM_alpha(double m_alpha) {
        this.m_alpha = m_alpha;
        Color fillColor = (Color) m_circle.getFill();
        fillColor = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), m_alpha);
        m_circle.setFill(fillColor);
    }

    /**
     * Sets the selection state of the cell.
     *
     * @param state true to select the cell, false to deselect
     */
    void setSelected(boolean state) {
        StrokeType strokeType;
        if (!m_circle.isDisabled()) {
            if (state) strokeType = StrokeType.OUTSIDE;
            else strokeType = StrokeType.INSIDE;

            m_circle.setStrokeType(strokeType);
        }
    }

    /**
     * Checks if the given circle is equal to the cell's circle.
     *
     * @param circle the circle to compare
     * @return true if the circles are equal, false otherwise
     */
    public boolean circleEquals(Circle circle) {
        return this.m_circle == circle;
    }

    /**
     * Marks the cell as a possible jump target.
     *
     * @param state true to mark as a possible jump target, false to unmark
     */
    void markAsPossibleJumps(boolean state) {
        if (!m_circle.isDisabled() && m_color == Colors.NONE) {
            Paint fillColor;
            if (state) fillColor = Color.BLACK;
            else fillColor = Color.WHITE;

            m_circle.setFill(fillColor);
        }
    }

    /**
     * Checks if the cell is disabled.
     *
     * @return true if the cell is disabled, false otherwise
     */
    boolean isDisabled() {
        return m_circle.isDisabled();
    }
}