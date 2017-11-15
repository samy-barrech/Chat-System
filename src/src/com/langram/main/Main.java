package com.langram.main;

import com.langram.utils.App;
import com.langram.utils.Settings;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class Main extends App {

    public void start(Stage stage) throws Exception {

        super.start(stage, "main.fxml", Settings.getDefaultWidth(), Settings.getDefaultHeight());

        // Get App name and title locale
        ResourceBundle globalMessages = ResourceBundle.getBundle("GlobalMessagesBundle", Settings.getLocale());
        stage.setTitle(Settings.getAppName() + " - " + globalMessages.getString("titleWindow"));

        // Show the stage
        stage.show();

    }

}
