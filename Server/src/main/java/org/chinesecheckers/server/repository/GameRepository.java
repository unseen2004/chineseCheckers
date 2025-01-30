// GameRepository.java
package org.chinesecheckers.server.repository;

import org.chinesecheckers.server.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}