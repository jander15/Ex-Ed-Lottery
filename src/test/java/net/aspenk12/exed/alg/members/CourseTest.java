package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Application;
import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.alg.containers.SpotMap;
import net.aspenk12.exed.util.CSV;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import static net.aspenk12.exed.alg.containers.Gender.*;
import static net.aspenk12.exed.alg.containers.Grade.*;


import static org.junit.Assert.*;


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
        SpotMap spotMap = new SpotMap(10);

        spotMap.put(SENIOR, MALE, 12);
        spotMap.put(SENIOR, FEMALE, 15);
        spotMap.put(JUNIOR, MALE, 8);
        spotMap.put(JUNIOR, FEMALE, 9);

        assertEquals(spotMap.get(SENIOR, MALE), 12);
        assertEquals(spotMap.get(SENIOR, FEMALE), 15);
        assertEquals(spotMap.get(JUNIOR, MALE), 8);
        assertEquals(spotMap.get(JUNIOR, FEMALE), 9);

        assertEquals(spotMap.getMaxSpots(), 10);
    }

    @Test
    public void testExampleCourses(){
        makeExampleCourses();

        assertEquals(12, Course.courseCount());

        Course soulSurfers = Course.get("SS");

        assertEquals(soulSurfers.courseName, "Soul Surfers");
        assertEquals(soulSurfers.teachers, "Kiffdawg");
        assertEquals(soulSurfers.courseId, "SS");

        SpotMap spotMap = soulSurfers.spotMap;

        for (Map<Gender, Integer> subMap : spotMap.values()) {
            assertEquals(subMap.get(MALE).intValue(), 2);
            assertEquals(subMap.get(FEMALE).intValue(), 2);
        }

        assertEquals(10, spotMap.getMaxSpots());
    }

    @Test
    public void testAddStudentToEmptyCourse() {
        SpotMap spotMap = new SpotMap(10);
        spotMap.put(SENIOR, FEMALE, 5);

        Course cataractCanyon = new Course("Cataract", "CC", "Various",  spotMap);

        Profile profile = new Profile(11223, "Stef", "Wojick", FEMALE, SENIOR, 21, 66, new ArrayList<>());
        Application app = new Application(profile);
        app.addNewPick(cataractCanyon, 1);
        Student stef = new Student(profile, app);

        Student retVal = cataractCanyon.placeStudent(stef);

        assertNull(retVal);

        assertTrue(cataractCanyon.getStudents().contains(stef));
        assertEquals(cataractCanyon.spotMap.getMaxSpots(), 9);
        assertEquals(cataractCanyon.spotMap.get(SENIOR, FEMALE), 4);
    }

    @Test
    public void testAddStudentToFullCourse() {

        //note zero max spots
        SpotMap spotMap = new SpotMap(0);
        spotMap.put(SENIOR, FEMALE, 5);

        Course cataractCanyon = new Course("Cataract", "CC", "Various",  spotMap);

        Profile profile = new Profile(11223, "Stef", "Wojick", FEMALE, SENIOR, 21, 66, new ArrayList<>());
        Application app = new Application(profile);
        app.addNewPick(cataractCanyon, 1);
        Student stef = new Student(profile, app);

        Student retVal = cataractCanyon.placeStudent(stef);

        assertFalse(cataractCanyon.getStudents().contains(stef));
        assertEquals(retVal, stef);
        assertEquals(cataractCanyon.spotMap.getMaxSpots(), 0);
        assertEquals(cataractCanyon.spotMap.get(SENIOR, FEMALE), 5);
    }

    @Test
    //stef should get outbid by chloe on cataract canyon because she placed a higher bid
    public void testOutbidStudent() {
        SpotMap spotMap = new SpotMap(10);

        //note, only one spot for senior girls
        spotMap.put(SENIOR, FEMALE, 1);

        Course cataractCanyon = new Course("Cataract", "CC", "Various",  spotMap);

        Profile profileStef = new Profile(11223, "Stef", "Wojick", FEMALE, SENIOR, 21, 66, new ArrayList<>());
        Application appStef = new Application(profileStef);
        appStef.addNewPick(cataractCanyon, 1);
        Student stef = new Student(profileStef, appStef);

        Profile profileChloe = new Profile(11567, "Chloe", "Springfield", FEMALE, SENIOR, 14, 21, new ArrayList<>());
        Application appChloe = new Application(profileChloe);
        appChloe.addNewPick(cataractCanyon, 5);
        Student chloe = new Student(profileChloe, appChloe);

        Student retVal1 = cataractCanyon.placeStudent(stef);
        assertNull(retVal1);

        Student retVal2 = cataractCanyon.placeStudent(chloe);
        assertEquals(stef, retVal2);

        assertFalse(cataractCanyon.getStudents().contains(stef));
        assertTrue(cataractCanyon.getStudents().contains(chloe));

        assertEquals(cataractCanyon.spotMap.getMaxSpots(), 9);
        assertEquals(cataractCanyon.spotMap.get(SENIOR, FEMALE), 0);
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