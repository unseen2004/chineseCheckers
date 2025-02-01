package org.chinesecheckers.server.model;

import jakarta.persistence.*;

/**
 * Represents a move entity in the Chinese Checkers server application.
 */
@Entity
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    /**
     * Gets the ID of the move.
     *
     * @return the move ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the move.
     *
     * @param id the move ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the starting x-coordinate of the move.
     *
     * @return the starting x-coordinate
     */
    public int getFromX() {
        return fromX;
    }

    /**
     * Sets the starting x-coordinate of the move.
     *
     * @param fromX the starting x-coordinate
     */
    public void setFromX(int fromX) {
        this.fromX = fromX;
    }

    /**
     * Gets the starting y-coordinate of the move.
     *
     * @return the starting y-coordinate
     */
    public int getFromY() {
        return fromY;
    }

    /**
     * Sets the starting y-coordinate of the move.
     *
     * @param fromY the starting y-coordinate
     */
    public void setFromY(int fromY) {
        this.fromY = fromY;
    }

    /**
     * Gets the ending x-coordinate of the move.
     *
     * @return the ending x-coordinate
     */
    public int getToX() {
        return toX;
    }

    /**
     * Sets the ending x-coordinate of the move.
     *
     * @param toX the ending x-coordinate
     */
    public void setToX(int toX) {
        this.toX = toX;
    }

    /**
     * Gets the ending y-coordinate of the move.
     *
     * @return the ending y-coordinate
     */
    public int getToY() {
        return toY;
    }

    /**
     * Sets the ending y-coordinate of the move.
     *
     * @param toY the ending y-coordinate
     */
    public void setToY(int toY) {
        this.toY = toY;
    }

    /**
     * Gets the game associated with the move.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the game associated with the move.
     *
     * @param game the game
     */
    public void setGame(Game game) {
        this.game = game;
    }
}