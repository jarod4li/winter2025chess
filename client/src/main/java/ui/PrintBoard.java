package ui;
import chess.*;
import static ui.EscapeSequences.*;

public class PrintBoard {
    private static final String EMPTY = " \u2003 ";
    private final ChessBoard board;

    public PrintBoard(ChessBoard board){
        this.board = board;
        board.resetBoard();
    }
    private static String getType(ChessPiece piece){
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                return WHITE_PAWN;
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                return WHITE_KING;
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                return WHITE_QUEEN;
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return WHITE_KNIGHT;
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                return WHITE_BISHOP;
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                return WHITE_ROOK;
            }
        }
        else{
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                return BLACK_PAWN;
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                return BLACK_KING;
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                return BLACK_QUEEN;
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return BLACK_KNIGHT;
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                return BLACK_BISHOP;
            }
            else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                return BLACK_ROOK;
            }
        }
        return null;
    }
    public static void drawing(int row, int col, ChessBoard board){
        if (row == 0 || col == 0 || row == 9 || col == 9) {
            System.out.print(SET_BG_COLOR_WHITE);
            if ((row > 0 && row < 9 && col == 9) || (row > 0 && row < 9 && col == 0)) {
                System.out.print("\u2003" + (row) + " ");
            }
            else if ((col > 0 && col < 9 && row == 0) || (col > 0 && col < 9 && row == 9)) {
                char column = (char) ('h' + 1 - col);
                System.out.print("\u2003" + column + " ");
            }
            else {
                System.out.print(EMPTY);
            }
        }
        else {
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece piece = board.getPiece(position);
            if (piece != null) {
                if ((row + col) % 2 == 0) {
                    System.out.print(SET_BG_COLOR_YELLOW);
                } else {
                    System.out.print(SET_BG_COLOR_GREEN);
                }
                System.out.print(getType(board.getPiece(position)));
            }
            else {
                if ((row + col) % 2 != 0) {
                    System.out.print(SET_BG_COLOR_GREEN);
                    System.out.print(EMPTY);
                }
                else {
                    System.out.print(SET_BG_COLOR_YELLOW);
                    System.out.print(EMPTY);
                }
            }
        }
    }
    public static void drawForPlayer1(ChessBoard board) {
        int row = 0;
        while (row < 10) {
            int col = 0;
            while (col < 10) {
                drawing(row, col, board);
                col++;
            }
            System.out.print(RESET_BG_COLOR);
            System.out.print("\n");
            row++;
        }
    }
    public static void drawForPlayer2(ChessBoard board) {
        int row = 9;
        while (row >= 0) {
            int col = 9;
            while (col >= 0) {
                drawing(row,col, board);
                col--;
            }
            System.out.print(RESET_BG_COLOR);
            System.out.print("\n");
            row--;
        }
    }
}
