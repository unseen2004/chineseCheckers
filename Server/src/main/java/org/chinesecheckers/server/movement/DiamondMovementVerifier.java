package org.chinesecheckers.server.movement;

import org.chinesecheckers.server.serverBoard.Board;
import org.chinesecheckers.server.serverBoard.Cell;

public class DiamondMovementVerifier extends DefaultMovementStrategy {

    @Override
    public int verifyMove(Board board, int x1, int y1, int x2, int y2, MoveValidationCondition[] moveValidationConditions) {
        Cell from = board.getField(x1, y1);
        Cell to = board.getField(x2, y2);

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