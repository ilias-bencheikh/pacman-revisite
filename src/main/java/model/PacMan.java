package model;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.animation.KeyFrame;

import static config.Cell.Content.vitesseP;

import javax.print.DocFlavor.STRING;

import config.Cell;
import geometry.RealCoordinates;
import gui.AppStateMachine.PlayingState;

/**
 * Implements Pac-Man character using singleton pattern. FIXME: check whether
 * singleton is really a good idea.
 */
public final class PacMan implements Critter {
    private Direction direction = Direction.NONE;
    private RealCoordinates pos;
    private boolean energized;
    private Timeline temps = new Timeline(new KeyFrame(Duration.seconds(5)));
    private boolean iszhonya = false;
    private boolean isvitesseP = false;
    private boolean isvitesseM = false;
    private Boolean isTeteDeMort = false;
    private final double COLLISION_THRESHOLD = 0.5;
    private Timeline temps_zhonya = new Timeline(new KeyFrame(Duration.seconds(3)));
    private Timeline temps_vitesseP = new Timeline(new KeyFrame(Duration.seconds(3)));
    private Timeline temps_vitesseM = new Timeline(new KeyFrame(Duration.seconds(3)));
    private Timeline temps_TeteDeMort = new Timeline(new KeyFrame(Duration.seconds(1)));
    private boolean isDead = false;

    private MediaPlayer mediaPlayerTimeStop;
    private MediaPlayer mediaPlayerThanos;

    private PacMan() {
        try {
            String path = "/sounds/dio-time-stop.mp3";
            String thanos = "/sounds/thanos.mp3";
            mediaPlayerTimeStop = new MediaPlayer(new Media(getClass().getResource(path).toString()));
            mediaPlayerTimeStop.setCycleCount(1);
            mediaPlayerTimeStop.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayerTimeStop.stop();
                    mediaPlayerTimeStop = new MediaPlayer(new Media(getClass().getResource(path).toString()));
                    mediaPlayerTimeStop.setCycleCount(1);
                    mediaPlayerTimeStop.seek(Duration.ZERO);
                }
            });
            mediaPlayerThanos = new MediaPlayer(new Media(getClass().getResource(thanos).toString()));
            mediaPlayerThanos.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayerThanos.stop();
                    mediaPlayerThanos = new MediaPlayer(new Media(getClass().getResource(thanos).toString()));
                    mediaPlayerThanos.setCycleCount(1);
                    mediaPlayerThanos.seek(Duration.ZERO);
                }
            });
            mediaPlayerThanos.setCycleCount(1);
        } catch (Exception e) {
            System.out.println("Erreur de lecture du fichier audio de time stop");
            e.printStackTrace();
        }
        temps.setCycleCount(1);
    }

    public static final PacMan INSTANCE = new PacMan();

    @Override
    public RealCoordinates getPos() {
        return pos;
    }

    @Override
    public double getSpeed(long deltaTNanoSeconds) {
        return isvitesseP ? 7 *deltaTNanoSeconds * 1E-9 : (isEnergized() ? 6 *deltaTNanoSeconds * 1E-9 : 4.5 * deltaTNanoSeconds * 1E-9);
    }
    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void setPos(RealCoordinates pos) {
        this.pos = pos;
    }

    public boolean ActifBonus(){
        return iszhonya || isvitesseP|| isvitesseM || isTeteDeMort;
    }


    public boolean getzhonya(){
        return iszhonya;
    }

    public double getTemps(){
        return temps.getCycleDuration().toSeconds();
    }

    public double getTempsCourant(){
        return temps.getCurrentTime().toSeconds();
    }

    public Double getTempsZhonya(){
        return temps_zhonya.getCycleDuration().toSeconds();
    }

    public Double getTempsCourantZhonya(){
        return temps_zhonya.getCurrentTime().toSeconds();
    }
    /**
     *
     * @return whether Pac-Man just ate an energizer
     */
    public boolean isEnergized() {
        return energized;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        this.isDead = dead;
    }

    public void resume() {
        if (temps.getStatus() == Timeline.Status.PAUSED) {
            temps.playFrom(Duration.seconds(temps.getCurrentTime().toSeconds()));
        }
        if (temps_zhonya.getStatus() == Timeline.Status.PAUSED) {
            temps_zhonya.playFrom(Duration.seconds(temps_zhonya.getCurrentTime().toSeconds()));
        }
        if (temps_vitesseP.getStatus() == Timeline.Status.PAUSED) {
            temps_vitesseP.playFrom(Duration.seconds(temps_vitesseP.getCurrentTime().toSeconds()));
        }
        if (temps_vitesseM.getStatus() == Timeline.Status.PAUSED) {
            temps_vitesseM.playFrom(Duration.seconds(temps_vitesseM.getCurrentTime().toSeconds()));
        }
        if(temps_TeteDeMort.getStatus() == Timeline.Status.PAUSED){
            temps_TeteDeMort.playFrom(Duration.seconds(temps_TeteDeMort.getCurrentTime().toSeconds()));
        }
    }

    public void pause() {
        temps.pause();
        temps_zhonya.pause();
        temps_vitesseP.pause();
        temps_vitesseM.pause();
        temps_TeteDeMort.pause();
    }

    public boolean isvitesseM() {
        return isvitesseM;
    }

    public void setEnergized(boolean energized) {
        this.energized = energized;
    }

    public void resetZhonya() {
        if (iszhonya) {
            temps_zhonya.stop();
            iszhonya = false;
        }
    }

    public void resetVitesseP() {
        if (isvitesseP) {
            temps_vitesseP.stop();
            isvitesseP = false;
        }
    }

    public void resetEnergizer() {
        if (energized) {
            temps.stop();
            energized = false;
        }
    }

    public void resetVitesseM() {
        if (isvitesseM) {
            temps_vitesseM.stop();
            isvitesseM = false;
        }
    }

    public void resetTeteDeMort() {
        if(isTeteDeMort){
            temps_TeteDeMort.stop();
            isTeteDeMort = false;
        }
    }

    public void resetAll() {
        resetEnergizer();
        resetZhonya();
        resetVitesseP();
        resetVitesseM();
        resetTeteDeMort();
    }

    public boolean verif_fin() {
        return temps.getStatus() == Timeline.Status.STOPPED;
    }

    public void fin_energizer(MazeState maze) {
        if (isEnergized() && verif_fin()) {
            setEnergized(false);
            for (var critter : maze.getCritters()) {
                if (critter instanceof Ghost) {
                    ((Ghost) critter).setDisableEnergizer(false);
                }
            }
        }
    }

    public void fin_zhonya(MazeState maze) {
        if (iszhonya && temps_zhonya.getStatus() == Timeline.Status.STOPPED) {
            iszhonya = false;
            if (!ActifBonus()) {
            Platform.runLater(() -> {
                PlayingState.getInstance().changeWallToBlue();
            });
        }
            if(isEnergized()){
                temps.playFrom(Duration.seconds(temps.getCurrentTime().toSeconds()));
            }
        }
    }

    public void fin_vitesseP(MazeState maze) {
        if (isvitesseP && temps_vitesseP.getStatus() == Timeline.Status.STOPPED) {
            isvitesseP = false;
            if (!ActifBonus()) {
            Platform.runLater(() -> {
                PlayingState.getInstance().changeWallToBlue();
            });
        }}
    }

    public void fin_vitesseM(MazeState maze) {
        if (isvitesseM && temps_vitesseM.getStatus() == Timeline.Status.STOPPED) {
            isvitesseM = false;
            if (!ActifBonus()) {
            Platform.runLater(() -> {
                PlayingState.getInstance().changeWallToBlue();
            });
        }}
    }

    public void fin_TeteDeMort(MazeState maze) {
        if (isTeteDeMort && temps_TeteDeMort.getStatus() == Timeline.Status.STOPPED) {
            isTeteDeMort = false;
            if (!ActifBonus()) {
            Platform.runLater(() -> {
                PlayingState.getInstance().changeWallToBlue();
            });
        }}
    }

    public void handlePacManPoints(MazeState maze) {
        if (!maze.getGridState(pos.round())) {
            if (maze.getConfig().getCell(pos.round()).getContent() == Cell.Content.ENERGIZER) {
                maze.addScore(10);
                maze.setGridState(true, pos.round().y(), pos.round().x());
                if (isEnergized()) {
                    temps.playFrom(Duration.seconds(0));
                } else {
                    setEnergized(true);
                    temps.play();
                }
                for (var critter : maze.getCritters()) {
                    if (critter instanceof Ghost) {
                        ((Ghost) critter).setDisableEnergizer(false);
                    }
                }
            } else if (maze.getConfig().getCell(pos.round()).getContent() == Cell.Content.DOT) {
                maze.addScore(1);
                maze.setGridState(true, pos.round().y(), pos.round().x());
            } else if (maze.getConfig().getCell(pos.round()).getContent() == Cell.Content.ZHONYA) {
                maze.addScore(50);
                if(mediaPlayerTimeStop != null)
                mediaPlayerTimeStop.play();
                Platform.runLater(() -> {
                    PlayingState.getInstance().changeWallToKhaki();
                });
                maze.setGridState(true, pos.round().y(), pos.round().x());
                iszhonya = true;
                temps_zhonya.play();
                if (isEnergized()) {
                    temps.pause();
                }

            } else if (maze.getConfig().getCell(pos.round()).getContent() == Cell.Content.vitesseP) {
                maze.addScore(50);
                Platform.runLater(() -> {
                    PlayingState.getInstance().changeWallToRoyalBlue();
                });
                maze.setGridState(true, pos.round().y(), pos.round().x());
                isvitesseP = true;
                temps_vitesseP.play();

            } else if (maze.getConfig().getCell(pos.round()).getContent() == Cell.Content.vitesseM) {
                maze.addScore(50);
                Platform.runLater(() -> {
                    PlayingState.getInstance().changeWallToRed();
                });
                maze.setGridState(true, pos.round().y(), pos.round().x());
                isvitesseM = true;
                temps_vitesseM.play();
            } else if (maze.getConfig().getCell(pos.round()).getContent() == Cell.Content.HEAL) {
                maze.addScore(50);
                maze.setGridState(true, pos.round().y(), pos.round().x());
                maze.setLives(maze.getLives() + 1);
                if (maze.getLives() > 1) {
                    PlayingState.getInstance().mediaPlayerCriticMusic.stop();
                    PlayingState.getInstance().mediaPlayerNormalMusic.play();

                }
            }
        else if(maze.getConfig().getCell(pos.round()).getContent() == Cell.Content.TeteDeMort){
            maze.addScore(50);
            maze.setGridState(true, pos.round().y(), pos.round().x());
            isTeteDeMort = true;
            temps_TeteDeMort.play();
            Platform.runLater(() -> {
                    PlayingState.getInstance().changeWallToGray();
                });
                if(mediaPlayerThanos != null)
                mediaPlayerThanos.play();
            for (var critter : maze.getCritters()) {
                if (critter instanceof Ghost) {
                    ((Ghost) critter).setMort(true);
                    maze.resetGhost((Ghost) critter);
                }}
            }
        else if(maze.getConfig().getCell(pos.round()).getContent() == Cell.Content.TeteDeMort){
            maze.addScore(50);
            maze.setGridState(true, pos.round().y(), pos.round().x());
            isTeteDeMort = true;
            temps_TeteDeMort.play();
            Platform.runLater(() -> {
                    PlayingState.getInstance().changeWallToGray();
                });
                if(mediaPlayerThanos != null)
                mediaPlayerThanos.play();
            for (var critter : maze.getCritters()) {
                if (critter instanceof Ghost) {
                    ((Ghost) critter).setMort(true);
                    maze.resetGhost((Ghost) critter);
                }}
            }
        
    }
    }

    public void handleCollisionsWithGhosts(MazeState maze) {
        var pacPos = PacMan.INSTANCE.getPos();
        for (var critter : maze.getCritters()) {
            if (critter instanceof Ghost && !((Ghost) critter).isMort() && !iszhonya && ((Ghost) critter).getSortie()) {
                var ghostPos = critter.getPos();
                double distance = Math.sqrt(Math.pow(pacPos.x() - ghostPos.x(), 2) + Math.pow(pacPos.y() - ghostPos.y(), 2));
                if (distance < COLLISION_THRESHOLD) {
                    Platform.runLater(() -> {
                        PlayingState.getInstance().changeWallToBlue();
                    });
                    maze.setGridState(true, pos.round().y(), pos.round().x());
                    handleGhostCollision(maze, (Ghost) critter);
                }
            }
        }
    }

    private void handleGhostCollision(MazeState maze, Ghost ghost) {
        if (isEnergized() && !ghost.getDisableEnergizer()) {
            maze.addScore(10);
            ghost.setMort(true);
            maze.resetGhost(ghost);
        } else {
            if (!isDead)
            maze.playerLost();
        }
    }

    public void playDeathAnimation() {
        System.out.println("PacMan is dead");
        isDead = true;
        // Now we need to play the death animation by changing the sprite

    }

}
