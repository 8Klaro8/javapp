package com.my;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    static Button buttonYes, buttonNo;
    static boolean answer = false;
    private static BorderPane borderPane;
    private static Pane emptyPane1, emptyPane2;
    private static HBox layout, layout2;
    private static Scene scene;
    private static VBox centerPanel;
    private static Stage window;
    private static Label label;
    private static final String MESSAGE = "Are you sure you want to close?";

    public static boolean confirmChoice(String text) {
        return mainFunc();
    }

    private static boolean mainFunc() {
        // set up window
        window = new Stage();

        // init. Comp
        initComp();

        // set comp.
        setComp();

        // add to comp.
        addComp();

        // add action
        addAction();
        
        // setup scene
        scene = new Scene(borderPane, 300,100);

        // add style
        setStyle();
        
        // init. the window
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }

    private static void addAction() {
        buttonNo.setOnAction(e -> {
            window.close();
        });

        buttonYes.setOnAction(e -> {
            answer = true;
            window.close();
        });
    }

    private static void addComp() {
        // items to layout
        layout.getChildren().add(label);
        layout2.getChildren().addAll(buttonYes, emptyPane2, buttonNo);

        // add layouts to centerPanel
        centerPanel.getChildren().addAll(layout,emptyPane1, layout2);

        // add centerpanel to borderlayout
        borderPane.setCenter(centerPanel);
    }

    private static void setComp() {
        buttonNo.setMinWidth(40);
        buttonYes.setMinWidth(40);

        emptyPane1.setPrefSize(0, 10);
        emptyPane2.setPrefSize(10, 0);

        layout.setAlignment(Pos.CENTER);
        layout2.setAlignment(Pos.CENTER);
        centerPanel.setAlignment(Pos.CENTER);
    }

    private static void initComp() {
        label = new Label(MESSAGE);

        buttonNo = new Button("No");
        buttonYes = new Button("Yes");

        borderPane = new BorderPane();

        emptyPane1 = new Pane();
        emptyPane2 = new Pane();

        layout = new HBox();
        layout2 = new HBox();

        centerPanel = new VBox();
    }

    private static void setStyle() {
        scene.getStylesheets().add(addStyleSheet());

        buttonNo.getStyleClass().add("buttonNo");
        buttonYes.getStyleClass().add("buttonYes");
        label.getStyleClass().add("label");
    }

    private static String addStyleSheet() {
        File cssFile = new File(
                "C:/Users/gergr/OneDrive/Dokumentumok/Java/FitnessApp_Javafx_Maven/javapp/src/main/java/com/my/css/ConfirmBoxStyle.css");
        return "file:///" + cssFile.getAbsolutePath()
                .replace("\\", "/").replace(" ", "%20");
    }
}
