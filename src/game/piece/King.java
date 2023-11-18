package game.piece;

import game.Square;
import game.movement.Direction;

import java.util.*;

public class King extends Piece {
    public King(Color color) {
        super(Type.KING, color);
        this.movementDirections = EnumSet.of(
                Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST,
                Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST
        );
    }

    @Override
    public void updateValidMoveSquares() {
        super.updateValidMoveSquares();
        retainNonThreatenedSquares();
        appendCastleMoves();
    }

    private void retainNonThreatenedSquares() {
        System.err.printf("[%s]#retainNonThreatenedSquares()%n", this);
        EnumSet<Square> enemyThreatenedSquares = board.getThreateningSquares(color.opposite());
        validMoveSquares.removeAll(enemyThreatenedSquares);
    }

    private void appendCastleMoves() {
        if (!this.hasMoved()) {
            System.err.printf("[%s]#appendCastleMoves()%n", this);
            // Castling rules for both sides -- this look up is faster than
            // dynamic generation of board square references;
            Square[] rookSquares = switch (this.color) {
                case WHITE -> new Square[]{Square.A1, Square.H1};
                case BLACK -> new Square[]{Square.A8, Square.H8};
            };
            Square[][] squaresToBeNonThreatened = switch (this.color) {
                case WHITE -> new Square[][]{
                        {Square.A1, Square.B1, Square.C1, Square.D1, Square.E1},
                        {Square.E1, Square.F1, Square.G1, Square.H1}
                };
                case BLACK -> new Square[][]{
                        {Square.A8, Square.B8, Square.C8, Square.D8},
                        {Square.D8, Square.E8, Square.F8, Square.G8, Square.H8}
                };
            };
            Square[] targetSquare = switch (this.color) {
                case WHITE -> new Square[]{Square.C1, Square.G1};
                case BLACK -> new Square[]{Square.C8, Square.G8};
            };

            for (int i = 0; i < rookSquares.length; i++) {
                Piece possibleUnmovedRook = board.getPiece(rookSquares[i]);

                if (possibleUnmovedRook instanceof Rook && !possibleUnmovedRook.hasMoved()) {
                    ArrayList<Square> listNotToBeThreatened = new ArrayList<>(List.of(squaresToBeNonThreatened[i]));
                    if (!listNotToBeThreatened.removeAll(board.getThreateningSquares(color.opposite()))) {
                        System.err.printf("[%s]#appendCastleMoves() | ADDING CASTLE MOVE FOR SQUARE%s%n", this, targetSquare[i]);
                        validMoveSquares.add(targetSquare[i]);
                    }
                }
            }
        }
    }

    public HashMap<Piece, Direction> getAttackingPieces() {
        return attackingPieces;
    }
}
