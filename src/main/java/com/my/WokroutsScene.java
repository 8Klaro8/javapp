package com.my;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
    private static Pane gapPane, gapPaneRight;
    private static ConnectToDB db = new ConnectToDB();
    private static Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
    private static float allCalorieBurnedLastWeek, allCalorieBurnedToday;

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
        caloriesBurnedWeekNum = new Label();
        caloriesBurnedDayNum = new Label();

        // get todays calorie
        get_todays_calorie();

        // get last weeks calorie
        get_last_week_calorie();

        // set calories label
        setCaloriesBurnedTodayText();
        setCaloriesBurnedText();

        // init pane
        gapPane = new Pane();
        gapPaneRight = new Pane();

        // set pane
        gapPane.setPrefSize(20, 20);
        gapPaneRight.setPrefSize(20, 0);

        // init. HBox
        topMenuBar = new HBox();

        // init. VBox
        centerPanel = new VBox(20);
        scrollBox = new VBox(10);

        // init ScrollPane
        workoutPanel = new ScrollPane();

        // set 'workoutPanel'
        workoutPanel.setMinViewportWidth(100);
        workoutPanel.setMaxWidth(210);
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
                    .setBasicProfPic(collectedWorkoutPaths.get(i), 40);
            // label.setStyle("-fx-background-color: black; -fx-text-fill: white;
            // -fx-background-radius: 5;");
            label.setPadding(new Insets(20, 20, 20, 20));
            label.setGraphicTextGap(20);
            label.setText(collectedWorkoutNames.get(i));
            label.getStyleClass().add("label");
            label.setMinWidth(170);
            String workoutName = label.getText();
            label.setOnMouseClicked(e -> {
                openInWorkoutScreen(workoutName);
            });
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
        borderLayout.setRight(gapPaneRight);

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

    public void get_last_week_calorie() {
        // get 'todaysDate'
        String todaysDateString = get_todays_date();
        // inti 'todaysDate'
        Date todaysDate = dateToSimpleDate(todaysDateString);
        // init 'allCalorieBurnedLastWeek'
        allCalorieBurnedLastWeek = 0;
        try {
            String allWorkoutDate = db.read_get_all_date(conn, CurrentUser.get_current_user());
            String allWorkoutBurnedCalorie = db.read_all_workout_burned_calorie(conn, CurrentUser.get_current_user());
            ArrayList<String> allWorkoutDateSep = separate_collect_workout_datas(allWorkoutDate);
            ArrayList<String> allWorkoutBurnedCalorieSep = separate_collect_workout_datas(allWorkoutBurnedCalorie);
            for (int i = 0; i < allWorkoutDateSep.size(); i++) {
                // get current date
                Date currDate = dateToSimpleDate(allWorkoutDateSep.get(i));
                // get timedifference in miliseconds
                long timeDiff = todaysDate.getTime() - currDate.getTime();
                // transform miliseconds to days
                long dayDiff = TimeUnit.MILLISECONDS.toDays(timeDiff) % 365;
                // checking if date is smaller than today but not older than a week
                if (dayDiff < 7) { // if workouts date is no longer than 7 days then get calorie
                    // Read other saved datas and get calorie by index
                    allCalorieBurnedLastWeek += Float.valueOf(allWorkoutBurnedCalorieSep.get(i));
                }
            }
            // set caloriedBuned font properties
            setCaloriesBurnedText();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void get_todays_calorie() {
        // get 'todaysDate'
        String todaysDateString = get_todays_date();
        // inti 'todaysDate'
        Date todaysDate = dateToSimpleDate(todaysDateString);
        // init 'allCalorieBurnedLastWeek'
        allCalorieBurnedToday = 0;
        try {
            String allWorkoutDate = db.read_get_all_date(conn, CurrentUser.get_current_user());
            String allWorkoutBurnedCalorie = db.read_all_workout_burned_calorie(conn, CurrentUser.get_current_user());
            ArrayList<String> allWorkoutDateSep = separate_collect_workout_datas(allWorkoutDate);
            ArrayList<String> allWorkoutBurnedCalorieSep = separate_collect_workout_datas(allWorkoutBurnedCalorie);
            for (int i = 0; i < allWorkoutDateSep.size(); i++) {
                // get current date
                Date currDate = dateToSimpleDate(allWorkoutDateSep.get(i));
                // get timedifference in miliseconds
                long timeDiff = todaysDate.getTime() - currDate.getTime();
                // transform miliseconds to days
                long dayDiff = TimeUnit.MILLISECONDS.toDays(timeDiff) % 365;
                // checking if date is smaller than today but not older than a week
                if (!(dayDiff > 0)) { // if workouts date is no longer than 7 days then get calorie
                    // Read other saved datas and get calorie by index
                    allCalorieBurnedToday += Float.valueOf(allWorkoutBurnedCalorieSep.get(i));
                }
            }
            // set caloriedBuned font properties
            setCaloriesBurnedTodayText();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String get_todays_date() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public Date dateToSimpleDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        try {
            java.util.Date utilDate = sdf.parse(date);
            String dateString = new SimpleDateFormat("yyyy-MM-dd").format(utilDate);
            return java.sql.Date.valueOf(dateString);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void setCaloriesBurnedText() {
        caloriesBurnedWeekNum.setText(String.valueOf(allCalorieBurnedLastWeek));
        caloriesBurnedWeekNum.setFont(Font.font("verdena", FontWeight.MEDIUM, 20));
        // caloriesBurnedWeekNum.setHorizontalAlignment(SwingConstants.CENTER);
        // caloriesBurnedWeekNum.setFont(Font.font(caloriesBurnedWeekNum.getFont().getName(),
        // caloriesBurnedWeekNum.getFont().getStyle(), 20));
    }

    private void setCaloriesBurnedTodayText() {
        caloriesBurnedDayNum.setText(String.valueOf(allCalorieBurnedToday));
        // caloriesBurnedDayNum.setHorizontalAlignment(SwingConstants.CENTER);
        caloriesBurnedDayNum.setFont(Font.font("verdena", FontWeight.MEDIUM, 20));
        // setFont(Font.font("verdena", FontWeight.MEDIUM, 20));
    }

    private void openInWorkoutScreen(String workoutName) {
        InWorkout inWorkoutScene = new InWorkout();
        window.setScene(inWorkoutScene.getInWorkoutScene(window, workoutName));
    }

}
