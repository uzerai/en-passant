package game;

import game.movement.MovementProjector;
import game.piece.Piece;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    private final EnumMap<Square, Piece> boardState;
    private final Stack<Move> moveHistory = new Stack<>();

    private final MovementProjector projectionEngine;

    public Board() {
        this.boardState = Stream.of(Square.values())
            .collect(
                ()-> new EnumMap<>(Square.class),
                (EnumMap<Square, Piece> m, Square square) -> m.put(square, null),
                EnumMap::putAll
            );
        this.projectionEngine = new MovementProjector(this);
    }

    public void fillStartingPositions() {
        for (Square square : Square.values()) {
            this.putPiece(Piece.fromStartingSquare(square), square);
        }
    }

    public Piece getPiece(Square square) {
        return boardState.get(square);
    }

    public Set<Piece> getPieces() {
        return boardState.values().stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public void putPiece(Piece piece, Square square) {
        System.err.printf("[Board]#putPiece([%s], %s)%n", piece, square);
        if (piece == null)
            return;

        if (piece.getBoard() == this) {
            this.moveHistory.push(new Move(piece, square));
            removePieceFromSquare(piece.getSquare());
        }

        piece.setBoard(this);
        piece.setSquare(square);

        boardState.put(square, piece);
        System.err.print(this);
        updateBoardPieces();
    }

    public void removePieceFromSquare(Square square) {
        Piece piece = getPiece(square);
        if (piece != null)
            piece.setSquare(null);

        boardState.put(square, null);
        updateBoardPieces();
    }

    public EnumMap<Square, Piece> getBoardState() {
        return this.boardState;
    }

    public MovementProjector getProjectionEngine() {
        return this.projectionEngine;
    }

    /**
     * <p>Returns the threatening squares <i><strong>of the color provided</strong></i>.</p>
     * <p> ie: {@code Piece.Color.WHITE} will return all {@code Piece.Color.WHITE} pieces' threatened squares.
     *
     * @param color the {@code Piece.Color} to return threatened squares for.
     * @return set of threatened {@code Square}s by the given {@code color}.
     */
    public EnumSet<Square> getThreateningSquares(Piece.Color color) {
        EnumSet<Square> threatenedSquares = EnumSet.noneOf(Square.class);
        for(Piece piece : getPieces()) {
            if (piece.getColor() == color) {
                threatenedSquares.addAll(piece.threateningSquares());
            }
        }
        return threatenedSquares;
    }

    public Move getLastMove() {
        if(moveHistory.empty())
            return null;

        return moveHistory.peek();
    }

    private void updateBoardPieces() {
        System.err.println("[Board]#updateBoardPieces()");
        for (Piece piece : getPieces()) {
            piece.updateValidMoveSquares();
        }
    }

    public String toString() {
        ArrayList<String> boardPrint = new ArrayList<>();
        StringBuilder boardString = new StringBuilder();

        boardState.values()
            .forEach(
                piece -> {
                    if (piece != null) {
                        boardPrint.add(
                                String.format("[%s]", piece.toTypeString())
                        );
                    } else {
                        boardPrint.add("[ ]");
                    }
                }
            );

        // print row numbers
        boardString.append("   1  2  3  4  5  6  7  8 \n");
        for(int i = 0; i < boardPrint.size(); i++) {
            // print with letters for cols
            if( i % 8 == 0) {
                boardString.append(String.format("%c ", (char) 65 + (i / 8)));
            }
            boardString.append(String.format("%s%s", boardPrint.get(i), (i + 1) % 8 == 0 ? "\n" : ""));
        }

        return boardString.toString();
    }
}
