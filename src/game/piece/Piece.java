package game.piece;

import game.Board;
import game.Square;
import game.movement.Direction;
import game.movement.MovementProjection;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Piece {
    protected final Color color;
    protected final Type type;
    protected Board board = null;
    protected Square square = null;
    private boolean hasMoved = false;
    // With default values, Piece cannot move.
    protected EnumSet<Direction> movementDirections  = EnumSet.noneOf(Direction.class);
    protected EnumSet<Direction> pinnedDirections = EnumSet.noneOf(Direction.class);
    // Pieces which are actively attacking this piece.
    protected HashMap<Piece, Direction> attackingPieces = new HashMap<>();
    protected EnumSet<Square> validMoveSquares = EnumSet.noneOf(Square.class);

    public enum Type {
        PAWN('P'),
        KNIGHT('N'),
        BISHOP('B'),
        ROOK('R'),
        QUEEN('Q'),
        KING('K');


        private final char denomination;

        Type(char denomination) {
            this.denomination = denomination;
        }

        public char getDenomination() {
            return denomination;
        }
    }

    public enum Color {
        WHITE, BLACK;

        public Color opposite() {
            return switch(this) {
                case WHITE -> BLACK;
                case BLACK -> WHITE;
            };
        }
    }

    protected Piece(Type type, Color color) {
        this.type = type;
        this.color = color;
    }

    public static Piece fromStartingSquare(Square square) {
        return switch(square) {
            case A1, H1 -> new Rook(Color.WHITE);
            case B1, G1 -> new Knight(Color.WHITE);
            case C1, F1 -> new Bishop(Color.WHITE);
            case D1 -> new Queen(Color.WHITE);
            case E1 -> new King(Color.WHITE);
            case A2, B2, C2, D2, E2, F2, G2, H2 -> new Pawn(Color.WHITE);
            case A8, H8 -> new Rook(Color.BLACK);
            case B8, G8 -> new Knight(Color.BLACK);
            case C8, F8 -> new Bishop(Color.BLACK);
            case D8 -> new Queen(Color.BLACK);
            case E8 -> new King(Color.BLACK);
            case A7, B7, C7, D7, E7, F7, G7, H7 -> new Pawn(Color.BLACK);
            default -> null;
        };
    }

    public Color getColor() {
        return this.color;
    }

    public Type getType() {
        return this.type;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public Square getSquare() {
        return this.square;
    }

    public void setSquare(Square square) {
        if (square == null) {
            threateningSquares().stream()
                    .map(threatenedSquare -> board.getPiece(threatenedSquare))
                    .filter(Objects::nonNull)
                    .forEach(piece -> piece.removeAttackedBy(this));
        }

        this.square = square;
    }
    public Board getBoard() { return this.board; }
    public void setBoard(Board board) { this.board = board; }
    public EnumSet<Square> validMoveSquares() { return validMoveSquares; }
    public EnumSet<Square> threateningSquares() {
        return this.validMoveSquares;
    }

    public void move(Square newSquare) {
        System.err.printf("[%s]#move(%s)%n", this, newSquare);
        if (validMoveSquares.contains(newSquare)) {
            board.putPiece(this, newSquare);
            hasMoved = true;
        } else {
            System.err.printf("Attempted invalid move for piece %s -> %s%n", this, newSquare);
        }
    }
    public void setAttackedBy(Piece piece, Direction direction) {
        System.err.printf("[%s]#setAttackedBy([%s], %s)%n", this, piece, direction);
        if (piece.getColor() != color) {
            this.attackingPieces.put(piece, direction);
            this.detectPinned(piece, direction);
        }
    }

    public void removeAttackedBy(Piece piece) {
        System.err.printf("[%s]#removeAttackedBy([%s])%n", this, piece);
        Direction direction = attackingPieces.remove(piece);
        pinnedDirections.remove(direction);
    }

    private void detectPinned(Piece piece, Direction direction) {
        System.err.printf("[%s]#detectPinned([%s], %s)%n", this, piece, direction);
        if(!piece.isSingleMovePiece()) {
            MovementProjection squaresBehindInDirectionOfAttack = board.getProjectionEngine()
                    .fromExclusive(square, direction, false);

            boolean containsSameColoredKing = squaresBehindInDirectionOfAttack
                    .getSeenPieces()
                    .keySet()
                    .stream()
                    .anyMatch(
                        seenPiece ->
                            (seenPiece.getType() == Type.KING) && (seenPiece.getColor() == color)
                    );

            if(containsSameColoredKing) {
                this.pinnedDirections.add(direction);
                retainOnlyPinLegalMoves();
            }
        }
    }

    public void updateValidMoveSquares() {
        System.err.printf("[%s]#updateValidMoveSquares()%n", this);

        EnumSet<Direction> pinnedDirections = EnumSet.copyOf(this.pinnedDirections);
        EnumSet<Direction> availableDirections = EnumSet.copyOf(movementDirections);

        if (!pinnedDirections.isEmpty()) {
            pinnedDirections.addAll(this.pinnedDirections.stream()
                    .map(Direction::opposite)
                    .collect(Collectors.toSet()));
            availableDirections.retainAll(pinnedDirections);
        }

        MovementProjection projection = new MovementProjection();
        for(Direction direction : availableDirections) {
            projection.merge(
                board.getProjectionEngine().fromWithCollisionExclusiveColor(
                        square, direction, isSingleMovePiece(), color
                )
            );
        }

        for (Map.Entry<Piece, Direction> entry : projection.getSeenPieces().entrySet()) {
            if (entry.getKey().getColor() != color)
                entry.getKey().setAttackedBy(this, entry.getValue());
        }

        validMoveSquares = projection.getProjection();
        //TODO: Fix rules around if the King is in check, forced moves.
//        retainOnlyCheckBlockMoves();
    }

    protected void retainOnlyPinLegalMoves() {
        System.err.printf("[%s]#retainOnlyPinLegalMoves()%n", this);
        if (!this.pinnedDirections.isEmpty() && !(this instanceof King)) {
            EnumSet<Square> pinnedDirectionSquares = EnumSet.noneOf(Square.class);

            for (Direction direction : pinnedDirections) {
                pinnedDirectionSquares.addAll(
                    board.getProjectionEngine()
                        .fromWithCollisionExclusiveColor(square, direction, isSingleMovePiece(), color)
                        .getProjection());
                pinnedDirectionSquares.addAll(
                    board.getProjectionEngine()
                        .fromWithCollisionExclusiveColor(square, direction.opposite(), isSingleMovePiece(), color)
                        .getProjection());
            }

            validMoveSquares.retainAll(pinnedDirectionSquares);
        }
    }

    protected void retainOnlyCheckBlockMoves() {
        if (this instanceof King)
            return;

        Optional<Piece> colorKing = board.getPieces()
                .stream()
                .filter(
                    piece -> piece.getColor() == this.color && piece instanceof King
                ).findAny();

        if (colorKing.isPresent()) {
            King king = (King) colorKing.get();
            List<Square> kingThreatened = king.getAttackingPieces().entrySet()
                .stream()
                .flatMap(entry ->
                    board.getProjectionEngine()
                        .fromWithCollisionExclusive(
                            king.getSquare(),
                            entry.getValue().opposite(),
                            entry.getKey().isSingleMovePiece()
                        )
                        .getProjection()
                        .stream()
                )
                .toList();

            if (!kingThreatened.isEmpty()) {
                validMoveSquares.retainAll(kingThreatened);
            }
        }
    }

    public String toTypeString() {
        String representationString = String.valueOf(type.getDenomination());
        return color == Color.BLACK ? representationString.toLowerCase() : representationString;
    }

    public boolean isSingleMovePiece() {
        return switch(type) {
            case PAWN, KING, KNIGHT -> true;
            case BISHOP, ROOK, QUEEN -> false;
        };
    }

    public String toString() {
        return String.format("%s:%s:%s", color, type, square);
    }
}
