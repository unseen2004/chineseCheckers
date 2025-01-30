package org.chinesecheckers.server.serverBoard;


public interface BoardFactory {
    Board createBoard(int numberOfPlayers);
}