package dataaccess;
import model.*;
import chess.ChessGame;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.sql.Connection;
import java.sql.SQLException;

public class GameTests {
    private static GameDAO gameDao;

    @BeforeEach
    public void setUp() throws DataAccessException, SQLException {
        gameDao = new GameDAO();
        try (Connection connection = new DatabaseManager().getConnection()) {
            gameDao.clear(connection);
        }
    }

    @Test
    public void testClear() {
        try {
            gameDao.createGame("testerGame");
            try (Connection connection = new DatabaseManager().getConnection()) {
                gameDao.clear(connection);
            }
            Collection<GameData> games = gameDao.returnGameMap();
            assertTrue(games.isEmpty());
            assertNotNull(games);
        } catch (DataAccessException | SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testCreateGame() {
        try {
            gameDao.createGame("NameOfGame");
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testDittoGame() {
        try {
            gameDao.createGame("NameOfGame");
            try (Connection connection = new DatabaseManager().getConnection()) {
                gameDao.clear(connection);
            }
            GameData duplicateGame = new GameData("NameOfGame");
            assertThrows(DataAccessException.class, () -> gameDao.createGame(duplicateGame.getGameName()));
        } catch (DataAccessException | SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testReadGame() {
        try {
            GameData game = gameDao.returnGame(gameDao.createGame("NameOfGame"));
            assertNotNull(game);
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testCantReadGame() {
        try {
            try (Connection connection = new DatabaseManager().getConnection()) {
                gameDao.clear(connection);
            }
            GameData game = gameDao.returnGame(gameDao.createGame("NameOfGame"));
            assertNotNull(game);
        } catch (DataAccessException | SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testReadGames() {
        try {
            gameDao.createGame("Madison");
            gameDao.createGame("Carl");
            gameDao.createGame("Bob");
            Collection<GameData> games = gameDao.returnGameMap();
            assertFalse(games.isEmpty());
            assertNotNull(games);
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testCantReadGames() {
        try {
            try (Connection connection = new DatabaseManager().getConnection()) {
                gameDao.clear(connection);
            }
            Collection<GameData> games = gameDao.returnGameMap();
            assertTrue(games.isEmpty());
            assertNotNull(games);
        } catch (DataAccessException | SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testPlayerNamerPass() {
        try {
            int gameId = gameDao.createGame("Jared's Game");
            String username = "Jared";
            gameDao.playerNamer(username, gameId, ChessGame.TeamColor.WHITE);
            GameData game = gameDao.returnGame(gameId);
            assertEquals(username, game.getWhiteUsername());
            assertNotNull(game);
            gameDao.playerNamer(username, gameId, ChessGame.TeamColor.BLACK);
            game = gameDao.returnGame(gameId);
            assertEquals(username, game.getBlackUsername());
            assertNotNull(game);
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testPlayerNamerFail() {
        try {
            int gameId = gameDao.createGame("Jared's Game");
            String username1 = "Jared";
            String username2 = "Steve";
            gameDao.playerNamer(username1, gameId, ChessGame.TeamColor.WHITE);
            assertThrows(DataAccessException.class, () -> gameDao.playerNamer(username2, gameId, ChessGame.TeamColor.WHITE));
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}
