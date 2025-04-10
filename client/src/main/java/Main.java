import chess.*;
import com.google.gson.Gson;
import exception.ResponseException;
import reqAndRes.*;
import messages.ErrorMessage;
import messages.LoadGameMessage;
import messages.NotificationMessage;
import ui.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Scanner;
import model.*;
import websocketClient.NotificationHandler;
import websocketClient.WebSocketFacade;
import websocket.messages.ServerMessage;

import javax.websocket.DeploymentException;

public class Main implements NotificationHandler {

    private static int gameID;
    private static ChessBoard chessBoard = new ChessBoard(true);


    private static void loggedInCommands(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException,
            URISyntaxException {
        System.out.println();
        displayLoggedInMenu();
        Scanner scanner = new Scanner(System.in);
        String command = scanner.next();
        scanner.nextLine();
        switch (command) {
            case "list":
                handleListGames(server, port);
                break;
            case "create":
                handleCreateGame(server, port);
                break;
            case "join":
                handleJoinGame(server, port);
                break;
            case "observe":
                handleObserveGame(server, port);
                break;
            case "help":
                loggedInCommands(server, port);
                break;
            case "logout":
                handleLogout(server);
                break;
            default:
                System.out.println("<Command not accepted>");
                loggedInCommands(server, port);
                break;
        }
    }

    private static void displayLoggedInMenu() {
        System.out.println("\"list\" - to list all games");
        System.out.println("\"create\" - to create and name a new game");
        System.out.println("\"join\" - to join a game and pick your color. ID required");
        System.out.println("\"observe\" - to observe a game. ID required");
        System.out.println("\"help\" - to list possible commands");
        System.out.println("\"logout\" - to log out");
    }

    private static void handleListGames(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException,
            URISyntaxException {
        ListGamesResponse resp = server.listgames();
        if (resp.getMessage() != null) {
            System.out.println(resp.getMessage());
        } else {
            System.out.println("Current Games:");
            for (GameData game : resp.getGames()) {
                System.out.println();
                System.out.println(game.getGameName());
                System.out.println("Game ID: " + game.getID());
                System.out.println("White Player: " + game.getWhiteUsername());
                System.out.println("Black Player: " + game.getBlackUsername());
            }
        }
        loggedInCommands(server, port);
    }

    private static void handleCreateGame(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException,
            URISyntaxException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Name of your new Game:");
        String name = scanner.nextLine();
        CreateGameRequest req = new CreateGameRequest(name);
        CreateGameResponse response = server.createGame(req);
        if (response.getMessage() != null) {
            System.out.println(response.getMessage());
        } else {
            System.out.println("<Successful Game Creation>");
            System.out.println("Game ID: " + response.getSequentialId());
            System.out.println();
        }
        loggedInCommands(server, port);
    }

    private static NotificationHandler notificationHandler = new Main();

    private static void handleJoinGame(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException, URISyntaxException {
        Scanner scanner = new Scanner(System.in);
        ChessGame.TeamColor color = selectTeamColor(scanner);
        int id;
        try {
            System.out.println("Game ID: ");
            id = scanner.nextInt();
            gameID = id;
        } catch (java.util.InputMismatchException e) {
            System.out.println("Input not accepted");
            scanner.nextLine();
            loggedInCommands(server, port);
            return;
        }
        JoinGameRequest request = new JoinGameRequest(color, id);
        JoinGameResponse response = server.joinGame(request);
        if (response.getMessage() != null) {
            System.out.println(response.getMessage());
            loggedInCommands(server, port);
        } else {
            System.out.println("<Successfully Joined Game>");
            WebSocketFacade webSocketFacade = new WebSocketFacade("http://localhost:" + port, notificationHandler);
            webSocketFacade.connect(TokenPlaceholder.token, color, id);
            System.out.println("\n");
            gamePlay(server, port);
        }
    }

    private static ChessGame.TeamColor selectTeamColor(Scanner scanner) {
        ChessGame.TeamColor color = null;
        int i = 0;
        while (i == 0) {
            System.out.println("Select Team Color: \"black\" or \"white\"");
            String selected = scanner.nextLine();
            if (selected.equals("white")) {
                color = ChessGame.TeamColor.WHITE;
                i++;
            } else if (selected.equals("black")) {
                color = ChessGame.TeamColor.BLACK;
                i++;
            }
        }
        return color;
    }

    private static void handleObserveGame(ServerFacade server, int port) throws IOException, ResponseException, DeploymentException,
            URISyntaxException {
        Scanner scanner = new Scanner(System.in);
        ChessGame.TeamColor color = ChessGame.TeamColor.SPECTATOR;
        int id;
        try {
            System.out.println("Game ID: ");
            id = scanner.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Input not accepted");
            scanner.nextLine();
            loggedInCommands(server, port);
            return;
        }
        JoinGameRequest request = new JoinGameRequest(color, id);
        JoinGameResponse response = server.joinGame(request);
        if (response.getMessage() != null) {
            System.out.println(response.getMessage());
            loggedInCommands(server, port);
        } else {
            System.out.println("<Successfully Joined Game as Spectator>");
            WebSocketFacade webSocketFacade = new WebSocketFacade("http://localhost:" + port, notificationHandler);
            webSocketFacade.connect(TokenPlaceholder.token, color, id);
            System.out.println("\n");
            gamePlay(server, port);
        }
    }

    private static void handleLogout(ServerFacade server) throws IOException {
        LogoutResponse response = server.logout();
        if (response.getMessage() != null) {
            System.out.println(response.getMessage());
        } else {
            System.out.println("<Successful Logout>");
            System.out.println();
            Main.listCommands();
        }
    }

    public static void listCommands() {
        System.out.println("Commands: ");
        System.out.println("\"register\" - to create your account");
        System.out.println("\"login\" - to log into your account");
        System.out.println("\"help\" - to list possible commands");
        System.out.println("\"quit\" - to quit your game");
    }

    public static void main(String[] args) throws IOException, ResponseException, DeploymentException, URISyntaxException {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream("server.properties")) {
            properties.load(in);
        }

        int port = Integer.parseInt(properties.getProperty("server.port"));
        ServerFacade server = new ServerFacade(port);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        Scanner scanner = new Scanner(System.in);
        System.out.println("♕ 240 Chess Client: " + piece);
        listCommands();
        while(true) {
            String input = scanner.nextLine();
            if (input.equals("register")) {
                System.out.println("New Username: ");
                String username = scanner.nextLine();
                System.out.println("New Password: ");
                String password = scanner.nextLine();
                System.out.println("Your Email Address: ");
                String email = scanner.nextLine();
                RegRequest req = new RegRequest(username, password, email);
                RegResponse resp = server.register(req);
                if(resp.getMessage() != null) {
                    System.out.println(resp.getMessage());
                    listCommands();
                }

                else{
                    TokenPlaceholder.token = resp.getAuth();
                    System.out.println("<Successful Registration>");
                    loggedInCommands(server, port);
                }
            }

            else if (input.equals("login")) {
                System.out.println("Username: ");
                String username = scanner.nextLine();
                System.out.println("Password: ");
                String password = scanner.nextLine();
                LoginRequest request = new LoginRequest(username, password);
                LoginResponse resp = server.login(request);
                if(resp.getMessage() != null){
                    System.out.println(resp.getMessage());
                    listCommands();
                }

                else{
                    TokenPlaceholder.token = resp.getAuth();
                    System.out.println("<Successful Login>");
                    loggedInCommands(server, port);
                }
            }

            else if (input.equals("help")) {
                System.out.println("\"register\" - to create your account");
                System.out.println("\"login\" - to log into your account");
                System.out.println("\"help\" - to list possible commands");
                System.out.println("\"quit\" - to quit your game");
            }

            else if (input.equals("quit")){
                System.out.println("<Program Terminated>");
                break;
            }

            else{
                System.out.println("<Command not accepted> ");
                listCommands();
            }
        }
    }

    private static void gamePlay(ServerFacade server, int port) throws ResponseException, DeploymentException, IOException, URISyntaxException {
        printCommands();

        WebSocketFacade webSocketFacade = new WebSocketFacade("http://localhost:" + port, notificationHandler);
        Scanner s = new Scanner(System.in);
        String command = s.next();
        s.nextLine();

        switch (command) {
            case "help" -> gamePlay(server, port);
            case "redraw" -> handleRedraw();
            case "leave" -> handleLeave(webSocketFacade, server, port);
            case "resign" -> handleResign(webSocketFacade, server, port);
            case "highlight" -> handleHighlight(server, port);
            case "make" -> handleMakeMove(webSocketFacade, server, port, s);
            default -> {
                System.out.println("Invalid Command");
                gamePlay(server, port);
            }
        }
    }

    private static void printCommands() {
        System.out.print("\n");
        System.out.println("\"help\" - for list of possible commands");
        System.out.println("\"redraw\" chess board");
        System.out.println("\"leave\" game");
        System.out.println("\"make\" move");
        System.out.println("\"resign\" game");
        System.out.println("\"highlight\" legal moves");
    }

    private static void handleRedraw() {
        PrintBoard.drawForPlayer2(chessBoard);
        System.out.println();
        PrintBoard.drawForPlayer1(chessBoard);
        System.out.println();
    }

    private static void handleLeave(WebSocketFacade webSocketFacade, ServerFacade server, int port)
            throws ResponseException, IOException, DeploymentException, URISyntaxException {
        webSocketFacade.leave(TokenPlaceholder.token, gameID);
        loggedInCommands(server, port);
    }

    private static void handleResign(WebSocketFacade webSocketFacade, ServerFacade server, int port)
            throws ResponseException, IOException, DeploymentException, URISyntaxException {
        webSocketFacade.resign(TokenPlaceholder.token, gameID);
        loggedInCommands(server, port);
    }

    private static void handleHighlight(ServerFacade server, int port)
            throws ResponseException, IOException, DeploymentException, URISyntaxException {
        loggedInCommands(server, port);
    }

    private static void handleMakeMove(WebSocketFacade webSocketFacade, ServerFacade server, int port, Scanner s)
            throws ResponseException, IOException, DeploymentException, URISyntaxException {
        String startPosition = getInputPosition("Enter the starting position: (Ex. a1)", s);

        if (startPosition == null) {
            gamePlay(server, port);
            return;
        }
        ChessPosition start = parseChessPosition(startPosition);
        if (start == null) {
            System.out.println("Invalid starting position. Enter column (a-h) and row (1-8).");
            gamePlay(server, port);
            return;
        }
        String endPosition = getInputPosition("Enter the end position: (Ex. a1)", s);
        if (endPosition == null) {
            gamePlay(server, port);
            return;
        }
        ChessPosition end = parseChessPosition(endPosition);
        if (end == null) {
            System.out.println("Invalid end position. Enter column (a-h) and row (1-8).");
            gamePlay(server, port);
            return;
        }
        ChessMove move = new ChessMove(start, end, null);
        webSocketFacade.makeMove(TokenPlaceholder.token, gameID, move);
        gamePlay(server, port);
    }

    private static String getInputPosition(String prompt, Scanner s) {
        System.out.println(prompt);
        String input = s.nextLine();
        if (input.length() != 2) {
            System.out.println("Invalid input. Enter a valid position: (a-h)(1-8)");
            return null;
        }
        return input;
    }

    private static ChessPosition parseChessPosition(String position) {
        try {
            char colLetter = position.charAt(0);
            int columnNumber = colLetter - 'a';
            columnNumber = 7 - columnNumber;
            int rowNumber = Character.getNumericValue(position.charAt(1)) - 1;
            if (rowNumber >= 0 && rowNumber <= 7 && columnNumber >= 0 && columnNumber <= 7) {
                return new ChessPosition(rowNumber, columnNumber);
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void notify(String message) {
        try{
            ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
            switch(serverMessage.getServerMessageType()) {
                case ERROR:
                    ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                    System.out.println(errorMessage.getErrorMessage());
                    break;
                case NOTIFICATION:
                    NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                    System.out.println(notificationMessage.getMessage());
                    break;
                case LOAD_GAME:
                    LoadGameMessage loadGame = new Gson().fromJson(message, LoadGameMessage.class);
                    chessBoard = loadGame.getGame().getChessGame().getBoard();
                    PrintBoard.drawForPlayer2(chessBoard);
                    System.out.println();
                    PrintBoard.drawForPlayer1(chessBoard);
                    System.out.print("\n");
                    break;
            }
        }
        catch(RuntimeException e){
            e.printStackTrace();
        }
    }
    }