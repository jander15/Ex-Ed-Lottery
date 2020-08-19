package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Application;
import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.alg.containers.Pick;
import net.aspenk12.exed.util.BadDataException;
import net.aspenk12.exed.util.CSV;
import net.aspenk12.exed.util.Util;
import net.aspenk12.exed.util.Warnings;

import java.util.ArrayList;
import java.util.List;

/**
 * The student class represents every Student applying for Ex Ed.
 * A student contains a Profile, full of validated info about the student,
 * and a an Application, which contains student-produced data about the student's picks.
 *
 * A major weak point of this class, (and this whole system in general), is the process of linking the
 * 'validated' Profile data with the unvalidated application data.
 * @see Profile
 * @see Application
 */
public class Student {
    public final Profile profile;
    public final Application application;

    private static List<Student> students;

    public static void createStudents(CSV csv){
        //only create students once, duh.
        if(students != null) return;

        students = new ArrayList<>();

        for (int i = 0; i < csv.rows(); i++) {
            String[] row = csv.get(i);

            //todo address failure condition here
            String email = row[0];
            int id = Util.getIDFromEmail(email);
            Profile profile = Profile.get(id);

            Application application = new Application(profile);

            //create an application for each student by reading in the application data
            //note the increment by 2, because the loop reads two values, course and bid, each cycle
            //starts at 1 because j = 0 is the email field.
            for (int j = 1; j < row.length; j += 2) {
                String courseString = row[j];
                String bidString = row[j + 1];

                if(courseString.equals("") || bidString.equals("")){
                    continue;
                }

                String courseId = Util.extractCourseID(courseString);

                Course course = Course.get(courseId);

                if(course == null){
                    throw new BadDataException(csv, i, j, courseId);
                }

                //these data points should be validated automatically by google forms
                //probably no need to catch the NumberFormatException
                int bid = Integer.parseInt(bidString);

                application.addPick(new Pick(course, bid));
            }

            application.validate();

            students.add(new Student(profile, application));
        }
    }

    public static List<Student> getStudents(){
        return students;
    }


    //public for testing - ideally students should only be created in createStudents()
    public Student(Profile profile, Application application) {
        this.profile = profile;
        this.application = application;

        if (!application.isValidated()) {
            Warnings.logWarning("Created a student with unvalidated application data");
        }
    }
}
