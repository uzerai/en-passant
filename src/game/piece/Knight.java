package game.piece;

import game.Square;
import game.movement.Direction;
import game.movement.ProjectionMapping;

import java.util.EnumSet;
import java.util.List;

public class Knight extends Piece{
    public Knight(Color color) {
        super(Type.KNIGHT, color);
        this.movementDirections = EnumSet.of(
                Direction.LEAP_NORTH_EAST,
                Direction.LEAP_EAST_NORTH,
                Direction.LEAP_EAST_SOUTH,
                Direction.LEAP_SOUTH_EAST,
                Direction.LEAP_SOUTH_WEST,
                Direction.LEAP_WEST_SOUTH,
                Direction.LEAP_WEST_NORTH,
                Direction.LEAP_NORTH_WEST
            );
    }

    @Override
    public EnumSet<Square> validMoveSquares() {
        EnumSet<Square> validSquares = EnumSet.noneOf(Square.class);
        for(Direction direction : movementDirections) {
            validSquares.addAll(ProjectionMapping.forPieceInDirection(this, direction, 1));
        }

        retainOnlyPinLegalMoves(validSquares);

        return validSquares;
    }

    @Override
    public EnumSet<Square> threateningSquares() {
        return validMoveSquares();
    }
}
