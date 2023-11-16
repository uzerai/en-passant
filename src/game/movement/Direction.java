package game.movement;

import game.piece.Piece;

public enum Direction {
    NORTH(0,1), NORTH_EAST(1,1), EAST(1,0), SOUTH_EAST(1,-1),
    SOUTH(0,-1), SOUTH_WEST(-1,-1), WEST(-1,0), NORTH_WEST(-1,1),
    // LEAP_* directions are exclusive to Knights
    LEAP_NORTH_EAST(2, 1), LEAP_EAST_NORTH(1, 2),
    LEAP_EAST_SOUTH(-1, 2), LEAP_SOUTH_EAST(-2, 1),
    LEAP_SOUTH_WEST(-2, -1), LEAP_WEST_SOUTH(-1, -2),
    LEAP_WEST_NORTH(1, -2), LEAP_NORTH_WEST(2, -1);

    private final int dirX;
    private final int dirY;

    Direction(int dirX, int dirY){
        this.dirX = dirX;
        this.dirY = dirY;
    }

    public int getDirectionX() {
        return this.dirX;
    }

    public int getDirectionY() {
        return this.dirY;
    }

    public Direction opposite() {
        return switch(this) {
            case NORTH -> SOUTH;
            case NORTH_EAST -> SOUTH_WEST;
            case EAST -> WEST;
            case SOUTH_EAST -> NORTH_WEST;
            case SOUTH -> NORTH;
            case SOUTH_WEST -> NORTH_EAST;
            case WEST -> EAST;
            case NORTH_WEST -> SOUTH_EAST;
            case LEAP_NORTH_EAST -> LEAP_SOUTH_WEST;
            case LEAP_EAST_NORTH -> LEAP_WEST_SOUTH;
            case LEAP_EAST_SOUTH -> LEAP_WEST_NORTH;
            case LEAP_SOUTH_EAST -> LEAP_NORTH_WEST;
            case LEAP_SOUTH_WEST -> LEAP_NORTH_EAST;
            case LEAP_WEST_SOUTH -> LEAP_EAST_NORTH;
            case LEAP_WEST_NORTH -> LEAP_EAST_SOUTH;
            case LEAP_NORTH_WEST -> LEAP_SOUTH_EAST;
        };
    }
}
