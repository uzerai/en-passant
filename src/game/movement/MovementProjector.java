package game.movement;

import game.Board;
import game.Square;
import game.piece.Piece;

import java.util.List;

public class MovementProjector {

    private final Board board;

    public MovementProjector(Board board) {
        this.board = board;
    }

    public MovementProjection from(Square origin, Direction direction, boolean singleMove) {
        MovementProjection projection = new MovementProjection();

        Square currentSquare = origin;
        while (currentSquare != null) {
            projection.getProjection().add(currentSquare);

            Piece possiblePieceInSquare = board.getPiece(currentSquare);
            if (possiblePieceInSquare != null)
                projection.getSeenPieces().put(possiblePieceInSquare, direction);

            currentSquare = currentSquare.nextInDirection(direction);

            if (singleMove)
                currentSquare = null;
        }

        return projection;
    }

    public MovementProjection fromWithCollision(Square origin, Direction direction, boolean singleMove) {
        MovementProjection projection = new MovementProjection();

        Square currentSquare = origin.nextInDirection(direction);
        while (currentSquare != null) {
            Piece possiblePieceInSquare = board.getPiece(currentSquare);
            projection.getProjection().add(currentSquare);

            if (possiblePieceInSquare != null) {
                projection.getSeenPieces().put(possiblePieceInSquare, direction);
                currentSquare = null;
            } else {
                currentSquare = currentSquare.nextInDirection(direction);
            }

            if (singleMove)
                currentSquare = null;
        }

        return projection;
    }

    public MovementProjection fromExclusive(Square origin, Direction direction, boolean singleMove) {
        MovementProjection projection = from(origin, direction, singleMove);
        projection.getProjection().remove(origin);
        projection.getSeenPieces().remove(board.getPiece(origin));

        return projection;
    }

    public MovementProjection fromWithCollisionExclusive(Square origin, Direction direction, boolean singleMove) {
        MovementProjection projection = fromWithCollision(origin, direction, singleMove);
        projection.getProjection().remove(origin);
        projection.getSeenPieces().remove(board.getPiece(origin));

        return projection;
    }

    public MovementProjection fromWithCollisionExclusiveColor(
        Square origin, Direction direction, boolean singleMove, Piece.Color color) {
        MovementProjection projection = fromWithCollision(origin, direction, singleMove);
        projection.getProjection().remove(origin);
        projection.getSeenPieces().remove(board.getPiece(origin));

        List<Piece> sameColorPieces = projection.getSeenPieces()
                .keySet()
                .stream()
                .filter(piece -> piece.getColor() == color)
                .toList();

        for (Piece piece : sameColorPieces) {
            projection.getSeenPieces().remove(piece);
            projection.getProjection().remove(piece.getSquare());
        }

        return projection;
    }
}
