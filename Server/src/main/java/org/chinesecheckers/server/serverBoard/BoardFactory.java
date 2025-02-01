package org.chinesecheckers.server.serverBoard;

public interface BoardFactory {
    Board createBoard(int numberOfPlayers) throws GameException;

    default void validatePlayerCount(int numberOfPlayers) throws GameException {
        if (numberOfPlayers < 1 || numberOfPlayers > 6 || numberOfPlayers == 5) {
            throw new GameException("Invalid number of players: " + numberOfPlayers);
        }
    }
}