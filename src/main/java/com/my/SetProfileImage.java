package com.my;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SetProfileImage {
    public static Label setBasicProfPic(String picPath, int size) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(picPath));
        ImageView imageIcon = new ImageView(image);
        // Image resizedImage = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        imageIcon.setFitHeight(size);
        imageIcon.setFitWidth(size);
        imageIcon.setPreserveRatio(true);

        return new Label("", imageIcon);
        // imageIcon = new ImageView(resizedImage, imageIcon.getDescription());
        // label.setIcon(imageIcon);
        // return label;
    }
}