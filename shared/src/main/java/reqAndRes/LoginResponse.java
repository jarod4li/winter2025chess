package reqAndRes;
public class LoginResponse {
    public LoginResponse() {}
    public LoginResponse(String message){
        this.message = message;
    }
    public LoginResponse(String username, String authToken){
        this.authToken = authToken;
        this.username = username;
    }
    public LoginResponse(String message, String username, String authToken){
        this.message = message;
        this.username = username;
        this.authToken = authToken;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setAuth(String authToken) {
        this.authToken = authToken;
    }
    public String getAuth() {
        return authToken;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    private String username;
    private String authToken;
    private String message;
}
