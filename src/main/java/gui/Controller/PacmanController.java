package gui.Controller;

import javafx.scene.input.KeyCode;
import model.Direction;
import model.PacMan;
import gui.AppStateMachine.PlayingState;
import javafx.scene.input.KeyEvent;
import gui.App;
import gui.AppStateMachine.AppState;
import gui.AppStateMachine.PauseState;

public class PacmanController {
    public static Direction currentDirection = Direction.NONE;
    public static Direction nextDirection = Direction.NONE;

    public void keyPressedHandler(KeyEvent event) {
        // Si le joueur appuie sur echap et qu'il a la possiblité d'ouvrir le menu pause
        if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE && PlayingState.getInstance().canPause) {
            // System.out.println("Vous avez appuyé sur ECHAP");
            // Si on est pas en pause
            if (PlayingState.getInstance() == App.app_state.getState()) {
                App.app_state.changeState(PauseState.getInstance());
            }
            // Si on est en pause
            else {
                App.app_state.changeState(PlayingState.getInstance());
            }
        }

        //Si le joueur se trouve dans le menu pause et qu'il appuie sur R
        if (event.getCode() == KeyCode.R && PauseState.getInstance() == App.app_state.getState()){
            PauseState.relaunch = true;
            App.app_state.changeState(PlayingState.getInstance());
            System.out.println("restart");
        }

        //Si le joueur se trouve dans le menu pause et qu'il appuie sur P
        if(event.getCode() == KeyCode.P && PauseState.getInstance() == App.app_state.getState()){
            App.app_state.changeState(PlayingState.getInstance());
            System.out.println("resume");
        }

        //Si le joueur se trouve dans le menu pause et qu'il appuie sur Q
        if(event.getCode() == KeyCode.A && PauseState.getInstance() == App.app_state.getState() || event.getCode() == KeyCode.Q && PauseState.getInstance() == App.app_state.getState()){
            System.exit(0);
        }

        else {
            Direction temp = getDirectionFromKeyEvent(event);
            if(temp != nextDirection){
                nextDirection = temp;
            }
        }
    }
    
    public void keyReleasedHandler(KeyEvent event) {
        // Nothing to do?
    }

    public Direction getDirectionFromKeyEvent(KeyEvent event){
        var keyCode = event.getCode();
        switch(keyCode){
            case LEFT -> {return Direction.WEST;}
            case RIGHT -> {return Direction.EAST;}
            case UP -> {return Direction.NORTH;}
            case DOWN -> {return Direction.SOUTH;}
            default -> {return PacMan.INSTANCE.getDirection();}
        }
    }
}
