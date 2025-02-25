package gui;

import static model.Ghost.BLINKY;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import model.Critter;
import model.Ghost;
import model.PacMan;
import java.util.Map;
import java.util.HashMap;

public final class CritterGraphicsFactory {
    private final double scale;

    private Image image_mort;
    private Image image_scared;
    private Image image_anim;
    private Image ZONF, ZOND;
    private Map<Critter, String> images_D, images_G, images_H, images_B;
    // Stockez les images déjà chargées dans un cache
    private HashMap<String, Image> imageCache = new HashMap<>();

    public CritterGraphicsFactory(double scale) {
        this.scale = scale;

        image_mort = loadImage("ghost_dead.gif");
        image_scared = loadImage("ghost_scared_haut.gif");
        image_anim = loadImage("ghost_rainbow_haut.gif");
        ZONF = loadImage("ghost_zon.gif");
        ZOND = loadImage("ghost_zon.png");

        images_D = Map.of(
                Ghost.BLINKY, "ghost_red_droite.gif",
                Ghost.CLYDE, "ghost_yellow_droite.gif",
                Ghost.INKY, "ghost_blue_droite.gif",
                Ghost.PINKY, "ghost_purple_droite.gif");

        images_G = Map.of(
                Ghost.BLINKY, "ghost_red_gauche.gif",
                Ghost.CLYDE, "ghost_yellow_gauche.gif",
                Ghost.INKY, "ghost_blue_gauche.gif",
                Ghost.PINKY, "ghost_purple_gauche.gif");

        images_H = Map.of(
                Ghost.BLINKY, "ghost_red_haut.gif",
                Ghost.CLYDE, "ghost_yellow_haut.gif",
                Ghost.INKY, "ghost_blue_haut.gif",
                Ghost.PINKY, "ghost_purple_haut.gif");

        images_B = Map.of(
                Ghost.BLINKY, "ghost_red_bas.gif",
                Ghost.CLYDE, "ghost_yellow_bas.gif",
                Ghost.INKY, "ghost_blue_bas.gif",
                Ghost.PINKY, "ghost_purple_bas.gif");
    }

    private Image loadImage(String filename) {
        // Vérifiez si l'image est déjà dans le cache
        if (imageCache.containsKey(filename)) {
            return imageCache.get(filename);
        }

        // Sinon, chargez l'image en arrière plan et mettez-la en cache
        Image image = new Image(filename, scale * 0.7, scale * 0.7, true, true, true);
        imageCache.put(filename, image);
        return image;
    }

    public GraphicsUpdater makeGraphics(Critter critter) {
        var size = 0.7;
        var url = "pac-man-fortnite.gif";
        if (critter instanceof Ghost) {
            url = switch ((Ghost) critter) {
                case BLINKY -> images_D.get(Ghost.BLINKY);
                case CLYDE -> images_D.get(Ghost.CLYDE);
                case INKY -> images_D.get(Ghost.INKY);
                case PINKY -> images_D.get(Ghost.PINKY);
            };

        }
        var image = new ImageView(new Image(url, scale * size, scale * size, true, true));
        Rotate rotation = new Rotate(0, scale * size / 2, scale * size / 2); // Initial rotation angle set to 0
        image.getTransforms().add(rotation);

        return new GraphicsUpdater() {
            @Override
            public void changeColor(javafx.scene.paint.Color color) {
            }

            public void update() {
                image.setTranslateX((critter.getPos().x() + (1 - size) / 2) * scale);
                image.setTranslateY((critter.getPos().y() + (1 - size) / 2) * scale);
                // Debug.out("sprite updated");
                if (critter instanceof PacMan) {
                    switch (((PacMan) critter).getDirection()) {
                        case NORTH:
                            rotation.setAngle(90);
                            break;
                        case EAST:
                            rotation.setAngle(180);
                            break;
                        case WEST:
                            rotation.setAngle(0);
                            break;
                        case SOUTH:
                            rotation.setAngle(270);
                            break;
                    }
                }
                if (critter instanceof Ghost) {
                    if (PacMan.INSTANCE.isEnergized() && !((Ghost) critter).getDisableEnergizer()
                            && !((Ghost) critter).isMort() && !PacMan.INSTANCE.getzhonya()) {
                        if (PacMan.INSTANCE.getTemps() - PacMan.INSTANCE.getTempsCourant() <= 2) {
                            image.setImage(image_anim);
                        } else {
                            image.setImage(image_scared);
                        }
                    } else if (((Ghost) critter).isMort()) {
                        image.setImage(image_mort);
                    } else if (PacMan.INSTANCE.getzhonya()) {
                        if (PacMan.INSTANCE.getTempsZhonya() - PacMan.INSTANCE.getTempsCourantZhonya() <= 1.5) {
                            image.setImage(ZONF);
                        } else {
                            image.setImage(ZOND);
                        }
                    } else {
                        switch (((Ghost) critter).getDirection()) {
                            case NORTH:
                                image.setImage(loadImage(images_H.get(critter)));
                                break;
                            case EAST:
                                image.setImage(loadImage(images_D.get(critter)));
                                break;
                            case WEST:
                                image.setImage(loadImage(images_G.get(critter)));
                                break;
                            case SOUTH:
                                image.setImage(loadImage(images_B.get(critter)));
                                break;
                        }

                    }
                }
            }

            @Override
            public Node getNode() {
                return image;
            }
        };
    }
}