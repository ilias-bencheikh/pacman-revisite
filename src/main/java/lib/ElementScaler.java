package lib;

import gui.App;
public class ElementScaler {
    //The window is squared so we only need one resolution
    private static double currentResolution = 0;
    private static double targetResolution = 620;

    private ElementScaler() {
        // Constructeur privé pour empêcher la création d'autres instances
    }

    public static void updateResolution() {
        currentResolution = App.screen.getHeight();
    }

    public static double scale(double value) {
        System.out.println("Scaling " + value + " to " + value * currentResolution / targetResolution);
        return value * currentResolution / targetResolution;
    }
}
