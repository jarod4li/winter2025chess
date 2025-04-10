package server;
import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;

public class Connection {
    public String username;
    public Session session;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username=username;
    }
    public Session getSession() {
        return session;
    }
    public void setSession(Session session) {
        this.session=session;
    }
    public Connection(String username, Session session) {
        this.username = username;
        this.session = session;
    }
}
