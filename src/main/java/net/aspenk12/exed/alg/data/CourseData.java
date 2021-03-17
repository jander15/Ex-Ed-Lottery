package net.aspenk12.exed.alg.data;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.alg.containers.Pick;
import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.alg.members.Student;

import java.util.*;

/**
 * Crunches all the numbers calculated on a course-by-course basis.
 */
public class CourseData {
    private final Course course;

    //average bid of all applicants
    private double avgBid;

    //average bid of students that ended up on the course
    private double avgPointExpenditure;

    //contains minimum bid per demographic for every trip
    private Map<Grade, Map<Gender, Integer>> demographicExpenditureMap = new HashMap<>();

    public CourseData(Course course) {
        this.course = course;
    }

    public void calculateAll(){
        calcAvgBid();
        calcAvgExpenditure();
        calcDemographicExpenditure();
    }

    /*test*/ void calcAvgBid(){
        Set<Student> applicants = course.getApplicants();

        int applicantSum = 0;
        for (Student s : applicants) {
            applicantSum += s.application.getPick(course).bid;
        }

        avgBid = applicantSum / ((double) applicants.size());
    }

    /*test*/ void calcAvgExpenditure(){
        Set<Student> applicants = course.getApplicants();

        int memberSum = 0;
        List<Student> members = course.getStudents();
        for (Student s : applicants) {
            memberSum += s.application.getPick(course).bid;
        }

        avgPointExpenditure = memberSum / ((double) members.size());
    }

    /*test*/ void calcDemographicExpenditure(){ //calculate bids to guarantee placement for each demographic------------------
        for (Grade grade : Grade.values()) {
            //submap in demographicExpenditureMap, maps gender to minimum point expenditure.
            Map<Gender, Integer> map = new HashMap<>();
            for (Gender gender : Gender.values()) {
                boolean full = (course.spotMap.getMaxSpots() == 0);
                boolean demographicFull = (course.spotMap.get(grade, gender) == 0);

                if(!full && !demographicFull){
                    //if there's a free spot on the trip for this student, the minimum bid is zero
                    map.put(gender, 0);
                    continue;
                }

                //subset contains the students placed on the trip that are relevant to the current grade/gender combination. These are people we can outbid for a spot.
                //our goal is to find the student with the minimum bid and figure out how much it would take to outbid them by 1
                Set<Student> subset;
                if (demographicFull) { //then we can only outbid students with the same demographic as us.
                    subset = new HashSet<>();

                    //find every student on this trip that matches our demographic.
                    for (Student s : course.getStudents()) {
                        if (s.isDemographic(grade, gender)) {
                            subset.add(s);
                        }
                    }
                } else {
                    //otherwise we can replace anybody on the trip
                    subset = new HashSet<>(course.getStudents());
                }

                //of this subset, what's the minimum bid?
                int min = findMinBid(subset, course);

                //we need to bid 1 point higher to guarantee an outbid
                map.put(gender, min + 1);
            }
            demographicExpenditureMap.put(grade, map);
        }
    }

    /*test*/ static int findMinBid(Set<Student> students, Course course){
        if(students.isEmpty()){
            throw new UnsupportedOperationException("Tried to find the minimum bid of an empty set of students");
        }

        int min = Integer.MAX_VALUE; //initialize min to max possible value, such that bid < min for basically all bid
        for (Student s : students) {
            int bid = s.application.getPick(course).bid;
            if(bid < min){
                min = bid;
            }
        }
        return min;
    }

    public Map<Grade, Map<Gender, Integer>> getDemographicExpenditureMap() {
        return demographicExpenditureMap;
    }

}
