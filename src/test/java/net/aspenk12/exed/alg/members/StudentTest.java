package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.alg.containers.Pick;
import net.aspenk12.exed.util.CSV;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class StudentTest {

    @Test
    public void testCreateStudents() {
        CourseTest.makeExampleCourses();
        makeExampleProfiles();
        makeExampleStudents();

        List<Student> students = Student.getStudents();


        //test alex's data because why not ig
        Student alex = students.get(0);

        assertEquals(alex.profile.firstName, "Alex");
        assertEquals(alex.profile.lastName, "Appleby" );
        assertEquals(alex.profile.id, 11325);
        assertEquals(alex.profile.gender, Gender.MALE);
        assertEquals(alex.profile.points, 5);
        assertEquals(alex.profile.grade, Grade.SENIOR);

        Pick firstPick = alex.application.getPick(0);

        //the fifth bid is actually the last one because one pick should be autoremoved
        Pick lastPick = alex.application.getPick(4);

        assertEquals(firstPick.course, Course.get("SS"));
        assertEquals(firstPick.bid, 5);

        assertEquals(lastPick.course, Course.get("ET"));
        assertEquals(lastPick.bid, 5);
    }

    /**
     * Creates example instances of Profile using test/resources/validatedtest.csv
     */
    public static void makeExampleProfiles(){
        File file = new File(StudentTest.class.getResource("/validatedtest.csv").getFile());
        Profile.createProfiles(new CSV(file));
    }

    /**
     * Creates example instances of Profile using test/resources/studenttest.csv
     */
    public static void makeExampleStudents(){
        File file = new File(StudentTest.class.getResource("/studenttest.csv").getFile());
        Student.createStudents(new CSV(file));
    }

}