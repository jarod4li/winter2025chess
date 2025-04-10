package websocketClient;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import commands.*;
public class WebSocketFacade extends Endpoint{

    private Session session;
    private NotificationHandler notification;
    public WebSocketFacade(String url, NotificationHandler notification) throws ResponseException, DeploymentException, IOException,
            URISyntaxException {

        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notification = notification;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    notification.notify(message);
                }
            });
        }

        catch(DeploymentException | IOException | URISyntaxException ex){
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
    public void connect(String authToken, ChessGame.TeamColor color, int gameID) throws ResponseException {
        try {
            var command = new ConnectCommand(authToken, color, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        try {
            var command = new MakeMoveCommand(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void sendCommand(Object command) throws ResponseException {
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(String authToken, int gameID) throws ResponseException {
        var command = new LeaveCommand(authToken, gameID);
        sendCommand(command);
    }

    public void resign(String authToken, int gameID) throws ResponseException {
        var command = new ResignCommand(authToken, gameID);
        sendCommand(command);
    }
}