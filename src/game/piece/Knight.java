package game.piece;

import game.movement.Direction;

import java.util.EnumSet;

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
}
