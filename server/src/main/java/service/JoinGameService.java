package service;
import chess.ChessGame;
import model.*;
import dataaccess.*;
import reqAndRes.*;
public class JoinGameService {
    public JoinGameResponse joinGame(JoinGameRequest request, String authToken) {
        try {
            GameDAO gameDAO = new GameDAO();
            AuthDAO authDAO = new AuthDAO();
            GameData game = gameDAO.returnGame(request.getGameID());
            if (game == null) {
                return new JoinGameResponse("Error! No game");
            }
            if (request.getPlayerColor() == ChessGame.TeamColor.SPECTATOR){
                return new JoinGameResponse();
            }
            if (request.getPlayerColor() == null) {
                if (authDAO.returnToken(authToken) == null) {
                    return new JoinGameResponse("Error! Give auth Token");
                }
                return new JoinGameResponse("Error! No game");
            }
            AuthData authtoken = authDAO.returnToken(authToken);
            if (authtoken == null) {
                return new JoinGameResponse("Error! Give auth Token");
            }
            if (game.getWhiteUsername() != null) {
                if (game.getWhiteUsername().equals(authtoken.getName()) && request.getPlayerColor() == ChessGame.TeamColor.WHITE) {
                    return new JoinGameResponse();
                }
            }
            if (game.getBlackUsername() != null) {
                if (game.getBlackUsername().equals(authtoken.getName()) && request.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                    return new JoinGameResponse();
                }
            }
            if (game.getBlackUsername() != null && request.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                return new JoinGameResponse("Error! Color is taken");
            }
            if (game.getWhiteUsername() != null && request.getPlayerColor() == ChessGame.TeamColor.WHITE) {
                return new JoinGameResponse("Error! Color is taken");
            }
            gameDAO.playerNamer(authtoken.getName(), request.getGameID(), request.getPlayerColor());
            return new JoinGameResponse();
        }
        catch (Exception exception) {
            return new JoinGameResponse(exception.getMessage());
        }
    }
}
