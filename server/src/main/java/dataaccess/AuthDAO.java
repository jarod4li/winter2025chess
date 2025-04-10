package dataaccess;

import java.util.*;
import model.AuthData;
import java.sql.*;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class AuthDAO {

    public AuthData returnToken(String authToken) throws DataAccessException {
        String query = "SELECT * FROM authtoken WHERE authToken=?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)) {

            prepStatement.setString(1, authToken);

            try (ResultSet resSet = prepStatement.executeQuery()) {
                if (resSet.next()) {
                    String retrievedToken = resSet.getString("authToken");
                    String username = resSet.getString("username");
                    return new AuthData(retrievedToken, username);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving token: " + e.getMessage());
        }
    }

    public void createToken(String authToken, String username) throws DataAccessException {
        String query = "INSERT INTO authtoken (authToken, username) VALUES(?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query, RETURN_GENERATED_KEYS)) {

            prepStatement.setString(1, authToken);
            prepStatement.setString(2, username);
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error creating token: " + e.getMessage());
        }
    }

    public void clear(Connection conn) throws DataAccessException {
        String query = "DELETE FROM authtoken";
        try (PreparedStatement prepStatement = conn.prepareStatement(query)) {
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing tokens: " + e.getMessage());
        }
    }

    public void delete(String authToken) throws DataAccessException {
        String query = "DELETE FROM authtoken WHERE authToken=?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)) {

            prepStatement.setString(1, authToken);
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting token: " + e.getMessage());
        }
    }
}
