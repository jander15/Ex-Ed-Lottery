package net.aspenk12.exed.ui;

import javafx.scene.control.Button;

import java.io.File;

/**
 * First part of the main window for entering student data
 */
public class StudentPane extends MainPane {
    private File studentFile;
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
        studentFile = openFileChooser();
        validateButton.setDisable(false);
    }
}
