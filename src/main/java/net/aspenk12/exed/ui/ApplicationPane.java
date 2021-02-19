package net.aspenk12.exed.ui;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import net.aspenk12.exed.alg.members.Profile;
import net.aspenk12.exed.alg.members.Student;
import net.aspenk12.exed.util.CSV;
import net.aspenk12.exed.util.ProfileLinkException;

import java.io.File;

/**
 * First part of the main window for entering student application data
 */
public class ApplicationPane extends MainPane {

    public ApplicationPane() {
        super("Add Student Course Apps","Attach Application Data", "Status: Waiting for application data");
    }

    @Override
    protected void run(){
        CSV applicationData = new CSV(openFileChooser());

        try {
            Student.createStudents(applicationData);
        } catch (ProfileLinkException e) {
            ErrorAlert.throwErrorWindow("Failed to link a student application with validated data", e.getMessage(), true);
        }

        statusText.setText("Status: Course applications loaded. " + Student.getStudents().size() + " students created and validated.");
        statusText.setFill(Color.GREEN);
    }
}
