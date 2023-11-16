package game.piece;

import game.Square;
import game.movement.Direction;
import game.movement.ProjectionMapping;

import java.util.EnumSet;

public class Rook extends Piece {
    public Rook(Color color) {
        super(Piece.Type.ROOK, color);
        this.movementDirections = EnumSet.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    }

    @Override
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
