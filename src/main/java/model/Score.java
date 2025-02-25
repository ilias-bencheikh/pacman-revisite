package model;

public class Score implements java.io.Serializable {
    private String name;
    private int score;
    
    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String toString() {
        return name + " : " + score;
    }
}
