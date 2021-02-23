package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.aspenk12.exed.alg.containers.Gender.*;
import static net.aspenk12.exed.alg.containers.Grade.*;
import static org.junit.Assert.*;

public class AlgorithmTest {
    @Test
    public void testSimpleAlgorithm() {

        //####### MAKE COURSE #######
        //note 3 total spots
        SpotMap spotMap = new SpotMap(3);

        //put 1 spot for every demographic
        for (Grade grade : Grade.values()) {
            spotMap.put(grade, MALE, 1);
            spotMap.put(grade, FEMALE, 1);

        }

        Course blackHills = new Course("Black Hills", "BH", "var", spotMap);

        Map<String, Course> courses = new HashMap<>();
        courses.put(blackHills.courseId, blackHills);

        //####### MAKE STUDENTS #######

        //3pts, sophomore male
        Student dillon = createSimpleStudent(12893, "Dillon", "Voorn", MALE, SOPHOMORE,
                14, 291, new Pick(blackHills, 3, 0));

        //11pts, senior male
        Student dyer = createSimpleStudent(11321, "Dyer", "Hunting", MALE, SENIOR,
                33, 433, new Pick(blackHills, 11, 0));

        //5pts, senior female
        Student tatum = createSimpleStudent(15136, "Tatum", "Johnson", FEMALE, SENIOR,
                25, 91, new Pick(blackHills, 5, 0));

        //9pts, sophomore female
        Student lexi = createSimpleStudent(12789, "Lexi", "CP", FEMALE, SOPHOMORE,
                9, 443, new Pick(blackHills, 9, 0));

        //13pts, freshman male
        Student andrew = createSimpleStudent(16896, "Andrew", "Perley", MALE, FRESHMAN,
                15, 2, new Pick(blackHills, 13, 0));

        //10pts, senior male, should conflict with dyer and not be placed
        Student lars = createSimpleStudent(16896, "Lars", "Palmeroy", MALE, SENIOR,
                19, 88, new Pick(blackHills, 10, 0));

        List<Student> students = new ArrayList<>();

        students.add(dillon);
        students.add(dyer);
        students.add(tatum);
        students.add(lexi);
        students.add(andrew);
        students.add(lars);

        //highest bidders are andrew, dyer, lars and lexi.
        //because lars and dyer are the same demographic, lars should be outbid and lexi should be on

        Algorithm alg = new Algorithm(courses, students);
        alg.run();

        //course should be full
        assertEquals(0, blackHills.spotMap.getMaxSpots());

        List<Student> courseList = blackHills.getStudents();

        assertTrue(courseList.contains(andrew));
        assertTrue(courseList.contains(dyer));
        assertTrue(courseList.contains(lexi));

        assertFalse(courseList.contains(lars));
        assertFalse(courseList.contains(tatum));
        assertFalse(courseList.contains(dillon));

        List<Student> unlucky = alg.getUnlucky();

        assertTrue(unlucky.contains(lars));
        assertTrue(unlucky.contains(tatum));
        assertTrue(unlucky.contains(dillon));
    }

    /**
     * Create a simple student with only one pick and no previous courses.
     */
    private Student createSimpleStudent(int id, String fn, String ln, Gender g, Grade gr, int pts, int lotto, Pick p){
        List<Course> empty = new ArrayList<>();
        Profile profile = new Profile(id, fn, ln, g, gr, pts, lotto, empty);

        Application application = new Application(profile, "johndoe@example.com");
        application.addNewPick(p.course, p.bid);

        return new Student(profile, application);
    }
}