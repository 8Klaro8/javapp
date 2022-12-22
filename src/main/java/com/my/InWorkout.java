package com.my;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class InWorkout {

    private static Label title, type, burnedCal, burnedCalNum, duration, durationNum, icon;
    private static Button editTitle, editType, back;
    private static Stage window;
    private static Scene inWorkoutScene;
    private static BorderPane borderLayout;
    private static ConnectToDB db = new ConnectToDB();
    private static Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
    private static String typeString, pathString;
    private static HBox topPanel, burnedCalBar, durationBar;
    private static VBox centerPanel;
    private static Pane paneLeft, paneRight;
    private static GridPane gridPane;

    public Scene getInWorkoutScene(Stage window, String workoutName) {

        // init. window
        this.window = window;

        // inti GridPane
        gridPane = new GridPane();

        // set 'gridPane'
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        // init boxes
        topPanel = new HBox(10);
        centerPanel = new VBox(20);
        burnedCalBar = new HBox(20);
        durationBar = new HBox(20);

        // init Panes
        paneLeft = new Pane();
        paneRight = new Pane();

        // init. labels
        title = new Label();
        type = new Label();
        burnedCal = new Label("Burned calorie");
        burnedCalNum = new Label();
        duration = new Label("Duration");
        durationNum = new Label();

        // init. buttons
        editTitle = new Button("Edit title");
        editType = new Button("Edit type");
        back = new Button("Back");

        // set alignemnt for boxes
        topPanel.setAlignment(Pos.CENTER_RIGHT);
        centerPanel.setAlignment(Pos.CENTER);
        burnedCal.setAlignment(Pos.CENTER);

        // set panes size
        paneLeft.setPrefSize(100, 100);
        paneRight.setPrefSize(100, 100);

        // setup center panel - get workout values
        try {
            String stringAllWorkoutDuration = db.read_all_workout_duration(conn, CurrentUser.get_current_user());
            String stringAllWorkoutCalorie = db.read_all_workout_burned_calorie(conn, CurrentUser.get_current_user());
            ArrayList<String> allWName = separate_collect_workout_datas(
                    db.read_all_workout_name(conn, CurrentUser.get_current_user()));
            ArrayList<String> allWType = separate_collect_workout_datas(
                    db.read_all_workout_type(conn, CurrentUser.get_current_user()));
            ArrayList<String> allWPath = separate_collect_workout_datas(
                    db.read_all_workout_path(conn, CurrentUser.get_current_user()));
            ArrayList<String> arrayListAllWorkoutDuration = separate_collect_workout_datas(stringAllWorkoutDuration);
            ArrayList<String> arrayListAllWorkoutCalorie = separate_collect_workout_datas(stringAllWorkoutCalorie);
            for (int i = 0; i < allWName.size(); i++) {
                if (allWName.get(i).equalsIgnoreCase(workoutName)) {
                    typeString = allWType.get(i);
                    pathString = allWPath.get(i);
                    durationNum.setText(arrayListAllWorkoutDuration.get(i) + " min");
                    burnedCalNum.setText(arrayListAllWorkoutCalorie.get(i) + " kcal");

                    // display 'name', 'type' and 'path'
                    title.setText(workoutName);
                    type.setText(typeString);
                    title.setFont(Font.font("verdena", FontWeight.MEDIUM, 30));
                    type.setFont(Font.font("verdena", FontWeight.MEDIUM, 30));

                    // // add mouse listener
                    // workoutName.addMouseListener(this);

                    // // when clicked, make it editable
                    // add_comp_to_centerPanel(path);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // set picture to 'icon'
        try {
            icon = SetProfileImage.setBasicProfPic(pathString, 100);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // add items to 'gridPane'
        gridPane.add(burnedCal, 0, 0);
        gridPane.add(burnedCalNum, 0, 1);
        gridPane.add(duration, 1, 0);
        gridPane.add(durationNum, 1, 1);



        // add items to 'burnedCalBar'
        burnedCalBar.getChildren().addAll(burnedCal, burnedCalNum);
        durationBar.getChildren().addAll(duration, durationNum);

        // add elemnts to 'topPanel'
        topPanel.getChildren().add(back);

        // add elemnts to VBox
        centerPanel.getChildren().addAll(title, editTitle, type, editType, icon);

        // init. layout
        borderLayout = new BorderPane();

        // add to borderLayout
        borderLayout.setTop(topPanel);
        borderLayout.setCenter(centerPanel);
        borderLayout.setLeft(paneLeft);
        borderLayout.setRight(paneRight);

        // init. scene
        inWorkoutScene = new Scene(borderLayout, 370, 500);

        // add style
        inWorkoutScene.getStylesheets().add(addStyleSheet());
        title.getStyleClass().add("title");
        type.getStyleClass().add("type");
        burnedCal.getStyleClass().add("burnedCal");
        duration.getStyleClass().add("duration");
        burnedCalNum.getStyleClass().add("burnedCalNum");
        durationNum.getStyleClass().add("durationNum");

        return inWorkoutScene;
    }

    private static String addStyleSheet() {
        File cssFile = new File(
                "C:/Users/gergr/OneDrive/Dokumentumok/Java/FitnessApp_Javafx_Maven/javapp/src/main/java/com/my/css/InWorkoutSceneStyle.css");
        return "file:///" + cssFile.getAbsolutePath()
                .replace("\\", "/").replace(" ", "%20");
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
