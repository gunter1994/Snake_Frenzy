package application;

/**
 * Created by samantha on 23/03/16.
 */
public class Player {
    private String username;
    private int wins;

    public Player(String u) {
        this.username = u;
    }

    public int getWins(){
        return wins;
    }

    public void setWins(int w) {
        this.wins = w;
    }

    public String getUsername() {
        return username;
    }
}
