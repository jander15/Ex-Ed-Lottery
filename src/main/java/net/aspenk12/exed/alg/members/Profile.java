package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Application;
import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.util.BadDataException;
import net.aspenk12.exed.util.CSV;
import net.aspenk12.exed.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A (Student) Profile contains 'validated' data about a student. It comprises of data specified in the validated CSV.
 */
public class Profile {
    public final int id;
    public final String firstName, lastName;
    public final Gender gender;
    public final Grade grade;
    public final int points;

    private final List<Course> previousCourses;

    private static Map<Integer, Profile> profiles;

    /**
     * This constructor is public for testing, but in practice it should only be called from inside the class.
     */
    public Profile(int id, String firstName, String lastName, Gender gender, Grade grade, int points, List<Course> previousCourses) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.grade = grade;
        this.points = points;
        this.previousCourses = previousCourses;
    }

    public static void createProfiles(CSV csv) {
        if(profiles != null) return;

        profiles = new HashMap<>();

        for (int i = 0; i < csv.rows(); i++) {
            String[] row = csv.get(i);

            String email = row[0];
            String firstName = row[1];
            String lastName = row[2];
            String genderString = row[3];
            String gradeString = row[4];
            String pointString = row[5];

            int id = Util.getIDFromEmail(email);

            //todo try to lambdaize all of these try-catches
            Gender gender;
            try {
                gender = Gender.getFromString(genderString);
            } catch (Exception e) {
                throw new BadDataException(csv, i, 3, genderString);
            }

            Grade grade;
            try {
                int gradeInt = Integer.parseInt(gradeString);
                grade = Grade.getFromInt(gradeInt);
            } catch (RuntimeException e){
                throw new BadDataException(csv, i, 4, gradeString);
            }

            int points;
            try {
               points = Integer.parseInt(pointString);
            } catch (RuntimeException e){
                throw new BadDataException(csv, i, 5, pointString);
            }

            List<Course> previousCourses = new ArrayList<>();

            //loop through previous course cells
            for (int j = 6; j < 9; j++) {
                String courseID = row[j];

                //obviously, don't try to add empty cells to previous courses
                if(!courseID.equals("")){
                    Course c = Course.get(courseID);
                    if(c == null) throw new BadDataException(csv,i, j, courseID);

                    previousCourses.add(c);
                }
            }

            Profile profile = new Profile(id, firstName, lastName, gender, grade, points, previousCourses);
            profiles.put(id, profile);
        }
    }

    public static Profile get(int id){
        return profiles.get(id);
    }

    public List<Course> getPreviousCourses(){
        return previousCourses;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

}
