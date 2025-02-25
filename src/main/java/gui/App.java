package gui;

import gui.AppStateMachine.AppState;
import gui.AppStateMachine.StartingLogosState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lib.ElementScaler;

public class App extends Application {

    public static Stage pStage;
    private static Pane root = new Pane();
    public static Scene screen = new Scene(root);

    public static AppState app_state = AppState.STARTING_LOGOS;

    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
        pStage = primaryStage;

        Platform.runLater(() -> {
            // Définit la taille de la fenêtre
            double screen_width = Screen.getPrimary().getBounds().getWidth();
            double screen_height = Screen.getPrimary().getBounds().getHeight();
            double min_screen_size = Math.min(screen_width, screen_height) - 100;
            pStage.setWidth(min_screen_size);
            pStage.setHeight(min_screen_size);

            pStage.setTitle("Pacman Dawn");
            try {
                pStage.getIcons().add(new Image(getClass().getResourceAsStream("/logos/3d_pacman.png")));
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'icone");
            }
            pStage.setResizable(false);

            System.out.println(app_state.showState());

            pStage.setScene(screen);
            pStage.setFullScreenExitHint(""); // Retire le message d'indication pour quitter le plein écran
            // pStage.setFullScreen(true);
            pStage.show();
            ElementScaler.updateResolution();

            // Etat intitial de l'application
            app_state.changeState(StartingLogosState.getInstance());
        });
    }
}
