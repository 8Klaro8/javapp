package com.my;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    static Button closeButton;
    private static Label messageLabel;
    private static Stage window;
    private static VBox layout;
    private static Scene scene;

    public static void display(String title, String message, String buttonText) {
        // set up window
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        // set up label
        messageLabel = new Label(message);

        // setup button
        closeButton = new Button(buttonText);
        closeButton.setOnAction(e -> window.close());

        // setup layout
        layout = new VBox(20);

        // add comp. to layout
        layout.getChildren().addAll(messageLabel, closeButton);

        // set layout alignment
        layout.setAlignment(Pos.CENTER);

        // setup scene
        scene = new Scene(layout, 250,130);

        // set style
        messageLabel.getStyleClass().add("messagleLabel");
        closeButton.getStyleClass().add("closeButton");
        scene.getStylesheets().add(addStyleSheet());
        // init. the window
        window.setScene(scene);
        window.showAndWait();
    }

    private static String addStyleSheet() {
        File cssFile = new File(
                "C:/Users/gergr/OneDrive/Dokumentumok/Java/FitnessApp_Javafx_Maven/javapp/src/main/java/com/my/css/AlertBoxStyle.css");
        return "file:///" + cssFile.getAbsolutePath()
                .replace("\\", "/").replace(" ", "%20");
    }
}
