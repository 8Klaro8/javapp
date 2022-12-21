package com.my;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WokroutsScene {

    private static BorderPane borderLayout;
    private static Scene workoutScene, loginScene;
    private static Stage window;
    private static Button backButton, addWorkoutButton, removeWorkoutButton;
    private static Label caloriesBurnedWeek, caloriesBurnedDay, caloriesBurnedWeekNum, caloriesBurnedDayNum,
            testwkLabel;
    private static HBox topMenuBar;
    private static VBox centerPanel, scrollBox;
    private static ScrollPane workoutPanel;
    private static Pane gapPane;
    private static ConnectToDB db = new ConnectToDB();
    private static Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));

    public Scene getWorkoutScene(Stage window) throws IOException {

        // assign stage (and scene)
        this.window = window;

        // init. Buttons
        backButton = new Button("Back");
        addWorkoutButton = new Button("Add Workout");
        removeWorkoutButton = new Button("Remove Workout");

        // add action
        backButton.setOnAction(e -> {
            try {
                openHomeScreen();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        addWorkoutButton.setOnAction(e -> {
            try {
                openAddWorkoutScreen();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        // init labels
        caloriesBurnedWeek = new Label("Calories burned last week");
        caloriesBurnedDay = new Label("Calories burned today");
        caloriesBurnedWeekNum = new Label("TEMP");
        caloriesBurnedDayNum = new Label("TEMP");

        // init pane
        gapPane = new Pane();

        // set pane
        gapPane.setPrefSize(20, 20);

        // init. HBox
        topMenuBar = new HBox();

        // init. VBox
        centerPanel = new VBox(20);
        scrollBox = new VBox(10);

        // init ScrollPane
        workoutPanel = new ScrollPane();

        // set 'workoutPanel'
        workoutPanel.setMinViewportWidth(100);
        workoutPanel.setMaxWidth(200);
        workoutPanel.setFitToWidth(true);

        // add to 'scrollBox'

        // all workout name
        ArrayList<String> collectedWorkoutNames = separate_collect_workout_datas(
                db.read_all_workout_name(conn, CurrentUser.get_current_user()));
        // all workout path
        ArrayList<String> collectedWorkoutPaths = separate_collect_workout_datas(
                db.read_all_workout_path(conn, CurrentUser.get_current_user()));

        for (int i = 0; i < collectedWorkoutNames.size(); i++) {
            Label label = new Label();
            label = SetProfileImage
                    .setBasicProfPic(collectedWorkoutPaths.get(i), 50);
            label.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-background-radius: 5;");
            label.setPadding(new Insets(20, 20, 20, 20));
            label.setGraphicTextGap(20);
            label.setText(collectedWorkoutNames.get(i));
            scrollBox.getChildren().add(label);
        }

        // create lables
        // for (int i = 0; i < 20; i++) {
        // testwkLabel = new Label();
        // testwkLabel = SetProfileImage
        // .setBasicProfPic("klaro\\src\\main\\resources\\assets\\basic_prif_pic
        // copy.png", 50);
        // testwkLabel.setStyle("-fx-background-color: black; -fx-text-fill: white;
        // -fx-background-radius: 5;");
        // testwkLabel.setPadding(new Insets(20, 20, 20, 20));
        // testwkLabel.setGraphicTextGap(20);
        // testwkLabel.setText("TESTsfe\nefefef");

        // scrollBox.getChildren().add(testwkLabel);
        // }

        // add to scrollPane
        workoutPanel.setContent(scrollBox);

        // set VBox
        centerPanel.setAlignment(Pos.CENTER);

        // set HBox
        topMenuBar.setAlignment(Pos.CENTER_RIGHT);
        topMenuBar.setPadding(new Insets(20, 20, 0, 0));

        // add to VBox
        centerPanel.getChildren().addAll(addWorkoutButton, removeWorkoutButton, gapPane, caloriesBurnedDay,
                caloriesBurnedDayNum,
                caloriesBurnedWeek, caloriesBurnedWeekNum);

        // add button to 'topMenuBar'
        topMenuBar.getChildren().add(backButton);

        // creating borderlayout
        borderLayout = new BorderPane();

        // hide scroll bar
        workoutPanel.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        workoutPanel.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        workoutPanel.setPadding(Insets.EMPTY);

        // add items to 'bordrLAyout'
        borderLayout.setTop(topMenuBar);
        borderLayout.setCenter(centerPanel);
        borderLayout.setLeft(workoutPanel);

        // creating scene
        workoutScene = new Scene(borderLayout, 370, 500);

        // add style
        workoutScene.getStylesheets().add(addStyleSheet());
        backButton.getStyleClass().add("backButton");
        addWorkoutButton.getStyleClass().add("addWorkoutButton");
        removeWorkoutButton.getStyleClass().add("removeWorkoutButton");
        workoutPanel.getStyleClass().add("workoutPanel");
        caloriesBurnedDay.getStyleClass().add("kcalDay");
        caloriesBurnedDayNum.getStyleClass().add("kcalDayNum");
        caloriesBurnedWeek.getStyleClass().add("kcalWeek");
        caloriesBurnedWeekNum.getStyleClass().add("kcalWeekNum");

        return workoutScene;
    }

    private static String addStyleSheet() {
        File cssFile = new File(
                "C:/Users/gergr/OneDrive/Dokumentumok/Java/FitnessApp_Javafx_Maven/javapp/src/main/java/com/my/css/WorkoutSceneStyle.css");
        return "file:///" + cssFile.getAbsolutePath()
                .replace("\\", "/").replace(" ", "%20");
    }

    private void openHomeScreen() throws IOException {
        HomeScene2 homeScene = new HomeScene2();
        window.setScene(homeScene.getHomeScene(window, loginScene));
    }

    private void openAddWorkoutScreen() throws IOException {
        AddWorkoutScene addWorkoutScene = new AddWorkoutScene();
        window.setScene(addWorkoutScene.getAddWorkoutScene(window));
    }

    private ArrayList<String> separate_collect_workout_datas(String inputed_method) {
        ArrayList<String> collectedWorkoutDatas = new ArrayList<String>();
        try {
            int begin = -1;
            int end = -1;
            int commaCounter = 0;
            String currentSubString = "";
            boolean beginAdded = false;
            for (int i = 0; i < inputed_method.length(); i++) {
                if (inputed_method.charAt(i) == ',') {
                    if (!(beginAdded)) {
                        begin = i + 1;
                        beginAdded = true;
                    }
                    commaCounter++;
                    if (commaCounter == 2) {
                        end = i;
                        currentSubString = inputed_method.substring(begin, end);
                        begin = i + 1;
                        commaCounter = 1;
                        collectedWorkoutDatas.add(currentSubString.trim());
                    }
                }
            }
            return collectedWorkoutDatas;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            return null;
        }

    }

}
