package server;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import server.handlers.*;
import spark.*;
import java.sql.*;

public class Server {
    public Server(){
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS `authtoken` (
              `authToken` varchar(45) NOT NULL,
              `username` varchar(45) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS `game` (
              `blackUsername` varchar(45) DEFAULT NULL,
              `game` blob NOT NULL,
              `gameID` int NOT NULL AUTO_INCREMENT,
              `gameName` varchar(45) NOT NULL,
              `whiteUsername` varchar(45) DEFAULT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS `user` (
              `email` varchar(150) NOT NULL,
              `password` varchar(150) NOT NULL,
              `username` varchar(150) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (java.sql.Connection conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        Spark.webSocket("/ws", WebSocket.class);
        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", new ClearHandler());
        Spark.post("/user", new RegisterHandler());
        Spark.post("/game", new CreateGameHandler());
        Spark.post("/session", new LoginHandler());
        Spark.put("/game", new JoinGameHandler());
        Spark.get("/game", new ListGamesHandler());
        Spark.delete("/session", new LogoutHandler());
        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
