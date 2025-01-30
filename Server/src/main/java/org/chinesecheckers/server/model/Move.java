package org.chinesecheckers.server.model;

import jakarta.persistence.*;


@Entity
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private int playerId;
    private String moveDetails;

    // Getters and setters
}