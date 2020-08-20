package net.aspenk12.exed.ui;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import net.aspenk12.exed.alg.members.Profile;
import net.aspenk12.exed.alg.members.Student;
import net.aspenk12.exed.util.CSV;

import java.io.File;

/**
 * First part of the main window for entering student application data
 */
public class ApplicationPane extends MainPane {

    public ApplicationPane() {
        super("Attach Application Data");
    }

    @Override
    protected void run(){
        CSV applicationData = new CSV(openFileChooser());
        Profile.createProfiles(applicationData);
    }
}
