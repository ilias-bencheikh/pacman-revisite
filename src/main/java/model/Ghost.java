package model;

import config.MazeConfig;
import geometry.RealCoordinates;
import java.time.LocalTime;
import gui.AppStateMachine.PlayingState;

public enum Ghost implements Critter {

    BLINKY, INKY, PINKY, CLYDE;

    // Variable pour la gestion des fantômes

    private RealCoordinates initialPos;
    private RealCoordinates exit_pos;
    private RealCoordinates pos;
    private Direction direction = Direction.NONE;
    private boolean sortie = false;
    private boolean mort = false;
    private final boolean disableGhost = false;
    private boolean disableEnergizer = false;

    // Variable pour la gestion des temps

    private long creationTime = System.nanoTime();
    private double respawnSeconds;

    // Fonction pour la manipulation des varibles

    @Override
    public RealCoordinates getPos() {
        return pos;
    }

    @Override
    public void setPos(RealCoordinates newPos) {
        pos = newPos;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public double getSpeed(long deltaTNanoSeconds) {
        PlayingState playingState = PlayingState.getInstance();
        double level = playingState.level;
        double speed = 4*deltaTNanoSeconds*1E-9;
        if (mort) {
            return 5 * deltaTNanoSeconds * 1E-9;
        } else if (disableGhost) {
            return 0;
        } else if (!sortie) {
            return 5 * deltaTNanoSeconds * 1E-9;
        }else if (PacMan.INSTANCE.isEnergized() && !disableEnergizer && !PacMan.INSTANCE.isvitesseM()) { 
            return speed*0.5;
        }else if (PacMan.INSTANCE.isvitesseM()){
            return speed*0.2;
        } else {
            if(this==BLINKY){
                return speed*(level/20+1);
            }
            if(this==PINKY){
                return speed*0.9*(level/20+1) ;
            }
            if(this==INKY){
                return speed*0.8*(level/20+1);
            }
            return speed*0.7*(level/20+1);
        }
    }

    public boolean isSortie() {
        return sortie;
    }

    public void setSortie(boolean sortie) {
        this.sortie = sortie;
    }

    public boolean isMort() {
        return mort;
    }

    public void setMort(boolean mort) {
        this.mort = mort;
    }

    public void setInitialPos(RealCoordinates initialPos) {
        this.initialPos = initialPos;
    }

    public boolean getSortie(){
        return sortie;
    }

    public void setExit_pos(RealCoordinates exit_pos) {
        this.exit_pos = exit_pos;
    }

    public boolean getDisableEnergizer() {
        return disableEnergizer;
    }
   
    public void setDisableEnergizer(boolean disableEnergizer) {
        this.disableEnergizer = disableEnergizer;
    }


    public void setTemps() {
        creationTime = System.nanoTime();
        if (mort) {
            return;
        } else {
            if (this == BLINKY) {
                respawnSeconds = 0.1;
            } else if (this == PINKY) {
                respawnSeconds = 2;
            } else if (this == INKY) {
                respawnSeconds = 3;
            } else {
                respawnSeconds = 4;
            }
        }
    }

    public boolean isRespawnTime() {
        long elapsedSeconds = (System.nanoTime() - creationTime) / 1_000_000_000;
        return elapsedSeconds >= respawnSeconds;
    }
    
    // Fonction pour la gestion des déplacements des fantômes

    public RealCoordinates nextPos(MazeConfig config, long deltaTNanoSeconds) {
        if (PacMan.INSTANCE.getzhonya()) {
            return pos;
        }
        if (mort) {
            Ghost_Move.animation_mort(this, initialPos, config, deltaTNanoSeconds,exit_pos);
            if (!mort) {
                setTemps();
            }
            return pos;
        } 
        else if (!sortie && !mort) {
            if (isRespawnTime()) {
                Ghost_Move.animation_sortie(this, config, deltaTNanoSeconds,exit_pos);
            }
            return pos;
        } 
        else {
            Direction[] directions;
            if (PacMan.INSTANCE.isEnergized() && !disableEnergizer) {
                directions = Ghost_Move.fuite_ghost(this, PacMan.INSTANCE, deltaTNanoSeconds, config);
            }
            else {
                directions = Ghost_Direction.nextDirection(this, PacMan.INSTANCE,config, deltaTNanoSeconds);
            }
            return Ghost_Move.nextPos(directions, this, config, deltaTNanoSeconds);
        }
    }
}