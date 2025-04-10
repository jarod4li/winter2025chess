package reqAndRes;
public class CreateGameRequest {
    public CreateGameRequest(String gameName){
        this.gameName = gameName;
    }
    public void setName(String gameName) {
        this.gameName = gameName;
    }
    public String getName() {
        return gameName;
    }
    private String gameName;
}
