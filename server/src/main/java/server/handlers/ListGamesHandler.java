package server.handlers;
import service.ListGamesService;
import reqAndRes.ListGamesResponse;
import spark.Request;
import spark.Response;
import spark.*;
import com.google.gson.Gson;
public class ListGamesHandler implements Route{
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String authToken = request.headers("Authorization");
        ListGamesService listGamesServ = new ListGamesService();
        ListGamesResponse listGamesResp = listGamesServ.listGames(authToken);
        if(listGamesResp.getMessage() == null){
            response.status(200);
        }
        else if(listGamesResp.getMessage().contains("Error! No auth token")){
            response.status(401);
        }
        else{
            response.status(500);
        }
        return new Gson().toJson(listGamesResp);
    }
}
