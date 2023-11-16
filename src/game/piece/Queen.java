package game.piece;

import game.Square;
import game.movement.Direction;
import game.movement.MovementMapping;

import java.util.EnumSet;

public class Queen extends Piece {
    public Queen(Color color) {
        super(Type.QUEEN, color);
        this.movementDirections = EnumSet.of(
                Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST,
                Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST
        );
    }

    @Override
    public EnumSet<Square> validMoveSquares() {
        EnumSet<Square> validSquares = EnumSet.noneOf(Square.class);
        for(Direction direction : movementDirections) {
            validSquares.addAll(MovementMapping.forPieceInDirection(this, direction));
        }

        retainOnlyPinLegalMoves(validSquares);
        retainCheckBlockMoves(validSquares);

        return validSquares;
    }

    @Override
    public EnumSet<Square> threateningSquares() {
        return validMoveSquares();
    }
}
