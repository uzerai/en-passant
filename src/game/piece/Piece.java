package game.piece;

import game.Board;
import game.Square;
import game.movement.Direction;
import game.movement.MovementMapping;

import java.util.*;

public abstract class Piece {
    protected final Color color;
    protected final Type type;
    protected Board board;
    protected Square position;
    private boolean hasMoved = false;
    protected EnumSet<Direction> movementDirections;
    protected EnumSet<Direction> pinnedDirections = EnumSet.noneOf(Direction.class);
    protected HashMap<Piece, Direction> pinningPieces = new HashMap<>(); //can pin multiple pieces

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

    private Piece(Type type, Color color, Board board, Square position) {
        this.type = type;
        this.color = color;
        this.board = board;
        this.position = position;
    }

    protected Piece(Type type, Color color) {
        this(type, color, null, null);
    }

    public static Piece fromStartingSquare(Square square, Board board) {
        Piece piece = switch(square) {
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
        board.putPiece(piece, square);

        return piece;
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

    public Square getPosition() {
        return this.position;
    }

    public void setPosition(Square position) {
        this.position = position;
        updatePinnings();
    }

    public void setBoard(Board board) { this.board = board; }

    public Board getBoard() {
        return this.board;
    }

    public void move(Square newSquare) {
        if (validMoveSquares().contains(newSquare)) {
            board.putPiece(this, newSquare);
            hasMoved = true;
        } else {
            System.err.printf("Attempted invalid move for piece %s -> %s%n", this, newSquare);
        }
    }

    protected void setPiecePinnedBy(Direction direction, Piece piece) {
        piece.addPinnedByDirection(direction);
        pinningPieces.put(piece, direction);
    }

    protected void addPinnedByDirection(Direction direction) {
        Square nextSquare = position;
        Piece pieceInSquare;

        while(nextSquare != null) {
            pieceInSquare = board.getPiece(nextSquare);

            if (pieceInSquare != null && pieceInSquare != this) {
                if(pieceInSquare instanceof King && pieceInSquare.getColor() == this.color)
                    this.pinnedDirections.add(direction);
                return; // if the piece was not null, but not a king, there's no direct line between this and king
            } else {
                nextSquare = Square.fromValue(
                        nextSquare.getColumn() + direction.getDirectionX(),
                        nextSquare.getRow() + direction.getDirectionY()
                );
            }
        }
    }

    protected void removePinnedDirection(Direction direction) {
        this.pinnedDirections.remove(direction);
    }

    protected void clearPinning() {
        for (Piece pinnedPiece : pinningPieces.keySet()) {
            pinnedPiece.removePinnedDirection(pinningPieces.get(pinnedPiece));

            // King uses pinningPieces as a reverse lookup to attackers;
            if(pinnedPiece instanceof King) {
                pinnedPiece.pinningPieces.remove(this);
            }
        }

        pinningPieces.clear();
    }

    protected void updatePinnings() {
        if (this instanceof Pawn || this instanceof  King) {
            return;
        }
        clearPinning();

        int maxMoves = type == Type.KING ? 1 : -1;

        for (Direction direction : movementDirections) {
            Square nextSquare = position;
            Piece pieceInSquare;
            int calculatedSquares = 0;

            while (nextSquare != null && (calculatedSquares < maxMoves || maxMoves == -1)) {
                pieceInSquare = board.getPiece(nextSquare);
                if (pieceInSquare != null && pieceInSquare != this) {
                    if (pieceInSquare.getColor() != this.color) {
                        setPiecePinnedBy(direction, pieceInSquare);
                        pieceInSquare.setPiecePinnedBy(direction.opposite(), this);
                    }

                    nextSquare = null;
                } else {
                    nextSquare = Square.fromValue(
                            nextSquare.getColumn() + direction.getDirectionX(),
                            nextSquare.getRow() + direction.getDirectionY()
                    );
                    calculatedSquares++;
                }
            }
        }
    }

    protected void retainOnlyPinLegalMoves(EnumSet<Square> validSquares) {
        if (!this.pinnedDirections.isEmpty()) {
            List<Square> pinnedDirectionSquares = pinnedDirections.stream().flatMap(
                direction -> {
                    List<Square> results = new ArrayList<>(
                            MovementMapping.forPieceInDirection(this, direction.opposite()));
                    results.addAll(MovementMapping.forPieceInDirection(this, direction));

                    return results.stream();
                }
            ).toList();

            if (!pinnedDirectionSquares.isEmpty())
                validSquares.retainAll(pinnedDirectionSquares);
        }
    }

    protected void retainCheckBlockMoves(EnumSet<Square> validSquares) {
        Optional<Piece> colorKing = board.getPieces().stream().filter(
                piece -> piece.getColor() == this.color && piece instanceof King
        ).findAny();

        if(colorKing.isPresent()) {
            King king = (King) colorKing.get();
            List<Square> kingThreatened = king.inCheckFromDirections()
                    .stream()
                    .flatMap(direction ->
                        MovementMapping.forPieceInDirection(king, direction).stream()
                    )
                    .toList();
            if (!kingThreatened.isEmpty())
                validSquares.retainAll(kingThreatened);
        }
    }

    //TODO: Add rules around if the King is in check, forced moves.
    public abstract EnumSet<Square> validMoveSquares();

    public abstract EnumSet<Square> threateningSquares();

    public String toTypeString() {
        String representationString = String.valueOf(type.getDenomination());
        if (color == Color.BLACK) {
            representationString = representationString.toLowerCase();
        }
        return representationString;
    }

    public String toString() {
        return String.format("%s:%s:%s", color, type, position);
    }
}
