package dataaccess;
import model.*;
import org.junit.jupiter.api.*;

import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthTests {
    private static AuthDAO authDao;

    @BeforeEach
    public void setUp() throws DataAccessException, SQLException {
        authDao = new AuthDAO();
        try (Connection conn = new DatabaseManager().getConnection()) {
            authDao.clear(conn);
        }
    }

    @Test
    public void testClear() {
        AuthData credentials = new AuthData("123", "Jared");
        try {
            authDao.createToken(credentials.getAuth(), credentials.getName());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try (Connection conn = new DatabaseManager().getConnection()) {
            authDao.clear(conn);
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }

        AuthData retrievedToken = null;
        try {
            retrievedToken = authDao.returnToken(credentials.getAuth());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertNull(retrievedToken);
    }

    @Test
    public void testCreateAuth() {
        AuthData credentials = new AuthData("123", "Jared");
        try {
            authDao.createToken(credentials.getAuth(), credentials.getName());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUnableCreateDuplicate() {
        AuthData credentials = new AuthData("123", "Jared");
        try {
            authDao.createToken(credentials.getAuth(), credentials.getName());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        AuthData duplicate = new AuthData("123", "Jared");
        assertThrows(DataAccessException.class, () -> {
            authDao.createToken(duplicate.getAuth(), duplicate.getName());
        });
    }

    @Test
    public void testDeletion() {
        AuthData credentials = new AuthData("123", "Jared");
        try {
            authDao.createToken(credentials.getAuth(), credentials.getName());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            authDao.delete(credentials.getAuth());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        AuthData placeholder = null;
        try {
            placeholder = authDao.returnToken(credentials.getAuth());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertNull(placeholder);
    }

    @Test
    public void testCantDeleteNull() {
        AuthData credentials = new AuthData(null, null);
        assertDoesNotThrow(() -> authDao.delete(credentials.getAuth()));
    }

    @Test
    public void testReading() {
        AuthData credentials = new AuthData("123", "Jared");
        try {
            authDao.createToken(credentials.getAuth(), credentials.getName());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        AuthData placeholder = null;
        try {
            placeholder = authDao.returnToken(credentials.getAuth());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        assertEquals(credentials.getAuth(), placeholder.getAuth());
        assertEquals(credentials.getName(), placeholder.getName());
    }

    @Test
    public void testAntiReading() {
        AuthData credentials = new AuthData("123", "Jared");
        AuthData placeholder = null;
        try {
            placeholder = authDao.returnToken(credentials.getAuth());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertNull(placeholder);
    }
}
