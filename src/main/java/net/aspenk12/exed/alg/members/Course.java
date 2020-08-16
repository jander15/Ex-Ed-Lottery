package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.util.CSV;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Instances of course are static and created all at once upon createCourses().
 * Course acts much like a dynamic enum, where every instance exists in a static context with its properties attached
 */
public class Course {
    private static HashMap<String, Course> courses;

    public final String courseName;
    public final String courseId;
    public final String teachers;
    public final SpotMap spotMap;

    public Course(String courseName, String courseId, String teachers, SpotMap spotMap) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.teachers = teachers;
        this.spotMap = spotMap;

        courses.put(courseId, this);
    }

    public static void createCourses(CSV csv){
        if(courses != null) return; //courses should only be created once

        courses = new HashMap<>();

        for (int i = 0; i < csv.rows(); i++) {
            String[] row = csv.get(i);

            //todo align this setup with the formatting of the actual csv
            String courseId = row[0];
            String courseName = row[1];
            String teachers = row[2];

            SpotMap spotMap = new SpotMap();

            Integer[] spots = getSpotsFromRow(row);

            //this is honestly the most practical way to fill the map, shut
            spotMap.put(Grade.SENIOR, Gender.MALE, spots[0]);
            spotMap.put(Grade.SENIOR, Gender.FEMALE, spots[1]);
            spotMap.put(Grade.JUNIOR, Gender.MALE, spots[2]);
            spotMap.put(Grade.JUNIOR, Gender.FEMALE, spots[3]);
            spotMap.put(Grade.SOPHOMORE, Gender.MALE, spots[4]);
            spotMap.put(Grade.SOPHOMORE, Gender.FEMALE, spots[5]);
            spotMap.put(Grade.FRESHMAN, Gender.MALE, spots[6]);
            spotMap.put(Grade.FRESHMAN, Gender.FEMALE, spots[7]);

            new Course(courseName, courseId, teachers, spotMap);

            //todo write a test for createCourses()
        }
    }

    /**
     * Custom double-hashmap specifically for managing spots on a trip
     */
    public static class SpotMap extends HashMap<Grade, HashMap<Gender, Integer>>{
        /*protected for testing*/ void put(Grade grade, Gender gender, int spots){
            HashMap<Gender, Integer> genderMap = get(grade);

            if(genderMap == null){
                genderMap = new HashMap<>();
                put(grade, genderMap);
            }
            //if gendermap exists, it's already in the encompassing map
            genderMap.put(gender, spots);
        }

        public int get(Grade grade, Gender gender){
            return get(grade).get(gender);
        }
    }

    /**
     * Takes a row from a course csv, cuts out the data with the course spots,
     * casts, then returns it as a smaller array of ints.
     */
    //todo make this return primitive data?
    /*protected for testing*/ static Integer[] getSpotsFromRow(String[] row){
        ArrayList<Integer> spotCounts = new ArrayList<>();
        //todo align this setup with the formatting of the actual csv
        String[] spots = Arrays.copyOfRange(row, 3, 11);
        for (String spot : spots) {
            int spotCount = Integer.parseInt(spot);
            spotCounts.add(spotCount);
        }

        Integer[] retVal = new Integer[spotCounts.size()];
        spotCounts.toArray(retVal);
        return retVal;
    }

    /**
     * @return the number of existing courses (instances of course)
     */
    public static int courseCount(){
        return courses.size();
    }
}
