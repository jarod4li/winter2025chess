package dataaccess;
import chess.ChessGame;
import java.util.*;
import java.lang.*;
import model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import chess.ChessBoard;
import chess.ChessPiece;
import java.sql.Connection;
import java.sql.SQLException;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
public class GameDAO {
    public void clear(Connection connection) throws DataAccessException {
        try (var prepStatement = connection.prepareStatement("DELETE FROM game")) {
            prepStatement.executeUpdate();
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
    public int createGame(String gameName) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var prepStatement = connection.prepareStatement("INSERT INTO game (gameName, game) VALUES(?, ?)", RETURN_GENERATED_KEYS)) {
                prepStatement.setString(1, gameName);
                prepStatement.setString(2, new Gson().toJson(new ChessGame()));
                prepStatement.executeUpdate();
                var gameID = 0;
                var resSet = prepStatement.getGeneratedKeys();
                if (resSet.next()) {
                    gameID = resSet.getInt(1);
                }
                return gameID;
            }
        }
        catch(SQLException exception){
            throw new DataAccessException(exception.getMessage());
        }
    }
    public GameData returnGame(int gameID) throws DataAccessException{
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var prepStatement = connection.prepareStatement("SELECT* FROM game WHERE gameID=?")) {
                prepStatement.setInt(1, gameID);
                try (var rs = prepStatement.executeQuery()) {
                    if (rs.next()) {
                        var json = rs.getString("game");
                        gameID = rs.getInt("gameID");
                        var gameName = rs.getString("gameName");
                        var blackUsername = rs.getString("blackUsername");
                        var whiteUsername = rs.getString("whiteUsername");
                        var game = new Gson().fromJson(json, ChessGame.class);
                        return new GameData(gameID, blackUsername, whiteUsername, gameName, game);
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
    public Collection<GameData> returnGameMap() throws DataAccessException{
        Collection<GameData> gameArrays  = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            try (var prepStatement = connection.prepareStatement("SELECT* FROM game")) {
                try (var rs = prepStatement.executeQuery()) {
                    while (rs.next()) {
                        var json = rs.getString("game");
                        var gameID = rs.getInt("gameID");
                        var gameName = rs.getString("gameName");
                        var blackUsername = rs.getString("blackUsername");
                        var whiteUsername = rs.getString("whiteUsername");
                        var assembler = new GsonBuilder();
                        var game = assembler.create().fromJson(json, ChessGame.class);
                        GameData updatedGame = new GameData(gameID, blackUsername, whiteUsername, gameName, game);
                        gameArrays.add(updatedGame);
                    }
                    return gameArrays;
                }
            }
        }
        catch(SQLException exception){
            throw new DataAccessException(exception.getMessage());
        }
    }
    public void playerNamer(String username, int gameID, ChessGame.TeamColor color) throws DataAccessException {
        var thisGame = returnGame(gameID);
        if (color == ChessGame.TeamColor.BLACK) {
            if(thisGame.getBlackUsername() != null){
                throw new DataAccessException("Nope. Taken");
            }
            try (Connection connection = DatabaseManager.getConnection()) {
                try (var prepStatement = connection.prepareStatement("UPDATE game SET blackUsername=? WHERE gameID=?")) {
                    prepStatement.setInt(2, gameID);
                    prepStatement.setString(1, username);
                    prepStatement.executeUpdate();
                }
            }
            catch (SQLException exception) {
                throw new DataAccessException(exception.getMessage());
            }
        }
        else {
            if(thisGame.getWhiteUsername() != null){
                throw new DataAccessException("Nope. Taken");
            }
            try (Connection connection = DatabaseManager.getConnection()) {
                try (var prepStatement = connection.prepareStatement("UPDATE game SET whiteUsername = ? WHERE gameID = ?")) {
                    prepStatement.setInt(2, gameID);
                    prepStatement.setString(1, username);
                    prepStatement.executeUpdate();
                }
            }
            catch (SQLException exception) {
                throw new DataAccessException(exception.getMessage());
            }
        }
    }
    public void updateGame(GameData game) throws DataAccessException {
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement("UPDATE game SET game =?, whiteUsername = ?, blackUsername = ? WHERE gameID=?")) {
            preparedStatement.setString(1, new Gson().toJson(game.getChessGame()));
            if (game.getWhiteUsername() != null) {
                preparedStatement.setString(2, game.getWhiteUsername());
            } else {
                preparedStatement.setNull(2, java.sql.Types.VARCHAR);
            }
            if (game.getBlackUsername() != null) {
                preparedStatement.setString(3, game.getBlackUsername());
            } else {
                preparedStatement.setNull(3, java.sql.Types.VARCHAR);
            }
            preparedStatement.setInt(4, game.getID());
            preparedStatement.executeUpdate();
        }
        catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }
}