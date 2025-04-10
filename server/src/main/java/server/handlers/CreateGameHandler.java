package server.handlers;
import reqAndRes.CreateGameRequest;
import reqAndRes.CreateGameResponse;
import service.CreateGameService;
import com.google.gson.*;
import spark.*;
public class CreateGameHandler implements Route{
    @Override
    public Object handle(Request request, Response response) throws Exception {
        CreateGameRequest gameCreation = new Gson().fromJson(request.body(), CreateGameRequest.class);
        String authToken = request.headers("Authorization");
        CreateGameService service = new CreateGameService();
        CreateGameResponse gameResponse = service.gameCreator(gameCreation, authToken);
        if(gameResponse.getMessage() == null){
            response.status(200);
        }
        else if(gameResponse.getMessage().contains("Error! No name")){
            response.status(400);
        }
        else if(gameResponse.getMessage().contains("Error! Need authorization")){
            response.status(401);
        }
        else{
            response.status(500);
        }
        return new Gson().toJson(gameResponse);
    }
}
