package server.handlers;
import com.google.gson.Gson;
import reqAndRes.*;
import service.LoginService;
import spark.*;
import spark.Request;
public class LoginHandler implements Route{
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LoginRequest loginReq = new Gson().fromJson(request.body(), LoginRequest.class);
        LoginService loginServ = new LoginService();
        LoginResponse loginResp = loginServ.logger(loginReq);
        if(loginResp.getMessage() == null){
            response.status(200);
        }
        else if(loginResp.getMessage().contains("Error! Wrong password")){
            response.status(401);
        }
        else{
            response.status(500);
        }
        return new Gson().toJson(loginResp);
    }
}
