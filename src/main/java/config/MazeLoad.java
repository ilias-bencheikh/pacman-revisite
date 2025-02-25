package config;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Random;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MazeLoad {
    public Cell[][] make(int level) {
        // load a maze from a json file
        InputStream path = null;
        switch (level) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                path = getClass().getResourceAsStream("/maze1.json");
                break;
            default:
                break;
        }
        // On veut acceder au fichir json dans resources
        String content = new BufferedReader(new InputStreamReader(path)).lines()
                .collect(Collectors.joining("\n"));
        JSONObject json = new JSONObject(content);
        JSONArray maze = json.getJSONArray("maze");
        JSONArray type_list = maze.getJSONArray(0);
        JSONArray value_list = maze.getJSONArray(1);

        // if the list are not a cube, throw an exception
        // for (int i = 0; i < type_list.length(); i++) {
        // System.out.println(type_list.getJSONArray(i).length());
        // System.out.println(value_list.getJSONArray(i).length());
        // if (type_list.getJSONArray(i).length() !=
        // value_list.getJSONArray(i).length()) {
        // throw new IllegalStateException("Les listes ne sont pas des carrés");
        // }
        // }

        // if 2 lists are not the same size, throw an exception
        if (type_list.length() != value_list.length()) {
            throw new IllegalStateException("Les listes ne sont pas de la même taille");
        }
        boolean zhonya = false;
        Boolean vitesseP = false;
        boolean vitesseM = false;
        boolean heal = false;
        boolean TeteDeMort = false;
        Cell[][] cells = new Cell[type_list.length()][type_list.getJSONArray(0).length()];

        for (int i = 0; i < type_list.length(); i++) {
            JSONArray row_type = type_list.getJSONArray(i);
            JSONArray row_value = value_list.getJSONArray(i);

            for (int j = 0; j < row_type.length(); j++) {
                String type = row_type.getString(j);
                String value = row_value.getString(j);

                if (aleatoire() && value.equals(".") && !zhonya) {
                    value = "ZHONYA";
                    zhonya = true;
                } else if (aleatoire() && value.equals(".") && !vitesseP) {
                    value = "vitesseP";
                    vitesseP = true;
                }

                else if (aleatoire() && value.equals(".") && !vitesseM) {
                    value = "vitesseM";
                    vitesseM = true;
                } else if (aleatoire() && value.equals(".") && !heal) {
                    value = "HEAL";
                    heal = true;
                } else if (aleatoire() && value.equals(".") && !TeteDeMort) {
                    value = "TeteDeMort";
                    TeteDeMort = true;
                }

                cells[i][j] = Cell.withContent(type, value);
            }
        }
        // print(cells);
        return cells;
    }

    public static void print(Cell[][] cells) {
        for (int i = 0; i < cells.length; i++) {
            Cell[] row = cells[i];
            for (int j = 0; j < row.length; j++) {
                Cell cell = row[j];
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    public static boolean aleatoire() {
        Random rand = new Random();
        int n = rand.nextInt(100);
        if (n < 1) {
            return true;
        }
        return false;
    }
}