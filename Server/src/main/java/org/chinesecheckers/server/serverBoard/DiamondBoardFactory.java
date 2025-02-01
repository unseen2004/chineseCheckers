package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.PlayerColor;

public class DiamondBoardFactory extends DefaultBoardFactory {

    @Override
    public Board createBoard(int numberOfPlayers) {
        m_board = new DiamondBoard(); // Create an instance of DiamondBoard
        setAllPlayers(false);

        switch (numberOfPlayers) {
            case 1:
                setPlayer(true, PlayerColor.RED, getRedPositions());
                break;
            case 2:
                setPlayer(true, PlayerColor.RED, getRedPositions());
                setPlayer(true, PlayerColor.GREEN, getGreenPositions());
                break;
            case 3:
                setPlayer(true, PlayerColor.RED, getRedPositions());
                setPlayer(true, PlayerColor.BLUE, getBluePositions());
                setPlayer(true, PlayerColor.YELLOW, getYellowPositions());
                break;
            case 4:
                setPlayer(true, PlayerColor.YELLOW, getYellowPositions());
                setPlayer(true, PlayerColor.ORANGE, getOrangePositions());
                setPlayer(true, PlayerColor.VIOLET, getVioletPositions());
                setPlayer(true, PlayerColor.BLUE, getBluePositions());
                break;
            case 6:
                setAllPlayers(true);
                break;
            default:
                throw new RuntimeException("Error");
        }

        int[] beginnings = {5, 4, 4, 3, 3, 3, 4, 4, 5};
        int[] endings = {9, 9, 10, 10, 11, 10, 10, 9, 9};
        for (int j = 5; j <= 13; j++) {
            for (int i = beginnings[j - 5]; i <= endings[j - 5]; i++) {
                m_board.setField(i, j, new Cell(PlayerColor.NONE, PlayerColor.NONE, PlayerColor.NONE, true, false));
            }
        }

        return m_board;
    }

    @Override
    protected Cell getField(boolean isColorInGame, PlayerColor color) {
        return super.getField(isColorInGame, color);
    }

    protected Cell getField(boolean isColorInGame, PlayerColor color, boolean isKing) {
        PlayerColor targetColor = getTargetColor(color);
        Cell cell = isColorInGame ? new Cell(color, color, targetColor, true, false) : new Cell(PlayerColor.NONE, color, targetColor, true, false);
        cell.setKing(isKing); // Set the king status using setKing method
        return cell;
    }

    @Override
    protected void setPlayer(boolean isColorInGame, PlayerColor color, int[][] positions) {
        for (int i = 0; i < positions.length; i++) {
            boolean isKing = (i == 0); // The first position is the king piece
            m_board.setField(positions[i][0], positions[i][1], getField(isColorInGame, color, isKing));
        }
    }
}