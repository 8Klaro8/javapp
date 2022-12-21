package com.my;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ChangeDetails {

    private static Scene changeDetailsScene;
    private static BorderPane borderLayout;
    private static Label usernameLabel, fNameLabel, lNameLabel, emailLabel;
    private static TextField usernameField, fNameField, lNameField, emailField;
    private static Button saveButton;
    private static HBox horizontalBar;
    private static VBox mainPane;
    private static Stage window;
    private static Scene loginScene;
    private static ArrayList<String> chosenUser;
    private static ConnectToDB db;
    private static Connection conn;
    private static Pane emptyPane, emptyPane2, emptyPane3, emptyPane4, emptyGapPane;
    private static String newUsername, newFName, newLName, newEmail;
    private static String user_id;

    public Scene getChangeDetailsScene(Stage window, Scene loginScene) throws IOException {

        // init DB and connection
        db = new ConnectToDB();
        conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));

        // assign parameters
        this.window = window;
        this.loginScene = loginScene;

        // init mainPane
        mainPane = new VBox(20);

        // init labels
        usernameLabel = new Label("Username");
        fNameLabel = new Label("First name");
        lNameLabel = new Label("Last name");
        emailLabel = new Label("email");

        // set labels font
        usernameLabel.setFont(Font.font("verdena", FontWeight.MEDIUM, 15));
        fNameLabel.setFont(Font.font("verdena", FontWeight.MEDIUM, 15));
        lNameLabel.setFont(Font.font("verdena", FontWeight.MEDIUM, 15));
        emailLabel.setFont(Font.font("verdena", FontWeight.MEDIUM, 15));

        // init textfields
        usernameField = new TextField();
        fNameField = new TextField();
        lNameField = new TextField();
        emailField = new TextField();

        // init. empty panes
        emptyPane = new Pane();
        emptyPane2 = new Pane();
        emptyPane3 = new Pane();
        emptyPane4 = new Pane();
        emptyGapPane = new Pane();

        // init. button
        saveButton = new Button("Save");

        // add action
        saveButton.setOnAction(e -> {
            newUsername = usernameField.getText();
            newFName = fNameField.getText();
            newLName = lNameField.getText();
            newEmail = emailField.getText();
            // validating email input
            if (!(newEmail.contains("@"))) {
                AlertBox.display("Invalid email", "Please use a valid email.", "Close");
                return;
            }
            if (!(newEmail.endsWith(".com") || newEmail.endsWith(".hu") || newEmail.endsWith(".org"))) {
                AlertBox.display("Invalid email", "Missing:\ne.g.:\t.com/.hu\nor invalid input.", "Close");
                return;
            }
            if (newEmail.startsWith("@") || newEmail.startsWith(".")) {
                AlertBox.display("Invalid email", "Please use a valid email.", "Close");
                return;
            }

            boolean usernameExists = db.username_exists(conn, newUsername);

            // check if username exists and check if that username does not belong to the current user
            try {
                if (usernameExists && !(newUsername.equals(CurrentUser.get_current_user()))) {
                    AlertBox.display("Exitst", "The username: " + newUsername  + " exists.", "Close");
                    return;
                }
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            if (!(newUsername.isEmpty())) {
                try {
                    db.insert_username(conn, newUsername, CurrentUser.get_current_user());
                    CurrentUser.set_current_user(newUsername);
                } catch (Exception err) {
                    System.out.println(err.getMessage());
                }
            }
            if (!(newFName.isEmpty())) {
                try {
                    db.insert_firstName(conn, newFName, CurrentUser.get_current_user());
                } catch (Exception err) {
                    System.out.println(err.getMessage());
                }
            }
            if (!(newLName.isEmpty())) {
                try {
                    db.insert_lastName(conn, newLName, CurrentUser.get_current_user());
                } catch (Exception err) {
                    System.out.println(err.getMessage());
                }
            } 
            if (!(newEmail.isEmpty())) {
                try {
                    db.insert_email(conn, newEmail, CurrentUser.get_current_user());
                } catch (Exception err) {
                    System.out.println(err.getMessage());
                }
            }
            try {
                openSettingsScene();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        // set promt text to textfields
        chosenUser = db.get_by_name(conn, CurrentUser.get_current_user());
        System.out.println(chosenUser.toString());
        usernameField.setText(chosenUser.get(2));
        fNameField.setText(chosenUser.get(4));
        lNameField.setText(chosenUser.get(5));
        emailField.setText(chosenUser.get(chosenUser.size() - 1));

        // add itmes to gridPane
        mainPane.getChildren().addAll(usernameLabel, usernameField, fNameLabel, fNameField, lNameLabel, lNameField, emailLabel, emailField, emptyGapPane, saveButton);

        // set mainPane
        mainPane.setAlignment(Pos.TOP_CENTER);

        // create borderlayout
        borderLayout = new BorderPane();

        // add items to borderlayout
        borderLayout.setCenter(mainPane);
        borderLayout.setRight(emptyPane);
        borderLayout.setLeft(emptyPane2);
        borderLayout.setBottom(emptyPane3);
        borderLayout.setTop(emptyPane4);

        // set 'emptyPanes' size
        emptyPane.setPrefSize(60, 0);
        emptyPane2.setPrefSize(60, 0);
        emptyPane3.setPrefSize(0, 80);
        emptyPane4.setPrefSize(0, 40);
        emptyGapPane.setPrefSize(0, 40);

        // create scene
        changeDetailsScene = new Scene(borderLayout, 370, 500);

        // add style
        changeDetailsScene.getStylesheets().add(addStyleSheet());
        usernameLabel.getStyleClass().add("usernameLabel");
        fNameLabel.getStyleClass().add("fNameLabel");
        lNameLabel.getStyleClass().add("lNameLabel");
        emailLabel.getStyleClass().add("emailLabel");
        saveButton.getStyleClass().add("saveButton");

        return changeDetailsScene;
    }

    private static String addStyleSheet() {
        File cssFile = new File(
                "C:/Users/gergr/OneDrive/Dokumentumok/Java/FitnessApp_Javafx_Maven/javapp/src/main/java/com/my/css/ChangeDetailsSceneStyle.css");
        return "file:///" + cssFile.getAbsolutePath()
                .replace("\\", "/").replace(" ", "%20");
    }

    // private void openHomeScreen() throws IOException {
    //     HomeScene2 homeScene = new HomeScene2();
    //     window.setScene(homeScene.getHomeScene(window, loginScene));
    // }

    private void openSettingsScene() throws IOException {
        SettingsScene settingsScene = new SettingsScene();
        window.setScene(settingsScene.getSettingsScene(window, loginScene));
    }
}
