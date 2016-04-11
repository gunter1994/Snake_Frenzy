package application;

import java.io.*;

// class for player to store their information
class Player {
    private String username;
    int highscore;
    File newFile = null;
    private String custom;
    private int wins;

    public Player() { this.wins = 0; }

    public void setCustom(String c) {
        this.custom = c;
    }

    public String getCustom() {
        return this.custom;
    }

    public void setUsername(String u) {
        this.username = u;
    }

    public String getUsername() {
        return this.username;
    }

    public void setWins(int w) { this.wins = w; }

    public int getWins() { return this.wins; }

    public void win() { wins++; }
}
