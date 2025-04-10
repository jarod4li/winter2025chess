package server.handlers;
import spark.*;
import spark.Request;
import spark.Response;
import service.LogoutService;
import reqAndRes.*;
import com.google.gson.Gson;
public class LogoutHandler implements Route{
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String authToken = request.headers("Authorization");
        LogoutService logoutServ = new LogoutService();
        LogoutResponse logoutResp = logoutServ.logout(authToken);
        if(logoutResp.getMessage() == null){
            response.status(200);
        }
        else if(logoutResp.getMessage().contains("Error! No auth token")){
            response.status(401);
        }
        else{
            response.status(500);
        }
        return new Gson().toJson(logoutResp);
    }
}
