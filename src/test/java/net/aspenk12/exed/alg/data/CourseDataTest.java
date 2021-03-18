package net.aspenk12.exed.alg.data;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.alg.containers.MockApplication;
import net.aspenk12.exed.alg.containers.SpotMap;
import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.alg.members.MockStudent;
import net.aspenk12.exed.alg.members.Student;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class CourseDataTest {

    @Test
    public void testMinPointsSimple(){
        //on a course with 1 spot for every demographic, 1 member, and 1 total spot, everyone only needs to outbid that person by 1 point.
        SpotMap spotMap = new SpotMap(1);
        spotMap.put(Grade.SENIOR, Gender.MALE, 1);
        spotMap.put(Grade.SENIOR, Gender.FEMALE, 1);
        spotMap.put(Grade.JUNIOR, Gender.MALE, 1);
        spotMap.put(Grade.JUNIOR, Gender.FEMALE, 1);
        spotMap.put(Grade.SOPHOMORE, Gender.MALE, 1);
        spotMap.put(Grade.SOPHOMORE, Gender.FEMALE, 1);
        spotMap.put(Grade.FRESHMAN, Gender.MALE, 1);
        spotMap.put(Grade.FRESHMAN, Gender.FEMALE, 1);

        Course course = new Course("someCourse", "SC", "jander", spotMap);

        //note bid = 5
        MockStudent mockStudent = new MockStudent(new MockApplication(course, 5));
        mockStudent.getProfile().setGrade(Grade.SENIOR);
        mockStudent.getProfile().setGender(Gender.MALE);

        course.placeStudent(mockStudent);
        CourseData courseData = new CourseData(course);

        courseData.calcDemographicExpenditure();

        for (Grade grade : Grade.values()) {
            for (Gender gender : Gender.values()) {
                int pointsToOutbid = courseData.getDemographicExpenditureMap().get(grade).get(gender);
                //note bid + 1
                assertEquals(6, pointsToOutbid);
            }
        }
    }

    @Test
    public void testMinPoints(){
        /*
        on a course with 1 spot for every demographic, 1 member, and 2 total spots,
        most people can bid 0 to get on. However, for someone with the same demographic
        as the one person on the trip, they have to outbid that member by 1 to get on the trip
        */
        SpotMap spotMap = new SpotMap(2);
        spotMap.put(Grade.SENIOR, Gender.MALE, 1);
        spotMap.put(Grade.SENIOR, Gender.FEMALE, 1);
        spotMap.put(Grade.JUNIOR, Gender.MALE, 1);
        spotMap.put(Grade.JUNIOR, Gender.FEMALE, 1);
        spotMap.put(Grade.SOPHOMORE, Gender.MALE, 1);
        spotMap.put(Grade.SOPHOMORE, Gender.FEMALE, 1);
        spotMap.put(Grade.FRESHMAN, Gender.MALE, 1);
        spotMap.put(Grade.FRESHMAN, Gender.FEMALE, 1);

        Course course = new Course("someCourse", "SC", "jander", spotMap);

        //note bid = 5
        MockStudent mockStudent = new MockStudent(new MockApplication(course, 5));

        //note grade and gender
        mockStudent.getProfile().setGrade(Grade.SOPHOMORE);
        mockStudent.getProfile().setGender(Gender.FEMALE);

        course.placeStudent(mockStudent);

        CourseData courseData = new CourseData(course);

        courseData.calcDemographicExpenditure();

        for (Grade grade : Grade.values()) {
            for (Gender gender : Gender.values()) {
                int pointsToOutbid = courseData.getDemographicExpenditureMap().get(grade).get(gender);

                if(grade.equals(Grade.SOPHOMORE) && gender.equals(Gender.FEMALE)){
                    //note bid + 1
                    assertEquals(6, pointsToOutbid);
                } else {
                    assertEquals(0, pointsToOutbid);
                }
            }
        }
    }

    @Test
    public void testAcdi() {
        Course course = new Course("Kataract Kanyon", "KK", "Joe Mama", null);
        Course otherCourse = new Course("other", "OT", "Jim Nasium", null);

        //make some mock students and stuff
        MockApplication ma1 = new MockApplication(otherCourse, 69);
        //second pick
        ma1.addNewPick(course, 30);
        Student s1 = new MockStudent(ma1);

        //first pick
        Student s2 = new MockStudent(new MockApplication(course, 0));

        MockApplication ma2 = new MockApplication(otherCourse, 69);

        //twelth pick
        for (int i = 0; i < 10; i++) {
            ma2.addNewPick(otherCourse, -420);
        }

        ma2.addNewPick(course, 1);
        Student s3 = new MockStudent(ma2);

        course.addApplicant(s1);
        course.addApplicant(s2);
        course.addApplicant(s3);

        CourseData courseData = new CourseData(course);

        courseData.calcAcdi();

        //first pick = 6 acdi; second pick = 4 acdi; 12th pick = 1 acdi; = 11 acdi in total, divided by number of students (3)
        assertEquals(11.0 / 3.0, courseData.getAcdi(), 0.00000001);
    }

    @Test
    public void testFindMinBid(){
        Course course = new Course("someCourse", "SC", null, null);
        MockStudent s4 = new MockStudent(new MockApplication(course, 4));
        MockStudent s3 = new MockStudent(new MockApplication(course, 3));
        MockStudent s1 = new MockStudent(new MockApplication(course, 1));
        MockStudent s2 = new MockStudent(new MockApplication(course, 2));

        Set<Student> set = new HashSet<>();
        set.add(s1);
        set.add(s2);
        set.add(s3);
        set.add(s4);

        int min = CourseData.findMinBid(set, course);

        assertEquals(1, min);
    }
}