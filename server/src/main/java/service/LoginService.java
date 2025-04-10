package service;
import  dataaccess.*;
import model.*;
import reqAndRes.LoginResponse;
import reqAndRes.LoginRequest;
import java.util.UUID;
import java.sql.*;
import org.mindrot.jbcrypt.*;
public class LoginService {
    private AuthDAO authDAO = new AuthDAO();
    private UserDAO userDAO = new UserDAO();
    private DatabaseManager dbMan = new DatabaseManager();
    private Connection connection;

    public LoginService() {
        try {
            Connection conn = dbMan.getConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public LoginResponse logger(LoginRequest request) {
        try{
            if(request.getName() != null && request.getPassword() != null){
                UserData thisUser = userDAO.returnUser(request.getName());
                //if (thisUser != null && thisUser.getPassword().equals(request.getPassword())){
                    if (thisUser != null && BCrypt.checkpw(request.getPassword(), thisUser.getPassword()) == true) {
                    String authToken = UUID.randomUUID().toString();
                    authDAO.createToken(new AuthData(authToken, request.getName()).getAuth(), request.getName());
                    return new LoginResponse(request.getName(), authToken);
                }
                else{
                    return new LoginResponse("Error! Wrong password");
                }
            }
            else{
                return new LoginResponse();
            }
        }
        catch(Exception exception){
            return new LoginResponse(exception.getMessage());
        }
    }
}
