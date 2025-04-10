package reqAndRes;

public class JoinGameResponse {
    public JoinGameResponse(String message){
        this.message = message;
    }
    public JoinGameResponse() {
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    private String message;
}
