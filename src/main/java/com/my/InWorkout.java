package com.my;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class InWorkout {

    private static Label title, newTitLabel, type, newTypeLabel, burnedCalorie, burnedCalNum, duration, durationNum,
            icon;
    private static Button editTitle, editType, back, done1, done2, remove;
    private static Stage window;
    private static Scene inWorkoutScene;
    private static BorderPane borderLayout;
    private static ConnectToDB db = new ConnectToDB();
    private static Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
    private static String typeString, pathString, currentType;
    private static HBox topPanel, removeBar, iconBar;
    private static Pane paneLeft, paneRight, gap1, gap2;
    private static GridPane gridPane1, gridPane2;
    private static VBox centerPanel;
    private static TextField titleField, typeField;
    private static ArrayList<String> chosenArrayList;
    ComboBox<String> typeCombBox;

    public Scene getInWorkoutScene(Stage window, String workoutName) {

        // init. all components
        initComponents(window);

        // add action to all components
        addActionToComponents();

        // set all components
        setComponents();

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
                    currentType = typeString; // setting current workout type
                    title.setFont(Font.font("verdena", FontWeight.MEDIUM, 30));
                    type.setFont(Font.font("verdena", FontWeight.MEDIUM, 30));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // set picture to 'icon'
        try {
            icon = SetProfileImage.setBasicProfPic(pathString, 80);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // add icon to 'iconBar'
        iconBar.getChildren().add(icon);

        // add 'remove' to 'removeBar'
        removeBar.getChildren().add(remove);

        // add elemnts to 'topPanel'
        topPanel.getChildren().add(back);

        // add items to 'gridPane'
        addItemsToGridPane();

        // add items to 'centerPanel'
        centerPanel.getChildren().addAll(gridPane1, gap1, iconBar, gap2, gridPane2, removeBar);

        // init. layout
        borderLayout = new BorderPane();

        // add to borderLayout
        addToBorderLayout();

        // init. scene
        inWorkoutScene = new Scene(borderLayout, 370, 500);

        // add style
        addCompToStyleSheet();

        return inWorkoutScene;
    }

    private void addToBorderLayout() {
        borderLayout.setTop(topPanel);
        borderLayout.setCenter(centerPanel);
        borderLayout.setLeft(paneLeft);
        borderLayout.setRight(paneRight);
    }

    private void addItemsToGridPane() {
        gridPane1.add(title, 0, 0);
        gridPane1.add(editTitle, 2, 0);
        gridPane1.add(type, 0, 1);
        gridPane1.add(editType, 2, 1);

        gridPane2.add(duration, 0, 0);
        gridPane2.add(durationNum, 2, 0);
        gridPane2.add(burnedCalorie, 0, 1);
        gridPane2.add(burnedCalNum, 2, 1);
    }

    private void addCompToStyleSheet() {
        inWorkoutScene.getStylesheets().add(addStyleSheet());
        title.getStyleClass().add("title");
        type.getStyleClass().add("type");
        duration.getStyleClass().add("duration");
        burnedCalNum.getStyleClass().add("burnedCalNum");
        durationNum.getStyleClass().add("durationNum");
        burnedCalorie.getStyleClass().add("burnedCalorie");
        back.getStyleClass().add("back");
        editTitle.getStyleClass().add("editTitle");
        editType.getStyleClass().add("editType");
        gridPane1.getStyleClass().add("gridPane1");
        gridPane2.getStyleClass().add("gridPane2");
        done1.getStyleClass().add("done1");
        done2.getStyleClass().add("done2");
        remove.getStyleClass().add("remove");

        paneLeft.getStyleClass().add("paneLeft");
    }

    private void setComponents() {
        // set button
        done1.setMinWidth(80);
        done2.setMinWidth(80);

        // set textfield
        titleField.setMaxWidth(64);
        typeField.setMaxWidth(64);

        // set 'gridPane'
        gridPane1.setAlignment(Pos.TOP_CENTER);
        gridPane1.setVgap(20);
        gridPane1.setHgap(30);
        gridPane1.setPadding(new Insets(20, 10, 10, 10));

        gridPane2.setAlignment(Pos.TOP_CENTER);
        gridPane2.setVgap(20);
        gridPane2.setHgap(30);
        gridPane2.setPadding(new Insets(20, 10, 10, 10));

        // set alignemnt for boxes
        topPanel.setAlignment(Pos.CENTER_RIGHT);
        topPanel.setPadding(new Insets(20, 20, 10, 0));
        removeBar.setAlignment(Pos.CENTER);

        // padding for 'remove'
        removeBar.setPadding(new Insets(10, 0, 0, 0));

        burnedCalorie.setAlignment(Pos.CENTER);
        iconBar.setAlignment(Pos.CENTER);

        // set panes size
        paneLeft.setPrefSize(20, 0);
        paneRight.setPrefSize(20, 0);
        gap1.setPrefSize(0, 10);
        gap2.setPrefSize(0, 20);
    }

    private void addActionToComponents() {
        back.setOnAction(e -> {
            try {
                openWorkoutScreen();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        editTitle.setOnAction(e -> {
            // remove elements
            BorderPane root = (BorderPane) inWorkoutScene.getRoot();
            ObservableList<Node> nodes = root.getChildren();
            nodes.clear();

            // remove all from gridPane1
            gridPane1.getChildren().clear();

            // add items to 'gridPane'
            gridPane1.add(titleField, 0, 0);
            gridPane1.add(done1, 2, 0);
            gridPane1.add(type, 0, 1);
            gridPane1.add(editType, 2, 1);

            // add panels to nodes
            nodes.addAll(centerPanel, paneLeft, paneRight, topPanel);

            // refresh screen
            borderLayout.requestLayout();
        });

        done1.setOnAction(e -> {
            // check if new title exists
            if (titleExists()) {
                AlertBox.display("Already exists", "The workout title:\t " + titleField.getText() + "\nalready exists.",
                        "Close");
                return;
            }

            // get new value
            String newTitle = titleField.getText();

            // init. new titleLabel
            newTitLabel = new Label(newTitle);

            try {
                update_data_in_db(newTitle, title.getText(), 1);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
            // remove elements
            BorderPane root = (BorderPane) inWorkoutScene.getRoot();
            ObservableList<Node> nodes = root.getChildren();
            nodes.clear();

            // remove all from gridPane1
            gridPane1.getChildren().clear();
            // add items to 'gridPane'
            gridPane1.add(newTitLabel, 0, 0);
            gridPane1.add(editTitle, 2, 0);
            gridPane1.add(type, 0, 1);
            gridPane1.add(editType, 2, 1);

            // style
            newTitLabel.getStyleClass().add("newTitLabel");

            // add all item to "nodes"
            nodes.addAll(centerPanel, paneLeft, paneRight, topPanel);

            // refresh screen
            borderLayout.requestLayout();
        });

        editType.setOnAction(e -> {
            // current workout type
            currentType = type.getText();

            // remove elements
            BorderPane root = (BorderPane) inWorkoutScene.getRoot();
            ObservableList<Node> nodes = root.getChildren();
            nodes.clear();

            // remove all from gridPane1
            gridPane1.getChildren().clear();

            // add items to 'gridPane'
            gridPane1.add(title, 0, 0);
            gridPane1.add(editTitle, 2, 0);
            gridPane1.add(typeCombBox, 0, 1);
            gridPane1.add(done2, 2, 1);

            // add panels to nodes
            nodes.addAll(centerPanel, paneLeft, paneRight, topPanel);

            // refresh screen
            borderLayout.requestLayout();
        });

        done2.setOnAction(e -> {

            // get new value
            // String newType = typeField.getText();
            String newType = typeCombBox.getValue();

            // Checking if type is correct
            if (!(is_correct_type(newType))) {
                return;
            }
            newType = makeFirstLetterBig(newType);
            // init. new titleLabel
            newTypeLabel = new Label(newType);

            try {
                update_data_in_db(newType, newType, 2);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
            // remove elements
            BorderPane root = (BorderPane) inWorkoutScene.getRoot();
            ObservableList<Node> nodes = root.getChildren();
            nodes.clear();

            // remove all from gridPane1
            gridPane1.getChildren().clear();
            // add items to 'gridPane'
            gridPane1.add(title, 0, 0);
            gridPane1.add(editTitle, 2, 0);
            gridPane1.add(newTypeLabel, 0, 1);
            gridPane1.add(editType, 2, 1);

            // style
            newTypeLabel.getStyleClass().add("newTypeLabel");

            // add all item to "nodes"
            nodes.addAll(centerPanel, paneLeft, paneRight, topPanel);

            // refresh screen
            borderLayout.requestLayout();

            try {
                update_data_in_db(newType, currentType, 2);
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        });

        remove.setOnAction(e -> {
            try {
                delete_selected_workout();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void initComponents(Stage window) {
        // init. window
        this.window = window;

        // comboBox
        typeCombBox = new ComboBox<String>();
        typeCombBox.getItems().addAll("Gym", "Cardio", "Street");

        // init textfields
        titleField = new TextField();
        typeField = new TextField();

        // inti GridPane
        gridPane1 = new GridPane();
        gridPane2 = new GridPane();

        // init boxes
        topPanel = new HBox(10);
        removeBar = new HBox(20);
        centerPanel = new VBox(10);
        iconBar = new HBox();

        // init Panes
        paneLeft = new Pane();
        paneRight = new Pane();
        gap1 = new Pane();
        gap2 = new Pane();

        // init. labels
        title = new Label();
        type = new Label();
        burnedCalorie = new Label("Burned calorie:");
        burnedCalNum = new Label();
        duration = new Label("Duration:");
        durationNum = new Label();

        // init. buttons
        editTitle = new Button("Edit title");
        editType = new Button("Edit type");
        back = new Button("Back");
        done1 = new Button("Done");
        done2 = new Button("Done");
        remove = new Button("remove");
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

    private void openWorkoutScreen() throws IOException {
        WokroutsScene workoutScene = new WokroutsScene();
        window.setScene(workoutScene.getWorkoutScene(window));
    }

    public void update_data_in_db(String newWorkoutData, String currentWorkoutData, int indexNum) throws IOException {
        // Collect workout datas
        ArrayList<String> allWName = separate_collect_workout_datas(
                db.read_all_workout_name(conn, CurrentUser.get_current_user()));
        ArrayList<String> allWType = separate_collect_workout_datas(
                db.read_all_workout_type(conn, CurrentUser.get_current_user()));
        ArrayList<String> allWPath = separate_collect_workout_datas(
                db.read_all_workout_path(conn, CurrentUser.get_current_user()));
        ArrayList<String> allDuration = separate_collect_workout_datas(
                db.read_all_workout_duration(conn, CurrentUser.get_current_user()));
        // use switch to switch between type and name
        switch (indexNum) {
            case 1:
                chosenArrayList = new ArrayList<>(allWName);
                update_old_data(newWorkoutData, currentWorkoutData, allWName, allWType, allWPath, allDuration, 1);
                break;
            case 2:
                chosenArrayList = new ArrayList<>(allWType);
                update_old_data(newWorkoutData, currentWorkoutData, allWName, allWType, allWPath, allDuration, 2);
                break;
            default:
                break;
        }
    }

    private void update_old_data(String newWorkoutData, String currentWorkoutData, ArrayList<String> allWName,
            ArrayList<String> allWType, ArrayList<String> allWPath, ArrayList<String> allDuration, int indexNum)
            throws IOException {
        // If program finds the data we want to update, then it removes the old data and
        // appends the new data.
        for (int i = 0; i < chosenArrayList.size(); i++) {
            if (chosenArrayList.get(i).equalsIgnoreCase(currentWorkoutData)) {
                chosenArrayList.remove(i);
                chosenArrayList.add(i, newWorkoutData);
            }
        }
        // Removes everything from db
        db.remove_all_workout_data(conn, CurrentUser.get_current_user());
        // Re-appends everything to db with the updated ArrayLists
        for (int i = 0; i < chosenArrayList.size(); i++) {
            // based on the indexNum we choose the appropriate 'append'
            // structure.(Append/relace type or name)
            switch (indexNum) {
                case 1:
                    db.add_workout_name(conn, chosenArrayList.get(i), CurrentUser.get_current_user());
                    db.add_workout_type(conn, allWType.get(i), CurrentUser.get_current_user());
                    db.add_workout_path(conn, allWPath.get(i), CurrentUser.get_current_user());
                    db.add_workout_duration(conn, allDuration.get(i), CurrentUser.get_current_user());
                    break;
                case 2:
                    db.add_workout_name(conn, allWName.get(i), CurrentUser.get_current_user());
                    db.add_workout_type(conn, chosenArrayList.get(i), CurrentUser.get_current_user());
                    db.add_workout_path(conn, allWPath.get(i), CurrentUser.get_current_user());
                    db.add_workout_duration(conn, allDuration.get(i), CurrentUser.get_current_user());
                    break;
                default:
                    break;
            }

        }
    }

    private boolean is_correct_type(String newWorkoutType) {
        if (!(newWorkoutType.equalsIgnoreCase("cardio") ||
                newWorkoutType.equalsIgnoreCase("gym") ||
                newWorkoutType.equalsIgnoreCase("street"))) {
            AlertBox.display("Wrong type given", "Please choose either:\n\t-Street\n\t-Gym\n\t-Cardio", "Close");
            return false;
        }
        return true;
    }

    private String makeFirstLetterBig(String newWorkoutType) {
        String bigLetter = newWorkoutType.substring(0, 1).toUpperCase();
        String rest = newWorkoutType.substring(1, newWorkoutType.length());
        newWorkoutType = bigLetter + rest;
        return newWorkoutType;
    }

    private void delete_selected_workout() throws IOException {
        // get the index of the chosen workout by it's name
        ArrayList<String> collectedWorkoutNames = separate_collect_workout_datas(
                db.read_all_workout_name(conn, CurrentUser.get_current_user()));
        ArrayList<String> collectedWorkoutTypes = separate_collect_workout_datas(
                db.read_all_workout_type(conn, CurrentUser.get_current_user()));
        ArrayList<String> collectedWorkoutPaths = separate_collect_workout_datas(
                db.read_all_workout_path(conn, CurrentUser.get_current_user()));
        ArrayList<String> collectedWorkoutdurations = separate_collect_workout_datas(
                db.read_all_workout_duration(conn, CurrentUser.get_current_user()));
        ArrayList<String> collectedWorkoutBurnedCalories = separate_collect_workout_datas(
                db.read_all_workout_burned_calorie(conn, CurrentUser.get_current_user()));
        ArrayList<String> collectedWorkoutDates = separate_collect_workout_datas(
                db.read_all_workout_date(conn, CurrentUser.get_current_user()));

        for (int i = 0; i < collectedWorkoutNames.size(); i++) {
            if (collectedWorkoutNames.get(i).equals(title.getText())) {

                // get workout type and path from db by index
                String typeToDelete = collectedWorkoutTypes.get(i);
                String pathToDelete = collectedWorkoutPaths.get(i);
                String durationToDelete = collectedWorkoutdurations.get(i);

                // delete items from ArrayList
                for (int j = 0; j < collectedWorkoutNames.size(); j++) {
                    if (collectedWorkoutNames.get(i).equals(title.getText())) {
                        collectedWorkoutNames.remove(i);
                        collectedWorkoutTypes.remove(i);
                        collectedWorkoutPaths.remove(i);
                        collectedWorkoutdurations.remove(i);
                        collectedWorkoutBurnedCalories.remove(i);
                        collectedWorkoutDates.remove(i);
                    }
                    break;
                }

                // delete all from db before appending new value
                db.remove_all_workout_data(conn, CurrentUser.get_current_user());

                // append values back to db
                for (int j = 0; j < collectedWorkoutNames.size(); j++) {
                    db.add_workout_name(conn, collectedWorkoutNames.get(j), CurrentUser.get_current_user());
                    db.add_workout_type(conn, collectedWorkoutTypes.get(j), CurrentUser.get_current_user());
                    db.add_workout_path(conn, collectedWorkoutPaths.get(j), CurrentUser.get_current_user());
                    db.add_workout_duration(conn, collectedWorkoutdurations.get(j), CurrentUser.get_current_user());
                    db.add_workout_burned_calorie(conn, collectedWorkoutBurnedCalories.get(i),
                            CurrentUser.get_current_user());
                    db.add_workout_date(conn, collectedWorkoutDates.get(i), CurrentUser.get_current_user());
                }
                openWorkoutScreen();
            }
        }
    }

    private boolean titleExists() {
        String newTitle = titleField.getText();
        String allWorkoutNameString;
        try {
            allWorkoutNameString = db.read_all_workout_name(conn, CurrentUser.get_current_user());
            ArrayList<String> allWorkoutNameCollection = separate_collect_workout_datas(allWorkoutNameString);
            for (int index = 0; index < allWorkoutNameCollection.size(); index++) {
                if (allWorkoutNameCollection.get(index).equalsIgnoreCase(newTitle)) {
                    return true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }
        return false;

    }
}
