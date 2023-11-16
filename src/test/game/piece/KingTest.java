package test.game.piece;

import game.Board;
import game.Square;
import game.piece.King;
import game.piece.Piece;
import game.piece.Queen;
import game.piece.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KingTest implements PieceTest {

    private Board board;
    private Piece king;

    @BeforeEach
    public void before() {
        board = new Board();
        king = new King(Piece.Color.WHITE);
    }

    @Test
    public void moveTest() {
        board.putPiece(king, Square.E5);
        assertEquals(board.getPiece(Square.E5), king);
        king.move(Square.A5); // attempt move to invalid square
        assertEquals(board.getPiece(Square.E5), king); // Piece didn't move
        king.move(Square.E4);
        assertEquals(board.getPiece(Square.E4), king); // Piece moved
        assertNull(board.getPiece(Square.E5)); // previous square is empty
    }

    @Test
    public void validMoveSquaresTest() {
        board.putPiece(king, Square.E5);
        assertEquals(EnumSet.of(
                Square.D4, Square.D5, Square.D6, Square.E4,
                Square.E6, Square.F4, Square.F5, Square.F6),
            king.validMoveSquares()
        );
        EnumSet<Square> validMovesBefore = king.validMoveSquares();
        Piece enemyQueen = new Queen(Piece.Color.BLACK);
        board.putPiece(enemyQueen, Square.E7);
        assertNotEquals(validMovesBefore.size(), king.validMoveSquares().size());
        assertFalse(king.validMoveSquares().stream().anyMatch(square -> enemyQueen.validMoveSquares().contains(square)));
    }

    @Test
    public void validMoveSquaresCastlingTest() {
        board.putPiece(king, Square.E1);
        Piece rookLeft = new Rook(Piece.Color.WHITE);
        Piece rookRight = new Rook(Piece.Color.WHITE);
        board.putPiece(rookLeft, Square.A1);
        assertTrue(king.validMoveSquares().contains(Square.C1));
        board.putPiece(rookRight, Square.H1);
        assertTrue(king.validMoveSquares().containsAll(List.of(Square.C1, Square.G1)));

        // Is cut off from threatened squares.
        Piece enemyRook = new Rook(Piece.Color.BLACK);
        Piece enemyRook2 = new Rook(Piece.Color.BLACK);
        board.putPiece(enemyRook, Square.B2);
        assertFalse(king.validMoveSquares().contains(Square.C1));
        board.putPiece(enemyRook2, Square.G2);
        assertFalse(king.validMoveSquares().contains(Square.G1));
    }

    @Test
    public void threateningSquaresTest() {
        for(Square square : Square.values()) {
            board.putPiece(king, square);
            assertEquals(king.validMoveSquares(), king.threateningSquares());
        }
    }

    @Test
    public void cannotBreakPinTest() {
        // Cannot break pin, cause cannot be pinned.
        assertTrue(true);
    }
}
