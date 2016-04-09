package application;

public class HighScore {
    private String name;
    private int score;

    HighScore(String name, int score){
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
