package net.aspenk12.exed.ui;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.util.CSV;

import java.io.File;

public class CoursePane extends MainPane {
    public CoursePane() {
        super("Create the Ex Ed Courses","Attach Course Data", "Status: Waiting for a CSV file");
    }

    @Override
    protected void run() {
        CSV courseData = new CSV(openFileChooser());
        Course.createCourses(courseData);

        statusText.setText("Status: Courses created from CSV. " + Course.courseCount() + " courses recognized, with " + Course.findTotalSpots() + " total spots for students.");
        statusText.setFill(Color.GREEN);
    }
}
