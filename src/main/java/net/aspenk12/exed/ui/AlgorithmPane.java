package net.aspenk12.exed.ui;

import javafx.scene.paint.Color;
import net.aspenk12.exed.alg.members.Algorithm;
import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.alg.members.Profile;
import net.aspenk12.exed.alg.members.Student;
import net.aspenk12.exed.util.CSV;

import java.util.List;

/**
 * Pane that runs the algorithm when all the data is ready.
 */
public class AlgorithmPane extends MainPane {

    public AlgorithmPane() {
        super("Run the Algorithm","Run", "Status: Waiting to run algorithm");
    }

    @Override
    protected void run(){
        Algorithm algorithm = new Algorithm(Course.getCourses(), Student.getStudents());

        algorithm.run();

        List<Student> unlucky = algorithm.getUnlucky();

        statusText.setText("Status: Algorithm complete. " + unlucky.size() + " students were unlucky, and were placed in the 2nd lottery.");
        statusText.setFill(Color.GREEN);
    }
}
