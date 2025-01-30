package org.chinesecheckers.server.serverBoard;

import org.chinesecheckers.common.PlayerColor;

public class DefaultBoardFactory implements BoardFactory {
    protected final DefaultBoard m_board = new DefaultBoard();

    @Override
    public Board createBoard(int numberOfPlayers) {
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

    protected void setAllPlayers(boolean isColorInGame) {
        setPlayer(isColorInGame, PlayerColor.GREEN, getGreenPositions());
        setPlayer(isColorInGame, PlayerColor.RED, getRedPositions());
        setPlayer(isColorInGame, PlayerColor.BLUE, getBluePositions());
        setPlayer(isColorInGame, PlayerColor.VIOLET, getVioletPositions());
        setPlayer(isColorInGame, PlayerColor.YELLOW, getYellowPositions());
        setPlayer(isColorInGame, PlayerColor.ORANGE, getOrangePositions());
    }

    protected void setPlayer(boolean isColorInGame, PlayerColor color, int[][] positions) {
        for (int[] pos : positions) {
            m_board.setField(pos[0], pos[1], getField(isColorInGame, color));
        }
    }

    protected Cell getField(boolean isColorInGame, PlayerColor color) {
        PlayerColor targetColor = getTargetColor(color);
        return isColorInGame ? new Cell(color, color, targetColor, true, false) : new Cell(PlayerColor.NONE, color, targetColor, true, false);
    }

    protected PlayerColor getTargetColor(PlayerColor color) {
        return switch (color) {
            case GREEN -> PlayerColor.RED;
            case RED -> PlayerColor.GREEN;
            case BLUE -> PlayerColor.ORANGE;
            case VIOLET -> PlayerColor.YELLOW;
            case YELLOW -> PlayerColor.VIOLET;
            case ORANGE -> PlayerColor.BLUE;
            default -> PlayerColor.NONE;
        };
    }

    protected int[][] getGreenPositions() {
        return new int[][]{{7, 1}, {6, 2}, {7, 2}, {6, 3}, {8, 3}, {7, 3}, {5, 4}, {6, 4}, {7, 4}, {8, 4}};
    }

    protected int[][] getRedPositions() {
        return new int[][]{{7, 17}, {6, 16}, {7, 16}, {6, 15}, {7, 15}, {8, 15}, {5, 14}, {6, 14}, {7, 14}, {8, 14}};
    }

    protected int[][] getBluePositions() {
        return new int[][]{{1, 5}, {2, 5}, {3, 5}, {4, 5}, {1, 6}, {2, 6}, {3, 6}, {2, 7}, {3, 7}, {2, 8}};
    }

    protected int[][] getVioletPositions() {
        return new int[][]{{2, 10}, {2, 11}, {3, 11}, {1, 12}, {2, 12}, {3, 12}, {1, 13}, {2, 13}, {3, 13}, {4, 13}};
    }

    protected int[][] getYellowPositions() {
        return new int[][]{{10, 5}, {11, 5}, {12, 5}, {13, 5}, {10, 6}, {11, 6}, {12, 6}, {11, 7}, {12, 7}, {11, 8}};
    }

    protected int[][] getOrangePositions() {
        return new int[][]{{11, 10}, {11, 11}, {12, 11}, {10, 12}, {11, 12}, {12, 12}, {10, 13}, {11, 13}, {12, 13}, {13, 13}};
    }
}