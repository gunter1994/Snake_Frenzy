package application;

import java.io.*;

// class for player to store their information
class Player {
    private String username;
    int highscore;
    File newFile = null;
    private String custom;

    Player() {

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
}
