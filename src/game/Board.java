package game;

import game.piece.Piece;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
    private final EnumMap<Square, Piece> boardState;

    public Board() {
        this.boardState = Stream.of(Square.values())
            .collect(
                ()-> new EnumMap<>(Square.class),
                (EnumMap<Square, Piece> m, Square square) -> m.put(square, null),
                EnumMap::putAll
            );
    }

    public void fillStartingPositions() {
        // sets up all the pieces as expected in a game of normal chess.
        this.boardState.putAll( Stream.of(Square.values())
            .collect(
                ()-> new EnumMap<>(Square.class),
                (EnumMap<Square, Piece> m, Square square) -> m.put(square, Piece.fromStartingSquare(square, this)),
                EnumMap::putAll
            ));
    }

    public void clearBoard() {
        this.boardState.clear();
    }

    public Piece getPiece(Square square) {
        return boardState.get(square);
    }

    public void putPiece(Piece piece, Square square) {
        //TODO: Maybe set piece#board to the board here?
        boardState.put(square, piece);
        if (piece.getPosition() != null && piece.getBoard() == this)
            clear(piece.getPosition());

        piece.setBoard(this);
        piece.setPosition(square);
    }

    public void clear(Square square) {
        Piece piece = getPiece(square);
        if (piece != null)
            piece.setPosition(null);

        boardState.put(square, null);
    }

    public Set<Piece> getPieces() {
        return boardState.values().stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public EnumMap<Square, Piece> getBoardState() {
        return this.boardState;
    }

    /**
     * <p>Returns the threatening squares <i><strong>of the color provided</strong></i>.</p>
     *
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
