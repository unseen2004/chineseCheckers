package org.chinesecheckers.server.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * Represents a game entity in the Chinese Checkers server application.
 */
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Move> moves;

    private String mode;
    private int numberOfPlayers;
    private int numberOfBots;

    /**
     * Gets the ID of the game.
     *
     * @return the game ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the game.
     *
     * @param id the game ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the list of moves in the game.
     *
     * @return the list of moves
     */
    public List<Move> getMoves() {
        return moves;
    }

    /**
     * Sets the list of moves in the game.
     *
     * @param moves the list of moves
     */
    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    /**
     * Gets the mode of the game.
     *
     * @return the game mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the mode of the game.
     *
     * @param mode the game mode
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Gets the number of players in the game.
     *
     * @return the number of players
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Sets the number of players in the game.
     *
     * @param numberOfPlayers the number of players
     */
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * Gets the number of bots in the game.
     *
     * @return the number of bots
     */
    public int getNumberOfBots() {
        return numberOfBots;
    }

    /**
     * Sets the number of bots in the game.
     *
     * @param numberOfBots the number of bots
     */
    public void setNumberOfBots(int numberOfBots) {
        this.numberOfBots = numberOfBots;
    }
}