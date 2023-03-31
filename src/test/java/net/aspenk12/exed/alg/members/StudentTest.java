package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.alg.containers.Pick;
import net.aspenk12.exed.util.CSV;
import net.aspenk12.exed.util.ProfileLinkException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    @Test
    public void testCreateStudents() {
        CourseTest.makeExampleCourses();
        ProfileTest.makeExampleProfiles();
        makeExampleStudents();

        List<Student> students = Student.getStudents();


        //test alex's data because why not ig
        Student alex = students.get(0);

        assertEquals(alex.profile.getFirstName(), "Alex");
        assertEquals(alex.profile.getLastName(), "Appleby" );
        assertEquals(alex.profile.getId(), 11325);
        assertEquals(alex.profile.getGender(), Gender.X);
        assertEquals(alex.profile.getPoints(), 5);
        assertEquals(alex.profile.getGrade(), Grade.SENIOR);

        Pick firstPick = alex.application.getPick(0);

        //the fifth bid is actually the last one because one pick should be autoremoved
        Pick lastPick = alex.application.getPick(4);

        assertEquals(firstPick.course, Course.get("SS"));
        assertEquals(firstPick.bid, 5);

        assertEquals(lastPick.course, Course.get("ET"));
        assertEquals(lastPick.bid, 5);
    }

    @Test
    public void testLinkFailure(){
        CourseTest.makeExampleCourses();
        ProfileTest.makeExampleProfiles();

        //note id discrepancy against data in test profiles (actual id = 11325)
        try {
            Student.linkProfile("alexandera11235@aspenk12.net");
            fail(); //linkProfile() should fail
        } catch (ProfileLinkException e) {
            assertEquals(11235, e.id);
            assertEquals("alexandera11235@aspenk12.net", e.email);

            //exception expected, pass
        }
    }

    /**
     * Creates example instances of Profile using test/resources/application-test.csv
     */
    public static void makeExampleStudents(){
        File file = new File(StudentTest.class.getResource("/application-test.csv").getFile());

        try {
            Student.createStudents(new CSV(file));
        } catch (ProfileLinkException e) {
            e.printStackTrace();
        }
    }

}