package reqAndRes;
import model.GameData;
import java.util.*;
public class ListGamesResponse {
    public ListGamesResponse() {}
    public ListGamesResponse(String message){
        this.message = message;
    }
    public ListGamesResponse(String message, Collection<GameData> games){
        this.games = games;
        this.message = message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setGames(Collection<GameData> games) {
        this.games = games;
    }
    public Collection<GameData> getGames() {
        return games;
    }
    private Collection<GameData> games;
    private String message;
}
