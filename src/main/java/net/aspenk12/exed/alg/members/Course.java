package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.alg.containers.Pick;
import net.aspenk12.exed.alg.containers.SpotMap;
import net.aspenk12.exed.util.CSV;
import net.aspenk12.exed.util.Util;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

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

    //todo consider making this a Set instead of a List
    private final List<Student> students =  new ArrayList<>();

    //applicants is the set of students applying to a course, NOT the students that get in.
    //this is really only used for calculating course data
    private Set<Student> applicants = new HashSet<>();

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

            String maxSpotString = row[3];
            int maxSpots = Util.parseIntCSV(maxSpotString, csv, i, 3);

            SpotMap spotMap = new SpotMap(maxSpots);

            Integer[] spots = getSpotsFromRow(row);

            //this is honestly the most practical way to fill the map, shut!
            spotMap.put(Grade.SENIOR, Gender.X, spots[0]);
            spotMap.put(Grade.JUNIOR, Gender.X, spots[1]);
            spotMap.put(Grade.SOPHOMORE, Gender.X, spots[2]);
            spotMap.put(Grade.FRESHMAN, Gender.X, spots[3]);

            spotMap.put(Grade.SENIOR, Gender.MALE, spots[4]);
            spotMap.put(Grade.SENIOR, Gender.FEMALE, spots[5]);
            spotMap.put(Grade.JUNIOR, Gender.MALE, spots[6]);
            spotMap.put(Grade.JUNIOR, Gender.FEMALE, spots[7]);
            spotMap.put(Grade.SOPHOMORE,Gender.MALE,spots[8]);
            spotMap.put(Grade.SOPHOMORE,Gender.FEMALE,spots[9]);
            spotMap.put(Grade.FRESHMAN,Gender.MALE,spots[10]);
            spotMap.put(Grade.FRESHMAN,Gender.FEMALE,spots[11]);

            //System.out.println(courseName + spotMap);

            new Course(courseName, courseId, teachers, spotMap);
        }
    }

    public void removeRestrictionsFromSpotMap(){
        int spotsLeft = spotMap.getRemainingSpots();
        spotMap.put(Grade.SENIOR, Gender.X, spotsLeft);
        spotMap.put(Grade.JUNIOR, Gender.X, spotsLeft);
        spotMap.put(Grade.SOPHOMORE, Gender.X, spotsLeft);
        spotMap.put(Grade.FRESHMAN, Gender.X, spotsLeft);

        spotMap.put(Grade.SENIOR, Gender.MALE, spotsLeft);
        spotMap.put(Grade.SENIOR, Gender.FEMALE, spotsLeft);
        spotMap.put(Grade.JUNIOR, Gender.MALE, spotsLeft);
        spotMap.put(Grade.JUNIOR, Gender.FEMALE, spotsLeft);
        spotMap.put(Grade.SOPHOMORE,Gender.MALE,spotsLeft);
        spotMap.put(Grade.SOPHOMORE,Gender.FEMALE,spotsLeft);
        spotMap.put(Grade.FRESHMAN,Gender.MALE,spotsLeft);
        spotMap.put(Grade.FRESHMAN,Gender.FEMALE,spotsLeft);
    }

    /**
     * Takes a row from a course csv, cuts out the data with the course spots,
     * casts, then returns it as a smaller array of ints.
     */
    //todo make this return primitive data? maybe even find a better way to do this? idk
    /*protected for testing*/ static Integer[] getSpotsFromRow(String[] row){
        ArrayList<Integer> spotCounts = new ArrayList<>();
        String[] spots = Arrays.copyOfRange(row, 4, 16);
        for (String spot : spots) {
            int spotCount = Integer.parseInt(spot);
            spotCounts.add(spotCount);
        }

        Integer[] retVal = new Integer[spotCounts.size()];
        spotCounts.toArray(retVal);
        return retVal;
    }
    /**

    /**
     * Attempts to place a student on this trip.
     *
     * @return The outbid student in the case of a bidding war. Otherwise, this method returns null.
     */
    public Student placeStudent(Student student){
        Profile profile = student.profile;

        //check total spots
        if(spotMap.getRemainingSpots() > 0) {

            //check grade level spots - this goes into the spotMap as Gender X
            if (spotMap.get(profile.grade, Gender.X) > 0) {

                if (profile.gender.equals(Gender.X)) {
                    addStudent(student);
                    return null;
                }
                //check gender-grade level spots if student gender is M or F
                else if (spotMap.get(profile.getGrade(), profile.getGender()) > 0) {
                    addStudent(student);
                    return null;
                }
            }
        }
        // a student can still get on the course by replacing another student if they meet certain conditions
                for (int i = 0; i < students.size(); i++) {
                    Student otherStudent = students.get(i);

                    //Only replace student's if they have not been placed on the course.
                    if (otherStudent.profile.placedCourse.isEmpty()) {
                        //if this student is demographically limited, they can only replace students of the same dem.
                        //otherwise, they can replace anyone

                        int gradeLevelSpots = spotMap.get(student.profile.grade, Gender.X);
                        int demographicSpots = spotMap.get(student.profile.grade, student.profile.gender);
                        boolean notLimited = gradeLevelSpots > 0 && demographicSpots > 0;
                        boolean sameGradeLevel = student.profile.grade.equals(otherStudent.profile.grade);
                        boolean canCompeteWithinGradeLevel = sameGradeLevel && demographicSpots > 0;
                        boolean canCompete = (otherStudent.unlucky==student.unlucky);

                        if (canCompete && (notLimited || canCompeteWithinGradeLevel || otherStudent.sameDemographic(student))) {
                            int bid = student.currentPick.bid;
                            int otherBid = otherStudent.currentPick.bid;

                            boolean largerBid = (bid > otherBid);
                            boolean equalBid = (bid == otherBid);
                            boolean betterTieBreaker = (profile.getLottoNumber() < otherStudent.profile.getLottoNumber());

                            if (largerBid || (equalBid && betterTieBreaker)) {
                                replaceStudent(student, otherStudent, i);
                                return otherStudent;
                            }
                        }
                    }
                }

        //if we got through the loop, the student didn't make it onto the trip.
        return student;
    }

    private void replaceStudent(Student student, Student otherStudent, int i){
        //if the students are the same demographic, you don't have to make any changes.
        if(!otherStudent.sameDemographic(student)) {
            spotMap.addSpot(otherStudent);
            spotMap.takeSpot(student);
        }
        students.set(i, student);

    }

    public void addStudent(Student s) {
        students.add(s);
        spotMap.takeSpot(s);
    }

    /**
     * Only to be used to add students to "unlucky" and "NoSignUp" Courses
     * @param s
     */
    public void forceAddStudent(Student s){
        students.add(s);
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

    public static int findTotalSpots(){
        int total = 0;
        for (Course c : courses.values()) {
            total += c.spotMap.getRemainingSpots();
        }
        return total;
    }

    public void addApplicant(Student student){
        applicants.add(student);
    }

    public Set<Student> getApplicants(){
        return applicants;
    }

    public static Map<String, Course> getCourses() {
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
