package net.aspenk12.exed.alg.containers;

import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.alg.members.CourseTest;
import net.aspenk12.exed.alg.members.Profile;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class ApplicationTest {

    @Test
    public void testValidation() {
        CourseTest.makeExampleCourses();

        List<Course> previousCourses = new ArrayList<>();
        previousCourses.add(Course.get("SS"));
        previousCourses.add(Course.get("JH"));
        previousCourses.add(Course.get("CC"));

        Profile profile = new Profile(11325, "Alex", "Appleby", Gender.MALE, Grade.SENIOR,30, previousCourses);

        Application application = new Application(profile);
        application.addNewPick(Course.get("SS"), 10); //course already attended
        application.addNewPick(Course.get("ZC"), 15);
        application.addNewPick(Course.get("WE"), 6);
        application.addNewPick(Course.get("CT"), -10); //negative value
        application.addNewPick(Course.get("WE"), 12); //duplicate, should be removed
        application.addNewPick(Course.get("NM"), 2);
        application.addNewPick(Course.get("ET"), 50); //more than points, should be rounded down

        application.validate();

        assertEquals(Course.get("ZC"), application.getPick(0).course);
        assertEquals(15, application.getPick(0).bid);

        assertEquals(Course.get("CT"), application.getPick(2).course);
        assertEquals(0, application.getPick(2).bid);

        assertEquals(Course.get("NM"), application.getPick(3).course);
        assertEquals(2, application.getPick(3).bid);

        assertEquals(Course.get("ET"), application.getPick(4).course);
        assertEquals(30, application.getPick(4).bid);
    }
}