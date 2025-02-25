package gui.AppStateMachine;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import animatefx.animation.Flash;
import gui.App;
import lib.FontLoader;
import lib.State;
import lib.ElementScaler;

public class MazeWinState implements State {
    private String state_name = "Maze Win State";
    private static final MazeWinState instance = new MazeWinState();

    private double MAX_FONT_SIZE = 20.0;
    private Font pixel_font = FontLoader.getPixelFont(MAX_FONT_SIZE);
    BorderPane win_menu = new BorderPane();

    Flash flashWin = new Flash();
    Timeline waitTime = new Timeline();

    private MazeWinState() {
        // Constructeur privé pour empêcher la création d'autres instances
    }

    public static MazeWinState getInstance() {
        return instance;
    }

    public String showState() {
        return state_name;
    }

    public void enter() {
        App.screen.setOnKeyPressed(null);
        MAX_FONT_SIZE = ElementScaler.scale(MAX_FONT_SIZE);
        Text winText = new Text("Vous avez gagne !");
        winText.setFont(pixel_font);
        winText.setFill(javafx.scene.paint.Color.WHITE);
        win_menu.setCenter(winText);
        
        flashWin.setNode(winText);
        flashWin.setSpeed(0.10);
        flashWin.setCycleCount(Flash.INDEFINITE);
        flashWin.play();

        win_menu.setStyle("-fx-background-color: black;");

        waitTime = new Timeline(new KeyFrame(Duration.seconds(5.0), event -> {
            App.app_state.changeState(HomeScreenState.getInstance());
        }));
        waitTime.setCycleCount(1);
        waitTime.play();
        App.screen.setRoot(win_menu);
    }

    public void exit() {
        waitTime.stop();
        flashWin.stop();
    }

    public void transitionTo(State s) {
        
    }
}
