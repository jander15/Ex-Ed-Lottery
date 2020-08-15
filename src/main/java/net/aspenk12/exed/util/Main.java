package net.aspenk12.exed.util;

import javafx.application.Application;
import javafx.stage.Stage;
import net.aspenk12.exed.ui.MainWindow;

/**
 * Don't run the main class directly
 * @see Launcher
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainWindow.launch();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
