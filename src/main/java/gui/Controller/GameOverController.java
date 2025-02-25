package gui.Controller;

import gui.App;
import gui.AppStateMachine.HomeScreenState;
import gui.AppStateMachine.PlayingState;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameOverController {

    public void keyPressedHandler(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            App.app_state.changeState(PlayingState.getInstance());
            System.out.println("Vous avez appuyé sur ENTER");
        }

        if (event.getCode() == KeyCode.ESCAPE){
            System.exit(0);
        }

        if(event.getCode() == KeyCode.M){
            App.app_state.changeState(HomeScreenState.getInstance());
        }
    }

    public void keyReleasedHandler(KeyEvent event) {
        // Rien
    }
}