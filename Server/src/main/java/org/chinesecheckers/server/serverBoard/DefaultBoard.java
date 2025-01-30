package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.PlayerColor;


public class DefaultBoard extends Board {
    public DefaultBoard() {
        columns = 13;
        rows = 17;
        cells = new Cell[columns + 1][rows + 1];
        for (int i = 1; i <= columns; i++) {
            for (int j = 1; j <= rows; j++) {
                setField(i, j, new Cell());
            }
        }
    }


    @Override
    public String getAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= columns; i++) {
            for (int j = 1; j <= rows; j++) {
                if (cells[i][j].isPlayable()) {
                    if (!cells[i][j].getCurrentColor().equals(PlayerColor.NONE)) {
                        if (!stringBuilder.toString().equals("")) {
                            stringBuilder.append(" ");
                        }
                        stringBuilder.append(cells[i][j].getCurrentColor().toString());
                        stringBuilder.append(" ");
                        stringBuilder.append(i);
                        stringBuilder.append(" ");
                        stringBuilder.append(j);
                    }
                }
            }
        }
        return stringBuilder.toString();
    }


    @Override
    public boolean isWinner(PlayerColor color) {
        Cell tempCell;
        for (int i = 1; i <= columns; i++) {
            for (int j = 1; j <= rows; j++) {
                tempCell = cells[i][j];
                if (tempCell.isPlayable() && tempCell.getCurrentColor().equals(color) && !tempCell.getCurrentColor().equals(tempCell.getTargetColor())) {
                    return false;
                }
            }
        }
        return true;
    }
}