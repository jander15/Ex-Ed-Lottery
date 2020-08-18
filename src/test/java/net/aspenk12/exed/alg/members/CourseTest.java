package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.util.CSV;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;
import static net.aspenk12.exed.alg.members.Course.*;


public class CourseTest {
    @Test
    public void testGetSpotsFromRow() {
        String[] row = {"SS", "Soul Surfers", "Kiffdawg", "1", "2", "3", "4", "5", "6", "7", "8"};

        Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8};

        Integer[] actual = Course.getSpotsFromRow(row);
        System.out.println(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSpotMap() {
        SpotMap spotMap = new SpotMap();

        spotMap.put(Grade.SENIOR, Gender.MALE, 12);
        spotMap.put(Grade.SENIOR, Gender.FEMALE, 15);
        spotMap.put(Grade.JUNIOR, Gender.MALE, 8);
        spotMap.put(Grade.JUNIOR, Gender.FEMALE, 9);

        assertEquals(spotMap.get(Grade.SENIOR, Gender.MALE), 12);
        assertEquals(spotMap.get(Grade.SENIOR, Gender.FEMALE), 15);
        assertEquals(spotMap.get(Grade.JUNIOR, Gender.MALE), 8);
        assertEquals(spotMap.get(Grade.JUNIOR, Gender.FEMALE), 9);
    }

    /**
     * Creates example instances of Course using test/resources/coursetest.csv
     */
    public static void makeExampleCourses(){
        File file = new File(CourseTest.class.getResource("/coursetest.csv").getFile());
        CSV csv = new CSV(file);
        Course.createCourses(csv);
    }
}