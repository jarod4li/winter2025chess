package ui;

import model.GameData;
import reqAndRes.*;
import com.google.gson.*;

import java.io.IOException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class ServerFacade {
    private String serverUrl;
    private HashMap<Integer, Integer> gameIdMap = new HashMap<>();

    private static int nextSequentialId = 1;


    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
        preloadGames();
    }

    public RegResponse register(RegRequest req) throws IOException {
        URL url = new URL(serverUrl + "/user");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.connect();
        try (var outputStream = conn.getOutputStream()) {
            outputStream.write(new Gson().toJson(req).getBytes());
        }
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            RegResponse resp = null;
            try (InputStream stream = conn.getErrorStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, RegResponse.class);
            }
            return resp;
        } else {
            RegResponse resp = null;
            try (InputStream stream = conn.getInputStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, RegResponse.class);
            }
            return resp;
        }
    }
    public void preloadGames() {
        try {
            listgames();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }


    public CreateGameResponse createGame(CreateGameRequest request) throws IOException {
        preloadGames();
        URL url = new URL(serverUrl + "/game");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.addRequestProperty("Authorization", TokenPlaceholder.token);
        conn.connect();
        try (var outStream = conn.getOutputStream()) {
            outStream.write(new Gson().toJson(request).getBytes());
        }
        CreateGameResponse resp;
        try (InputStream stream = (HttpURLConnection.HTTP_OK == conn.getResponseCode())
                ? conn.getInputStream()
                : conn.getErrorStream()) {
            InputStreamReader streamReader = new InputStreamReader(stream);
            resp = new Gson().fromJson(streamReader, CreateGameResponse.class);
        }
        if (resp != null && resp.getID() != null) {
            int dbId = resp.getID();
            gameIdMap.put(nextSequentialId, dbId);
            resp.setSequentialId(nextSequentialId);
            nextSequentialId++;
            }
        return resp;
    }

    public LoginResponse login(LoginRequest request) throws IOException {
        URL url = new URL(serverUrl + "/session");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.connect();
        try (var outStream = conn.getOutputStream()) {
            outStream.write(new Gson().toJson(request).getBytes());
        }
        if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
            LoginResponse resp = null;
            try (InputStream stream = conn.getErrorStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, LoginResponse.class);
            }
            return resp;
        } else {
            LoginResponse resp = null;
            try (InputStream stream = conn.getInputStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, LoginResponse.class);
            }
            return resp;
        }
    }

    public LogoutResponse logout() throws IOException {
        URL url = new URL(serverUrl + "/session");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("DELETE");
        conn.addRequestProperty("Authorization", TokenPlaceholder.token);
        conn.connect();
        if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
            LogoutResponse resp = null;
            try (InputStream stream = conn.getErrorStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, LogoutResponse.class);
            }
            return resp;
        } else {
            LogoutResponse resp = null;
            try (InputStream stream = conn.getInputStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, LogoutResponse.class);
            }
            return resp;
        }
    }

    public JoinGameResponse joinGame(JoinGameRequest request) throws IOException {
        listgames();
        Integer variable = request.getGameID();
        if (!gameIdMap.containsKey(variable)) {
            JoinGameResponse errorResponse = new JoinGameResponse();
            errorResponse.setMessage("Error! No game");
            return errorResponse;
        }
        request.setGameID(gameIdMap.get(variable));
        URL url = new URL(serverUrl + "/game");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("PUT");
        conn.addRequestProperty("Authorization", TokenPlaceholder.token);
        conn.setDoOutput(true);
        conn.connect();
        try (var outStream = conn.getOutputStream()) {
            outStream.write(new Gson().toJson(request).getBytes());
        }
        if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
            JoinGameResponse resp = null;
            try (InputStream stream = conn.getErrorStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, JoinGameResponse.class);
            }
            return resp;
        } else {
            JoinGameResponse resp = null;
            try (InputStream stream = conn.getInputStream()) {
                InputStreamReader streamReader = new InputStreamReader(stream);
                resp = new Gson().fromJson(streamReader, JoinGameResponse.class);
            }
            return resp;
        }
    }
    public ListGamesResponse listgames() throws IOException {
        URL url = new URL(serverUrl + "/game");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("GET");
        conn.addRequestProperty("Authorization", TokenPlaceholder.token);
        conn.connect();
        ListGamesResponse resp = null;
        try (InputStream stream = (HttpURLConnection.HTTP_OK == conn.getResponseCode())
                ? conn.getInputStream()
                : conn.getErrorStream()) {
            InputStreamReader streamReader = new InputStreamReader(stream);
            resp = new Gson().fromJson(streamReader, ListGamesResponse.class);
        }
        gameIdMap.clear();
        if (resp != null && resp.getGames() != null) {
            List<GameData> games = (List<GameData>) resp.getGames();
            int sequentialId = 1;
            for (GameData game : games) {
                int dbId = game.getID();
                gameIdMap.put(sequentialId, dbId);
                game.setID(sequentialId);
                sequentialId++;
            }
            nextSequentialId = sequentialId;
        }
        return resp;
    }
}
