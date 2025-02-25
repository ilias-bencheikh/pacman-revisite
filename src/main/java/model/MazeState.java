package model;

import config.MazeConfig;
import geometry.IntCoordinates;
import geometry.RealCoordinates;
import gui.App;
import gui.AppStateMachine.PlayingState;
import gui.Controller.PacmanController;
import javafx.scene.media.Media;
import gui.AppStateMachine.GameOverState;
import gui.AppStateMachine.MazeWinState;

import java.util.List;
import java.util.Map;

import animatefx.animation.GlowText;
import animatefx.animation.Shake;

import static model.Ghost.*;

public final class MazeState {

    private final MazeConfig config;
    private final int height;
    private final int width;

    private final boolean[][] gridState;

    private final List<Critter> critters;
    private int score;

    private final Map<Critter, RealCoordinates> initialPos;
    private int lives = 3;

    private Media mediaDeath = new Media(getClass().getResource("/sounds/explosion.mp3").toString());
    private javafx.scene.media.MediaPlayer mediaPlayerDeath = new javafx.scene.media.MediaPlayer(mediaDeath);

    public MazeState(MazeConfig config) {
        if(mediaPlayerDeath != null){
            mediaPlayerDeath.setCycleCount(1);
            mediaPlayerDeath.setOnEndOfMedia(() -> {
                mediaPlayerDeath.stop();
                mediaPlayerDeath = new javafx.scene.media.MediaPlayer(mediaDeath);
                mediaPlayerDeath.seek(javafx.util.Duration.ZERO);
                mediaPlayerDeath.setCycleCount(1);
            });
        }
        this.config = config;
        height = config.getHeight();
        width = config.getWidth();
        critters = List.of(PacMan.INSTANCE, Ghost.CLYDE, BLINKY, INKY, PINKY);
        gridState = new boolean[height][width];
        // Met le gridState à true if si il n'a pas de boule
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                gridState[i][j] = !config.getCell(new IntCoordinates(j, i)).hasDot() ;
            }
        }
        initialPos = Map.of(
                PacMan.INSTANCE, config.getPacManPos().toRealCoordinates(1.0),
                BLINKY, config.getBlinkyPos().toRealCoordinates(1.0),
                INKY, config.getInkyPos().toRealCoordinates(1.0),
                CLYDE, config.getClydePos().toRealCoordinates(1.0),
                PINKY, config.getPinkyPos().toRealCoordinates(1.0));
        resetCritters();
        for (var ghost : List.of(BLINKY, INKY, PINKY, CLYDE)) {
            ghost.setInitialPos(initialPos.get(ghost));
            ghost.setExit_pos(config.getExit_pos().toRealCoordinates(1.0));
        }
    }

    public List<Critter> getCritters() {
        return critters;
    }

    public double getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean canMoveInDirection(Direction direction, IntCoordinates pos) {
        if (config.getCell(pos).canMoveInDirection(direction))
            return true;
        return false;
    }

    private void handleWallCollisions(Critter critter, long deltaTns) {
        var curPos = critter.getPos();
        var nextPos = critter.nextPos(deltaTns, config);
        var curNeighbours = curPos.intNeighbours();
        var nextNeighbours = nextPos.intNeighbours();
        if (!curNeighbours.containsAll(nextNeighbours)) {
            switch (critter.getDirection()) {
                case NORTH -> {
                    for (var n : curNeighbours) {
                        if (config.getCell(n).northWall()) {
                            nextPos = curPos.floorY();
                            critter.setDirection(Direction.NONE);
                            break;
                        }
                    }
                }
                case EAST -> {
                    for (var n : curNeighbours) {
                        if (config.getCell(n).eastWall()) {
                            nextPos = curPos.ceilX();
                            critter.setDirection(Direction.NONE);
                            break;
                        }
                    }
                }
                case SOUTH -> {
                    for (var n : curNeighbours) {
                        if (config.getCell(n).southWall()) {
                            nextPos = curPos.ceilY();
                            critter.setDirection(Direction.NONE);
                            break;
                        }
                    }
                }
                case WEST -> {
                    for (var n : curNeighbours) {
                        if (config.getCell(n).westWall()) {
                            nextPos = curPos.floorX();
                            critter.setDirection(Direction.NONE);
                            break;
                        }
                    }
                }
                default -> {
                    critter.setDirection(Direction.NONE);
                    break;
                }
            }
        }
        if (critter instanceof PacMan) {
            if (config.getCell(critter.getPos().round()).canMoveInDirection(PacmanController.nextDirection)) {
                switch (PacmanController.nextDirection) {
                    case NORTH, SOUTH -> {
                        if (PacmanController.currentDirection == Direction.EAST) {
                            int w = critter.getPos().round().x();
                            if (critter.getPos().x() <= w || true) {
                                critter.setPos(new RealCoordinates(w, critter.getPos().y()));
                            }
                            System.out.println("NS : EAST");
                        } else if (PacmanController.currentDirection == Direction.WEST) {
                            int e = critter.getPos().round().x();
                            if (critter.getPos().x() <= e || true) {
                                critter.setPos(new RealCoordinates(e, critter.getPos().y()));
                            }
                            System.out.println("NS : WEST");
                        } else {
                            critter.setPos(nextPos.warp(width, height));
                        }
                        critter.setDirection(PacmanController.nextDirection);
                        PacmanController.currentDirection = PacmanController.nextDirection;
                    }

                    case EAST, WEST -> {
                        if (PacmanController.currentDirection == Direction.SOUTH) {
                            int s = critter.getPos().round().y();
                            if (critter.getPos().y() <= s || true) {
                                critter.setPos(new RealCoordinates(critter.getPos().x(), s));
                            }
                            System.out.println("EW : SOUTH");
                        } else if (PacmanController.currentDirection == Direction.NORTH) {
                            int n = critter.getPos().round().y();
                            if (critter.getPos().y() <= n || true) {
                                critter.setPos(new RealCoordinates(critter.getPos().x(), n));
                            }
                            System.out.println("EW : NORTH");
                        } else {
                            critter.setPos(nextPos.warp(width, height));
                        }
                        critter.setDirection(PacmanController.nextDirection);
                        PacmanController.currentDirection = PacmanController.nextDirection;
                    }
                }
            } else {
                critter.setPos(nextPos.warp(width, height));
            }
        } else {
            critter.setPos(nextPos.warp(width, height));
        }
    }

    public void update(Long deltaTns) {
        if (!PacMan.INSTANCE.isDead())
            PacMan.INSTANCE.handleCollisionsWithGhosts(this);
        for (var critter : critters) {
            handleWallCollisions(critter, deltaTns);
        }
        PacMan.INSTANCE.handlePacManPoints(this);
        if (!PacMan.INSTANCE.isDead())
            PacMan.INSTANCE.handleCollisionsWithGhosts(this);
        PacMan.INSTANCE.fin_energizer(this);
        PacMan.INSTANCE.fin_energizer(this);
        PacMan.INSTANCE.fin_zhonya(this);
        PacMan.INSTANCE.fin_vitesseP(this);
        PacMan.INSTANCE.fin_vitesseM(this);
        PacMan.INSTANCE.fin_TeteDeMort(this);
        gameisWon();
    }

    public void addScore(int increment) {
        score += increment * 10;
        PlayingState.getInstance().score_graphics.setText("" + score);
        new GlowText(PlayingState.getInstance().score_graphics, javafx.scene.paint.Color.WHITE,
                javafx.scene.paint.Color.YELLOW)
                .play();
        displayScore();
    }

    private void displayScore() {
        System.out.println("Score: " + score);
    }

    public void gameisWon() {
        for (int i = 0; i < gridState.length; i++) {
            for (int j = 0; j < gridState[i].length; j++) {
                if (!gridState[i][j]) {
                    return;
                }
            }
        }
        PlayingState.getInstance().nextLevel();
    }

    public void playerLost() {
        Shake shake = new Shake(PlayingState.getInstance().game_root);
        if(mediaPlayerDeath != null){
            mediaPlayerDeath.play();
        }
        PlayingState.getInstance().gameView.stop();
        shake.play();
        PlayingState.getInstance().canPause = false;
        PacMan.INSTANCE.setDead(true);
        shake.setOnFinished(e -> {
            resetCritters();
            lives--;
            PlayingState.getInstance().life_graphics_update(lives);
            PlayingState.getInstance().canPause = true;
            System.out.println("shake");
            System.out.println("Lives: " + lives);
            PacMan.INSTANCE.setDead(false);
            if (lives == 1) {
                PlayingState.getInstance().mediaPlayerNormalMusic.stop();
                PlayingState.getInstance().mediaPlayerCriticMusic.play();
            }
            if (lives <= 0) {
                System.out.println("Game over!");
                App.app_state.changeState(GameOverState.getInstance());
            }
            System.out.println("Lives: " + lives);
            if (lives > 0)
                PlayingState.getInstance().gameView.play();
        });

    }

    public void resetCritter(Critter critter) {
        critter.setDirection(Direction.NONE);
        if (critter instanceof Ghost) {
            ((Ghost) critter).setTemps();
            ((Ghost) critter).setMort(false);
            ((Ghost) critter).setSortie(false);
            ((Ghost) critter).setPos(initialPos.get(critter));
            ((Ghost) critter).setDisableEnergizer(false);
        } else {
            critter.setPos(initialPos.get(critter));
            ((PacMan) critter).setEnergized(false);
            ((PacMan)critter).resetEnergizer();
            ((PacMan) critter).resetZhonya();
            ((PacMan) critter).resetVitesseP();
            ((PacMan) critter).resetVitesseM();
            ((PacMan) critter).resetTeteDeMort();
        }

    }

    public void resetGhost(Critter critter) {
        ((Ghost) critter).setSortie(false);
        ((Ghost) critter).setDisableEnergizer(true);
    }

    private void resetCritters() {
        for (var critter : critters)
            resetCritter(critter);
    }

    public MazeConfig getConfig() {
        return config;
    }

    public boolean getGridState(IntCoordinates pos) {
        return gridState[pos.y()][pos.x()];
    }

    public int getScore() {
        return score;
    }

    public void setGridState(boolean b, int y, int x) {
        gridState[y][x] = b;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int l) {
        lives = l;
        PlayingState.getInstance().life_graphics_update(l);
    }

    public void resetScore() {
        score = 0;
    }

    public void setScore(int s) {
        score = s;
        PlayingState.getInstance().score_graphics.setText("" + score);
    }
}