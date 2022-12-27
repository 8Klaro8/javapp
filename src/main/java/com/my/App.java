package com.my;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Button loginButton, registerButton;
    private static Stage window;
    private static Scene loginScene;
    private static Label usernameLabel, passwordLabel;
    private static PasswordField passworTextField;
    private static TextField usernameTextField;
    private static Pane emptyPane, emptyPane2, emptyPane3, emptyPane4, buttonGapPane;
    private static BorderPane borderLayout;
    private static GridPane layout;
    private static VBox buttonBox;
    private static final String TABLE_NAME = "my_users";
    Scene homeScene;
    // estabilish connection to DB
    ConnectToDB db = new ConnectToDB();
    Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        window.setOnCloseRequest(e -> {
            closeProgram();
            e.consume();
        });

        // init comp
        initComp();

        // set textfield size
        usernameTextField.setMinSize(200, 10);

        // add action
        addAction();

        // set up layouts
        setUpLayouts();

        // set emptyPane's size
        setPanesSize();

        // set VBox alignment
        buttonBox.setAlignment(Pos.CENTER);

        // add to VBox
        buttonBox.getChildren().addAll(usernameLabel, usernameTextField, passwordLabel, passworTextField, buttonGapPane,
                loginButton, registerButton);

        // add items to stylesheet
        addStyle();

        // add to layout
        layout.add(buttonBox, 0, 0);

        // add layouts to border layout
        addToBorderLayout();

        // set up scenes
        loginScene = new Scene(borderLayout, 370, 500);

        // add CSS
        loginScene.getStylesheets().add(addStyleSheet());

        // set window title
        window.setTitle("KlaroFit");

        // set scene and show
        // Scene homeScene = HomeScreen.getHomeScreen();
        // homeScene2.getStylesheets().add(addStyleSheet());

        // temp.
        // ChangeAvatarScene changeAvatarScene = new ChangeAvatarScene();
        // SettingsScene settingsScene = new SettingsScene();
        WokroutsScene wkScene = new WokroutsScene();

        // Set the resized image as the window's icon
        window.getIcons().add(AppLogoClass.getLogo());

        // window.setScene(wkScene.getWorkoutScene(stage));
        window.setScene(loginScene);
        window.show();

    }

    private void setPanesSize() {
        emptyPane.setPrefSize(60, 0);
        emptyPane2.setPrefSize(60, 0);
        emptyPane3.setPrefSize(0, 80);
        emptyPane4.setPrefSize(0, 30);
        buttonGapPane.setPrefSize(0, 20);
    }

    private void setUpLayouts() {
        borderLayout = new BorderPane();
        emptyPane = new Pane();
        emptyPane2 = new Pane();
        emptyPane3 = new Pane();
        emptyPane4 = new Pane();
        buttonGapPane = new Pane();
    }

    private void addToBorderLayout() {
        borderLayout.setLeft(emptyPane);
        borderLayout.setRight(emptyPane2);
        borderLayout.setTop(emptyPane3);
        borderLayout.setBottom(emptyPane4);
        borderLayout.setCenter(layout);
    }

    private void addStyle() {
        usernameLabel.getStyleClass().add("username-label");
        passwordLabel.getStyleClass().add("password-label");

        usernameTextField.getStyleClass().add("username-textfield");
        passworTextField.getStyleClass().add("password-textfield");

        emptyPane.getStyleClass().add("empty1");
        emptyPane2.getStyleClass().add("empty2");
        emptyPane3.getStyleClass().add("empty3");
        emptyPane4.getStyleClass().add("empty4");

        layout.getStyleClass().add("layout");

        loginButton.getStyleClass().add("login-button");
        registerButton.getStyleClass().add("register-button");
    }

    private void addAction() {
        loginButton.setOnAction(e -> {
            // declare username and password
            String username = usernameTextField.getText();
            String givenPWD = passworTextField.getText();
            // declare password hash
            HashPassword hashPWD = new HashPassword();
            // check if fields are empty
            if (username.isEmpty() || givenPWD.isEmpty()) {
                AlertBox.display("Error", "Missing username or password", "Close");
            }
            String storedPassword = db.get_hash_by_username(conn, TABLE_NAME, username);
            try {
                if (hashPWD.validatePassword(givenPWD, storedPassword)) {
                    usernameTextField.setText("");
                    passworTextField.setText("");
                    // write current user to file
                    CurrentUser.set_current_user(username);
                    // change to home page/ aka LOGIN
                    openHomeScreen();
                } else {
                    AlertBox.display("Error", "Wrong password", "Close");
                    usernameTextField.setText("");
                    passworTextField.setText("");
                }
            } catch (Exception err) {
                // check if user is NOT exists and give respond to it
                if (username.isEmpty() || givenPWD.isEmpty()) {
                    AlertBox.display("Empty", "Empty fields", "Close");
                    return;
                }
                try {
                    boolean userFound = db.username_exists(conn, username);
                    if (!(userFound)) {
                        AlertBox.display("User not found", "The user: " + username + " is not registered yet.",
                                "Close");
                        usernameTextField.setText("");
                        passworTextField.setText("");
                    }
                } catch (Exception err_2) {
                    System.out.println(err_2.getMessage());
                }
                System.out.println(err.getMessage());
            }
        });
        registerButton.setOnAction(e -> {
            openRegisterScene();
        });
    }

    private void initComp() {
        // init. Label
        usernameLabel = new Label("Username");
        passwordLabel = new Label("Password");

        // set label size
        usernameLabel.setFont(Font.font("verdena", FontWeight.MEDIUM, 20));
        passwordLabel.setFont(Font.font("verdena", FontWeight.MEDIUM, 20));

        // init. Button
        loginButton = new Button("Login");
        registerButton = new Button("Register");

        // init. 'textArea'
        usernameTextField = new TextField();
        passworTextField = new PasswordField();
        usernameTextField.setPrefSize(10, 40);
        passworTextField.setPrefSize(10, 40);

        // init. (center) layout
        layout = new GridPane();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setVgap(10);
        layout.setHgap(10);
        layout.setPadding(new Insets(10, 10, 10, 10));

        // init. VBox
        buttonBox = new VBox(10);
    }

    private static String addStyleSheet() {
        File cssFile = new File(
                "C:/Users/gergr/OneDrive/Dokumentumok/Java/FitnessApp_Javafx_Maven/javapp/src/main/java/com/my/css/Viper.css");
        return "file:///" + cssFile.getAbsolutePath()
                .replace("\\", "/").replace(" ", "%20");
    }

    private static void closeProgram() {
        if (ConfirmBox.confirmChoice("Are you sure you want to close the program?")) {
            closeTheProgram();
        }
    }

    private static void closeTheProgram() {
        System.out.println("File saved!");
        window.close();
    }

    public static void main(String[] args) {
        launch();
    }

    public void openHomeScreen() throws IOException {
        HomeScene2 homeS = new HomeScene2();
        window.setScene(homeS.getHomeScene(window, loginScene));
    }

    public void openRegisterScene() {
        RegisterScene regScene = new RegisterScene();
        window.setScene(regScene.getRegisterScene(window, loginScene));
    }
}