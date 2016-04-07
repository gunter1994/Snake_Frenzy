package application;

/**
 * Created by adam on 07/04/16.
 */
public class HighScore {
    private String name;
    private int score;

    public HighScore(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String getName(){
        return name;
    }
    public int getScore(){
        return score;
    }
}
