package com.my;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginContext;
import java.awt.image.BufferedImage;

/**
 * JavaFX App
 */
public class HomeScene2 {

    private static Button changeProfPicButton, myWorkoutsButton, settingsButton, logoutButton;
    private static Stage window;
    private static Scene homeScene, loginScene;
    private static Label usernameLabel, passwordLabel, imageLabel, greetingsLabel;
    private static TextField usernameTextField, passworTextField;
    private static Pane emptyPane, emptyPane2, emptyPane3, emptyPane4, buttonGapPane;
    private static BorderPane borderLayout;
    private static GridPane layout;
    private static VBox buttonBox;
    private static Image image1;
    private static ImageView view;
    private static Image image;
    private static HBox menuBox;
    private static ConnectToDB db = new ConnectToDB();
    private static Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));

    public Scene getHomeScene(Stage window, Scene logiScene) throws IOException {

        // make stage and scene equal to this
        this.window = window;
        this.loginScene = logiScene;

        // set logo


        // Set the resized image as the window's icon
        window.getIcons().add(AppLogoClass.getLogo());

        // get current user
        String currentUserString = CurrentUser.get_current_user();

        // init. Label
        usernameLabel = new Label("Username");
        passwordLabel = new Label("Password");
        greetingsLabel = new Label("Hello, " + currentUserString + "!");

        // set label size
        usernameLabel.setFont(Font.font("verdena", FontWeight.MEDIUM, 20));
        passwordLabel.setFont(Font.font("verdena", FontWeight.MEDIUM, 20));

        // init. Button
        changeProfPicButton = new Button("Change Profile");
        myWorkoutsButton = new Button("My Workouts");
        settingsButton = new Button("Settings");
        logoutButton = new Button("Logout");

        // init. pic
        String baseProfPic = db.get_prof_pic_path(conn, "my_users", CurrentUser.get_current_user());
        imageLabel = SetProfileImage.setBasicProfPic(baseProfPic, 150);

        // init. 'textArea'
        usernameTextField = new TextField();
        passworTextField = new TextField();
        usernameTextField.setPrefSize(10, 40);
        passworTextField.setPrefSize(10, 40);

        // set textfield size
        usernameTextField.setMinSize(200, 10);

        // add action
        addAction();

        // set up layouts
        borderLayout = new BorderPane();
        emptyPane = new Pane();
        emptyPane2 = new Pane();
        emptyPane3 = new Pane();
        emptyPane4 = new Pane();
        buttonGapPane = new Pane();

        // set emptyPane's size
        emptyPane.setPrefSize(60, 0);
        emptyPane2.setPrefSize(60, 0);
        emptyPane3.setPrefSize(0, 80);
        emptyPane4.setPrefSize(0, 30);
        buttonGapPane.setPrefSize(0, 10);

        // init. (center) layout
        layout = new GridPane();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setVgap(10);
        layout.setHgap(10);
        layout.setPadding(new Insets(10, 10, 10, 10));

        // init. VBox
        buttonBox = new VBox(10);

        // init. HBox
        menuBox = new HBox(10);

        // set VBox alignment
        buttonBox.setAlignment(Pos.CENTER);

        // set VBox spacing
        buttonBox.setPadding(new Insets(30, 0, 0, 0));

        // set HBox alignment
        menuBox.setAlignment(Pos.BASELINE_CENTER);

        // set HBox spacing
        menuBox.setSpacing(200);
        menuBox.setPadding(new Insets(20, 0, 0, 0));

        // add to VBox
        buttonBox.getChildren().addAll(greetingsLabel, imageLabel, buttonGapPane, changeProfPicButton,
                myWorkoutsButton);

        // add to HBox
        menuBox.getChildren().addAll(settingsButton, logoutButton);

        // add items to stylesheet
        usernameLabel.getStyleClass().add("username-label");
        passwordLabel.getStyleClass().add("password-label");

        usernameTextField.getStyleClass().add("username-textfield");
        passworTextField.getStyleClass().add("password-textfield");

        emptyPane.getStyleClass().add("empty1");
        emptyPane2.getStyleClass().add("empty2");
        emptyPane3.getStyleClass().add("empty3");
        emptyPane4.getStyleClass().add("empty4");

        layout.getStyleClass().add("layout");

        changeProfPicButton.getStyleClass().add("changeProfPicButton");
        myWorkoutsButton.getStyleClass().add("myWorkoutsButton");

        imageLabel.getStyleClass().add("imageLabel");

        logoutButton.getStyleClass().add("logoutButton");
        settingsButton.getStyleClass().add("settingsButton");

        greetingsLabel.getStyleClass().add("greetingsLabel");

        // add to layout
        layout.add(buttonBox, 0, 0);

        // add layouts to border layout
        borderLayout.setLeft(emptyPane);
        borderLayout.setRight(emptyPane2);
        borderLayout.setTop(menuBox);
        borderLayout.setBottom(emptyPane4);
        borderLayout.setCenter(layout);

        // set up scenes
        homeScene = new Scene(borderLayout, 370, 500);

        // add CSS
        homeScene.getStylesheets().add(getImageLink());

        // set window title
        return homeScene;
    }

    private void addAction() {
        changeProfPicButton.setOnAction(e -> {
            try {
                openChangeAvatarScreen();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        imageLabel.setOnMouseClicked(e -> {
            // change screen to be able to choose avatar
            try {
                openChangeAvatarScreen();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        logoutButton.setOnAction(e -> {
            try {
                openLoginScreen();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        settingsButton.setOnAction(e -> {
            // open settings screen
            try {
                openSettingsScreen();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        myWorkoutsButton.setOnAction(e -> {
            try {
                openWorkoutScreen();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private static String getImageLink() {
        File cssFile = new File(
                "C:/Users/gergr/OneDrive/Dokumentumok/Java/FitnessApp_Javafx_Maven/javapp/src/main/java/com/my/css/HomeSceneStyle.css");
        return "file:///" + cssFile.getAbsolutePath()
                .replace("\\", "/").replace(" ", "%20");
    }

    private static void closeProgram() throws FileNotFoundException {
        if (ConfirmBox.confirmChoice("Are you sure you want to close the program?")) {
            closeTheProgram();
        }
    }

    private static void closeTheProgram() {
        System.out.println("File saved!");
        window.close();
    }

    private void openLoginScreen() throws IOException {
        // logout
        window.setScene(loginScene);
        // set current user to none, when user logs out
        CurrentUser.set_current_user("");
    }

    private void openChangeAvatarScreen() throws FileNotFoundException {
        ChangeAvatarScene changeAvatarScene = new ChangeAvatarScene();
        window.setScene(changeAvatarScene.getChangeAvatarScene(window, loginScene));
    }

    private void openSettingsScreen() throws IOException {
        SettingsScene settingsScene = new SettingsScene();
        window.setScene(settingsScene.getSettingsScene(window, loginScene));
    }

    private void openWorkoutScreen() throws IOException {
        WokroutsScene workoutScene = new WokroutsScene();
        window.setScene(workoutScene.getWorkoutScene(window));
    }

}