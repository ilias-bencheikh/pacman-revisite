package gui.AppStateMachine;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import animatefx.animation.BounceIn;

import java.util.ArrayList;

import java.util.ArrayList;

import config.MazeConfig;
import gui.App;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import gui.GameView;
import gui.GraphicsUpdater;
import gui.Controller.PacmanController;
import model.Critter;
import model.MazeState;
import lib.FontLoader;
import lib.State;
import lib.ElementScaler;

public class PlayingState extends App implements State {
    private String state_name = "Playing State";

    public StackPane game = new StackPane();
    public BorderPane game_root = new BorderPane();

    public boolean canPause = true;
    private boolean nextLevel = false;
    public int level = 1;

    private String musicFileName = "/ost/epic-hybrid.mp3";
    private Media mediaNormalMusic = new Media(getClass().getResource(musicFileName).toString());
    private String musicFileName2 = "/ost/eternal-energy.mp3";
    private Media mediaCriticMusic = new Media(getClass().getResource(musicFileName2).toString());
    public MediaPlayer mediaPlayerNormalMusic;
    {
        try {
            mediaPlayerNormalMusic = new MediaPlayer(mediaNormalMusic);
        } catch (Exception e) {
            System.out.println("Erreur de lecture du fichier audio");
        }
    }
    public MediaPlayer mediaPlayerCriticMusic;
    {
        try {
            mediaPlayerCriticMusic = new MediaPlayer(mediaCriticMusic);
        } catch (Exception e) {
            System.out.println("Erreur de lecture du fichier audio");
        }
    }
    public Label score_graphics = new Label("" + 0);

    public HBox life_graphics = new HBox();

    private double MAX_FONT_SIZE = 30.0;
    private Font pixel_font = FontLoader.getPixelFont(MAX_FONT_SIZE);
    private Font pixel_little_font = FontLoader.getPixelFont(20);

    private static final PlayingState instance = new PlayingState();

    public MazeState maze;
    public GameView gameView;

    private PlayingState() {
        // Constructeur privé pour empêcher la création d'autres instances
    }

    public static PlayingState getInstance() {
        return instance;
    }

    public String showState() {
        return state_name;
    }

    public Label createScoreGraphics() {
        Label score_graphics = new Label();

        score_graphics.setTextAlignment(TextAlignment.LEFT);
        score_graphics.setFont(pixel_font);
        score_graphics.setTextFill(javafx.scene.paint.Color.WHITE);

        return score_graphics;
    }

    public void life_graphics_update(int lives) {
        life_graphics.getChildren().clear(); // Efface les vies précédentes
        for (int i = 0; i < lives; i++) {
            Image img = new Image(getClass().getResourceAsStream("/pixel_pacman_icon.png"));
            ImageView view = new ImageView(img);
            view.setPreserveRatio(true);
            view.setFitHeight(ElementScaler.scale(25));
            view.setFitWidth(ElementScaler.scale(25));

            Label lifeLabel = new Label();
            lifeLabel.setGraphic(view);

            life_graphics.getChildren().add(lifeLabel);
        }
    }

    public void initializeMaze() {
        game.getChildren().clear();
        game_root.getChildren().clear();
        BorderPane game_screen = new BorderPane();
        mediaPlayerNormalMusic.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayerCriticMusic.setCycleCount(MediaPlayer.INDEFINITE);

        if(HomeScreenState.getInstance().getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING && !nextLevel)
            HomeScreenState.getInstance().getMediaPlayer().stop();
        if(!nextLevel)
        mediaPlayerNormalMusic.play();
        BorderPane root = new BorderPane();

        game_screen.setStyle("-fx-background-color: black;");

        int previousScore = 0;
        int previousLives = 3;

        if(maze != null && nextLevel){
            previousScore = maze.getScore();
            previousLives = maze.getLives();
            nextLevel = false;
        }
        maze = new MazeState(MazeConfig.makeMaze(level));
        maze.setScore(previousScore);
        maze.setLives(previousLives);

        score_graphics = createScoreGraphics();
        life_graphics_update(maze.getLives());

        var pacmanController = new PacmanController();
        App.screen.setOnKeyPressed(pacmanController::keyPressedHandler);
        App.screen.setOnKeyReleased(pacmanController::keyReleasedHandler);

        double scale = 0;
        // Calculate the scale according to screen resolution and by making sure that
        // all the maze will be visible
        if (maze.getWidth() > maze.getHeight()) {
            scale = (App.screen.getWidth() - 100) / maze.getWidth();
        } else {
            scale = (App.screen.getHeight() - 100) / maze.getHeight();
        }
        gameView = new GameView(maze, root, scale);

        game_root.setCenter(gameView.getGameRoot());

        game_screen.setCenter(game_root);

        // Ajoutez life_graphics en bas du BorderPane
        game_screen.setBottom(life_graphics);

        Text lifeText = new Text("Vie: ");
        lifeText.setFill(javafx.scene.paint.Color.WHITE);
        lifeText.setFont(pixel_font);

        HBox bottomBox = new HBox(lifeText, life_graphics);
        game_screen.setBottom(bottomBox);


        // Ajoutez score_graphics en haut du BorderPane
        Text pauseText = new Text("Echap : Pause");
        pauseText.setFill(javafx.scene.paint.Color.YELLOW);
        pauseText.setFont(pixel_little_font);

        BorderPane topBox = new BorderPane();
        topBox.setLeft(score_graphics);
        topBox.setRight(pauseText);
        topBox.setPadding(new javafx.geometry.Insets(5));

        game_screen.setTop(topBox);

        game.getChildren().add(game_screen);
        Text levelText = new Text("Niveau " + level);
        levelText.setFill(javafx.scene.paint.Color.WHITE);
        levelText.setFont(pixel_font);
        game.getChildren().add(levelText);
        BounceIn intro = new BounceIn(game_root);
        canPause = false;
        intro.setOnFinished(e -> {
            intro.stop();
            game.getChildren().remove(levelText);
            gameView.animate();
            canPause = true;
        });
        intro.play();
    }

    public ArrayList<GraphicsUpdater> getGraphicsUpdaters() {
        return gameView.getGraphicsUpdaters();
    }

    public void changeWallToKhaki() {
        for (var updater : getGraphicsUpdaters()) {
            updater.changeColor(javafx.scene.paint.Color.KHAKI);
        }
    }

    public void changeWallToBlue() {
        for (var updater : getGraphicsUpdaters()) {
            updater.changeColor(javafx.scene.paint.Color.BLUE);
        }
    }

    public void changeWallToRoyalBlue() {
        for (var updater : getGraphicsUpdaters()) {
            updater.changeColor(javafx.scene.paint.Color.ROYALBLUE);
        }
    }

    public void changeWallToRed() {
        for (var updater : getGraphicsUpdaters()) {
            updater.changeColor(javafx.scene.paint.Color.RED);
        }
    }

    public void changeWallToGray() {
        for (var updater : getGraphicsUpdaters()) {
            updater.changeColor(javafx.scene.paint.Color.GREY);
        }
    }

    public void enter() {
        App.screen.setRoot(game);
    }

    public void exit() {
        gameView.stop();
    }

    public int getLevel(){
        return level;
    }

    public void nextLevel(){
        nextLevel = true;
        level++;
        gameView.stop();
        game.getChildren().clear();
        initializeMaze();
    }

    public void transitionTo(State s) {
        if (s instanceof GameOverState || s instanceof MazeWinState || s instanceof PlayingState) {
            if (mediaPlayerNormalMusic.getStatus() == MediaPlayer.Status.PLAYING)
                mediaPlayerNormalMusic.stop();
            if (mediaPlayerCriticMusic.getStatus() == MediaPlayer.Status.PLAYING)
                mediaPlayerCriticMusic.stop();
            game.getChildren().clear();
        }

        if (s instanceof PauseState){
            for(Critter c : maze.getCritters()){
                if(c instanceof model.PacMan){
                    ((model.PacMan) c).pause();
                }
            }
        }
    }
}
