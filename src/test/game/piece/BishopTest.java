package test.game.piece;

import game.Board;
import game.Square;
import game.piece.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

public class BishopTest implements PieceTest {

    private Board board;
    private Piece bishop;

    @BeforeEach
    public void before() {
        board = new Board();
        bishop = new Bishop(Piece.Color.WHITE);
    }

    @Test
    public void moveTest() {

    }

    @Test
    public void validMoveSquaresTest() {
        board.putPiece(bishop, Square.E5);
        assertEquals(
            EnumSet.of(Square.A1,
                Square.B2, Square.B8, Square.C3, Square.C7,
                Square.D4, Square.D6, Square.F4, Square.F6,
                Square.G3, Square.G7, Square.H2, Square.H8
            ),
            bishop.validMoveSquares()
        );

        Piece blocker = new Pawn(Piece.Color.BLACK);
        board.putPiece(blocker, Square.F4);
        //Blocks the G3 and H2 squares from being valid moves.
        assertFalse(bishop.validMoveSquares().contains(Square.G3));
        assertFalse(bishop.validMoveSquares().contains(Square.H2));
        // Should include the square that the blocker is on if color is not same
        assertTrue(bishop.validMoveSquares().contains(Square.F4));
    }

    @Test
    public void threateningSquaresTest() {
        board.putPiece(bishop, Square.E5);
        assertEquals(bishop.validMoveSquares(), bishop.threateningSquares());
    }

    @Test
    public void cannotBreakPinTest() {
        Piece king = new King(bishop.getColor());
        Piece enemyRook = new Rook(bishop.getColor().opposite());
        board.putPiece(king, Square.E1);
        board.putPiece(bishop, Square.E2);
        board.putPiece(enemyRook, Square.E4);
        assertTrue(bishop.validMoveSquares().isEmpty());

        Piece enemyQueen = new Queen(bishop.getColor().opposite());
        board.putPiece(enemyQueen, Square.A5);
        board.removePieceFromSquare(enemyRook.getSquare());
        board.putPiece(bishop, Square.C3);
        assertTrue(bishop.validMoveSquares().contains(enemyQueen.getSquare()));
    }
}
