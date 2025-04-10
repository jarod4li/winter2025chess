package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    private ChessBoard board;
    private ChessPosition myPosition;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type && Objects.equals(board, that.board) && Objects.equals(myPosition, that.myPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type, board, myPosition);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                ", board=" + board +
                ", myPosition=" + myPosition +
                '}';
    }

    /**
     * @return Which team this chess piece belongs to
     */

    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> bishopCalculation(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.addAll(directionCalculation(board, position, 1, 1));
        moves.addAll(directionCalculation(board, position, 1, -1));
        moves.addAll(directionCalculation(board, position, -1, 1));
        moves.addAll(directionCalculation(board, position, -1, -1));
        return moves;
    }
    private Collection<ChessMove> rookCalculation(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.addAll(directionCalculation(board, position, 1, 0));
        moves.addAll(directionCalculation(board, position, -1, 0));
        moves.addAll(directionCalculation(board, position, 0, 1));
        moves.addAll(directionCalculation(board, position, 0, -1));
        return moves;
    }
    private Collection<ChessMove> queenCalculation(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.addAll(bishopCalculation(board, position));
        moves.addAll(rookCalculation(board, position));
        return moves;
    }
    private Collection<ChessMove> directionCalculation(ChessBoard board, ChessPosition position, int rowIncrementer, int colIncrementer) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        while (true) {
            row += rowIncrementer;
            col += colIncrementer;
            if (row < 1 || row > 8 || col < 1 || col > 8) {
                break;
            }
            ChessPosition aheadPosition = new ChessPosition(row, col);
            ChessPiece aheadPiece = board.getPiece(aheadPosition);
            if (aheadPiece == null) {
                moves.add(new ChessMove(position, aheadPosition, null));
            } else {
                if (aheadPiece.getTeamColor() != this.pieceColor) {
                    moves.add(new ChessMove(position, aheadPosition, null));
                }
                break;
            }
        }
        return moves;
    }
    public static void lookAhead(int aheadRow, int aheadCol, ChessBoard board, Collection<ChessMove> moves, ChessPosition position){
        if (aheadRow >= 1 && aheadRow <= 8 && aheadCol >= 1 && aheadCol <= 8) {
            ChessPosition aheadPosition = new ChessPosition(aheadRow, aheadCol);
            ChessPiece myPiece = board.getPiece(position);
            ChessPiece aheadPiece = board.getPiece(aheadPosition);
            if (aheadPiece == null || aheadPiece.getTeamColor() != myPiece.pieceColor) {
                moves.add(new ChessMove(position, aheadPosition, null));
            }
        }
    }
    private Collection<ChessMove> knightCalculation(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] jumps = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
        for (int[] jump : jumps) {
            int aheadRow = position.getRow() + jump[0];
            int aheadCol = position.getColumn() + jump[1];
            lookAhead(aheadRow, aheadCol, board, moves, position);
        }

        return moves;
    }
    private Collection<ChessMove> kingCalculation(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }

                int aheadRow = position.getRow() + rowOffset;
                int aheadCol = position.getColumn() + colOffset;
                lookAhead(aheadRow, aheadCol, board, moves, position);
            }
        }
        return moves;
    }
    private Collection<ChessMove> pawnCalculation(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int direction;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            direction = 1;
        } else {
            direction = -1;
        }
        int startRow;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            startRow = 2;
        } else {
            startRow = 7;
        }
        int promoRow;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            promoRow = 8;
        } else {
            promoRow = 1;
        }
        ChessPosition aheadPosition = new ChessPosition(position.getRow() + direction, position.getColumn());
        if (board.getPiece(aheadPosition) == null) {
            if (aheadPosition.getRow() == promoRow) {
                addPromotionMoves(moves, position, aheadPosition);
            } else {
                moves.add(new ChessMove(position, aheadPosition, null));
            }
            if (position.getRow() == startRow) {
                ChessPosition twiceMoved = new ChessPosition(position.getRow() + 2 * direction, position.getColumn());
                if (board.getPiece(twiceMoved) == null) {
                    moves.add(new ChessMove(position, twiceMoved, null));
                }
            }
        }
        int[] diags = {-1, 1};
        for (int diag : diags) {
            ChessPosition capturable = new ChessPosition(position.getRow() + direction, position.getColumn() + diag);
            if (positionValidator(capturable)) {
                ChessPiece capturablePiece = board.getPiece(capturable);
                if (capturablePiece != null && capturablePiece.getTeamColor() != this.pieceColor) {
                    if (capturable.getRow() == promoRow) {
                        addPromotionMoves(moves, position, capturable);
                    }
                    else {
                        moves.add(new ChessMove(position, capturable, null));
                    }
                }
            }
        }

        return moves;
    }
    private boolean positionValidator(ChessPosition position) {
        return position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8;
    }
    private void addPromotionMoves(Collection<ChessMove> moves, ChessPosition start, ChessPosition end) {
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
    }



    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (this.type == PieceType.BISHOP) {
            return bishopCalculation(board, myPosition);
        }
        if (this.type == PieceType.KING) {
            return kingCalculation(board, myPosition);
        }
        if (this.type == PieceType.KNIGHT) {
            return knightCalculation(board, myPosition);
        }
        if (this.type == PieceType.PAWN) {
            return pawnCalculation(board, myPosition);
        }
        if (this.type == PieceType.ROOK) {
            return rookCalculation(board, myPosition);
        }
        if (this.type == PieceType.QUEEN) {
            return queenCalculation(board, myPosition);
        }
        return null;
    }
}