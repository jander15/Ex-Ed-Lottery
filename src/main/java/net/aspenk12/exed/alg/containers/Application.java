package net.aspenk12.exed.alg.containers;

import net.aspenk12.exed.alg.members.Student;
import net.aspenk12.exed.util.Warnings;

import java.util.ArrayList;

public class Application {
    private Student student;
    private ArrayList<Pick> picks = new ArrayList<>();
    private boolean validated = false;

    public static final int MAX_PICKS = 12;
    public static final int MIN_PICKS = 6;


    public Application(Student student) {
        this.student = student;
    }

    /**
     * Adds a pick to the application. Picks ordered by which is added first.
     */
    public void addPick(Pick pick){
        if(validated){
            throw new UnsupportedOperationException("Tried to modify an application after validation");
        }
        picks.add(pick);
    }

    public void validate(){
        for (Pick pick : picks) {
            if (pick.points > student.points){
                pick.points = student.points;
                Warnings.logWarning("Student " + student.getFullName() + " wagered more points than they had.");
            }
            if (pick.points < 0){
                pick.points = 0;
                Warnings.logWarning("Student " + student.getFullName() + " wagered negative points,");
            }
        }

        //todo remove previous courses
        //todo remove duplicate bets

        if(picks.size() < MIN_PICKS){
            Warnings.logWarning("Student " + student.getFullName() + " had fewer than the minimum amount of picks");
        }

        if(picks.size() > MAX_PICKS){
            Warnings.logWarning("Student " + student.getFullName() + " had more than the maximum amount of picks");
        }

    }

}
