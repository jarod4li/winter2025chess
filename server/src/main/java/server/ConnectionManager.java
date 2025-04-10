package server;
import org.eclipse.jetty.websocket.api.Session;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Connection>> connectionList = new ConcurrentHashMap<>();
    public void remove(String username) {
        for (Set<Connection> connections : connectionList.values()) {
            connections.removeIf(connection -> connection.getUsername().equals(username));
        }
    }
    public void add(Integer gameID, String authToken, Session session) {
        Connection connection = new Connection(authToken, session);
        connectionList.computeIfAbsent(gameID, k -> new HashSet<>()).add(connection);
    }
}