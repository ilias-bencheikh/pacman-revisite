package gui.Controller;

import gui.App;
import gui.AppStateMachine.HomeScreenState;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class StartingLogoController {
    public void keyPressedHandler(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            App.app_state.changeState(HomeScreenState.getInstance());
            System.out.println("Vous avez appuyé sur ENTER"); 
        }
        else{

        }
    }

    public void keyReleasedHandler(KeyEvent event) {
        // Rien
    }
}
