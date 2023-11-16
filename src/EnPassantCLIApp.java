import game.Board;
import game.Square;
import game.piece.Pawn;
import game.piece.Piece;
import game.piece.Rook;

public class EnPassantCLIApp {

    private final Board board = new Board();

    public static void main(String[] args) {
        EnPassantCLIApp app = new EnPassantCLIApp();
        app.run();
    }

    public void run() {
        Rook rook = new Rook(Piece.Color.WHITE);
        board.putPiece(rook, Square.E4);
        board.putPiece(new Pawn(Piece.Color.BLACK), Square.B4);
        board.putPiece(new Pawn(Piece.Color.WHITE), Square.E7);
        printBoard();
        System.out.println(rook.validMoveSquares());
    }

    private void printBoard() {
        System.out.println(this.board);
    }
}
