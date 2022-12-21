package com.my;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.application.Application;

// import org.apache.http.*;
// import org.json.simple.JSONObject;
// import org.json.simple.parser.JSONParser;

// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;

public class AddWorkoutScene {

    private static Scene addWorkoutScene;
    private static BorderPane borderLayout;
    private static Stage window;
    private static Label workoutTitle, workoutType, workoutDuration, workoutIcon;
    private static TextField workoutTitleField, workoutDurationField;
    private static Button backButton, addButton;
    private static ChoiceBox<String> typeDropDown;
    private static ChoiceBox<String> iconDropDown;
    private static VBox mainPanel;
    private static HBox bottomPanel, topPanel;
    private static Pane empty1, empty2;
    private static final String DIRECTORY_PATH = "src/main/resources/com/my/assets/typeOfWorkouts";
    private static String chosenIconPath = "";
    private static String typeOfWorkout = "";
    private static ConnectToDB db = new ConnectToDB();
    private static Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));

    public Scene getAddWorkoutScene(Stage window) {
        String version = System.getProperty("javafx.runtime.version");
        System.out.println("JavaFX version: " + version);
        // assign window (and scenes)
        this.window = window;

        // init main panel
        mainPanel = new VBox(15);

        // init 'bottomPanel'
        bottomPanel = new HBox();
        topPanel = new HBox();

        // init. label
        workoutTitle = new Label("Workout's title");
        workoutType = new Label("Workout's type");
        workoutDuration = new Label("Workout's duration (min)");
        workoutIcon = new Label("Icon");

        // init. panes
        empty1 = new Pane();
        empty2 = new Pane();

        // init. textfields
        workoutTitleField = new TextField();
        workoutDurationField = new TextField();

        // init. buttons
        backButton = new Button("Back");
        addButton = new Button("Add");

        // init. dropdowns
        typeDropDown = new ChoiceBox<String>();
        iconDropDown = new ChoiceBox<String>();

        // set icondropdown
        ComboBox<Pair<String, Image>> comboBox = new ButtonCell().getComboBox();

        // create workout type icon labels-------------------------
        // loop thru among the files
        File dir = new File(DIRECTORY_PATH);
        File[] directoryListing = dir.listFiles();

        int columnCounter = 0;
        int rowCounter = 0;

        if (directoryListing != null) {
            for (File file : directoryListing) {
                String filePath = file.getPath();
                try {
                    // comboBox.getItems().add(filePath, new Image(new FileInputStream(filePath)));
                    comboBox.getItems()
                            .add(new Pair<String, Image>(filePath, new Image(new FileInputStream(filePath))));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        // set HBox
        bottomPanel.setPrefSize(0, 70);
        bottomPanel.setAlignment(Pos.TOP_CENTER);

        topPanel.setPrefSize(0, 40);
        topPanel.setAlignment(Pos.CENTER_RIGHT);
        topPanel.setPadding(new Insets(20, 20, 0, 0));

        // set/ align VBox
        mainPanel.setAlignment(Pos.CENTER);

        // set textfield
        workoutDurationField.setMaxWidth(60);
        workoutDurationField.setAlignment(Pos.CENTER);
        workoutTitleField.setMaxWidth(180);
        workoutTitleField.setMinHeight(30);
        workoutDurationField.setFont(Font.font("verdana", FontWeight.BOLD, 17));
        workoutTitleField.setFont(Font.font("verdana", FontWeight.BOLD, 17));

        // set panes
        empty1.setPrefSize(70, 0);
        empty2.setPrefSize(70, 0);

        // set dropdowns
        typeDropDown.setMinSize(140, 20);

        // add entries to choicebox and set default value
        typeDropDown.getItems().addAll("Gym", "Cardio", "Street");
        typeDropDown.setValue("Please choose");

        // positioning dropdown

        // add action
        typeDropDown.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!(oldValue.equals(newValue))) {
                typeOfWorkout = newValue;
            }
        });

        backButton.setOnAction(e -> {
            try {
                openWorkoutSceneScreen();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        // combobox
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            chosenIconPath = comboBox.getValue().getKey();
        });

        addButton.setOnAction(e -> {
            for (int i = 0; i < workoutDurationField.getText().length(); i++) {
                if (Character.isDigit(workoutDurationField.getText().charAt(i)) == false) {
                    AlertBox.display("Error", "Please type number only for duration", "Close");
                    return;
                }
            }
            if (workoutTitleField.getText().isEmpty()) {
                AlertBox.display("Title is missing", "Please give a workout title", "Close");
                return;
            }
            if (typeOfWorkout.isEmpty()) {
                AlertBox.display("Type is missing", "Please choose a workout type", "Close");
                return;
            }
            if (chosenIconPath.isEmpty()) {
                AlertBox.display("Icon is missing", "Please choose a workout icon", "Close");
                return;
            }
            if (workoutDurationField.getText().isEmpty()) {
                AlertBox.display("Duration is missing", "Please give a workout duration", "Close");
                return;
            } else {
                try {
                    db.add_workout_name(conn, workoutTitleField.getText(), CurrentUser.get_current_user());
                    db.add_workout_type(conn, typeOfWorkout, CurrentUser.get_current_user());
                    db.add_workout_path(conn, chosenIconPath, CurrentUser.get_current_user());
                    db.add_workout_duration(conn, workoutDurationField.getText(), CurrentUser.get_current_user());
                    db.add_workout_date(conn, get_date_now(), CurrentUser.get_current_user());
                    // get users weight
                    String weight = db.get_weight_by_username(conn, CurrentUser.get_current_user());
                    // db.add_workout_burned_calorie(conn, getCalorie(getResponse(weight,
                    // workoutDurationField)), CurrentUser.get_current_user());

                    // go_back_to_my_workouts();
                } catch (Exception err) {
                    System.out.println(err.getMessage());
                }
            }

        });

        // set labels
        workoutTitle.setFont(Font.font("verdena", FontWeight.MEDIUM, 17));
        workoutType.setFont(Font.font("verdena", FontWeight.MEDIUM, 17));
        workoutDuration.setFont(Font.font("verdena", FontWeight.MEDIUM, 17));
        workoutIcon.setFont(Font.font("verdena", FontWeight.MEDIUM, 17));

        // add to 'bottomPanel'
        bottomPanel.getChildren().add(addButton);

        // add to 'topPanel'
        topPanel.getChildren().add(backButton);

        // add to VBox - 'mainpanel'
        mainPanel.getChildren().addAll(workoutTitle, workoutTitleField, workoutType, typeDropDown, workoutIcon,
                comboBox, workoutDuration,
                workoutDurationField);

        // create borderlayout
        borderLayout = new BorderPane();

        // add to layout
        borderLayout.setCenter(mainPanel);
        borderLayout.setLeft(empty1);
        borderLayout.setRight(empty2);
        borderLayout.setBottom(bottomPanel);
        borderLayout.setTop(topPanel);

        // init. scene
        addWorkoutScene = new Scene(borderLayout, 370, 500);

        // add style
        addWorkoutScene.getStylesheets().add(addStyleSheet());
        workoutTitle.getStyleClass().add("workoutTitle");
        workoutType.getStyleClass().add("workoutType");
        workoutDuration.getStyleClass().add("workoutDuration");
        typeDropDown.getStyleClass().addAll("choice-box");
        typeDropDown.getStyleClass().add("choice-box-popup");
        addButton.getStyleClass().add("addButton");
        backButton.getStyleClass().add("backButton");
        workoutDurationField.getStyleClass().add("wkDuratField");
        workoutTitleField.getStyleClass().add("wkTitleField");
        workoutIcon.getStyleClass().add("workoutIcon");

        return addWorkoutScene;
    }

    private static String addStyleSheet() {
        File cssFile = new File(
                "C:/Users/gergr/OneDrive/Dokumentumok/Java/FitnessApp_Javafx_Maven/javapp/src/main/java/com/my/css/AddWorkoutSceneStyle.css");
        return "file:///" + cssFile.getAbsolutePath()
                .replace("\\", "/").replace(" ", "%20");
    }

    private void openWorkoutSceneScreen() throws IOException {
        WokroutsScene workoutScene = new WokroutsScene();
        window.setScene(workoutScene.getWorkoutScene(window));
    }

    private String get_date_now() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return String.valueOf(dtf.format(now));
    }

    // private HttpRequest getResponse(String usersWeight, String currentDuration) {
    //     HttpRequest request = HttpRequest.newBuilder()
    //             .uri(URI.create(
    //                     String.format(
    //                             "https://fitness-calculator.p.rapidapi.com/burnedcalorie?activityid=co_2&activitymin=%s&weight=%s",
    //                             currentDuration, usersWeight)))
    //             .header("X-RapidAPI-Key",
    //                     "b4b40d284amshacf7b676b928e88p1a5c77jsned0db45910bf")
    //             .header("X-RapidAPI-Host", "fitness-calculator.p.rapidapi.com")
    //             .method("GET", HttpRequest.BodyPublishers.noBody())
    //             .build();
    //     return request;
    // }

    // private String getCalorie(HttpRequest request) throws org.json.simple.parser.ParseException {
    //     HttpResponse<String> response;
    //     try {
    //         response = HttpClient.newHttpClient().send(request,
    //                 HttpResponse.BodyHandlers.ofString());
    //         JSONParser parser = new JSONParser();

    //         JSONObject json = (JSONObject) parser.parse(String.valueOf(response.body()));
    //         Object calorieJson = json.values().toArray()[1];
    //         JSONObject calorie = (JSONObject) parser.parse(String.valueOf(calorieJson));
    //         return String.valueOf(calorie.get("burnedCalorie"));

    //     } catch (IOException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     } catch (InterruptedException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    //     return null;
    // }
}
