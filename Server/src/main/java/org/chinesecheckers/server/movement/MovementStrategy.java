package org.chinesecheckers.server.movement;

import org.chinesecheckers.server.serverBoard.Board;

/**
 * Defines the movement strategy for the Chinese Checkers game.
 */
public interface MovementStrategy {

    /**
     * Verifies the validity of a move on the board.
     *
     * @param board                    the game board
     * @param x1                       the starting x-coordinate
     * @param y1                       the starting y-coordinate
     * @param x2                       the ending x-coordinate
     * @param y2                       the ending y-coordinate
     * @param moveValidationConditions the conditions to validate the move
     * @return the distance of the move if valid, otherwise 0
     */
    int verifyMove(Board board, int x1, int y1, int x2, int y2, MoveValidationCondition[] moveValidationConditions);

    /**
     * Makes a move on the board.
     *
     * @param board the game board
     * @param x1    the starting x-coordinate
     * @param y1    the starting y-coordinate
     * @param x2    the ending x-coordinate
     * @param y2    the ending y-coordinate
     * @return the updated game board
     */
    Board makeMove(Board board, int x1, int y1, int x2, int y2);
}