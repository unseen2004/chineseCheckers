package org.chinesecheckers.server.serverBoard;
import org.chinesecheckers.common.PlayerColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DefaultBoardFactoryTest {

    @Test
    void testCreateBoardWithValidPlayerCount() throws GameException {
        DefaultBoardFactory factory = new DefaultBoardFactory();
        Board board = factory.createBoard(2);
        assertNotNull(board);
        assertEquals(13, board.getColumns());
        assertEquals(17, board.getRows());
    }

    @Test
    void testCreateBoardWithInvalidPlayerCount() {
        DefaultBoardFactory factory = new DefaultBoardFactory();
        assertThrows(GameException.class, () -> factory.createBoard(5));
    }

    @Test
    void testSetPlayer() throws GameException {
        DefaultBoardFactory factory = new DefaultBoardFactory();
        Board board = new DefaultBoard(13, 17);
        factory.setPlayer(board, PlayerColor.RED, factory.getRedPositions(), true);
        assertEquals(PlayerColor.RED, board.getCell(7, 17).getCurrentColor());
    }

    @Test
    void testInitializeCentralCells() throws GameException {
        DefaultBoardFactory factory = new DefaultBoardFactory();
        Board board = new DefaultBoard(13, 17);
        factory.initializeCentralCells(board);
        assertEquals(PlayerColor.NONE, board.getCell(7, 9).getCurrentColor());
    }

    @Test
    void testGetTargetColor() {
        DefaultBoardFactory factory = new DefaultBoardFactory();
        assertEquals(PlayerColor.RED, factory.getTargetColor(PlayerColor.GREEN));
        assertEquals(PlayerColor.NONE, factory.getTargetColor(PlayerColor.NONE));
    }
}