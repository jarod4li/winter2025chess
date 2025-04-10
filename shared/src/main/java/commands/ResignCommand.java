package commands;
import websocket.commands.UserGameCommand;
public class ResignCommand extends UserGameCommand {
    public ResignCommand(String authToken, Integer gameID) {
        super(CommandType.RESIGN, authToken, gameID);
    }
}