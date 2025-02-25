package gui.AppStateMachine;

import gui.App;
import gui.Controller.GameOverController;
import lib.ElementScaler;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import animatefx.animation.SlideInLeft;
import javafx.animation.Timeline;
import lib.FontLoader;
import lib.State;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Score;
import model.LeaderBoard;

public class GameOverState implements State {
    private String state_name = "Game Over";
    private static final GameOverState instance = new GameOverState();
    StackPane restart_menu = new StackPane();

    private double MAX_FONT_SIZE = 20; // Définit la taille du texte pour le score
    private Font pixel_font = FontLoader.getPixelFont(MAX_FONT_SIZE);

    private GameOverState() {
        // Constructeur privé pour empêcher la création d'autres instances
    }

    public static GameOverState getInstance() {
        return instance;
    }

    public String showState() {
        return state_name;
    }

    public void enter() {
        App.screen.setOnKeyPressed(null);
        restart_menu.getChildren().clear(); // Clear any added Texts when entering the state
        BorderPane gameOverPane = new BorderPane();
        gameOverPane.setMinSize(App.screen.getWidth(), App.screen.getHeight());
        restart_menu.getChildren().add(gameOverPane);

        // On affiche le menu de Game Over avec le texte "Game Over" au dessus centré
        // et le score du joueur
        MAX_FONT_SIZE = ElementScaler.scale(MAX_FONT_SIZE);

        Text gameOver = new Text("Game Over" + "\n" + "Score : " + PlayingState.getInstance().maze.getScore());
        gameOver.setTextAlignment(TextAlignment.CENTER);
        gameOver.setFont(pixel_font);
        gameOver.setFill(javafx.scene.paint.Color.WHITE);
        gameOverPane.setTop(gameOver);
        BorderPane.setAlignment(gameOver, Pos.CENTER);

        restart_menu.setStyle("-fx-background-color: black;");

        LeaderBoard.load();
        String name = HomeScreenState.getInstance().getUserName();
        if (name == null || name.equals("")) {
            name = "Player";
        }
        LeaderBoard.addScore(new Score(name , PlayingState.getInstance().maze.getScore()));
        LeaderBoard.save();
        
        Text leaderBoard = new Text("LeaderBoard :\n");
        leaderBoard.setOpacity(0);
        SlideInLeft slideInLeftLeaderBoard = new SlideInLeft(leaderBoard);
        slideInLeftLeaderBoard.setCycleCount(1);
        leaderBoard.setTextAlignment(TextAlignment.CENTER);
        leaderBoard.setTranslateY(ElementScaler.scale(-60));
        leaderBoard.setFont(pixel_font);
        leaderBoard.setFill(javafx.scene.paint.Color.WHITE);

        SlideInLeft slideInScoreNumber1 = new SlideInLeft();
        SlideInLeft slideInScoreNumber2 = new SlideInLeft();
        SlideInLeft slideInScoreNumber3 = new SlideInLeft();
        SlideInLeft slideInScoreNumber4 = new SlideInLeft();
        SlideInLeft slideInScoreNumber5 = new SlideInLeft();
        SlideInLeft slideInScoreNumber6 = new SlideInLeft();

        Text scoreNumber1 = new Text("1. " + LeaderBoard.getScores().get(0).toString());
        slideInScoreNumber1.setNode(scoreNumber1);
        slideInScoreNumber1.setSpeed(2);
        scoreNumber1.setOpacity(0);

        Text scoreNumber2 = new Text("2. " + LeaderBoard.getScores().get(1).toString());
        slideInScoreNumber2.setNode(scoreNumber2);
        slideInScoreNumber2.setSpeed(2);
        scoreNumber2.setOpacity(0);

        Text scoreNumber3 = new Text("3. " + LeaderBoard.getScores().get(2).toString());
        slideInScoreNumber3.setNode(scoreNumber3);
        slideInScoreNumber3.setSpeed(2);
        scoreNumber3.setOpacity(0);

        Text scoreNumber4 = new Text("4. " + LeaderBoard.getScores().get(3).toString());
        slideInScoreNumber4.setNode(scoreNumber4);
        scoreNumber4.setOpacity(0);

        Text scoreNumber5 = new Text("5. " + LeaderBoard.getScores().get(4).toString());
        slideInScoreNumber5.setNode(scoreNumber5);
        slideInScoreNumber5.setSpeed(2);
        scoreNumber5.setOpacity(0);

        Text scoreNumber6 = new Text("6. " + LeaderBoard.getScores().get(5).toString());
        slideInScoreNumber6.setNode(scoreNumber6);
        slideInScoreNumber6.setSpeed(2);
        scoreNumber6.setOpacity(0);

        slideInLeftLeaderBoard.setOnFinished(e -> {
            scoreNumber1.setTextAlignment(TextAlignment.CENTER);
            scoreNumber1.setFont(pixel_font);
            scoreNumber1.setFill(javafx.scene.paint.Color.WHITE);
            scoreNumber1.setTranslateY(ElementScaler.scale(-30));
            restart_menu.getChildren().add(scoreNumber1);
            slideInScoreNumber1.setCycleCount(1);
            slideInScoreNumber1.play();
            Timeline timeline = new Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0.001)));
            timeline.setCycleCount(1);
            timeline.play();
            timeline.setOnFinished(e1 -> {
                scoreNumber1.setOpacity(1);
            });
            slideInScoreNumber1.setOnFinished(e2 -> {
                scoreNumber2.setTextAlignment(TextAlignment.CENTER);
                scoreNumber2.setFont(pixel_font);
                scoreNumber2.setFill(javafx.scene.paint.Color.WHITE);
                scoreNumber2.setTranslateY(ElementScaler.scale(0));
                restart_menu.getChildren().add(scoreNumber2);
                slideInScoreNumber2.setCycleCount(1);
                slideInScoreNumber2.play();
                Timeline ntmJavaFx = new Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0.001)));
                ntmJavaFx.setCycleCount(1);
                ntmJavaFx.play();
                ntmJavaFx.setOnFinished(e1 -> {
                    scoreNumber2.setOpacity(1);
                });
            });
            slideInScoreNumber2.setOnFinished(e3 -> {
                scoreNumber3.setTextAlignment(TextAlignment.CENTER);
                scoreNumber3.setFont(pixel_font);
                scoreNumber3.setFill(javafx.scene.paint.Color.WHITE);
                scoreNumber3.setTranslateY(ElementScaler.scale(30));
                restart_menu.getChildren().add(scoreNumber3);
                slideInScoreNumber3.setCycleCount(1);
                slideInScoreNumber3.play();
                Timeline jvaisPtUnCable = new Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0.001)));
                jvaisPtUnCable.setCycleCount(1);
                jvaisPtUnCable.play();
                jvaisPtUnCable.setOnFinished(e1 -> {
                    scoreNumber3.setOpacity(1);
                });
            });
            slideInScoreNumber3.setOnFinished(e4 -> {
                scoreNumber4.setTextAlignment(TextAlignment.CENTER);
                scoreNumber4.setFont(pixel_font);
                scoreNumber4.setFill(javafx.scene.paint.Color.WHITE);
                scoreNumber4.setTranslateY(ElementScaler.scale(60));
                restart_menu.getChildren().add(scoreNumber4);
                slideInScoreNumber4.setCycleCount(1);
                slideInScoreNumber4.play();
                Timeline cDlaMerde = new Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0.001)));
                cDlaMerde.setCycleCount(1);
                cDlaMerde.play();
                cDlaMerde.setOnFinished(e1 -> {
                    scoreNumber4.setOpacity(1);
                });               
            });
            slideInScoreNumber4.setOnFinished(e5 -> {
                scoreNumber5.setTextAlignment(TextAlignment.CENTER);
                scoreNumber5.setFont(pixel_font);
                scoreNumber5.setFill(javafx.scene.paint.Color.WHITE);
                scoreNumber5.setTranslateY(ElementScaler.scale(90));
                restart_menu.getChildren().add(scoreNumber5);
                slideInScoreNumber5.setCycleCount(1);
                slideInScoreNumber5.play();
                Timeline qefhioiqfeho = new Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0.001)));
                qefhioiqfeho.setCycleCount(1);
                qefhioiqfeho.play();
                qefhioiqfeho.setOnFinished(e1 -> {
                    scoreNumber5.setOpacity(1);
                });
            });
            slideInScoreNumber5.setOnFinished(e6 -> {
                scoreNumber6.setTextAlignment(TextAlignment.CENTER);
                scoreNumber6.setFont(pixel_font);
                scoreNumber6.setFill(javafx.scene.paint.Color.WHITE);
                scoreNumber6.setTranslateY(ElementScaler.scale(120));
                restart_menu.getChildren().add(scoreNumber6);
                slideInScoreNumber6.setCycleCount(1);
                slideInScoreNumber6.play();
                Timeline aaaaa = new Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0.001)));
                aaaaa.setCycleCount(1);
                aaaaa.play();
                aaaaa.setOnFinished(e1 -> {
                    scoreNumber6.setOpacity(1);
                });
            });
            slideInScoreNumber6.setOnFinished(e7 -> {
                Text pressEnter = new Text("\nAppuyez sur ENTREE pour rejouer\nAppuyez sur M pour revenir au menu\nAppuyez sur ECHAP pour quitter");
                pressEnter.setOpacity(0);
                pressEnter.setTextAlignment(TextAlignment.CENTER);
                pressEnter.setFont(pixel_font);
                pressEnter.setFill(javafx.scene.paint.Color.WHITE);

                restart_menu.getChildren().add(pressEnter);

                pressEnter.setTranslateY(ElementScaler.scale(150));

                SlideInLeft slideInLeft = new SlideInLeft(pressEnter);
                slideInLeft.setCycleCount(1);
                slideInLeft.play();
                Timeline aaaaa = new Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.seconds(0.001)));
                aaaaa.setCycleCount(1);
                aaaaa.play();
                aaaaa.setOnFinished(e1 -> {
                    pressEnter.setOpacity(1);
                });
            });
        });

        restart_menu.getChildren().add(leaderBoard);
        slideInLeftLeaderBoard.play();
        leaderBoard.setOpacity(1);

        var GameOverController = new GameOverController();
        App.screen.setOnKeyPressed(GameOverController::keyPressedHandler);

        App.screen.setRoot(restart_menu);
    }

    public void exit() {
        restart_menu.getChildren().clear(); // Clear any added Texts when exiting the state
    }

    public void transitionTo(State s) {
        if (s instanceof PlayingState) {
            PlayingState.getInstance().initializeMaze();
        }
    }
}
