package com.my;

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

public class ConfirmBox {

    static Button buttonYes, buttonNo;
    static boolean answer = false;

    public static boolean confirmChoice(String text) {
        // set up window
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Confirm it");
        
        // set up label
        Label messageLabel = new Label(text);
        
        // setup button
        buttonYes = new Button("Yes");
        buttonNo = new Button("No");
        buttonYes.setOnAction(e -> {answer = true; window.close();});
        buttonNo.setOnAction(e -> {answer = false; window.close();});
        
        // setup layout
        VBox layout = new VBox();
        
        // add comp. to layout
        layout.getChildren().addAll(messageLabel, buttonYes, buttonNo);
        
        // set layout alignment
        layout.setAlignment(Pos.CENTER);
        
        // setup scene
        Scene scene = new Scene(layout, 300,80);
        
        // init. the window
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
