package application;

import java.io.*;

// class for player to store their information
class Player {
    private String username;
    int highscore;
    File newFile = null;
    private String custom;
    private int wins;

    Player() {
        wins = 0;
    }

    void setCustom(String c) {
        this.custom = c;
    }

    String getCustom() {
        return this.custom;
    }

    void setUsername(String u) {
        this.username = u;
    }

    String getUsername() {
        return this.username;
    }

    int getWins() { return this.wins; }

    void win() { wins++; }
}
