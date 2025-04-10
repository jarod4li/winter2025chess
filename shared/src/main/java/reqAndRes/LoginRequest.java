package reqAndRes;

public class LoginRequest {
    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }
    public void setName(String username) {
        this.username = username;
    }
    public String getName() {
        return username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    private String username;
    private String password;
}
