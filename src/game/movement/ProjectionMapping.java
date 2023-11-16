package game.movement;

import game.Board;
import game.Square;
import game.piece.Piece;

import java.util.EnumSet;

public class ProjectionMapping {

    public static EnumSet<Square> fromPositionDirection(
            Square position,
            Direction direction,
            Board board,
            Piece.Color color,
            int projectionLengthLimit
    ) {
        if (board == null)
            return EnumSet.noneOf(Square.class);

        EnumSet<Square> validSquares = EnumSet.noneOf(Square.class);

        Square nextSquare = position;
        Piece pieceInSquare;
        int projectionLength = 0;

        while (nextSquare != null) {
            if(projectionLengthLimit > 0 && projectionLength >= projectionLengthLimit) {
                return validSquares;
            }

            pieceInSquare = board.getPiece(nextSquare);
            if (pieceInSquare != null && nextSquare != position) {
                if (pieceInSquare.getColor() != color) {
                    validSquares.add(nextSquare);
                    projectionLength++;
                }

                nextSquare = null;
            } else {
                if(nextSquare != position){
                    validSquares.add(nextSquare);
                    projectionLength++;
                }

                nextSquare = Square.fromValue(
                        nextSquare.getColumn() + direction.getDirectionX(),
                        nextSquare.getRow() + direction.getDirectionY()
                );
            }
        }

        return validSquares;
    }

    public static EnumSet<Square> fromPositionDirection(
            Square position, Direction direction,
            Board board, Piece.Color color
    ) {
        return fromPositionDirection(position, direction, board, color, -1);
    }

    public static EnumSet<Square> forPieceInDirection(Piece piece, Direction direction, int projectionLengthLimit) {
        return fromPositionDirection(piece.getPosition(), direction,
                piece.getBoard(), piece.getColor(), projectionLengthLimit);
    }
    public static EnumSet<Square> forPieceInDirection(Piece piece, Direction direction) {
        return fromPositionDirection(piece.getPosition(), direction, piece.getBoard(), piece.getColor());
    }

}
