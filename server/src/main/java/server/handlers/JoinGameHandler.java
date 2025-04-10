package server.handlers;
import service.JoinGameService;
import reqAndRes.JoinGameRequest;
import reqAndRes.JoinGameResponse;
import spark.*;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
public class JoinGameHandler implements Route{
    @Override
    public Object handle(Request request, Response response) throws Exception {
        JoinGameRequest joinGameReq = new Gson().fromJson(request.body(), JoinGameRequest.class);
        String authToken = request.headers("Authorization");
        JoinGameService joinGameServ = new JoinGameService();
        JoinGameResponse joinGameResp = joinGameServ.joinGame(joinGameReq, authToken);
        if(joinGameResp.getMessage() == null){
            response.status(200);
        }
        else if(joinGameResp.getMessage().contains("Error! No game")){
            response.status(400);
        }
        else if(joinGameResp.getMessage().contains("Error! Give auth Token")){
            response.status(401);
        }
        else if(joinGameResp.getMessage().contains("Error! Color is taken")){
            response.status(403);
        }
        else{
            response.status(500);
        }
        return new Gson().toJson(joinGameResp);
    }
}
