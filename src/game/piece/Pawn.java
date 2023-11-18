package game.piece;

import game.Move;
import game.Square;
import game.movement.Direction;

import java.util.Arrays;
import java.util.EnumSet;

public class Pawn extends Piece {
    private static final Square[][] STARTING_POSITIONS = {
            // White starting positions
            { Square.A2, Square.B2, Square.C2, Square.D2,
                    Square.E2, Square.F2, Square.G2, Square.H2 },
            // Black starting positions
            { Square.A7, Square.B7, Square.C7, Square.D7,
                    Square.E7, Square.F7, Square.G7, Square.H7 }
    };

    public Pawn(Color color) {
        super(Type.PAWN, color);
    }

    @Override
    public void updateValidMoveSquares() {
        EnumSet<Square> validSquares = EnumSet.noneOf(Square.class);

        // Pawns can move forward one square (forward relative to black/white)
        int forwardMovement = this.getColor() == Color.WHITE ? 1 : -1;
        validSquares.add(Square.fromValue(square.getColumn(), square.getRow() + forwardMovement));

        Square[] startingPositions = switch(color) {
            case WHITE -> STARTING_POSITIONS[0];
            case BLACK -> STARTING_POSITIONS[1];
        };

        // Allow double movement when in starting position.
        if (Arrays.stream(startingPositions).anyMatch((square) -> square == this.square) && !hasMoved()) {
            Square possibleDoubleMove = Square.fromValue(square.getColumn(), square.getRow() + (2 * forwardMovement));
            // Can't double-move if there is a piece there.
            if (board.getPiece(possibleDoubleMove) == null) {
                validSquares.add(possibleDoubleMove);
            }
        }


        Move lastMove = board.getLastMove();
        // Also do en-passant logic depending on lastMove on the board.
        validSquares.addAll(
            threateningSquares()
                .stream()
                .filter(square -> {
                    Piece piece = board.getPiece(square);

                    if (lastMove != null && lastMove.wasPawnDoubleMove()) {
                        // If the last double pawn move was a move to a square next to the current pawn.
                        boolean enPassantAttackable = lastMove.getNewSquare()
                                .equals(this.square.nextInDirection(Direction.EAST))
                             || lastMove.getNewSquare()
                                .equals(this.square.nextInDirection(Direction.WEST));

                        if (enPassantAttackable && (lastMove.getNewSquare().getColumn() == square.getColumn())) {
                            piece = lastMove.getPiece();
                        }
                    }

                    return piece != null && piece.getColor() != color;
                })
                .toList()
        );

        // Make squares which break pinning illegal.
        retainOnlyPinLegalMoves();
        retainOnlyCheckBlockMoves();

        validMoveSquares = validSquares;
    }

    @Override
    public EnumSet<Square> threateningSquares() {
        Square rightAttackSquare = Square.fromValue(square.getColumn() + 1, square.getRow() + 1);
        Square leftAttackSquare = Square.fromValue(square.getColumn() - 1, square.getRow() + 1);

        EnumSet<Square> possibleThreatenedSquares = EnumSet.noneOf(Square.class);
        if (rightAttackSquare != null)
            possibleThreatenedSquares.add(rightAttackSquare);
        if (leftAttackSquare != null)
            possibleThreatenedSquares.add(leftAttackSquare);

        return possibleThreatenedSquares;
    }
}
