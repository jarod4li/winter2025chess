package service;
import java.util.UUID;
import reqAndRes.RegResponse;
import reqAndRes.RegRequest;
import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.*;
public class RegisterService {
    private AuthDAO auth = new AuthDAO();
    private UserDAO user = new UserDAO();
    public RegResponse registration(RegRequest req) {
        try {
            if(user.returnUser(req.getName()) != null){
                return new RegResponse("Error! Name already being used!");
            }
            if(req.getName() == null || req.getPassword() == null || req.getEmail() == null){
                return new RegResponse("Error! Fill all required fields!");
            }
            String hashedPassword = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt());
            //req.getPassword()
            UserData thisUser = new UserData(req.getName(), hashedPassword, req.getEmail());
            user.createUser(thisUser.getName(), thisUser.getPassword(), thisUser.getEmail());
            String tok = UUID.randomUUID().toString();
            auth.createToken(new AuthData(tok, req.getName()).getAuth(), req.getName());
            return new RegResponse(req.getName(), tok);
        }
        catch(Exception exception){
            return new RegResponse(exception.getMessage());
        }

    }
}
