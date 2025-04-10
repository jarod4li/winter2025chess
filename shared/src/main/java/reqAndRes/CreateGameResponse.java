package reqAndRes;
public class CreateGameResponse {
    public CreateGameResponse(String message){
        this.message = message;
    }
    public CreateGameResponse(int gameID) {
        this.gameID = gameID;
    }
    public CreateGameResponse(String message, int gameID){
        this.message = message;
        this.gameID = gameID;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setID(Integer id){
        this.gameID = id;
    }
    public Integer getID() {
        return gameID;
    }
    private String message;
    private Integer gameID;
    private int sequentialId;
    public int getSequentialId() {
        return sequentialId;
    }
    public void setSequentialId(int sequentialId) {
        this.sequentialId = sequentialId;
    }
}
