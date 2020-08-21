package net.aspenk12.exed.alg.containers;

import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.alg.members.Profile;
import net.aspenk12.exed.util.Warnings;

import java.util.ArrayList;
import java.util.List;

public class Application {
    private Profile profile;
    private List<Pick> picks = new ArrayList<>();
    private boolean validated = false;

    public static final int MAX_PICKS = 12;
    public static final int MIN_PICKS = 6;

    /**
     * @param profile The application needs a student profile to validate it's data.
     */
    public Application(Profile profile) {
        this.profile = profile;
    }

    /**
     * Adds a pick to the application. Picks ordered by which is added first.
     */
    public void addNewPick(Course course, int bid){
        if(validated){
            throw new UnsupportedOperationException("Tried to modify an application after validation");
        }

        int index = picks.size() - 1;
        picks.add(new Pick(course, bid, index));
    }

    public Pick getPick(int i){
        if(!validated){
            Warnings.logWarning("Application was indexed before validation");
        }
        return picks.get(i);
    }

    public boolean isValidated() {
        return validated;
    }

    public int pickCount(){
        return picks.size();
    }

    public List<Pick> getPicks(){
        return picks;
    }

    /**
     * Validates student-provided pick data. Validating this data is particularly important because:
     * a) there's a ton of it, way too much to look through manually.
     * b) because it's sourced from every student, there's plenty of opportunity for tomfoolery, mistakes, etc.
     *
     * Validation can't happen in the constructor because of how we populate the application data.
     * If you're ambitious you could fix this.
     *
     * Yes, we filter out negative picks.
     */
    public void validate(){
        //create a copy of picks that we can modify as we iterate through
        List<Pick> copy = new ArrayList<>();

        for (Pick pick : picks) {
            if (pick.bid > profile.points){
                pick.bid = profile.points;
                Warnings.logWarning("Student " + profile.getFullName() + " wagered more points than they had.");
            }
            if (pick.bid < 0){
                pick.bid = 0;
                Warnings.logWarning("Student " + profile.getFullName() + " wagered negative points,");
            }

            //remove courses previously attended by the student
            boolean alreadyAttended = false;
            List<Course> previousCourses = profile.getPreviousCourses();
            for (Course course : previousCourses) {
                if(pick.course.equals(course)){
                    alreadyAttended = true;
                    break;
                }
            }

            //is there a way to continue inside of the nested loop?
            if(alreadyAttended){
                continue;
            }

            //only add this course to copy if there aren't any other picks on the same course
            //at this point, copy is full of all other validated picks
            boolean alreadyApplying = false;
            for (Pick copyPick : copy) {
                if(copyPick.course.equals(pick.course)){
                    alreadyApplying = true;
                    break;
                }
            }

            if(!alreadyApplying){
                copy.add(pick);
            }
        }

        if(copy.size() < MIN_PICKS){
            Warnings.logWarning("Student " + profile.getFullName() + " had fewer than the minimum amount of picks");
        }

        if(copy.size() > MAX_PICKS){
            Warnings.logWarning("Student " + profile.getFullName() + " had more than the maximum amount of picks");
        }

        picks = copy;
        validated = true;
    }
}
