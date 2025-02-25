package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class LeaderBoard implements Serializable {
    // This class is used to store the scores of the players we want to save the 6
    // best scores

    private static final long serialVersionUID = 1L;

    private static ArrayList<Score> scores = new ArrayList<>(Arrays.asList(
            new Score("Dick", 0),
            new Score("Panini", 0),
            new Score("Chicken", 0),
            new Score("NullPointer", 0),
            new Score("Jej", 0),
            new Score("Zizi", 0)));

    public class LeaderBoardIO {
        public static void saveScores() {
            String filename = "src/main/resources/save/scores.ser";
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(LeaderBoard.getScores());
                System.out.println("Scores saved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void loadScores() {
            InputStream is = LeaderBoardIO.class.getResourceAsStream("/save/scores.ser");
            try (ObjectInputStream ois = new ObjectInputStream(is)) {
                Object obj = ois.readObject();
                if (obj instanceof ArrayList<?>) {
                    ArrayList<?> list = (ArrayList<?>) obj;
                    if (!list.isEmpty() && list.get(0) instanceof Score) {
                        ArrayList<Score> scores = new ArrayList<>();
                        for (Object item : list) {
                            scores.add((Score) item);
                        }
                        LeaderBoard.setScores(scores);
                        System.out.println("Scores loaded successfully.");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                saveScores();
            }
        }
    }

    public static void addScore(Score score) {
        if (scores.size() < 6) {
            scores.add(score);
        } else {
            for (int i = 0; i < scores.size(); i++) {
                if (scores.get(i).getScore() < score.getScore()) {
                    scores.add(i, score);
                    break;
                }
            }
            // Supprimez le dernier élément s'il y a plus de 6 scores
            if (scores.size() > 6) {
                scores.remove(scores.size() - 1);
            }
        }
        sort();
    }

    public static void sort() {
        scores.sort(new Comparator<Score>() {
            @Override
            public int compare(Score s1, Score s2) {
                return Integer.compare(s2.getScore(), s1.getScore());
            }
        });
    }

    public static ArrayList<Score> getScores() {
        return scores;
    }

    public static void setScores(ArrayList<Score> tab) {
        if (scores.size() > 6) {
            scores = new ArrayList<Score>(tab.subList(0, 6));
        } else {
            scores = tab;
        }
    }

    public static void reset() {
        scores = new ArrayList<Score>();
    }

    public static void print() {
        for (int i = 0; i < scores.size(); i++) {
            System.out.println(scores.get(i).getName() + " : " + scores.get(i).getScore());
        }
    }

    public static void save() {
        LeaderBoardIO.saveScores();
    }

    public static void load() {
        LeaderBoardIO.loadScores();
    }
}
