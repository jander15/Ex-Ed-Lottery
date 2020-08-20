package net.aspenk12.exed.ui;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.util.CSV;

import java.io.File;

public class CoursePane extends MainPane {
    private Text statusText = new Text("Status: Waiting for a CSV file");

    public CoursePane() {
        super("Attach Course Data");
        statusText.setFill(Color.RED);

        statusText.setTextAlignment(TextAlignment.CENTER);
        statusText.wrappingWidthProperty().bind(widthProperty());

        vBox.getChildren().add(statusText);
    }

    @Override
    protected void run() {
        CSV courseData = new CSV(openFileChooser());
        Course.createCourses(courseData);

        statusText.setText("Status: Courses created from CSV. " + Course.courseCount() + " courses recognized.");
        statusText.setFill(Color.GREEN);
    }
}
