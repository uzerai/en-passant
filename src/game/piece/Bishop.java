package game.piece;

import game.Square;
import game.movement.Direction;
import game.movement.ProjectionMapping;

import java.util.EnumSet;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Color color) {
        super(Type.BISHOP, color);
        this.movementDirections = EnumSet.of(
                Direction.NORTH_EAST, Direction.SOUTH_EAST, Direction.SOUTH_WEST, Direction.NORTH_WEST
        );
    }

    public EnumSet<Square> validMoveSquares() {
        EnumSet<Square> validSquares = EnumSet.noneOf(Square.class);
        for(Direction direction : movementDirections) {
            validSquares.addAll(ProjectionMapping.forPieceInDirection(this, direction));
        }

        retainOnlyPinLegalMoves(validSquares);

        return validSquares;
    }

    @Override
    public EnumSet<Square> threateningSquares() {
        return validMoveSquares();
    }
}
