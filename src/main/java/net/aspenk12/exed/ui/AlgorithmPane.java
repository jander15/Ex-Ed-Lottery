package net.aspenk12.exed.ui;

import javafx.scene.paint.Color;
import net.aspenk12.exed.alg.containers.Pick;
import net.aspenk12.exed.alg.members.Algorithm;
import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.alg.members.Profile;
import net.aspenk12.exed.alg.members.Student;
import net.aspenk12.exed.util.CSV;

import java.util.*;

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
        //run "unlucky" students through algorithm with no restrictions
        algorithm.run2();

        List<Student> secondLotto = algorithm.getSecondLotto();

        //Create "Unlucky course"
        Course c = new Course("2ndLotto","zz2ndLotto","none",null);
        for(Student s: secondLotto){
            c.forceAddStudent(s);
        }
        //Create "NoSignUp course"
        Course noSignUp = new Course("NoSignUp","zzNoSignUp","none",null);
        for (Profile p: Profile.getProfiles().values()) {
            boolean signedUp=false;
            for (Student s: Student.getStudents()){
                if (s.profile.equals(p)){
                    signedUp=true;
                }
            }
            if(!signedUp){
                noSignUp.forceAddStudent(new Student(p,null));
            }

        }


        statusText.setText("Status: Algorithm complete. " + secondLotto.size() + " students were unlucky, and were placed in the 2nd lottery.");
        statusText.setFill(Color.GREEN);
    }


}
