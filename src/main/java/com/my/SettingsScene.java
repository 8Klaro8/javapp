package com.my;

import java.io.File;
import java.io.IOException;
import java.security.Provider;
import java.sql.Connection;
import java.time.chrono.HijrahChronology;
import java.util.ArrayList;

import javafx.geometry.Insets;
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

public class SettingsScene {

    private static Scene settingsScene, loginScene;
    private static Stage window;
    private static BorderPane borderLayout;
    private static GridPane verticalBar;
    private static Button changeDetailsButton, backButton;
    private static HBox horizontalBar;
    private static Label usernamLabel, fNameLabel, lNameLabel, emailLabel, usernameField, fNameField, lNameField,
            emailField;
    private static Pane emptyPane, emptyPane2, emptyPane3;
    private static ArrayList<String> chosenUser;
    private static ConnectToDB db = new ConnectToDB();
    private static Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
    public Scene getSettingsScene(Stage window, Scene loginScene) throws IOException {

        // init. components
        initComp(window, loginScene);

        // set components
        setComp();

        // add items to 'gridPane'
        addToGridPane();

        // add action
        addAction();

        // style buttons
        changeDetailsButton.setMinSize(100, 30);
        backButton.setMinSize(70, 30);

        // add buttons to horizontal bar
        horizontalBar.getChildren().addAll(changeDetailsButton, backButton);

        // create border layout
        borderLayout = new BorderPane();

        // add bars to borderlayout
        addToBorderLayout();

        // create settings scene
        settingsScene = new Scene(borderLayout, 370, 500);

        // add style
        addStyle();

        return settingsScene;
    }

    private void addToBorderLayout() {
        borderLayout.setTop(horizontalBar);
        borderLayout.setCenter(verticalBar);
        borderLayout.setRight(emptyPane);
        borderLayout.setLeft(emptyPane2);
        borderLayout.setBottom(emptyPane3);
    }

    private void addAction() {
        backButton.setOnAction(e -> {
            try {
                openHomeScreen();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        changeDetailsButton.setOnAction(e -> {
            try {
                openChangeDetailsScene();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void addToGridPane() {
        verticalBar.add(usernamLabel, 0, 0);
        verticalBar.add(fNameLabel, 0, 1);
        verticalBar.add(lNameLabel, 0, 2);
        verticalBar.add(emailLabel, 0, 3);

        verticalBar.add(usernameField, 1, 0);
        verticalBar.add(fNameField, 1, 1);
        verticalBar.add(lNameField, 1, 2);
        verticalBar.add(emailField, 1, 3);
    }

    private void setComp() {
        // style horizontal baar
        horizontalBar.setAlignment(Pos.BASELINE_CENTER);
        horizontalBar.setSpacing(155);
        horizontalBar.setPadding(new Insets(20, 0, 0, 0));

        // set verticals bar padding
        verticalBar.setPadding(new Insets(20, 0, 0, 0));

        // align VBox
        verticalBar.setAlignment(Pos.CENTER_LEFT);
        verticalBar.setVgap(60);
        verticalBar.setHgap(40);

        // set label font
        usernamLabel.setFont(Font.font("verdena", FontWeight.MEDIUM, 12));
        fNameLabel.setFont(Font.font("verdena", FontWeight.MEDIUM, 12));
        lNameLabel.setFont(Font.font("verdena", FontWeight.MEDIUM, 12));
        emailLabel.setFont(Font.font("verdena", FontWeight.MEDIUM, 12));

        // set 'emptyPanes' size
        emptyPane.setPrefSize(60, 0);
        emptyPane2.setPrefSize(60, 0);
        emptyPane3.setPrefSize(0, 80);
    }

    private void initComp(Stage window, Scene loginScene) throws IOException {
        // assign window and scene
        this.window = window;
        this.loginScene = loginScene;

        // init. GridPanes
        verticalBar = new GridPane();

        // init HBox
        horizontalBar = new HBox(50);

        // init. buttons
        changeDetailsButton = new Button("Change details");
        backButton = new Button("Back");

        // init. labels
        usernamLabel = new Label("Username:");
        fNameLabel = new Label("First name:");
        lNameLabel = new Label("Last name:");
        emailLabel = new Label("Email:");

        // init. textfields
        chosenUser = db.get_by_name(conn, CurrentUser.get_current_user());

        // check if field is empy yet and if so then display nothing
        usernameField = new Label(fieldIsEmpty(2));
        fNameField = new Label(fieldIsEmpty(4));
        lNameField = new Label(fieldIsEmpty(5));
        emailField = new Label(fieldIsEmpty(chosenUser.size() - 1));

        // init. empty panes
        emptyPane = new Pane();
        emptyPane2 = new Pane();
        emptyPane3 = new Pane();
    }

    private String fieldIsEmpty(int indexNum) {
        String fieldValue = chosenUser.get(indexNum).strip();
        if(fieldValue.equals(",")){
            return "";
        }
        return chosenUser.get(indexNum);
    }

    private void addStyle() {
        settingsScene.getStylesheets().add(addStyleSheet());
        changeDetailsButton.getStyleClass().add("changeDetailsButton");
        backButton.getStyleClass().add("backButton");
        usernamLabel.getStyleClass().add("usernameLabel");
        fNameLabel.getStyleClass().add("fNameLabel");
        lNameLabel.getStyleClass().add("lNameLabel");
        emailLabel.getStyleClass().add("emailLabel");

        usernameField.getStyleClass().add("usernameField");
        fNameField.getStyleClass().add("fNameField");
        lNameField.getStyleClass().add("lNameField");
        emailField.getStyleClass().add("emailField");

        emptyPane.getStyleClass().add("empty");
        emptyPane2.getStyleClass().add("empty2");
        emptyPane3.getStyleClass().add("empty3");
    }

    private static String addStyleSheet() {
        File cssFile = new File(
                "C:/Users/gergr/OneDrive/Dokumentumok/Java/FitnessApp_Javafx_Maven/javapp/src/main/java/com/my/css/SettingsSceneStyle.css");
        return "file:///" + cssFile.getAbsolutePath()
                .replace("\\", "/").replace(" ", "%20");
    }

    private void openHomeScreen() throws IOException {
        HomeScene2 homeScene = new HomeScene2();
        window.setScene(homeScene.getHomeScene(window, loginScene));
    }

    private void openChangeDetailsScene() throws IOException {
        ChangeDetails changeDetailsScene = new ChangeDetails();
        window.setScene(changeDetailsScene.getChangeDetailsScene(window, loginScene));
    }
}
