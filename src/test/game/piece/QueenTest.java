package test.game.piece;

import game.Board;
import game.Square;
import game.piece.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

public class QueenTest implements PieceTest {

    private Board board;
    private Piece queen;

    @BeforeEach
    public void before() {
        board = new Board();
        queen = new Queen(Piece.Color.WHITE);
    }

    @Test
    public void moveTest() {

    }

    @Test
    public void validMoveSquaresTest() {
        board.putPiece(queen, Square.D1);
        assertEquals(EnumSet.of(
                    Square.A1, Square.A4, Square.B1, Square.B3,
                    Square.C1, Square.C2, Square.D2, Square.D3,
                    Square.D4, Square.D5, Square.D6, Square.D7,
                    Square.D8, Square.E1, Square.E2, Square.F1,
                    Square.F3, Square.G1, Square.G4, Square.H1, Square.H5
                ),
                queen.validMoveSquares()
        );
    }

    @Test
    public void threateningSquaresTest() {

    }

    @Test
    public void cannotBreakPinTest() {
        Piece king = new King(queen.getColor());
        Piece enemyBishop = new Bishop(queen.getColor().opposite());
        board.putPiece(king, Square.E1);
        board.putPiece(queen, Square.F2);

        assertFalse(queen.validMoveSquares().isEmpty());
        board.putPiece(enemyBishop, Square.G3);
        // queen can attack
        assertTrue(queen.validMoveSquares().contains(enemyBishop.getSquare()));
        enemyBishop.move(Square.H4);
        assertTrue(queen.validMoveSquares().contains(enemyBishop.getSquare()));
        // contains all squares between the two pieces.
        assertTrue(queen.validMoveSquares().size() > 1);

        // actually tests for block-required squares.
        EnumSet<Square> legalMovesBefore = queen.validMoveSquares();
        Piece enemyRook = new Rook(queen.getColor().opposite());
        board.putPiece(enemyRook, Square.F6);
        System.out.println(board);
        assertEquals(legalMovesBefore, queen.validMoveSquares());
        board.removePieceFromSquare(enemyBishop.getSquare());
        System.out.println(legalMovesBefore);
        System.out.println(queen.validMoveSquares());
        assertTrue(legalMovesBefore.size() < queen.validMoveSquares().size());
        enemyRook.move(Square.E6);
        queen.move(Square.E2);
        assertTrue(queen.validMoveSquares().contains(enemyRook.getSquare()));
    }

    @Test
    public void pinnedByTest() {
        Piece king = new King(queen.getColor());
        Piece enemyBishop = new Bishop(queen.getColor().opposite());
        board.putPiece(king, Square.E1);
        board.putPiece(queen, Square.F2);
        board.putPiece(enemyBishop, Square.G3);
        // queen can attack
        assertTrue(queen.validMoveSquares().contains(enemyBishop.getSquare()));
    }

    @Test
    public void forcedMoveSquareTest() {
//        Piece king = new King(queen.getColor());
//        Piece enemyRook = new Rook(queen.getColor().opposite());
//        board.putPiece(king, Square.E1);
//        board.putPiece(enemyRook, Square.E6);
//        board.putPiece(queen, Square.F3);
//        assertEquals(EnumSet.of(Square.E2, Square.E3, Square.E4), queen.validMoveSquares());
//
//        board.putPiece(queen, Square.E3);
//        Piece enemyBishop = new Bishop(queen.getColor().opposite());
//        board.removePieceFromSquare(enemyRook.getSquare());
//        board.putPiece(enemyBishop, Square.A5);
    }
}
