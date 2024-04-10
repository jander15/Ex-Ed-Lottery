package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Application;
import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.alg.containers.Pick;
import net.aspenk12.exed.util.*;

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
    public boolean unlucky = false;

    //tracks which picks the student has 'tried to get on' in the algorithm
    /*Algorithm*/ Pick currentPick;

    private static List<Student> students = new ArrayList<>();

    /**
     * When creating students, run through the application data, parse it, and
     * attempt to pair each course application to a validated student profile via the id included in the student email
     * @param csv Student Application CSV
     */
    public static void createStudents(CSV csv) throws ProfileLinkException{
        //only create students once, duh.
        if(students.size()>100) return;

        for (int i = 0; i < csv.rows(); i++) {
            String[] row = csv.get(i);

            String email = row[0];
            System.out.println(email);
            Profile profile = linkProfile(email);

            Application application = new Application(profile, email);

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
                if (profile.grade==Grade.SENIOR){
                    bid=profile.points;
                }

                application.addNewPick(course, bid);
            }

            application.validate();
            //only create students who haven't already been placed
            if (profile.placedCourse.isEmpty()){
                new Student(profile, application);
            }

        }

    }

    static Profile linkProfile(String email) throws ProfileLinkException{
        int id = Util.getIDFromEmail(email);
        Profile profile = Profile.get(id);

        if(profile == null) {
            throw new ProfileLinkException(email, id);
        } else return profile;
    }

    public static List<Student> getStudents(){
        return students;
    }

    /**
     * Are these two students the same gender and grade?
     */
    public boolean sameDemographic(Student student){
        return isDemographic(student.profile.getGrade(), student.profile.getGender());
    }

    public boolean isDemographic(Grade grade, Gender gender){

        boolean sameGender = gender.equals(profile.getGender());
        boolean sameGrade = grade.equals(profile.getGrade());
        //if gender is X then only look at Grade level
        if (gender.equals(Gender.X)){
            return sameGrade;
        }
        //otherwise look at both Gender and Grade Level
        else {
            return sameGender && sameGrade;
        }
    }

    /**
     * Advances the currentPick to the next one
     * @return true if there are no remaining picks to advance to
     */
    public boolean advancePick(){
        //MAIN ISSUE
        if(currentPick.index == application.pickCount() - 1){
            //the current pick is the last pick
            return true;
        }

        System.out.println(application.email);
        currentPick = application.getPick(currentPick.index + 1);
        System.out.println(application.getPicks().size());
        return false;
    }
    public int getBid(){
        return this.application.getPick(0).bid;
    }


    //protected for testing - ideally students should only be created internally in createStudents()
    //use MockStudent
    //made public to create course with students who didn't sign up - probably a better way to do this!
    public Student(Profile profile, Application application) {
        this.profile = profile;
        this.application = application;

        try{
            //add this student as an applicant to all courses
            for (int i = 0; i < application.pickCount(); i++) {
                application.getPick(i).course.addApplicant(this);
            }

            if (!application.isValidated()) {
                Warnings.logWarning("Created a student with unvalidated application data");
            }

            currentPick = application.getPick(0);
        } catch (NullPointerException e){
            //catch null pointer when mock students are used
            Warnings.logWarning(e.getMessage());
        }

        students.add(this);
    }
}
