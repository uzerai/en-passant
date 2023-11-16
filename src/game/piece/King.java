package game.piece;

import game.Square;
import game.movement.Direction;
import game.movement.ProjectionMapping;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class King extends Piece {
    public King(Color color) {
        super(Type.KING, color);
        this.movementDirections = EnumSet.of(
                Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST,
                Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST
        );
    }

    @Override
    public EnumSet<Square> validMoveSquares() {
        EnumSet<Square> validSquares = EnumSet.noneOf(Square.class);
        for (Direction direction : movementDirections) {
            validSquares.addAll(ProjectionMapping.forPieceInDirection(this, direction, 1));
        }

        EnumSet<Square> enemyThreatenedSquares = board.getThreateningSquares(this.color.opposite());

        // if currently in starting position
        if (!this.hasMoved()) {
            // Castling rules for both sides.
            Square[] rookSquares = switch(this.color) {
                case WHITE -> new Square[]{Square.A1, Square.H1};
                case BLACK -> new Square[]{Square.A8, Square.H8};
            };
            Square[][] squaresToBeNonThreatened = switch(this.color) {
                case WHITE -> new Square[][]{
                        {Square.A1, Square.B1, Square.C1, Square.D1, Square.E1},
                        {Square.E1, Square.F1, Square.G1, Square.H1}
                };
                case BLACK -> new Square[][]{
                        {Square.A8, Square.B8, Square.C8, Square.D8},
                        {Square.D8, Square.E8, Square.F8, Square.G8, Square.H8}
                };
            };
            Square[] targetSquare = switch(this.color) {
                case WHITE -> new Square[]{Square.C1, Square.G1};
                case BLACK -> new Square[]{Square.C8, Square.G8};
            };

            for (int i = 0; i < rookSquares.length; i++) {
                Piece possibleUnmovedRook = board.getPiece(rookSquares[i]);

                if (possibleUnmovedRook != null && !possibleUnmovedRook.hasMoved()){
                    ArrayList<Square> listNotToBeThreatened = new ArrayList<>(List.of(squaresToBeNonThreatened[i]));
                    if (!listNotToBeThreatened.removeAll(enemyThreatenedSquares)) {
                        validSquares.add(targetSquare[i]);
                    }
                }
            }
        }

        validSquares.removeAll(enemyThreatenedSquares);

        return validSquares;
    }

    @Override
    public EnumSet<Square> threateningSquares() {
        // The king can attack any square which is a valid move.
        return validMoveSquares();
    }
}
