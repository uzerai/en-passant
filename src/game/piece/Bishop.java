package game.piece;

import game.movement.Direction;

import java.util.EnumSet;

public class Bishop extends Piece {
    public Bishop(Color color) {
        super(Type.BISHOP, color);
        this.movementDirections = EnumSet.of(
                Direction.NORTH_EAST, Direction.SOUTH_EAST, Direction.SOUTH_WEST, Direction.NORTH_WEST
        );
    }
}
