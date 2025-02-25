package lib;

public interface State {
    void enter();
    void exit();
    String showState();
    void transitionTo(State s);
}