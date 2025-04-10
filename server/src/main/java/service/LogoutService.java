package service;
import dataaccess.*;
import reqAndRes.LogoutResponse;

public class LogoutService {
    private AuthDAO authDAO = new AuthDAO();
    public LogoutResponse logout(String authToken) {
        try{
            if(authDAO.returnToken(authToken) == null){
                return new LogoutResponse("Error! No auth token");
            }
            else{
                authDAO.delete(authToken);
                return new LogoutResponse();
            }
        }
        catch(Exception exception){
            return new LogoutResponse(exception.getMessage());
        }
    }
}
