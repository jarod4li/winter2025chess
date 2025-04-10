package model;
import chess.ChessGame;
import java.util.Objects;
public class GameData implements Comparable{
    private ChessGame game;
    private String gameName;
    private int gameID;
    private String blackUsername;
    private String whiteUsername;

    //Put in placeholder methods for lower parameters
    public GameData(){}

    public GameData(String gameName){

    }
    public GameData(int gameID, String blackUsername, String whiteUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.game = game;
        this.blackUsername = blackUsername;
        this.whiteUsername = whiteUsername;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "game=" + game +
                ", gameName='" + gameName + '\'' +
                ", gameID=" + gameID +
                ", blackUsername='" + blackUsername + '\'' +
                ", whiteUsername='" + whiteUsername + '\'' +
                '}';
    }

    public int getID(){
        return gameID;
    }
    public void setID(int gameID){
        this.gameID = gameID;
    }
    public String getBlackUsername(){
        return blackUsername;
    }
    public void setBlackUsername(String blackUsername){
        this.blackUsername = blackUsername;
    }
    public String getWhiteUsername(){
        return whiteUsername;
    }
    public void setWhiteUsername(String whiteUsername){
        this.whiteUsername = whiteUsername;
    }
    public String getGameName(){
        return gameName;
    }
    public void setGameName(String gameName){
        this.gameName = gameName;
    }
    public ChessGame getChessGame(){
        return game;
    }
    public void setGame(ChessGame game){
        this.game = game;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        GameData gameData = (GameData) o;
        return gameID == gameData.gameID && Objects.equals(blackUsername, gameData.blackUsername)
                && Objects.equals(whiteUsername,
                gameData.whiteUsername) && Objects.equals(gameName, gameData.gameName)
                && Objects.equals(game, gameData.game);
    }
    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName);
    }
    @Override
    public int compareTo(Object o) {
        return o.hashCode() - this.hashCode();
    }
}
