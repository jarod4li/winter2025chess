package dataaccess;
import model.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    private static UserDAO userDao;

    @BeforeEach
    public void setup() throws DataAccessException, SQLException {
        userDao = new UserDAO();
        try (Connection connection = new DatabaseManager().getConnection()) {
            userDao.clear(connection);
        }
    }

    @Test
    public void testClear() {
        UserData user = new UserData("jared", "123", "jbou234@gmail.com");
        try {
            userDao.createUser(user.getName(), user.getPassword(), user.getEmail());
            try (Connection connection = new DatabaseManager().getConnection()) {
                userDao.clear(connection);
            }
            UserData retrievedUser = userDao.returnUser(user.getName());
            assertNull(retrievedUser);
        } catch (DataAccessException | SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testUserCreation() {
        UserData user = new UserData("jared", "123", "jbou234@gmail.com");
        try {
            userDao.createUser(user.getName(), user.getPassword(), user.getEmail());
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testDittoUsername() {
        UserData user = new UserData("jared", "123", "jbou234@gmail.com");
        try {
            userDao.createUser(user.getName(), user.getPassword(), user.getEmail());
            UserData duplicateUser = new UserData("jared", "123", "jbou234@gmail.com");
            assertThrows(DataAccessException.class, () ->
                    userDao.createUser(duplicateUser.getName(), duplicateUser.getPassword(), duplicateUser.getEmail()));
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testReturnUser() {
        UserData user = new UserData("jared", "123", "jbou234@gmail.com");
        try {
            userDao.createUser(user.getName(), user.getPassword(), user.getEmail());
            UserData fetchedUser = userDao.returnUser(user.getName());
            assertNotNull(fetchedUser);
            assertEquals(user.getName(), fetchedUser.getName());
            assertEquals(user.getPassword(), fetchedUser.getPassword());
            assertEquals(user.getEmail(), fetchedUser.getEmail());
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    public void testReturnUserNonExisting() {
        UserData user = new UserData("jared", "123", "jbou234@gmail.com");
        try {
            userDao.createUser(user.getName(), user.getPassword(), user.getEmail());
            UserData nonExistingUser = userDao.returnUser("nonExistingUser");
            assertNull(nonExistingUser);
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}
