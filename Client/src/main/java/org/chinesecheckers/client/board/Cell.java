package org.chinesecheckers.client.board;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import org.chinesecheckers.common.Colors;

public class Cell {
    private final int x;
    private final int y;
    private final Circle circle;
    private Colors color = Colors.NONE;
    private double alpha; // Add this field

    public Cell(int x, int y, Circle circle) {
        this.x = x;
        this.y = y;
        this.circle = circle;
        this.alpha = 1.0; // Initialize the alpha value to fully opaque
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    Colors getColor() {
        return color;
    }

    void setColor(Colors color) {
        this.color = color;
        Color fillColor = switch (color) {
            case NONE -> Color.WHITE;
            case RED -> Color.RED;
            case GREEN -> Color.GREEN;
            case YELLOW -> Color.YELLOW;
            case BLUE -> Color.BLUE;
            case ORANGE -> Color.ORANGE;
            case VIOLET -> Color.VIOLET;
        };
        fillColor = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), alpha); // Set alpha
        circle.setFill(fillColor);
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
        Color fillColor = (Color) circle.getFill();
        fillColor = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), alpha);
        circle.setFill(fillColor);
    }

    void setSelected(boolean state) {
        StrokeType strokeType;
        if (!circle.isDisabled()) {
            if (state) strokeType = StrokeType.OUTSIDE;
            else strokeType = StrokeType.INSIDE;

            circle.setStrokeType(strokeType);
        }
    }

    public boolean circleEquals(Circle circle) {
        return this.circle == circle;
    }

    void markAsPossibleJumps(boolean state) {
        if (!circle.isDisabled() && color == Colors.NONE) {
            Paint fillColor;
            if (state) fillColor = Color.BLACK;
            else fillColor = Color.WHITE;

            circle.setFill(fillColor);
        }
    }

    boolean isDisabled() {
        return circle.isDisabled();
    }
}