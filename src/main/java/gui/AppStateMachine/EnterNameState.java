package gui.AppStateMachine;

import javafx.scene.text.Font;

import gui.App;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Text;
import gui.Controller.EnterNameController;
import lib.State;
import lib.ElementScaler;
import lib.FontLoader;

public class EnterNameState implements State {
    BorderPane start_button = new BorderPane();
    Text buttonText1 = new Text();
    Text buttonText2 = new Text();

    Image img = new Image(getClass().getResourceAsStream("/start.gif"));
    ImageView view = new ImageView(img);

    Image beforeImage =new Image(getClass().getResourceAsStream("/start_gris_png.png"));
    ImageView beforeImageView = new ImageView(beforeImage);

    public static boolean canPlay = false;

    private String state_name = "EnterName State";

    private static final EnterNameState instance = new EnterNameState();

    private BorderPane enter_name_menu = new BorderPane();

    private Font pixel_font = FontLoader.getPixelFont(15);
    private Font pixel_mid_font = FontLoader.getPixelFont(12);
    private Font pixel_little_font = FontLoader.getPixelFont(10);

    private String userName = "";

    private EnterNameState() {
        // Constructeur privé pour empêcher la création d'autres instances
    }

    public static EnterNameState getInstance() {
        return instance;
    }

    public String showState() {
        return state_name;
    }

    private Pane createStartButton() {
        start_button.setMaxHeight(App.screen.getHeight() / 4);
        start_button.setMaxWidth(App.screen.getWidth() / 4);

        start_button.setStyle("-fx-background-color: black");


        start_button.setCenter(beforeImageView);

        return start_button;
    }

    public void enter() {
        userName = "";
        
        pixel_font = FontLoader.getPixelFont(ElementScaler.scale(15));

        System.out.println(ElementScaler.scale(12));
        App.screen.setOnKeyPressed(null);
        enter_name_menu.setStyle("-fx-background-color: black;");
        // We want to add at the top of the screen a field to type our name
        Label typeName = new Label("Entrez votre nom :");
        typeName.setFont(pixel_font);
        typeName.setTextFill(javafx.scene.paint.Color.WHITE);
        typeName.setTextAlignment(TextAlignment.CENTER);
        Label name_label = new Label(userName);
        name_label.setFont(pixel_font);
        name_label.setTextFill(javafx.scene.paint.Color.WHITE);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        TextField name_field = new TextField();
        name_field.setStyle("-fx-text-alignment: center; -fx-text-fill: white;");
        name_field.setAlignment(Pos.CENTER);
        Label error_label = new Label("Entree un nom avec au moins 3 caracteres");
        error_label.setFont(pixel_little_font);
        error_label.setTextFill(javafx.scene.paint.Color.RED);
        error_label.setTextAlignment(TextAlignment.CENTER);
        vbox.getChildren().addAll(typeName ,name_label, error_label);
        name_field.textProperty().addListener((observable, oldValue, newValue) -> {
            // We want to limit the size of the name to 8 characters max and to not allow
            // special characters and if the character is uppercase we want to put it in
            // lowercase
            if (newValue.length() > 8) {
                name_field.setText(oldValue);
                newValue = oldValue;
                // We want to display a message to the user to tell him that he can't use
                // special characters
                name_label.setText(userName);

                start_button.setCenter(beforeImageView);
                canPlay = false;
                buttonText1.setText("");
                buttonText2.setText("");
            } 
            
            if (newValue.length() < 3){

                start_button.setCenter(beforeImageView);
                canPlay = false;
                buttonText1.setText("");
                buttonText2.setText("");
            }

            if(newValue.matches(".*[^a-zA-Z].*")){
                name_field.setText(oldValue);
                error_label.setText("Caracteres non valide");
                name_label.setText(userName);
            }
            
            
            else {
                error_label.setText("");
                if (newValue.matches(
                        ".*[A-Z].*")) {
                    newValue = newValue.toLowerCase();
                }
                System.out.println(newValue);
                userName = newValue;

                if(userName.length() >= 3){
                    canPlay = true;
                    buttonText1.setText("Appuyez sur Entree");
                    buttonText2.setText("pour lancer le jeu");
                    start_button.setCenter(view);
                }

                if(userName.length() < 3){
                    error_label.setText("Entrer un nom avec au moins 3 caracteres");
                }

                name_field.setText(userName);
                name_label.setText(userName);
            }
        });

        buttonText1.setFill(javafx.scene.paint.Color.WHITE);
        buttonText1.setFont(pixel_font);

        buttonText2.setFill(javafx.scene.paint.Color.WHITE);
        buttonText2.setFont(pixel_font);

        VBox buttonVBox = new VBox(createStartButton(), buttonText1, buttonText2);
        buttonVBox.setAlignment(Pos.CENTER);

        HBox mainContainer = new HBox(vbox, buttonVBox);
        mainContainer.setPadding(new javafx.geometry.Insets(10));
        mainContainer.setAlignment(Pos.CENTER);

        enter_name_menu.getChildren().add(name_field);

        Text cancel_text = new Text("ECHAP pour annuler");
        cancel_text.setFont(pixel_font);
        cancel_text.setFill(javafx.scene.paint.Color.WHITE);
        cancel_text.setTextAlignment(TextAlignment.LEFT);
        enter_name_menu.setBottom(cancel_text);

        enter_name_menu.setCenter(mainContainer);

        EnterNameController controller = new EnterNameController();
        App.screen.setOnKeyPressed(controller::keyPressedHandler);
        App.screen.setRoot(enter_name_menu);
    }

    public void exit() {
        enter_name_menu.getChildren().clear();
        App.screen.setOnKeyPressed(null);
        HomeScreenState.getInstance().setUserName(userName);
    }

    public void transitionTo(State s) {
        if (s instanceof PlayingState) {
            HomeScreenState.getInstance().getMediaPlayer().stop();
            PlayingState.getInstance().initializeMaze();
        }
    }
}
