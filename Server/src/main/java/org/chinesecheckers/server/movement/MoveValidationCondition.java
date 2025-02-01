package org.chinesecheckers.server.movement;

/**
 * Represents a condition to validate a move in the Chinese Checkers game.
 */
public interface MoveValidationCondition {

    /**
     * Verifies if the move is valid based on the condition.
     *
     * @return true if the move is valid, otherwise false
     */
    boolean verify();
}