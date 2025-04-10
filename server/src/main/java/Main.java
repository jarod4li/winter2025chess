import chess.*;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import server.Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server server = new Server();
        int port = server.run(0);
        Properties properties = new Properties();
        properties.setProperty("server.port", String.valueOf(port));
        try (FileOutputStream outStream = new FileOutputStream("server.properties")) {
            properties.store(outStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}