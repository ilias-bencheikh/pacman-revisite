package model;

import config.MazeConfig;
import geometry.RealCoordinates;

public class Ghost_Move{

    private static final double COLLISION_THRESHOLD = 2;

    // Fonction pour les déplacements des fantômes dans le jeu 

    public static RealCoordinates nextPos(Direction[] d, Ghost g, MazeConfig config, long delaTimeNanoSeconds) {
        for (Direction dir : d) {
            if (config.getCell(g.getPos().round()).canMoveInDirection(dir) && dir != Ghost_tools.inverse(g.getDirection())) {
                switch (dir) {
                    case NORTH, SOUTH -> {
                        if (g.getDirection() == Direction.EAST || g.getDirection() == Direction.WEST) {
                            int x = g.getPos().round().x();
                            if (x - 0.1 <= g.getPos().x() && g.getPos().x() <= x) {
                                g.setDirection(dir);
                                return new RealCoordinates(x, g.getPos().y());
                            }
                        } else {
                            g.setDirection(dir);
                            return g.getPos().plus(Ghost_tools.DirectionToRealCoordinates(dir).times(g.getSpeed(delaTimeNanoSeconds)));
                        }
                    }
                    case EAST, WEST -> {
                        if (g.getDirection() == Direction.SOUTH || g.getDirection() == Direction.NORTH) {
                            int y = g.getPos().round().y();
                            if (y - 0.1 <= g.getPos().y() && g.getPos().y() <= y) {
                                g.setDirection(dir);
                                return new RealCoordinates(g.getPos().x(), y);

                            }
                        }

                        else {
                            g.setDirection(dir);
                            return g.getPos().plus(Ghost_tools.DirectionToRealCoordinates(dir).times(g.getSpeed(delaTimeNanoSeconds)));
                        }
                    }
                    case NONE -> {
                        return g.getPos();
                    }
                }
            }
        }
        return g.getPos().plus(Ghost_tools.DirectionToRealCoordinates(g.getDirection()).times(g.getSpeed(delaTimeNanoSeconds)));
    }

    public static Direction[] fuite_ghost(Ghost g, PacMan p, long deltaTNanoSeconds,MazeConfig config) {
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        double distance = Ghost_tools.distance(g.getPos(),p.getPos());
        if(distance < 7){
            return Ghost_tools.inverse(Ghost_Direction.nextDirection_Blinky(g, p.getPos(), deltaTNanoSeconds));
        }
        else{
            if (config.getCell(g.getPos().round()).canMoveInDirection(g.getDirection())) {
                directions[0] = g.getDirection();
                return directions;
            } else {
                return Ghost_tools.triAleatoire(directions);
            }
        }
    }

    //Fonction importante pour les animations de déplacement des fantômes dans le jeu

    public static void animation_sortie(Ghost g, MazeConfig config, long deltaTNanoSeconds, RealCoordinates exit_pos) {
        RealCoordinates ghost_Pos = g.getPos();

        if (ghost_Pos.x() >= exit_pos.x()-0.1 && ghost_Pos.x() <= exit_pos.x()+0.1 && ghost_Pos.y() >= exit_pos.y()-0.1 && ghost_Pos.y() <= exit_pos.y()+0.1) {
            g.setPos(exit_pos);
            g.setSortie(true);
        } else {
            Direction[] directions = Ghost_Direction.nextDirection_Blinky(g, exit_pos, deltaTNanoSeconds);
            g.setPos(nextPos(directions, g, config, deltaTNanoSeconds));
        }
    }

    public static void animation_mort(Ghost g, RealCoordinates initialPos, MazeConfig config, long deltaTNanoSeconds, RealCoordinates exit_pos) {
        RealCoordinates ghost_Pos = g.getPos();
        double distance = Math.sqrt(Math.pow(ghost_Pos.x() - exit_pos.x(), 2) + Math.pow(ghost_Pos.y() - exit_pos.y(), 2));
    
        if (distance < COLLISION_THRESHOLD) {
            g.setPos(initialPos);
            g.setMort(false);
        } else {
            Direction[] directions = Ghost_Direction.nextDirection_Blinky(g, exit_pos, deltaTNanoSeconds);
            g.setPos(nextPos(directions, g, config, deltaTNanoSeconds));
        }
            }
}