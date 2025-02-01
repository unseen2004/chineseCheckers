package org.chinesecheckers.server.movement;

import org.chinesecheckers.server.serverBoard.Board;
import org.chinesecheckers.server.serverBoard.IllegalCellException;
import org.springframework.stereotype.Component;
import org.chinesecheckers.server.serverBoard.GameException;

import static org.chinesecheckers.common.PlayerColor.NONE;

@Component
public class DefaultMovementStrategy implements MovementStrategy {

    @Override
    public int verifyMove(Board board, int x1, int y1, int x2, int y2, MoveValidationCondition[] moveValidationConditions) {

        if (moveValidationConditions.length != 2) {
            return -1;
        }
        int dx = x2 - x1;
        int dy = y2 - y1;

        if (IllegalCells(board, x1, y1, x2, y2)) {
            return 0;
        }
        if (isWrongPawnState(board, x1, y1, x2, y2)) {
            return 0;
        }
        if (isNotPreviousPawn(moveValidationConditions)) {
            return 0;
        }

        switch (Math.abs(dy)) {
            // Same row
            case 0: {
                // x dist
                switch (Math.abs(dx)) {

                    case 0: {
                        return 0;
                    }
                    // Short move
                    case 1: {
                        return moveValidationConditions[0].verify() ? 1 : 0;
                    }
                    // Long Move?
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


    private boolean IllegalCells(Board board, int x1, int y1, int x2, int y2) {
        return board.getField(x1, y1) == null || board.getField(x2, y2) == null || !board.getField(x1, y1).isPlayable() || !board.getField(x2, y2).isPlayable();
    }

    private boolean isWrongPawnState(Board board, int x1, int y1, int x2, int y2) {
        return !board.getField(x2, y2).getCurrentColor().equals(NONE) || board.getField(x1, y1).getCurrentColor().equals(NONE);
    }


    private boolean isNotPreviousPawn(MoveValidationCondition[] moveValidationConditions) {
        return !moveValidationConditions[0].verify() && moveValidationConditions[1].verify();
    }


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