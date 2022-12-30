package com.my;

import java.io.File;
import java.io.IOException;
import java.lang.Thread.State;
import java.sql.Connection;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegisterScene {
    private static Label usernameLabel, passwordLabel, repPasswordLabel, checkBoxLabel, heightLabel, weightLabel;
    private static TextField usernameField, heigthField, weightField;
    private static Pane emptyTop, emptyBottom, emptyLeft, emptyRight;
    private static HBox usernameHBox, passwordHBox, repPasswordHBox, showPasswordHBox, heightHBox, weightHBox,
            buttonsHBox;
    private static Scene registerScene, loginScene, homeScene;
    private static BorderPane borderLayout;
    private static GridPane gridLayout, bottomGridLayout, topGridLayout;
    private static CheckBox checkbox;
    private static Button registerButton, backToLoginButton;
    private static VBox mainPanel;
    private static PasswordField passwordField, repPasswordField;
    String tempPassword;
    Stage window;
    private static String username;
    private static HashPassword hashPWD = new HashPassword();
    private static final String BASE_PROF_PIC = "javapp/src/main/resources/com/my/assets/prof_pics/prif_pic_2.png";

    public Scene getRegisterScene(Stage window, Scene loginScene) {

        // make stage and scene equal to
        this.window = window;
        this.loginScene = loginScene;

        // init. checkbox
        checkbox = new CheckBox();

        // set checkbox checked

        // init VBox
        mainPanel = new VBox(10);

        // init. buttons
        registerButton = new Button("Register");
        backToLoginButton = new Button("Back");

        // add action to button
        backToLoginButton.setOnAction(e -> {
            try {
                openLoginScreen();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        registerButton.setOnAction(e -> {
            String password = passwordField.getText();
            String repPassword = repPasswordField.getText();

            // save weight and height

            if (!(password.equals(repPassword))) {
                AlertBox.display("Password error", "Password doesnt match", "Close");
                passwordField.setText("");
                repPasswordField.setText("");
            } else {
                username = usernameField.getText();
                if (username.isEmpty() || password.isEmpty() || repPassword.isEmpty()) {
                    AlertBox.display("Empty", "Fields cannot be empty", "Close");
                    return;
                }
                if (heigthField.getText().isEmpty() || weightField.getText().isEmpty()) {
                    AlertBox.display("Missing", "Please give height and weight too.", "Close");
                    return;
                }
                if (Integer.valueOf(weightField.getText()) < 40 || Integer.valueOf(weightField.getText()) > 160) {
                    AlertBox.display("Wrong input", "Weight must be between 40kg and 160kg", "Close");
                    return;
                }
                if (Integer.valueOf(heigthField.getText()) < 100) {
                    AlertBox.display("Wrong input", "Height can't be lower than 100cm.", "Close");
                    return;
                }

                // Hash password
                try {
                    // Basic profile image
                    String hashedPW = hashPWD.generateStorngPasswordHash(password);
                    // Store user
                    ConnectToDB db = new ConnectToDB();
                    Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
                    // control if username exists in DB
                    boolean usernameExists = db.username_exists(conn, username);
                    if (!(usernameExists)) {
                        db.add_user(conn, username, hashedPW, ", ", ", ", BASE_PROF_PIC, heigthField.getText(), weightField.getText(), ", ", ", ", ", ", ", ");

                        // Login freshly registered user
                        CurrentUser.set_current_user(username);
                        // Add start value to new user
                        db.insert_basic_values(conn, CurrentUser.get_current_user());
                        openHomeScreen();
                    } else {
                        AlertBox.display("User exists", "The user: " + username + " already exists.", "Close");
                        return;
                    }

                } catch (Exception err) {
                    System.out.println(err.getMessage());
                }
            }
        });

        // init. labels
        usernameLabel = new Label("Username:");
        passwordLabel = new Label("Password:");
        repPasswordLabel = new Label("re-password:");
        checkBoxLabel = new Label("Show/ hide password");
        heightLabel = new Label("Height:");
        weightLabel = new Label("Weight:");

        // init. fields
        usernameField = new TextField();
        heigthField = new TextField();
        weightField = new TextField();

        // inti. password fields
        passwordField = new PasswordField();
        repPasswordField = new PasswordField();

        // init. panes
        emptyTop = new Pane();
        emptyBottom = new Pane();
        emptyLeft = new Pane();
        emptyRight = new Pane();

        // style textFields
        heigthField.setMaxSize(40, 10);
        weightField.setMaxSize(40, 10);

        // setup layouts
        borderLayout = new BorderPane();
        gridLayout = new GridPane();
        bottomGridLayout = new GridPane();
        topGridLayout = new GridPane();

        // adjust 'gridLayout'
        gridLayout.setAlignment(Pos.TOP_CENTER);
        gridLayout.setVgap(30);
        gridLayout.setHgap(30);
        gridLayout.setPadding(new Insets(10, 10, 10, 10));

        // adjust 'mainPanel'
        mainPanel.setAlignment(Pos.TOP_CENTER);

        // add items to gridLayout
        gridLayout.add(usernameLabel, 0, 0);
        gridLayout.add(usernameField, 1, 0);
        gridLayout.add(passwordLabel, 0, 1);
        gridLayout.add(passwordField, 1, 1);
        gridLayout.add(repPasswordLabel, 0, 2);
        gridLayout.add(repPasswordField, 1, 2);
        gridLayout.add(checkbox, 0, 3);
        gridLayout.add(checkBoxLabel, 1, 3);
        gridLayout.add(heightLabel, 0, 4);
        gridLayout.add(heigthField, 1, 4);
        gridLayout.add(weightLabel, 0, 5);
        gridLayout.add(weightField, 1, 5);

        // add item(s) to bottomGridLayout
        bottomGridLayout.setAlignment(Pos.TOP_CENTER);
        bottomGridLayout.setPrefHeight(100);
        bottomGridLayout.setPadding(new Insets(20, 0, 0, 0));
        bottomGridLayout.add(registerButton, 0, 0);

        // add item(s) to topGridLayout
        topGridLayout.setAlignment(Pos.CENTER_RIGHT);
        topGridLayout.setPrefHeight(60);
        topGridLayout.setPadding(new Insets(0, 20, 0, 0));
        topGridLayout.add(backToLoginButton, 0, 0);

        // set emptyPanes sizes
        emptyTop.setPrefSize(0, 60);
        emptyBottom.setPrefSize(0, 60);
        emptyLeft.setPrefSize(30, 0);
        emptyRight.setPrefSize(30, 0);

        // add layouts to border layout
        borderLayout.setLeft(emptyLeft);
        borderLayout.setRight(emptyRight);
        borderLayout.setTop(topGridLayout);
        borderLayout.setBottom(bottomGridLayout);
        borderLayout.setCenter(gridLayout);

        // create register scene
        registerScene = new Scene(borderLayout, 370, 500);

        // add checkbox listener

        // add Style
        registerScene.getStylesheets().add(addStyleSheet());
        emptyTop.getStyleClass().add("emptyTop");
        emptyBottom.getStyleClass().add("emptyBottom");
        emptyLeft.getStyleClass().add("emptyLeft");
        emptyRight.getStyleClass().add("emptyRight");

        usernameLabel.getStyleClass().add("usernameLabel");
        passwordLabel.getStyleClass().add("passwordLabel");
        repPasswordLabel.getStyleClass().add("repPasswordLabel");
        heightLabel.getStyleClass().add("heightLabel");
        weightLabel.getStyleClass().add("weightLabel");
        checkBoxLabel.getStyleClass().add("checkBoxLabel");
        registerButton.getStyleClass().add("registerButton");
        backToLoginButton.getStyleClass().add("backToLoginButton");

        return registerScene;
    }

    private static String addStyleSheet() {
        File cssFile = new File(
                "C:/Users/gergr/OneDrive/Dokumentumok/Java/FitnessApp_Javafx_Maven/javapp/src/main/java/com/my/css/RegisterSceneStyle.css");
        return "file:///" + cssFile.getAbsolutePath()
                .replace("\\", "/").replace(" ", "%20");
    }

    public void openLoginScreen() throws IOException {
        window.setScene(loginScene);
    }

    public void openHomeScreen() throws IOException {
        HomeScene2 homeScene = new HomeScene2();
        window.setScene(homeScene.getHomeScene(window, loginScene));
    }

}
