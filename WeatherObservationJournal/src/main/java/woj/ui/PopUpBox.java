package woj.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class to create informational popup window for the user interface.
 */
public class PopUpBox {
    
    /**
     * Create popup box with title, message and confirmation button.
     * @param title title of the popup window
     * @param message message shown on the popup window
     */
    static void show(String title, String message) {
        Stage window = new Stage();
        window.setOpacity(0.9);
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        
        Label label = new Label(message);
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> window.close());
        
        Color color = Color.LIGHTBLUE;
        
        BackgroundFill buttonFill = new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY);
        BackgroundFill buttonPressedFill = new BackgroundFill(color.darker(), new CornerRadii(10), Insets.EMPTY);
        BackgroundFill mouseOverFill = new BackgroundFill(color.brighter(), new CornerRadii(10), Insets.EMPTY);
        
        okButton.setBackground(new Background(buttonFill));
        okButton.setOnMousePressed(e -> okButton.setBackground(new Background(buttonPressedFill)));
        okButton.setOnMouseReleased(e -> okButton.setBackground(new Background(buttonFill)));
        okButton.setOnMouseEntered(e -> okButton.setBackground(new Background(mouseOverFill)));
        okButton.setOnMouseExited(e -> okButton.setBackground(new Background(buttonFill)));
        
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20,20,20,20));
        layout.getChildren().addAll(label, okButton);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }  
}
