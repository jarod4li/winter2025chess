package reqAndRes;
public class LogoutResponse {
    public LogoutResponse(String message){
        this.message = message;
    }
    public LogoutResponse(){

    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    private String message;
}
