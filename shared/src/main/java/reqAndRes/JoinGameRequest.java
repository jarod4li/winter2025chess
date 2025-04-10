package reqAndRes;
import chess.ChessGame;
public class JoinGameRequest {
    public JoinGameRequest(ChessGame.TeamColor playerColor, int gameID){
        this.playerColor = playerColor;
        this.gameID = gameID;
    }
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
    public int getGameID() {
        return gameID;
    }
    public void setPlayerColor(ChessGame.TeamColor colorOfPlayer) {
        this.playerColor = colorOfPlayer;
    }
    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
    private ChessGame.TeamColor playerColor;
    private int gameID;
}
