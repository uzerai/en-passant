package test.game.piece;

import game.Board;
import game.Square;
import game.piece.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

public class RookTest implements PieceTest{

    private Board board;
    private Piece rook;

    @BeforeEach
    public void before() {
        board = new Board();
        rook = new Rook(Piece.Color.WHITE);
    }

    @Test
    public void moveTest() {

    }

    @Test
    public void validMoveSquaresTest() {
        board.putPiece(rook, Square.E4);
        board.putPiece(new Pawn(Piece.Color.BLACK), Square.B4);
        board.putPiece(new Pawn(Piece.Color.WHITE), Square.E7);
        assertEquals(EnumSet.of(
                Square.B4, Square.C4, Square.D4, Square.E1,
                Square.E2, Square.E3, Square.E5, Square.E6,
                Square.F4, Square.G4, Square.H4), rook.validMoveSquares());
    }

    @Test
    public void threateningSquaresTest() {
        board.putPiece(rook, Square.E4);
        assertEquals(rook.validMoveSquares(), rook.threateningSquares());
    }

    @Test
    public void cannotBreakPinTest() {
        Piece king = new King(rook.getColor());
        Piece enemyBishop = new Bishop(rook.getColor().opposite());
        board.putPiece(king, Square.E1);
        board.putPiece(rook, Square.F2);
        assertFalse(rook.validMoveSquares().isEmpty());
        board.putPiece(enemyBishop, Square.G3);
        assertTrue(rook.validMoveSquares().isEmpty());

        Piece enemyRook = new Rook(rook.getColor().opposite());
        board.putPiece(enemyRook, Square.F6);
        //still pinned by bishop
        assertTrue(rook.validMoveSquares().isEmpty());
        board.removePieceFromSquare(Square.G3);
        // no longer pinned.
        assertFalse(rook.validMoveSquares().isEmpty());
        rook.move(Square.E2);
        enemyRook.move(Square.E6);
        // can take the enemy rook causing the pin now that bishop is gone.
        assertEquals(EnumSet.of(Square.E3, Square.E4, Square.E5, Square.E6), rook.validMoveSquares());
    }
}
