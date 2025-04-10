package service;
import dataaccess.*;
import reqAndRes.ListGamesResponse;
public class ListGamesService {
    private AuthDAO authDAO = new AuthDAO();
    private GameDAO gameDAO = new GameDAO();
    public ListGamesResponse listGames(String authToken) {
        try{
            ListGamesResponse gameMap = new ListGamesResponse();
            if(authDAO.returnToken(authToken) == null){
                return new ListGamesResponse("Error! No auth token");
            }
            else{
                gameMap.setGames(gameDAO.returnGameMap());
                return gameMap;
            }
        }
        catch (Exception exception){
            return new ListGamesResponse(exception.getMessage());
        }
    }
}
