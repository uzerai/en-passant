package game.piece;

import game.movement.Direction;

import java.util.EnumSet;

public class Rook extends Piece {
    public Rook(Color color) {
        super(Piece.Type.ROOK, color);
        this.movementDirections = EnumSet.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    }
}
