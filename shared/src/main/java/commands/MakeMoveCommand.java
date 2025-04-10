package commands;
import chess.ChessMove;
import websocket.commands.UserGameCommand;
public class MakeMoveCommand extends UserGameCommand {
    private ChessMove move;
    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = (ChessMove) move;
    }
    public void setMove(ChessMove move) {
        this.move = (ChessMove) move;
    }
    public ChessMove getMove() {
        return move;
    }
}
