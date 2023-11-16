package test.game.piece;

import game.Board;
import game.Square;
import game.piece.Bishop;
import game.piece.King;
import game.piece.Pawn;
import game.piece.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

public class PawnTest implements PieceTest {

    private Board board;
    private Piece pawn;

    @BeforeEach
    public void before() {
        board = new Board();
        pawn = new Pawn(Piece.Color.WHITE);
    }

    @Test
    public void moveTest() {
        //TODO: take the l
    }

    @Test
    public void validMoveSquaresTest() {
        //TODO: Split into separate test cases -- tedious to maintain like this.
        board.putPiece(pawn, Square.B2);
        // Starting square so should allow double moving.
        assertTrue(pawn.validMoveSquares().contains(Square.B4));
        assertFalse(pawn.validMoveSquares().contains(pawn.threateningSquares().toArray()[0]));
        assertFalse(pawn.validMoveSquares().contains(pawn.threateningSquares().toArray()[1]));

        Piece pawn2 = new Pawn(Piece.Color.WHITE);
        board.putPiece(pawn2, Square.C3);
        assertFalse(pawn.validMoveSquares().contains(pawn.threateningSquares().toArray()[0]));
        assertFalse(pawn.validMoveSquares().contains(pawn.threateningSquares().toArray()[1]));

        Piece pawn3 = new Pawn(Piece.Color.BLACK);
        board.putPiece(pawn3, Square.C3);
        assertFalse(pawn.validMoveSquares().contains(pawn.threateningSquares().toArray()[0]));
        assertTrue(pawn.validMoveSquares().contains(pawn.threateningSquares().toArray()[1]));

        Piece pawn4 = new Pawn(Piece.Color.BLACK);
        board.putPiece(pawn4, Square.A3);
        assertTrue(pawn.validMoveSquares().contains(Square.B4));
        assertTrue(pawn.validMoveSquares().containsAll(pawn.threateningSquares()));


    }

    @Test
    public void threateningSquaresTest() {
        board.putPiece(pawn, Square.B2);

        Piece pawn2 = new Pawn(Piece.Color.BLACK);
        board.putPiece(pawn2, Square.C3);
        Piece pawn3 = new Pawn(Piece.Color.BLACK);
        board.putPiece(pawn3, Square.A3);
        assertTrue(pawn.validMoveSquares().contains(Square.A3));
        assertTrue(pawn.validMoveSquares().containsAll(pawn.threateningSquares()));
        assertEquals(2, pawn.threateningSquares().size());
        board.clear(Square.A3);
        // remains two even without the piece.
        assertEquals(2, pawn.threateningSquares().size());
    }

    @Test
    public void cannotBreakPinTest() {
        board.putPiece(pawn, Square.B2);
        Piece allyKing = new King(Piece.Color.WHITE);
        board.putPiece(allyKing, Square.A1);
        Piece enemyBishop = new Bishop(Piece.Color.BLACK);
        board.putPiece(enemyBishop, Square.D4);
        enemyBishop.move(Square.C3);
        assertEquals(EnumSet.of(Square.C3), pawn.validMoveSquares());
        enemyBishop.move(Square.E5);
        assertTrue(pawn.validMoveSquares().isEmpty());
    }

    @Test
    public void enPassantTest() {
        board.putPiece(pawn, Square.E5);
        Piece enemyPawn = new Pawn(pawn.getColor().opposite());
        board.putPiece(enemyPawn, Square.D7);
        enemyPawn.move(Square.D5);
        assertTrue(pawn.validMoveSquares().contains(Square.D6));
        assertFalse(pawn.validMoveSquares().contains(Square.F6));
    }
}
