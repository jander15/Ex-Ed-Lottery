package net.aspenk12.exed.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * First part of the main window for entering student data
 */
public class StudentPane extends MainPane {
    private File studentCSV;
    private Button validateButton;

    public StudentPane() {
        super();

        Button uploadButton = new Button("Upload Student Data");
        uploadButton.onActionProperty().setValue(e -> uploadStudentCSV());

        validateButton = new Button("Validate Student Data");
        validateButton.setDisable(true);

        vBox.getChildren().add(uploadButton);
        vBox.getChildren().add(validateButton);
    }

    private void uploadStudentCSV(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        studentCSV = fileChooser.showOpenDialog(MainWindow.getStage());
        validateButton.setDisable(false);
    }
}
