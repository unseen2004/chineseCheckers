package org.chinesecheckers.server.repository;

import org.chinesecheckers.server.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing Game entities.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    /**
     * Retrieves all games from the database.
     *
     * @return a list of all games
     */
    @Query("SELECT g FROM Game g")
    List<Game> findAllGames();
}