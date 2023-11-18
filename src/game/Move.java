package game;

import game.piece.Pawn;
import game.piece.Piece;

public class Move {
    private final Piece piece;
    private final Square oldSquare;
    private final Square newSquare;

    public Move(Piece piece, Square newSquare) {
        this.piece = piece;
        this.oldSquare = piece.getSquare();
        this.newSquare = newSquare;
    }

    public boolean wasPawnDoubleMove() {
        return (piece instanceof Pawn && ((oldSquare.getRow() - newSquare.getRow()) > 1));
    }

    public void doMove() {
        this.piece.move(newSquare);
    }

    public Piece getPiece() {
        return piece;
    }

    public Square getOldSquare() {
        return oldSquare;
    }

    public Square getNewSquare() {
        return newSquare;
    }
}
