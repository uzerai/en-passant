package game.movement;

import game.Square;
import game.piece.Piece;

import java.util.EnumSet;
import java.util.HashMap;

public class MovementProjection {
    private final EnumSet<Square> projection = EnumSet.noneOf(Square.class);
    private final HashMap<Piece, Direction> seenPieces = new HashMap<>();

    public EnumSet<Square> getProjection() {
        return this.projection;
    }

    public HashMap<Piece, Direction> getSeenPieces() {
        return this.seenPieces;
    }

    public MovementProjection merge(MovementProjection mergeProjection) {
        projection.addAll(mergeProjection.getProjection());
        seenPieces.putAll(mergeProjection.getSeenPieces());

        return this;
    }
}
