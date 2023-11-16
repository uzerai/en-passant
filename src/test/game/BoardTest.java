package test.game;

import game.Board;
import game.Square;
import game.piece.Piece;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Test
    public void boardCreatedTest() {
        Board board = new Board();
        assertFalse(board.getBoardState().isEmpty());
        // Should only contain keys with null values.
        board.getBoardState().values().removeIf(Objects::isNull);
        assertTrue(board.getBoardState().isEmpty());
    }

    @Test
    public void fillStartingPositionsTest() {
        Board board = new Board();

        assertFalse(board.getBoardState().isEmpty());
        board.fillStartingPositions();
        board.getBoardState().values().removeIf(Objects::isNull);
        assertFalse(board.getBoardState().isEmpty());
    }

    @Test
    public void pawnTakesMove() {
        Board board = new Board();
        board.fillStartingPositions();
        Piece pawn1 = board.getPiece(Square.A2);
        Piece pawn2 = board.getPiece(Square.B7);
        pawn1.move(Square.A4);
        pawn2.move(Square.B5);
        pawn1.move(Square.B5);
        assertEquals(pawn1.getPosition(), Square.B5);
    }
}
