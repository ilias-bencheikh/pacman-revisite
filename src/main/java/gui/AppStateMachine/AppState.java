package gui.AppStateMachine;

import lib.State;

//Implémentation d'une machine à état en utilisant un singleton

public enum AppState {
    STARTING_LOGOS(StartingLogosState.getInstance()),
    HOME_SCREEN(HomeScreenState.getInstance()),
    ENTER_NAME(EnterNameState.getInstance()),
    PLAYING(PlayingState.getInstance()),
    PAUSE(PauseState.getInstance()),
    GAMEOVER(GameOverState.getInstance());

    private State current_state;

    private AppState(State initial_state) {
        current_state = initial_state;
    }

    public State getState() {
        return current_state;
    }

    public String showState(){
        return current_state.showState();
    }

    public void changeState(State s) {
        current_state.exit();
        current_state.transitionTo(s);
        current_state = s;
        current_state.enter();
    }

}