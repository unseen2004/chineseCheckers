package org.chinesecheckers.server.movement;

import org.chinesecheckers.server.serverBoard.Board;


public interface MovementStrategy {

    int verifyMove(Board board, int x1, int y1, int x2, int y2, MoveValidationCondition[] moveValidationConditions);


    Board makeMove(Board board, int x1, int y1, int x2, int y2);
}