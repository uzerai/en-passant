package game.piece;

import game.movement.Direction;

import java.util.EnumSet;

public class Queen extends Piece {
    public Queen(Color color) {
        super(Type.QUEEN, color);
        this.movementDirections = EnumSet.of(
                Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST,
                Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST
        );
    }
}
