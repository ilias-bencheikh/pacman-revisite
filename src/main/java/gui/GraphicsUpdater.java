package gui;

import javafx.scene.Node;

public interface GraphicsUpdater {
    void update();
    Node getNode();
    void changeColor(javafx.scene.paint.Color color);
}
