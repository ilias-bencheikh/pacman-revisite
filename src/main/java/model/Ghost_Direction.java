package model;

import config.MazeConfig;
import geometry.RealCoordinates;

public class Ghost_Direction {
    
    public static Direction[] nextDirection(Ghost ghost, Critter PacMan, MazeConfig config ,long deltaTNanoSeconds) {
        if (ghost == Ghost.PINKY) {
           return nextDirection_Pinky(ghost, PacMan, deltaTNanoSeconds);
        } 
        else if (ghost == Ghost.CLYDE) {
            return nextDirection_Clyde(ghost, PacMan, config, deltaTNanoSeconds);
        } 
        else if (ghost == Ghost.INKY) {
            return nextDirection_Inky(Ghost.BLINKY, ghost, PacMan, deltaTNanoSeconds);
        }
        else {
           return nextDirection_Blinky(ghost, PacMan.getPos(), deltaTNanoSeconds);
        }
    }

    // Fonctions pour les déplacements de Blinky dans le jeu

    public static Direction[] nextDirection_Blinky(Ghost Blinky, RealCoordinates PacMan, long deltaTNanoSeconds) {
        RealCoordinates BlinkyPos = Blinky.getPos();
        Direction[] possibleDirections = {
                Direction.NORTH,
                Direction.EAST,
                Direction.SOUTH,
                Direction.WEST };
        double[] tabDistance = new double[4];
        int i = 0;
        for (Direction dir : possibleDirections) {
            RealCoordinates newPos = BlinkyPos
                    .plus(Ghost_tools.DirectionToRealCoordinates(dir).times(Blinky.getSpeed(deltaTNanoSeconds)));

            double distance = Ghost_tools.distance(newPos, PacMan);
            tabDistance[i] = distance;
            i += 1;
        }
        return Ghost_tools.tri(possibleDirections, tabDistance);
    }

    // Fonctions pour les déplacements de Pinky dans le jeu

    public static Direction[] nextDirection_Pinky(Ghost pinky, Critter pacman, long deltaTNanoSeconds) {
        RealCoordinates pacManPos = pacman.getPos();
        RealCoordinates pinkyPos = pinky.getPos();
        Direction[] possibleDirections = {
                Direction.NORTH,
                Direction.EAST,
                Direction.SOUTH,
                Direction.WEST
        };
        RealCoordinates targetPos = pacManPos
                .plus(Ghost_tools.DirectionToRealCoordinates(pacman.getDirection()).times(pacman.getSpeed(deltaTNanoSeconds)));

        double[] tabDistance = new double[4];
        int i = 0;

        for (Direction dir : possibleDirections) {
            RealCoordinates newPos = pinkyPos.plus(Ghost_tools.DirectionToRealCoordinates(dir).times(pinky.getSpeed(deltaTNanoSeconds)));
            double distance = Ghost_tools.distance(newPos, targetPos);
            tabDistance[i] = distance;
            i++;
        }

        return Ghost_tools.tri(possibleDirections, tabDistance);
    }

    // Fonctions pour les déplacements de Clyde dans le jeu

    public static Direction[] nextDirection_Clyde(Ghost clyde, Critter pacMan, MazeConfig config, long deltaTNanoSeconds) {
        RealCoordinates clydePos = clyde.getPos();
        RealCoordinates pacManPos = pacMan.getPos();
        Direction[] tab = {
                Direction.NORTH,
                Direction.EAST,
                Direction.SOUTH,
                Direction.WEST
        };
        double distanceToPacMan = Ghost_tools.distance(clydePos, pacManPos);

        if (distanceToPacMan < 7) {
            return nextDirection_Blinky(clyde, pacManPos, deltaTNanoSeconds);
        } else {
            if (config.getCell(clyde.getPos().round()).canMoveInDirection(clyde.getDirection())) {
                tab[0] = clyde.getDirection();
                return tab;
            } else {
                return Ghost_tools.triAleatoire(tab);
            }
        }
    }

    // Fonctions pour les déplacements de Inky dans le jeu

    public static Direction[] nextDirection_Inky(Ghost INKY, Ghost BLINKY, Critter PacMan, long deltaTNanoSeconds) {
        RealCoordinates pacManPos = PacMan.getPos();
        RealCoordinates inkyPos = INKY.getPos();
        RealCoordinates blinkyPos = BLINKY.getPos();

        RealCoordinates targetPos = new RealCoordinates(
                pacManPos.x() + 2 * (pacManPos.x() - blinkyPos.x()),
                pacManPos.y() + 2 * (pacManPos.y() - blinkyPos.y()));

        // Liste des directions possibles
        Direction[] possibleDirections = {
                Direction.NORTH,
                Direction.EAST,
                Direction.SOUTH,
                Direction.WEST
        };

        double[] tabDistance = new double[4];
        int i = 0;

        for (Direction dir : possibleDirections) {
            RealCoordinates newPos = inkyPos
                    .plus(Ghost_tools.DirectionToRealCoordinates(dir).times(INKY.getSpeed(deltaTNanoSeconds)));

            double distance = Ghost_tools.distance(newPos, targetPos);
            tabDistance[i] = distance;
            i++;
        }

        return Ghost_tools.tri(possibleDirections, tabDistance);
    }

}
