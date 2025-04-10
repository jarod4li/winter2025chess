package service;
import reqAndRes.ClearResponse;
import dataaccess.*;
import java.sql.Connection;
import java.sql.SQLException;

public class ClearService {
    public ClearResponse clearEverything() {
        GameDAO game = new GameDAO();
        UserDAO user = new UserDAO();
        AuthDAO auth = new AuthDAO();
        try (Connection connection = new DatabaseManager().getConnection()) {
            game.clear(connection);
            user.clear(connection);
            auth.clear(connection);
        }
        catch (DataAccessException | SQLException exception) {
            return new ClearResponse(exception.getMessage());
        }
        return new ClearResponse();
    }
}
