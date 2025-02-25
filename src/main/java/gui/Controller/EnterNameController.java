package gui.Controller;

import gui.App;
import gui.AppStateMachine.EnterNameState;
import gui.AppStateMachine.HomeScreenState;
import gui.AppStateMachine.PlayingState;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EnterNameController {
    
    public void keyPressedHandler(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER) && EnterNameState.canPlay) {
            App.app_state.changeState(PlayingState.getInstance());
            System.out.println("Vous avez appuyé sur ENTER"); 
        }
        if(event.getCode().equals(KeyCode.ESCAPE)){
            System.out.println("ESCAPE key pressed"); // Debugging line
            App.app_state.changeState(HomeScreenState.getInstance());
            System.out.println(App.app_state.getState());
        }
        else{
    
        }
    }

    public void keyReleasedHandler(KeyEvent event) {
        // Rien
    }
}
