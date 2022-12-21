package com.my;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChangeAvatarScene {

    private static BorderPane borderLayout;
    private static Scene changeAvatarScene;
    private static ScrollPane scrollPane;
    private static GridPane scrollContainer;
    private static final String DIRECTORY_PATH = "src/main/resources/com/my/assets";
    private static Pane emptyPane, emptyPane2, emptyPane3, emptyPane4;
    private static Stage window;
    private static Scene loginScene;

    public Scene getChangeAvatarScene(Stage window, Scene loginScene) {

        // asign window
        this.window = window;
        this.loginScene = loginScene;

        // init. VBox
        scrollContainer = new GridPane();

        // build up method to get all the profil pics --------------------------
        // 1. Read picture paths 2. create image and add it to a label 3. add the imaged
        // label to 'scrollContainer'
        File dir = new File(DIRECTORY_PATH);
        File[] directoryListing = dir.listFiles();

        int columnCounter = 0;
        int rowCounter = 0;

        if (directoryListing != null) {

            for (File file : directoryListing) {
                String filePath = file.getPath(); // current pic path

                try {
                    Label imagLabel = SetProfileImage.setBasicProfPic(filePath, 150);
                    imagLabel.setMaxWidth(200);
                    imagLabel.setMinHeight(200);
                    imagLabel.setPadding(new Insets(0, 10, 0, 10));
                    imagLabel.setStyle("-fx-background-color: rgb(53, 53, 53);");
                    imagLabel.setOnMouseClicked(e -> {
                        // set as profile pic on click and go back to home screen
                        // gets profile pics path from set actioncommand
                        ConnectToDB db = new ConnectToDB();
                        Connection conn = db.connect_to_db("accounts", "postgres", System.getenv("PASSWORD"));
                        try {
                            db.set_prof_pic_path(conn, "my_users", filePath, CurrentUser.get_current_user());
                            openHomeScreen();
                        } catch (Exception err) {
                            System.out.println(err.getMessage());
                        }
                    });
                    scrollContainer.add(imagLabel, columnCounter, rowCounter);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                columnCounter++;
                if (columnCounter == 2) {
                    rowCounter++;
                    columnCounter = 0;
                }
            }
        } else {
            throw new IllegalArgumentException("Directory cannot be found: " + DIRECTORY_PATH);
        }

        // init. 'scrollPane'
        scrollPane = new ScrollPane();

        // init. emotyPanes
        emptyPane = new Pane();
        emptyPane2 = new Pane();
        emptyPane3 = new Pane();
        emptyPane4 = new Pane();

        // set emptyPanes sizes
        // emptyPane.setPrefSize(60, 0);
        // emptyPane2.setPrefSize(60, 0);
        // emptyPane3.setPrefSize(0, 30);
        // emptyPane4.setPrefSize(0, 30);

        // set VBox
        scrollContainer.setAlignment(Pos.CENTER);
        scrollContainer.setMinWidth(235);

        // set scrollPane viewport and hbar policy
        scrollPane.setPrefViewportWidth(200);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);

        // add items to 'scrollPane'
        scrollPane.setContent(scrollContainer);

        // add scrollPane to 'centeringStackPane'

        // inint 'borderLayout'
        borderLayout = new BorderPane();

        // set border layout's
        borderLayout.setCenter(scrollPane);
        borderLayout.setLeft(emptyPane);
        borderLayout.setRight(emptyPane2);
        borderLayout.setTop(emptyPane3);
        borderLayout.setBottom(emptyPane4);

        // init. scene
        changeAvatarScene = new Scene(borderLayout, 370, 500);

        // add style sheet
        changeAvatarScene.getStylesheets().add(getImageLink());
        scrollContainer.getStyleClass().add("scrollContainer");
        scrollPane.getStyleClass().add("scrollPane");

        return changeAvatarScene;
    }

    private static String getImageLink() {
        File cssFile = new File(
                "C:/Users/gergr/OneDrive/Dokumentumok/Java/FitnessApp_Javafx_Maven/javapp/src/main/java/com/my/css/ChangeAvatarSceneStyle.css");
        return "file:///" + cssFile.getAbsolutePath()
                .replace("\\", "/").replace(" ", "%20");
    }

    private void openHomeScreen() throws IOException {
        HomeScene2 homeScene = new HomeScene2();
        window.setScene(homeScene.getHomeScene(window, loginScene));
    }
}
