package gui.AppStateMachine;

import gui.App;
import gui.Controller.StartingLogoController;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.scene.effect.GaussianBlur;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import animatefx.animation.Flash;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import animatefx.animation.Tada;
import animatefx.animation.ZoomOut;
import animatefx.animation.FadeOutLeft;
import animatefx.animation.RubberBand;

import java.util.ArrayList;

import animatefx.animation.BounceInUp;
import lib.FontLoader;
import lib.State;
import lib.ElementScaler;

public class StartingLogosState implements State {
    private String state_name = "Starting Logos State";
    private static final StartingLogosState instance = new StartingLogosState();
    private String musicFileName = "/ost/Carl-Orff-O-Fortuna-_-Carmina-Burana.wav";
    private Media media = new Media(getClass().getResource(musicFileName).toString());
    public MediaPlayer mediaPlayer = new MediaPlayer(media);
    private StackPane black_background = new StackPane();
    private StackPane starting_logos = new StackPane();

    private double MAX_FONT_SIZE = 30.0;
    private Font pixel_font = FontLoader.getPixelFont(MAX_FONT_SIZE);

    private Text skipText = new Text("Appuyez sur entree pour passer");


    private Flash skipTextFlash = new Flash(skipText);

    private StartingLogosState() {
        // Constructeur privé pour empêcher la création d'autres instances
    }

    public static StartingLogosState getInstance() {
        return instance;
    }

    public String showState() {
        return state_name;
    }

    private ImageView createGhost(String name) {
        ImageView ghost = new ImageView(new Image(getClass().getResourceAsStream("/logos/3d_" + name + ".png")));
        ghost.setFitHeight(ElementScaler.scale(100));
        ghost.setFitWidth(ElementScaler.scale(100));
        ghost.setPreserveRatio(true);
        return ghost;
    }

    private TranslateTransition cTranslateTransition(ImageView ghost, double fromX, double fromY, double toX) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), ghost);
        translateTransition.setFromX(fromX); // Position de départ en X
        translateTransition.setFromY(fromY); // Position de départ en Y

        translateTransition.setToX(toX);
        translateTransition.setCycleCount(1);
        return translateTransition;
    }

    private void createBackground(String bgPath) {
        starting_logos.setMaxHeight(App.pStage.getHeight());
        starting_logos.setMaxWidth(App.pStage.getWidth());
        starting_logos.setStyle("-fx-background-image: url('" + bgPath + "'); " +
                "-fx-background-repeat: repeat; " +
                "-fx-background-size: cover;");
        System.out.println(App.pStage.getHeight() + " " + App.pStage.getWidth());
    }

    private ArrayList<ImageView> createGhosts(boolean isEaten) {
        ArrayList<ImageView> ghosts = new ArrayList<ImageView>();
        if (isEaten) {
            for (int i = 0; i < 4; i++) {
                ghosts.add(createGhost("eat"));
            }
        } else {
            for (int i = 0; i < 4; i++) {
                switch (i) {
                    case 0:
                        ghosts.add(createGhost("inky"));
                        break;
                    case 1:
                        ghosts.add(createGhost("pinky"));
                        break;
                    case 2:
                        ghosts.add(createGhost("clyde"));
                        break;
                    case 3:
                        ghosts.add(createGhost("blinky"));
                        break;
                }
            }
        }
        return ghosts;
    }

    private ArrayList<TranslateTransition> createTranslateTransitions(ArrayList<ImageView> ghosts, String direction) {
        ArrayList<TranslateTransition> translateTransitions = new ArrayList<TranslateTransition>();
        double halfScreenHeight = starting_logos.getMaxHeight() / 2;
        double halfScreenWidth = starting_logos.getMaxWidth() / 2;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    if (direction == "left") {
                        translateTransitions.add(cTranslateTransition(ghosts.get(i), -halfScreenWidth - 100,
                                -halfScreenHeight + 100, halfScreenWidth + 100));
                    } else if (direction == "right") {
                        translateTransitions.add(cTranslateTransition(ghosts.get(i), halfScreenWidth + 100,
                                -halfScreenHeight + 100, -halfScreenWidth - 100));
                    }
                    break;
                case 1:
                    if (direction == "left") {
                        translateTransitions.add(cTranslateTransition(ghosts.get(i), -halfScreenWidth - 100, -75,
                                halfScreenWidth + 100));
                    } else if (direction == "right") {
                        translateTransitions.add(cTranslateTransition(ghosts.get(i), halfScreenWidth + 100, 0 - 75,
                                -halfScreenWidth - 100));
                    }
                    break;
                case 2:
                    if (direction == "left") {
                        translateTransitions.add(
                                cTranslateTransition(ghosts.get(i), -halfScreenWidth - 100, 75, halfScreenWidth + 100));
                    } else if (direction == "right") {
                        translateTransitions.add(
                                cTranslateTransition(ghosts.get(i), halfScreenWidth + 100, 75, -halfScreenWidth - 100));
                    }
                    break;
                case 3:
                    if (direction == "left") {
                        translateTransitions.add(cTranslateTransition(ghosts.get(i), -halfScreenWidth - 100,
                                halfScreenHeight - 100, halfScreenWidth + 100));
                    } else if (direction == "right") {
                        translateTransitions.add(cTranslateTransition(ghosts.get(i), halfScreenWidth + 100,
                                halfScreenHeight - 100, -halfScreenWidth - 100));
                    }
                    break;
            }
        }
        return translateTransitions;
    }

    private Timeline createTranslateTransition(ArrayList<TranslateTransition> translateTransitions,
            ArrayList<ImageView> ghosts) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            for (TranslateTransition translateTransition : translateTransitions) {
                translateTransition.play();
            }
            for (ImageView ghost : ghosts) {
                ghost.setVisible(false);
                starting_logos.getChildren().add(ghost);
                skipText.toFront();
            }
            Timeline zizi = new Timeline(new KeyFrame(Duration.seconds(0.2), event2 -> {
                for (ImageView ghost : ghosts) {
                    ghost.setVisible(true);
                }
            }));
            zizi.setCycleCount(1);
            zizi.play();
        }));
        timeline.setOnFinished(event -> {
            timeline.stop();
        });
        return timeline;
    }

    private Timeline createBouncingTitle(StackPane pane, Label text1, Label text2) {
        return new Timeline(new KeyFrame(Duration.seconds(1.5), event0 -> {
            if(App.app_state.getState() != StartingLogosState.getInstance()){
                return;
            }
            pane.setVisible(false);
            starting_logos.getChildren().removeAll();
            starting_logos.getChildren().add(pane);
            BounceInUp bounce8h30 = new BounceInUp(text1);
            BounceInUp bounceStudio = new BounceInUp(text2);
            bounce8h30.play();
            bounceStudio.play();
            Timeline jpp = new Timeline(new KeyFrame(Duration.seconds(0.1), event1 -> {
                pane.setVisible(true);
            }));
            jpp.setCycleCount(1);
            jpp.play();
            Timeline blurText = new Timeline(new KeyFrame(Duration.seconds(2), event3 -> {
                GaussianBlur gaussianBlur = new GaussianBlur();
                gaussianBlur.setRadius(10);
                text1.setEffect(gaussianBlur);
                text2.setEffect(gaussianBlur);
            }));
            blurText.setCycleCount(1);
            blurText.play();
        }));
    }

    private Timeline createFadeOut(StackPane p) {
        return new Timeline(new KeyFrame(Duration.seconds(2.0), event2 -> {
            if(App.app_state.getState() != StartingLogosState.getInstance()){
                return;
            }
            FadeOut fadeOutTransitionText1 = new FadeOut(p);
            fadeOutTransitionText1.play();
        }));
    }

    private Label crLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setFont(pixel_font);
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        return label;
    }

    private void createCharacterIcon(BorderPane character, String name) {
        Image ch = new Image(getClass().getResourceAsStream("/logos/3d_" + name + ".png"));
        ImageView viewCharacter = new ImageView(ch);
        Image bordePixel = new Image(getClass().getResourceAsStream("/logos/border_pixel.png"));
        ImageView viewBorderPixel = new ImageView(bordePixel);
        viewBorderPixel.setFitHeight(100);
        viewBorderPixel.setFitWidth(200);
        Label label = crLabel(name);
        viewCharacter.setFitHeight(400);
        viewCharacter.setFitWidth(400);
        viewCharacter.setPreserveRatio(true);

        // Frame the label with viewBorderPixel
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(viewBorderPixel, label);
        stackPane.setAlignment(Pos.CENTER);

        character.setBottom(viewCharacter);
        character.setTop(stackPane);

    }

    private Timeline createPopCharacter(String name, Group group) {
        BorderPane character = new BorderPane();
        Tada lightSpeedIn = new Tada(character);
        character.setVisible(false);
        createCharacterIcon(character, name);
        group.getChildren().add(character);
        if(App.app_state.getState() != StartingLogosState.getInstance()){
            return null;
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2.5), event -> {
            character.setVisible(true);
            Platform.runLater(() -> {
                lightSpeedIn.play();
            });
        }));
        return timeline;
    }

    private Timeline createCharacterAnimation(String name, Group group) {
        Timeline res = createPopCharacter(name, group);
        res.setCycleCount(1);
        return res;
    }

    private Timeline createZoomOut(Node node) {
        ZoomOut zoomOut = new ZoomOut(node);
        Timeline zoomOutTimeline = new Timeline(new KeyFrame(Duration.seconds(2.5), event -> {
            zoomOut.play();
            new RubberBand(starting_logos).play();
        }));
        zoomOutTimeline.setCycleCount(1);
        return zoomOutTimeline;
    }

    private Timeline slideToHomeScreen(Timeline timelineTranslateTransition) {
        Timeline slideHome = new Timeline(new KeyFrame(Duration.seconds(2), event3 -> {
            timelineTranslateTransition.play();
            FadeOutLeft fadeOut = new FadeOutLeft(starting_logos);
            fadeOut.setDelay(Duration.seconds(0.5));
            fadeOut.setSpeed(0.75);
            fadeOut.play();
        }));
        slideHome.setCycleCount(1);
        return slideHome;
    }

    private void changeHomeScreenStateWait(Timeline timelineTranslateTransition) {
        timelineTranslateTransition.setOnFinished(event -> {
            timelineTranslateTransition.stop();
            Timeline wait = new Timeline(new KeyFrame(Duration.seconds(2), event3 -> {
                if(App.app_state.getState() == StartingLogosState.getInstance()){
                    //mediaPlayer.stop();
                    App.app_state.changeState(HomeScreenState.getInstance());
                }
            }));
            wait.setCycleCount(1);
            wait.setOnFinished(event2 -> {
                wait.stop();
            });
            wait.play();
        });
    }

    private void createPopCharactersAndFinalSlide() {
        if(App.app_state.getState() != StartingLogosState.getInstance()){
            return;
        }
        Group characters = new Group();
        starting_logos.getChildren().add(characters);
        GaussianBlur gaussianBlur = new GaussianBlur();
        gaussianBlur.setRadius(10);
        Timeline pacmanPop = createCharacterAnimation("pacman", characters);
        Timeline blinkyPop = createCharacterAnimation("blinky", characters);
        Timeline pinkyPop = createCharacterAnimation("pinky", characters);
        Timeline inkyPop = createCharacterAnimation("inky", characters);
        Timeline clydePop = createCharacterAnimation("clyde", characters);
        pacmanPop.play();
        new RubberBand(starting_logos).play();
        pacmanPop.setOnFinished(event -> {
            pacmanPop.stop();
            Timeline zoomOut = createZoomOut(characters.getChildren().get(0));
            zoomOut.play();
            blinkyPop.play();
            blinkyPop.setOnFinished(event2 -> {
                blinkyPop.stop();
                Timeline zoomOutBlinky = createZoomOut(characters.getChildren().get(1));
                zoomOutBlinky.play();
                pinkyPop.play();
            });
        });
        pinkyPop.setOnFinished(event -> {
            pinkyPop.stop();
            Timeline zoomOutPinky = createZoomOut(characters.getChildren().get(2));
            zoomOutPinky.play();
            inkyPop.play();
        });
        inkyPop.setOnFinished(event -> {
            inkyPop.stop();
            Timeline zoomOutInky = createZoomOut(characters.getChildren().get(3));
            zoomOutInky.play();
            clydePop.play();
        });
        ArrayList<ImageView> ghosts = createGhosts(true);
        ArrayList<TranslateTransition> translateTransitionsRight = createTranslateTransitions(ghosts, "right");
        Timeline timelineTranslateTransition = createTranslateTransition(translateTransitionsRight, ghosts);
        Timeline ghostSlideToHome = slideToHomeScreen(timelineTranslateTransition);
        clydePop.setOnFinished(event -> {
            Timeline zoomOutClyde = createZoomOut(characters.getChildren().get(4));
            zoomOutClyde.play();
            ghostSlideToHome.play();
        });
        clydePop.setOnFinished(event -> {
            clydePop.stop();
            ghostSlideToHome.play();
        });
        ghostSlideToHome.setOnFinished(event -> {
            ghostSlideToHome.stop();
            timelineTranslateTransition.play();
        });
        changeHomeScreenStateWait(timelineTranslateTransition);
    }

    private void whenGhostTranslateIsFinishedAnimation() {
        if(App.app_state.getState() != StartingLogosState.getInstance()){
            return;
        }
        double maxX = Screen.getPrimary().getBounds().getMaxX();
        double halfScreen = starting_logos.getMaxHeight() / 2;

        Label textGameBy = crLabel("A game by");
        Label text8h30 = crLabel("8h30");
        Label textStudio = crLabel("Studio");

        StackPane paneGameBy = new StackPane();
        paneGameBy.getChildren().add(textGameBy);
        paneGameBy.opacityProperty().setValue(0.0);

        StackPane pane8h30Studio = new StackPane();
        pane8h30Studio.setMaxHeight(halfScreen);
        pane8h30Studio.setMaxWidth(maxX);
        pane8h30Studio.getChildren().addAll(text8h30, textStudio);
        StackPane.setAlignment(text8h30, Pos.TOP_CENTER);
        StackPane.setAlignment(textStudio, Pos.CENTER);

        FadeIn fadeInTransitionText1 = new FadeIn(paneGameBy);
        fadeInTransitionText1.play();
        starting_logos.getChildren().add(paneGameBy);

        Timeline fadeOutText1 = createFadeOut(paneGameBy);
        fadeOutText1.setCycleCount(1);
        fadeOutText1.play();

        Timeline bouncing8h30 = createBouncingTitle(pane8h30Studio, text8h30, textStudio);
        bouncing8h30.setCycleCount(1);

        fadeOutText1.setOnFinished(event1 -> {
            fadeOutText1.stop();
            bouncing8h30.play();
        });

        bouncing8h30.setOnFinished(event2 -> {
            bouncing8h30.stop();
            createPopCharactersAndFinalSlide();
        });

    }

    public void enter() {
        skipText.setTextAlignment(javafx.scene.text.TextAlignment.LEFT);
        skipText.setFill(javafx.scene.paint.Color.WHITE);
        skipText.setFont(FontLoader.getPixelFont(ElementScaler.scale(20)));
        skipText.setTranslateY(App.pStage.getHeight() / 2 - 50);
        starting_logos.getChildren().add(skipText);
        skipTextFlash.setCycleCount(Timeline.INDEFINITE);
        skipTextFlash.setSpeed(0.5);
        skipTextFlash.play();
        MAX_FONT_SIZE = ElementScaler.scale(MAX_FONT_SIZE);
        mediaPlayer.setStartTime(Duration.seconds(1));
        mediaPlayer.play();
        black_background.setStyle("-fx-background-color: black");
        black_background.setMaxHeight(App.pStage.getHeight());
        black_background.setMaxWidth(App.pStage.getWidth());
        starting_logos.setMaxHeight(App.pStage.getHeight());
        starting_logos.setMaxWidth(App.pStage.getWidth());

        black_background.getChildren().add(starting_logos);
        createBackground("/black_background.jpg");

        ArrayList<ImageView> ghosts = createGhosts(false);
        ArrayList<TranslateTransition> translateTransitionsLeft = createTranslateTransitions(ghosts, "left");

        Timeline timeline = createTranslateTransition(translateTransitionsLeft, ghosts);
        timeline.setCycleCount(1);
        Platform.runLater(() -> {
            timeline.play();
        });

        translateTransitionsLeft.get(0).setOnFinished(event -> {
            for (TranslateTransition translateTransition : translateTransitionsLeft) {
                translateTransition.stop();
            }
            // Remove the ghost from the pane when the animation is finished
            for (ImageView ghost : ghosts) {
                starting_logos.getChildren().remove(ghost);
            }
            whenGhostTranslateIsFinishedAnimation();
        });

        var startingLogoController = new StartingLogoController();
        App.screen.setOnKeyPressed(startingLogoController::keyPressedHandler);
         App.screen.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if(event.getEventType().equals(javafx.scene.input.MouseEvent.MOUSE_CLICKED)){
                    App.app_state.changeState(HomeScreenState.getInstance());
                }
            }
        });

        App.screen.setRoot(black_background);
    }

    public void exit() {
        skipTextFlash.stop();
        starting_logos.getChildren().clear();
        mediaPlayer.stop();
        App.screen.setOnMouseClicked(null);
    }

    public void transitionTo(State s) {

    }
}
