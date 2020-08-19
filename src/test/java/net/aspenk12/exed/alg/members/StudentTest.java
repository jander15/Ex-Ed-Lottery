package net.aspenk12.exed.alg.members;

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

        //todo finish this test
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