package net.aspenk12.exed.ui;

import javafx.scene.paint.Color;
import net.aspenk12.exed.alg.members.Profile;
import net.aspenk12.exed.util.CSV;

public class StudentPane extends MainPane{

    public StudentPane() {
        super("Verify the student body","Attach Student Data", "Status: Waiting for Student Data");
    }

    @Override
    protected void run() {
        CSV profileData = new CSV(openFileChooser());
        Profile.createProfiles(profileData);

        statusText.setText("Status: Students created from verified data. " + Profile.count() + " students created.");
        statusText.setFill(Color.GREEN);
    }
}
