package client;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import reqAndRes.*;
import server.Server;
import service.ClearService;
import ui.ServerFacade;
import ui.TokenPlaceholder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void clear() {
        ClearService clearer = new ClearService();
        clearer.clearEverything();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void registerSuccess() {
        RegRequest request = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        try {
            RegResponse response = facade.register(request);
            assertNull(response.getMessage());
            assertNotNull(response.getAuth());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void registerFail() {
        RegRequest request = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        try {
            facade.register(request);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        RegRequest request2 = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        try{
            RegResponse response = facade.register(request2);
            assertEquals("Error! Name already being used!", response.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    @Test
    void loginSuccess(){
        RegRequest request = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        try {
            facade.register(request);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        LoginRequest loginRequest = new LoginRequest("Jadenizer", "Earthbounder4");
        try{
            LoginResponse response = facade.login(loginRequest);
            assertNull(response.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void loginFail(){
        RegRequest request = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        try {
            facade.register(request);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        LoginRequest loginRequest = new LoginRequest("NotJaden", "ImSneakingInNow");
        try{
            LoginResponse response = facade.login(loginRequest);
            assertEquals("Error! Wrong password", response.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    @Test
    void logout(){
        RegRequest request = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        try {
            facade.register(request);
            LoginRequest loginRequest = new LoginRequest("Jadenizer", "Earthbounder4");
            LoginResponse response = facade.login(loginRequest);
            assertNull(response.getMessage());
            TokenPlaceholder.token = response.getAuth();
            LogoutResponse response2 = facade.logout();
            assertNull(response2.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void logoutFail(){
        RegRequest request = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        try {
            facade.register(request);
            LoginRequest loginRequest = new LoginRequest("Jaden", "Something");
            LoginResponse response = facade.login(loginRequest);
            TokenPlaceholder.token = response.getAuth();
            LogoutResponse response2 = facade.logout();
            assertNotNull(response2.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void createGameSuccess(){
        RegRequest request1 = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        LoginRequest request2 = new LoginRequest("Jadenizer", "Earthbounder4");
        CreateGameRequest request3 = new CreateGameRequest("Jaden's Game");
        try{
            facade.register(request1);
            LoginResponse response = facade.login(request2);
            TokenPlaceholder.token = response.getAuth();
            CreateGameResponse response2 = facade.createGame(request3);
            assertNull(response2.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    @Test
    void createGameFail(){
        RegRequest request1 = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        LoginRequest request2 = new LoginRequest("Jadenizer", "Earthbounder4");
        CreateGameRequest request3 = new CreateGameRequest("Jaden's Game");
        try{
            facade.register(request1);
            LoginResponse response = facade.login(request2);
            TokenPlaceholder.token = null;
            CreateGameResponse response2 = facade.createGame(request3);
            assertNotNull(response2.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    @Test
    void listGamesSuccess(){
        RegRequest request1 = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        LoginRequest request2 = new LoginRequest("Jadenizer", "Earthbounder4");
        CreateGameRequest request3 = new CreateGameRequest("Jaden's Game");
        try{
            facade.register(request1);
            LoginResponse response = facade.login(request2);
            TokenPlaceholder.token = response.getAuth();
            facade.createGame(request3);
            ListGamesResponse response2 = facade.listgames();
            assertNull(response2.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void listGamesFail(){
        RegRequest request1 = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        LoginRequest request2 = new LoginRequest("Jadenizer", "Earthbounder4");
        CreateGameRequest request3 = new CreateGameRequest("Jaden's Game");
        try{
            facade.register(request1);
            LoginResponse response = facade.login(request2);
            TokenPlaceholder.token = null;
            facade.createGame(request3);
            ListGamesResponse response2 = facade.listgames();
            assertNotNull(response2.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    @Test
    void joinGameSuccess(){
        RegRequest request1 = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
        LoginRequest request2 = new LoginRequest("Jadenizer", "Earthbounder4");
        CreateGameRequest request3 = new CreateGameRequest("Jaden's Game");
        try{
            facade.register(request1);
            LoginResponse response = facade.login(request2);
            TokenPlaceholder.token = response.getAuth();
            CreateGameResponse response2 = facade.createGame(request3);
            int gameID = 1;
            JoinGameRequest request4 = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);
            JoinGameResponse response3 = facade.joinGame(request4);
            assertNull(response3.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    @Test
    void joinGameFail(){
    RegRequest request1 = new RegRequest("Jadenizer", "Earthbounder4", "kunzlerj9@gmail.com");
    RegRequest otherRequest1 = new RegRequest("Jo Bro", "Ferrariman4", "jonahk12@gmail.com");
    LoginRequest request2 = new LoginRequest("Jadenizer", "Earthbounder4");
    LoginRequest otherRequest2 = new LoginRequest("Jo Bro", "Ferrariman4");
    CreateGameRequest request3 = new CreateGameRequest("Jaden's Game");
        try{
        facade.register(request1);
        facade.register(otherRequest1);
        LoginResponse response = facade.login(request2);
        TokenPlaceholder.token = response.getAuth();
        CreateGameResponse response2 = facade.createGame(request3);
        int gameID = 1;
        JoinGameRequest request4 = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);
        facade.joinGame(request4);
        LoginResponse response3 = facade.login(otherRequest2);
        TokenPlaceholder.token = response3.getAuth();
        JoinGameRequest request5 = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);
        JoinGameResponse response4 = facade.joinGame(request5);
        assertEquals("Error! Color is taken", response4.getMessage());
    }
        catch(IOException e){
        System.out.println(e.getMessage());
    }
}
}
