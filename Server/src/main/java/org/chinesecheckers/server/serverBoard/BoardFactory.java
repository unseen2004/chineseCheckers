package org.chinesecheckers.server.serverBoard;

/**
 * Factory interface for creating game boards.
 */
public interface BoardFactory {

    /**
     * Creates a game board for the specified number of players.
     *
     * @param numberOfPlayers the number of players
     * @return the created game board
     * @throws GameException if the number of players is invalid
     */
    Board createBoard(int numberOfPlayers) throws GameException;

    /**
     * Validates the number of players.
     *
     * @param numberOfPlayers the number of players
     * @throws GameException if the number of players is less than 1, more than 6, or equal to 5
     */
    default void validatePlayerCount(int numberOfPlayers) throws GameException {
        if (numberOfPlayers < 1 || numberOfPlayers > 6 || numberOfPlayers == 5) {
            throw new GameException("Invalid number of players: " + numberOfPlayers);
        }
    }
}