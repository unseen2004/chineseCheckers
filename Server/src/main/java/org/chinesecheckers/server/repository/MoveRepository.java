package org.chinesecheckers.server.repository;

import org.chinesecheckers.server.model.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing Move entities.
 */
@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {

    /**
     * Retrieves all moves for a specific game, ordered by move ID.
     *
     * @param gameId the ID of the game
     * @return a list of moves for the specified game
     */
    @Query("SELECT m FROM Move m WHERE m.game.id = :gameId ORDER BY m.id")
    List<Move> findMovesByGameId(@Param("gameId") Long gameId);
}