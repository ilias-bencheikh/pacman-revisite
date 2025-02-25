package model;

import java.util.Random;
import geometry.RealCoordinates;

public class Ghost_tools {

    //Fonction auxiliaire pour le déplacement des fantômes dans le jeu

    public static double distance(RealCoordinates point1, RealCoordinates point2) {
        double deltaX = point1.x() - point2.x();
        double deltaY = point1.y() - point2.y();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public static RealCoordinates DirectionToRealCoordinates(Direction dir) {
        switch (dir) {
            case NORTH:
                return RealCoordinates.NORTH_UNIT;
            case EAST:
                return RealCoordinates.EAST_UNIT;
            case SOUTH:
                return RealCoordinates.SOUTH_UNIT;
            case WEST:
                return RealCoordinates.WEST_UNIT;
            default:
                return RealCoordinates.ZERO;
        }
    }

    public static Direction[] tri(Direction[] directions, double[] distances) {
        int n = distances.length;
        for (int i = 1; i < n; i++) {
            Direction currentDirection = directions[i];
            double currentDistance = distances[i];
            int j = i - 1;

            while (j >= 0 && distances[j] > currentDistance) {
                directions[j + 1] = directions[j];
                distances[j + 1] = distances[j];
                j--;
            }

            directions[j + 1] = currentDirection;
            distances[j + 1] = currentDistance;
        }
        return directions;
    }

    public static Direction[] triAleatoire(Direction[] directions) {
        Random rand = new Random();

        for (int i = directions.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            Direction temp = directions[i];
            directions[i] = directions[index];
            directions[index] = temp;
        }

        return directions;
    }

    public static Direction inverse(Direction dir) {
        switch (dir) {
            case NORTH:
                return Direction.SOUTH;
            case EAST:
                return Direction.WEST;
            case SOUTH:
                return Direction.NORTH;
            case WEST:
                return Direction.EAST;
            default:
                return Direction.NONE;
        }
    }

    public static Direction[] inverse(Direction[] d) {
        Direction[] res = new Direction[d.length];
        for (int i = 0; i < d.length; i++) {
            res[i] = inverse(d[i]);
        }
        return res;
    }
}