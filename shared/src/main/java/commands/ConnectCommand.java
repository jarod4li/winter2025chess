package commands;
import chess.ChessGame;
import websocket.commands.UserGameCommand;
public class ConnectCommand extends UserGameCommand {
    private ChessGame.TeamColor playerColor;
    public ConnectCommand(String authToken, ChessGame.TeamColor color, Integer gameID) {
        super(CommandType.CONNECT, authToken, gameID);
        this.playerColor=color;
    }
    public void setColor(ChessGame.TeamColor color) {
        this.playerColor = color;
    }
    public ChessGame.TeamColor getColor() {
        return playerColor;
    }
}
