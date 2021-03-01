package net.aspenk12.exed.ui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Just throws a standardized error window.
 * It might make sense to make this more object oriented, but this is a pretty nice way to do things.
 */
public class ErrorAlert {

    //don't create an instance, use the static method instead.
    private ErrorAlert(){}

    public static void throwErrorWindow(String header, String content){
        throwErrorWindow(header, content, false);
    }

    public static void throwErrorWindow(Throwable t){
        throwErrorWindow("An error occurred at runtime.", t.getMessage());
        t.printStackTrace();
    }

    public static void throwErrorWindow(String header, String content, boolean closeOnExit){
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.initOwner(MainWindow.getStage());

        error.setHeaderText(header);
        error.setContentText(content);

        Stage stage = (Stage) error.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

        if(closeOnExit){
            //closes whole application on exit
            error.setOnCloseRequest(e -> Platform.exit());
        }

        error.showAndWait();

    }
}
