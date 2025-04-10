package dataaccess;
import java.sql.SQLException;
import java.util.*;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserDAO {
    public void createUser(String username, String password, String email) throws DataAccessException{
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var prepStatement = connection.prepareStatement("INSERT INTO user (username, password, email) VALUES(?, ?, ?)")) {
                prepStatement.setString(1, username);
                prepStatement.setString(2, password);
                prepStatement.setString(3, email);
                prepStatement.executeUpdate();
            }
        }
        catch (SQLException exception) {
                throw new DataAccessException(exception.getMessage());
            }

    }
    public UserData returnUser(String username) throws DataAccessException{
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var prepStatement = connection.prepareStatement("SELECT * FROM user WHERE username=?")) {
                prepStatement.setString(1, username);
                try (var rs = prepStatement.executeQuery()) {
                    if (rs.next()) {
                        username = rs.getString("username");
                        var password = rs.getString("password");
                        var email = rs.getString("email");
                        return new UserData(username, password, email);
                    } else {
                        return null;
                    }
                }
            }
        }
        catch(SQLException exception){
            throw new DataAccessException(exception.getMessage());
        }
    }
    public void clear(Connection connection) throws DataAccessException{
        try (var prepStatement = connection.prepareStatement("DELETE FROM user")) {
            prepStatement.executeUpdate();
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
}
