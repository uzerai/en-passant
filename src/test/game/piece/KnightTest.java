package test.game.piece;

import game.Board;
import game.Square;
import game.piece.Knight;
import game.piece.Pawn;
import game.piece.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KnightTest implements PieceTest {

    private Board board;
    private Piece knight;

    @BeforeEach
    public void before() {
        board = new Board();
        knight = new Knight(Piece.Color.WHITE);
    }

    @Test
    public void moveTest() {
        board.putPiece(knight, Square.E6);
        board.putPiece(new Pawn(Piece.Color.WHITE), Square.E7);
        board.putPiece(new Pawn(Piece.Color.WHITE), Square.E4);
        board.putPiece(new Pawn(Piece.Color.BLACK), Square.F6);
        board.putPiece(new Pawn(Piece.Color.BLACK), Square.D6);

        for (Square square : new Square[]{Square.C5, Square.C7, Square.D4, Square.D8, Square.F4, Square.F8, Square.G5, Square.G7}) {
            board.putPiece(knight, Square.E6);
            knight.move(square);
            assertEquals(board.getPiece(square), knight);
        }
    }

    @Test
    public void validMoveSquaresTest() {
        board.putPiece(knight, Square.E5);
        assertEquals(
                EnumSet.of(Square.C4, Square.C6,
                        Square.D3, Square.D7,
                        Square.F3, Square.F7,
                        Square.G4, Square.G6),
                knight.validMoveSquares()
        );
    }

    @Test
    public void threateningSquaresTest() {
        board.putPiece(knight, Square.E5);
        assertEquals(knight.validMoveSquares(), knight.threateningSquares());
    }

    @Override
    public void cannotBreakPinTest() {

    }
}
