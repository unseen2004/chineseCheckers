package org.chinesecheckers.server.movement;

import org.chinesecheckers.server.serverBoard.Board;
import org.chinesecheckers.server.serverBoard.Cell;
import org.springframework.stereotype.Component;

/**
 * Implements the diamond movement strategy for the Chinese Checkers game.
 */
@Component
public class DiamondMovementStrategy extends DefaultMovementStrategy {

    /**
     * Verifies the validity of a move on the board according to the diamond movement strategy.
     *
     * @param board                    the game board
     * @param x1                       the starting x-coordinate
     * @param y1                       the starting y-coordinate
     * @param x2                       the ending x-coordinate
     * @param y2                       the ending y-coordinate
     * @param moveValidationConditions the conditions to validate the move
     * @return the distance of the move if valid, otherwise 0
     */
    @Override
    public int verifyMove(Board board, int x1, int y1, int x2, int y2, MoveValidationCondition[] moveValidationConditions) {
        Cell from = board.getField(x1, y1);
        Cell to = board.getField(x2, y2);

        if (from == null || to == null) {
            return 0; // Invalid move if either cell is null
        }

        if (from.isKing()) {
            // King piece can jump over common pieces
            return super.verifyMove(board, x1, y1, x2, y2, moveValidationConditions);
        } else {
            // Common pieces cannot jump over the king piece
            if (to.isKing()) {
                return 0;
            }
            return super.verifyMove(board, x1, y1, x2, y2, moveValidationConditions);
        }
    }
}