package org.chinesecheckers.server.movement;

import org.chinesecheckers.server.serverBoard.Board;
import org.chinesecheckers.server.serverBoard.GameException;
import org.springframework.stereotype.Component;

import static org.chinesecheckers.common.PlayerColor.NONE;

/**
 * Implements the default movement strategy for the Chinese Checkers game.
 */
@Component
public class DefaultMovementStrategy implements MovementStrategy {

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
    @Override
    public int verifyMove(Board board, int x1, int y1, int x2, int y2, MoveValidationCondition[] moveValidationConditions) {
        if (moveValidationConditions.length != 2) {
            return -1;
        }
        int dx = x2 - x1;
        int dy = y2 - y1;

        if (IllegalCells(board, x1, y1, x2, y2) || isWrongPawnState(board, x1, y1, x2, y2)) {
            return 0;
        }
        if (isNotPreviousPawn(moveValidationConditions)) {
            return 0;
        }

        switch (Math.abs(dy)) {
            case 0: {
                switch (Math.abs(dx)) {
                    case 0: {
                        return 0;
                    }
                    case 1: {
                        return moveValidationConditions[0].verify() ? 1 : 0;
                    }
                    case 2: {
                        if (!board.getField((x1 + x2) / 2, y1).getCurrentColor().equals(NONE)) {
                            return 2;
                        } else {
                            return 0;
                        }
                    }
                }
            }
            case 1: {
                if (y1 % 2 == 0) {
                    if (dx == 0 || dx == 1) {
                        return moveValidationConditions[0].verify() ? 1 : 0;
                    } else {
                        return 0;
                    }
                } else {
                    if (dx == 0 || dx == -1) {
                        return moveValidationConditions[0].verify() ? 1 : 0;
                    } else {
                        return 0;
                    }
                }
            }
            case 2: {
                if (Math.abs(dx) != 1) {
                    return 0;
                }
                if (y1 % 2 == 0) {
                    if (x2 < x1 && !board.getField(x1, (y1 + y2) / 2).getCurrentColor().equals(NONE)) {
                        return 2;
                    } else if (x2 > x1 && !board.getField(x1 + 1, (y2 + y1) / 2).getCurrentColor().equals(NONE)) {
                        return 2;
                    } else {
                        return 0;
                    }
                } else {
                    if (x2 < x1 && !board.getField(x1 - 1, (y1 + y2) / 2).getCurrentColor().equals(NONE)) {
                        return 2;
                    } else if (x2 > x1 && !board.getField(x1, (y1 + y2) / 2).getCurrentColor().equals(NONE)) {
                        return 2;
                    } else {
                        return 0;
                    }
                }
            }
            default: {
                return 0;
            }
        }
    }

    /**
     * Checks if the specified cells are illegal.
     *
     * @param board the game board
     * @param x1    the starting x-coordinate
     * @param y1    the starting y-coordinate
     * @param x2    the ending x-coordinate
     * @param y2    the ending y-coordinate
     * @return true if any of the cells are illegal, otherwise false
     */
    private boolean IllegalCells(Board board, int x1, int y1, int x2, int y2) {
        return board.getField(x1, y1) == null || board.getField(x2, y2) == null || !board.getField(x1, y1).isPlayable() || !board.getField(x2, y2).isPlayable();
    }

    /**
     * Checks if the pawn state is incorrect for the move.
     *
     * @param board the game board
     * @param x1    the starting x-coordinate
     * @param y1    the starting y-coordinate
     * @param x2    the ending x-coordinate
     * @param y2    the ending y-coordinate
     * @return true if the pawn state is incorrect, otherwise false
     */
    private boolean isWrongPawnState(Board board, int x1, int y1, int x2, int y2) {
        return !board.getField(x2, y2).getCurrentColor().equals(NONE) || board.getField(x1, y1).getCurrentColor().equals(NONE);
    }

    /**
     * Checks if the move is not the previous pawn.
     *
     * @param moveValidationConditions the conditions to validate the move
     * @return true if the move is not the previous pawn, otherwise false
     */
    private boolean isNotPreviousPawn(MoveValidationCondition[] moveValidationConditions) {
        return !moveValidationConditions[0].verify() && moveValidationConditions[1].verify();
    }

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
    @Override
    public Board makeMove(Board board, int x1, int y1, int x2, int y2) {
        try {
            board.addPiece(x2, y2, board.getColor(x1, y1));
            board.removePiece(x1, y1);
            return board;
        } catch (GameException e) {
            e.printStackTrace();
            return board;
        }
    }
}