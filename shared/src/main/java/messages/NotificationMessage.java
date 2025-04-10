package messages;
import websocket.messages.ServerMessage;
public class NotificationMessage extends ServerMessage {
    private String message;
    public NotificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message=message;
    }
    public void setMessage(String message) {
        this.message=message;
    }
    public String getMessage() {
        return message;
    }
}
