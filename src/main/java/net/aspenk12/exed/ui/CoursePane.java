package net.aspenk12.exed.ui;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.util.CSV;

import java.io.File;

public class CoursePane extends MainPane {
    private Button fileButton = new Button("Select Course Data");
    private Text statusText = new Text("Status: Waiting for a CSV file");

    public CoursePane() {
        super();

        fileButton.onActionProperty().setValue(e -> getFile());

        statusText.setFill(Color.RED);

        vBox.getChildren().addAll(fileButton, statusText);
    }

    private void getFile(){
        File file = openFileChooser();
        CSV csv = new CSV(file);
        Course.createCourses(csv);

        statusText.setText("Status: Courses created from CSV. " + Course.courseCount() + " courses recognized.");
        statusText.setFill(Color.GREEN);
    }
}
