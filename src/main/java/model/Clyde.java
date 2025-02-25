package model;

import java.util.Random;
import config.MazeConfig;
import geometry.RealCoordinates;

public class Clyde {

    public static Direction[] nextDirection(Ghost clyde, Critter pacMan, MazeConfig config, long deltaTNanoSeconds) {
        RealCoordinates clydePos = clyde.getPos();
        RealCoordinates pacManPos = pacMan.getPos();
        Direction[] tab = {
                Direction.NORTH,
                Direction.EAST,
                Direction.SOUTH,
                Direction.WEST
        };
        double distanceToPacMan = Ghost_tools.distance(clydePos, pacManPos);

        if (distanceToPacMan < 5) {
            return Blinky.nextDirection(clyde, pacManPos, deltaTNanoSeconds);
        } else {
            if (config.getCell(clyde.getPos().round()).canMoveInDirection(clyde.getDirection())) {
                tab[0] = clyde.getDirection();
                return tab;
            } else {
                return triAleatoire(tab);
            }
        }
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

}
