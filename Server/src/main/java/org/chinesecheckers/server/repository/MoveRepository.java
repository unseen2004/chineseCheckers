package org.chinesecheckers.server.repository;

import org.chinesecheckers.server.model.Move;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoveRepository extends JpaRepository<Move, Long> {
}