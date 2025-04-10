package model;

import java.util.Objects;

public class AuthData {
    private String authToken;
    private String username;
    public AuthData(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }
    public String getAuth() {
        return authToken;
    }
    public void setAuth(String authToken) {
        this.authToken = authToken;
    }
    public String getName() {
        return username;
    }
    public void setName(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "AuthData{" +
                "authToken='" + authToken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthData authData=(AuthData) o;
        return Objects.equals(authToken, authData.authToken) && Objects.equals(username, authData.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }
}
