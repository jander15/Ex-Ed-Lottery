package net.aspenk12.exed.ui;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Just throws a standardized error window
 */
public class ErrorAlert {

    private ErrorAlert(){}

    public static void throwErrorWindow(String header, String content){
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.initOwner(MainWindow.getStage());

        error.setHeaderText(header);
        error.setContentText(content);

        Stage stage = (Stage) error.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        error.showAndWait();
    }
}
