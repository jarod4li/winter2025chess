package dataaccess;

import chess.ChessGame;
import model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.*;
import java.util.*;

public class GameDAO {

    private static final String INSERT_GAME_SQL = "INSERT INTO game (gameName, game) VALUES(?, ?)";
    private static final String SELECT_GAME_SQL = "SELECT * FROM game WHERE gameID=?";
    private static final String SELECT_ALL_GAMES_SQL = "SELECT * FROM game";
    private static final String UPDATE_GAME_SQL = "UPDATE game SET game=?, whiteUsername=?, blackUsername=? WHERE gameID=?";
    private static final String DELETE_GAME_SQL = "DELETE FROM game";
    private static final String UPDATE_BLACK_USERNAME_SQL = "UPDATE game SET blackUsername=? WHERE gameID=?";
    private static final String UPDATE_WHITE_USERNAME_SQL = "UPDATE game SET whiteUsername=? WHERE gameID=?";

    private static final Gson gson = new Gson();

    public void clear(Connection connection) throws DataAccessException {
        try (PreparedStatement prepStatement = connection.prepareStatement(DELETE_GAME_SQL)) {
            prepStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataAccessException("Error clearing games: " + exception.getMessage());
        }
    }

    public int createGame(String gameName) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(INSERT_GAME_SQL, Statement.RETURN_GENERATED_KEYS)) {

            prepStatement.setString(1, gameName);
            prepStatement.setString(2, gson.toJson(new ChessGame()));
            prepStatement.executeUpdate();

            try (ResultSet resSet = prepStatement.getGeneratedKeys()) {
                if (resSet.next()) {
                    return resSet.getInt(1);
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException("Error creating game: " + exception.getMessage());
        }
        return 0;
    }

    public GameData returnGame(int gameID) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(SELECT_GAME_SQL)) {

            prepStatement.setInt(1, gameID);

            try (ResultSet rs = prepStatement.executeQuery()) {
                if (rs.next()) {
                    String json = rs.getString("game");
                    String gameName = rs.getString("gameName");
                    String blackUsername = rs.getString("blackUsername");
                    String whiteUsername = rs.getString("whiteUsername");

                    ChessGame game = gson.fromJson(json, ChessGame.class);
                    return new GameData(gameID, blackUsername, whiteUsername, gameName, game);
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException("Error retrieving game: " + exception.getMessage());
        }
        return null;
    }

    public Collection<GameData> returnGameMap() throws DataAccessException {
        Collection<GameData> gameArrays = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(SELECT_ALL_GAMES_SQL);
             ResultSet rs = prepStatement.executeQuery()) {

            while (rs.next()) {
                String json = rs.getString("game");
                int gameID = rs.getInt("gameID");
                String gameName = rs.getString("gameName");
                String blackUsername = rs.getString("blackUsername");
                String whiteUsername = rs.getString("whiteUsername");

                ChessGame game = gson.fromJson(json, ChessGame.class);
                GameData updatedGame = new GameData(gameID, blackUsername, whiteUsername, gameName, game);
                gameArrays.add(updatedGame);
            }
        } catch (SQLException exception) {
            throw new DataAccessException("Error retrieving all games: " + exception.getMessage());
        }
        return gameArrays;
    }

    public void playerNamer(String username, int gameID, ChessGame.TeamColor color) throws DataAccessException {
        GameData thisGame = returnGame(gameID);
        if (color == ChessGame.TeamColor.BLACK && thisGame.getBlackUsername() != null) {
            throw new DataAccessException("Black player slot is already taken.");
        }
        if (color == ChessGame.TeamColor.WHITE && thisGame.getWhiteUsername() != null) {
            throw new DataAccessException("White player slot is already taken.");
        }

        String query = (color == ChessGame.TeamColor.BLACK) ? UPDATE_BLACK_USERNAME_SQL : UPDATE_WHITE_USERNAME_SQL;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query)) {

            prepStatement.setString(1, username);
            prepStatement.setInt(2, gameID);
            prepStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataAccessException("Error assigning player: " + exception.getMessage());
        }
    }

    public void updateGame(GameData game) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_GAME_SQL)) {

            preparedStatement.setString(1, gson.toJson(game.getChessGame()));

            if (game.getWhiteUsername() != null) {
                preparedStatement.setString(2, game.getWhiteUsername());
            } else {
                preparedStatement.setNull(2, Types.VARCHAR);
            }

            if (game.getBlackUsername() != null) {
                preparedStatement.setString(3, game.getBlackUsername());
            } else {
                preparedStatement.setNull(3, Types.VARCHAR);
            }

            preparedStatement.setInt(4, game.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new DataAccessException("Error updating game: " + exception.getMessage());
        }
    }
}
