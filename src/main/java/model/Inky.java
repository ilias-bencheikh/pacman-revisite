package model;

import geometry.RealCoordinates;

public class Inky {
    public static Direction[] nextDirection(Ghost INKY, Ghost BLINKY, Critter PacMan, long deltaTNanoSeconds) {
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
