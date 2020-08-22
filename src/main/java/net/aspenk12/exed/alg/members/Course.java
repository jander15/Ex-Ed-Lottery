package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.alg.containers.SpotMap;
import net.aspenk12.exed.util.CSV;
import net.aspenk12.exed.util.Util;

import java.util.*;

/**
 * Instances of course are static and created all at once upon createCourses().
 * Course acts much like a dynamic enum, where every instance exists in a static context with its properties attached
 */
public class Course {
    private static Map<String, Course> courses = new HashMap<>();

    public final String courseName;
    public final String courseId;
    public final String teachers;
    public final SpotMap spotMap;

    private final List<Student> students =  new ArrayList<>();

    public Course(String courseName, String courseId, String teachers, SpotMap spotMap) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.teachers = teachers;
        this.spotMap = spotMap;

        courses.put(courseId, this);
    }

    public static void createCourses(CSV csv){
        if(!courses.isEmpty()) return; //courses should only be created once

        for (int i = 0; i < csv.rows(); i++) {
            String[] row = csv.get(i);

            String courseId = row[0];
            String courseName = row[1];
            String teachers = row[2];

            String maxSpotString = row[11];
            int maxSpots = Util.parseIntCSV(maxSpotString, csv, i, 11);

            SpotMap spotMap = new SpotMap(maxSpots);

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
        }
    }

    /**
     * Takes a row from a course csv, cuts out the data with the course spots,
     * casts, then returns it as a smaller array of ints.
     */
    //todo make this return primitive data? maybe even find a better way to do this? idk
    /*protected for testing*/ static Integer[] getSpotsFromRow(String[] row){
        ArrayList<Integer> spotCounts = new ArrayList<>();
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
     * Attempts to place a student on this trip.
     *
     * @return The outbid student in the case of a bidding war. Otherwise, this method returns null.
     */
    /*Algorithm*/ Student placeStudent(Student student){
        Profile profile = student.profile;
        if(spotMap.getMaxSpots() > 0){
            if(spotMap.get(profile.grade, profile.gender) > 0){
                addStudent(student);
                return null;
            }
        }

        for (int i = 0; i < students.size(); i++) {
            Student otherStudent = students.get(i);

            //if the other student is the same demographic as this one, they can be outbid
            if (otherStudent.sameDemographic(student)){
                int bid = student.currentPick.bid;
                int otherBid = otherStudent.currentPick.bid;

                if(bid == otherBid){
                    Profile otherProfile = otherStudent.profile;
                    if(profile.lottoNumber > otherProfile.lottoNumber){
                        return otherStudent;
                    }
                } else if (bid > otherBid){
                    students.set(i, student);
                    return otherStudent;
                }
            }
        }

        //if we got through the loop, the student didn't make it onto the trip.
        return student;
    }

    private void addStudent(Student s) {
        students.add(s);
        spotMap.takeSpot(s);
    }

    /**
     * Gets an instance of course using the course ID.
     */
    public static Course get(String ID){
        return courses.get(ID);
    }

    public List<Student> getStudents(){
        return students;
    }

    /**
     * @return the number of existing courses (instances of course)
     */
    public static int courseCount(){
        return courses.size();
    }

    public static Map<String, Course> getCourses(){
        return courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        return courseId != null ? courseId.equals(course.courseId) : course.courseId == null;
    }

}
