package model;

import geometry.RealCoordinates;

public class Pinky {

    public static Direction[] nextDirection(Ghost pinky, Critter pacman, long deltaTNanoSeconds) {
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

}
