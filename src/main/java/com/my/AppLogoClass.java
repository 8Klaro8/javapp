package com.my;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;

public class AppLogoClass {

    private static String LOGO_PATH = "javapp/src/main/resources/com/my/assets/logo_folder/icon.png";

    public static Image getLogo() throws FileNotFoundException {
        return new Image(new FileInputStream(LOGO_PATH));
    }
}
