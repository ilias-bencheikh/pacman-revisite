package model;

import geometry.RealCoordinates;

public class Blinky {
    public static Direction[] nextDirection(Ghost Blinky, RealCoordinates PacMan, long deltaTNanoSeconds) {
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
}
