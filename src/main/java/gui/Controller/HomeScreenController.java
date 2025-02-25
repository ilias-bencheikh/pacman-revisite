package gui.Controller;

import java.io.File;
import java.nio.file.Files;

import gui.App;
import gui.AppStateMachine.EnterNameState;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class HomeScreenController {

    private ButtonType buttonYes = new ButtonType("Oui");
    private ButtonType buttonOk = new ButtonType("Ok");
    private ButtonType buttonNo = new ButtonType("Non");
    private Alert alertConfirm = new Alert(Alert.AlertType.INFORMATION, "Fichier de sauvegarde supprimé", buttonOk);
    private Alert alertDelete = new Alert(Alert.AlertType.WARNING,
            "Voulez vous supprimer les scores sauvegardés ?", buttonYes, buttonNo);
    private Alert alertError = new Alert(Alert.AlertType.ERROR,
            "Erreur lors de la suppression du fichier de sauvegarde", buttonOk);
    private Alert quitGame = new Alert(Alert.AlertType.CONFIRMATION, "Voulez vous quitter le jeu ?", buttonYes,
            buttonNo);
    private boolean suppressInProgress = false;

    public void keyPressedHandler(KeyEvent event) {
        if (event.getCode().equals(KeyCode.R) && !suppressInProgress) {
            if (!alertDelete.isShowing() && !alertConfirm.isShowing() && !alertError.isShowing()) {
                alertDelete.setResultConverter(dialogButton -> {
                    if (dialogButton == buttonYes && !suppressInProgress) {
                        suppressInProgress = true; // Indique que la suppression est en cours
                        // On supprime le fichier de sauvegarde
                        System.out.println("Suppression du fichier de sauvegarde");
                        String path = "src/main/resources/save/scores.ser";
                        if (alertDelete.isShowing()) {
                            File file = new File(path);
                            try {
                                if (file.exists()) {
                                    Files.delete(file.toPath());
                                    alertDelete.close();
                                    alertConfirm.showAndWait();
                                    return buttonYes;
                                } else {
                                    throw new Exception("Le fichier de sauvegarde n'existe pas");
                                }
                            } catch (Exception e) {
                                System.out.println("Erreur lors de la suppression du fichier de sauvegarde");
                                e.printStackTrace();
                                // On affiche une fenêtre d'erreur pour dire que le fichier n'a pas pu être
                                // supprimé
                                alertError.showAndWait();
                                return buttonYes;
                            }
                        }
                    }
                    return buttonNo;
                });
                alertDelete.showAndWait();
            }
        }
        if (event.getCode().equals(KeyCode.ENTER)) {
            App.app_state.changeState(EnterNameState.getInstance());
            System.out.println("Vous avez appuyé sur ENTER");
        }
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            quitGame.setResultConverter(dialogButton -> {
                if (dialogButton == buttonYes) {
                    System.exit(0);
                }
                return buttonNo;
            });
            quitGame.showAndWait();
        }
        if (alertError.getResult() == buttonOk) {
            alertError.close();
            suppressInProgress = false; // Indique que la suppression est terminée
        }
        if (alertConfirm.getResult() == buttonOk) {
            alertConfirm.close();
            suppressInProgress = false; // Indique que la suppression est terminée
        }
    }

    public void keyReleasedHandler(KeyEvent event) {
        // Rien
    }
}
