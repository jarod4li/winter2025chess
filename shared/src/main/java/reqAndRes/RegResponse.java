package reqAndRes;

public class RegResponse {
    public RegResponse(String message) {
        this.message = message;
    }
    public RegResponse(String username, String authToken){
        this.username = username;
        this.authToken = authToken;
    }
    public RegResponse(String username, String authToken, String message){
        this.username = username;
        this.authToken = authToken;
        this.message = message;
    }
    public String getName() {
        return username;
    }
    public void setName(String username) {
        this.username = username;
    }
    public String getAuth() {
        return authToken;
    }
    public void setAuth(String authToken) {
        this.authToken = authToken;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    private String username;
    private String authToken;
    private String message;
}
