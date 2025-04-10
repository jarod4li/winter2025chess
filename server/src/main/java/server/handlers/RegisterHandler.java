package server.handlers;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.*;
import reqAndRes.*;
import service.*;
public class RegisterHandler implements Route{
    @Override
    public Object handle(Request request, Response response) throws Exception {
        RegRequest registerRequest = new Gson().fromJson(request.body(), RegRequest.class);
        RegisterService service = new RegisterService();
        RegResponse regResponse = service.registration(registerRequest);
        if(regResponse.getMessage() == null){
            response.status(200);
        }
        else if(regResponse.getMessage().contains("Error! Fill all required fields!")){
            response.status(400);
        }
        else if(regResponse.getMessage().contains("Error! Name already being used!")){
            response.status(403);
        }
        else{
            response.status(500);
        }
        return new Gson().toJson(regResponse);
    }
}
