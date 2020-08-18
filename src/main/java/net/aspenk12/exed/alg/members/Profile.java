package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.util.BadDataException;
import net.aspenk12.exed.util.CSV;
import net.aspenk12.exed.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * A (Student) Profile contains 'validated' data about a student. It comprises of data specified in the validated CSV
 */
public class Profile {
    public final int id;
    public final String firstName, lastName;
    public final Gender gender;
    public final Grade grade;
    public final int points;

    private static Map<Integer, Profile> profiles;

    public Profile(int id, String firstName, String lastName, Gender gender, Grade grade, int points) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.grade = grade;
        this.points = points;
    }

    public static void createProfiles(CSV csv) {
        if(profiles != null) return;

        for (int i = 0; i < csv.rows(); i++) {
            String[] row = csv.get(i);

            String email = row[0];
            String firstName = row[1];
            String lastName = row[2];
            String genderString = row[3];
            String gradeString = row[4];
            String pointString = row[5];
            String prevCourse1Str = row[6];
            String prevCourse2Str = row[7];
            String prevCourse3Str = row[8];

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

            Course prevCourse1 = Course.get(prevCourse1Str);
            Course prevCourse2 = Course.get(prevCourse2Str);
            Course prevCourse3 = Course.get(prevCourse3Str);

            if(prevCourse1 == null || prevCourse2 == null || prevCourse3 == null){
                //throw new BadDataException();
            }

            Profile profile = new Profile(id, firstName, lastName, gender, grade, points);
            profiles.put(id, profile);
        }
    }
}
