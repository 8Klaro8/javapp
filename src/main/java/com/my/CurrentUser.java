package com.my;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CurrentUser {
    private static final String USER_FILE_PATH = "src/main/java/com/my/current_user/current_user.txt";
    

    public static String get_current_user() throws IOException {
        // read current user from txt file
        Path fileName = Path.of(USER_FILE_PATH);
        return Files.readString(fileName);
    }

    public static void set_current_user(String userText) throws IOException {
        // read current user from txt file
        Path fileName = Path.of(USER_FILE_PATH);
        Files.writeString(fileName, userText);
    }

}
