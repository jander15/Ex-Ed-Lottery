package net.aspenk12.exed.alg.data;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.alg.members.Student;

import java.util.*;

/**
 * Crunches all the numbers calculated on a course-by-course basis.
 *
 * @author Alex Appleby
 */
public class CourseData {
    private final Course course;

    //average bid of all applicants
    private double avgBid;

    //average bid of students that ended up on the course
    private double avgPointExpenditure;

    //adjusted course demand index, course demand independent of size of trip, point bids, selectivity, etc.
    //this is only based on the order of picks, and does not take bid size or expenditure into account.
    //note that this metric is somewhat flawed, and can still be skewed by course value.
    private double acdi;

    //between 0 and 1;
    private double percentMale, percentFemale;

    //applicant map is for all applicants, student for students on the trip only.
    private Map<Grade, Double> applicantGradeMap;
    private Map<Grade, Double> studentGradeMap;


    //contains minimum bid per demographic for every trip
    private Map<Grade, Map<Gender, Integer>> demographicExpenditureMap = new HashMap<>();

    public CourseData(Course course) {
        this.course = course;
    }

    public void calculateAll(){
        calcAvgBid();
        calcAvgExpenditure();
        calcDemographicExpenditure();
        calcAcdi();
        calcGenderDist();
        applicantGradeMap = calcGradeDist(course.getApplicants());
        studentGradeMap = calcGradeDist(new HashSet<>(course.getStudents()));
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

    /**
     * @see ACDI
     */
    /*test*/ void calcAcdi(){
        Set<Student> applicants = course.getApplicants();

        int acdiSum = 0;

        for (Student s : applicants) {
            //index is the 'order' of this particular pick. 0 = 1st, 1 = 2nd, etc. etc.
            int index = s.application.getPick(course).index;
            acdiSum += ACDI.weigh(index);
        }

        //Normalize the ACDI by the amount of students in the applicant pool.
        //This allows ACDI readings to be compared year-to-year.
        acdi = acdiSum / (double) Student.getStudents().size();
    }

    /*test*/ void calcGenderDist(){
        int totalMales = 0, totalFemales = 0;

        for (Student s : course.getStudents()) {
            if(s.profile.getGender().equals(Gender.MALE)){
                totalMales++;
            } else {
                totalFemales++;
            }
        }

        double size = course.getStudents().size();
        percentMale = totalMales / size;
        percentFemale = totalFemales / size;
    }

    /**
     * returns a distribution of grade values from a set of students.
     * @return Maps grade value to percentage of student set. Percentage is double, value 0 to 1.
     */
    /*test*/ static Map<Grade, Double> calcGradeDist(Set<Student> students){
        Map<Grade, Integer> totalPerGrade = new HashMap<>();
        //initiate all values at 0;
        for (Grade g : Grade.values()) {
            totalPerGrade.put(g, 0);
        }

        for (Student student : students) {
            //increase by 1
            int total = totalPerGrade.get(student.profile.getGrade());
            totalPerGrade.put(student.profile.getGrade(), total + 1);
        }

        Map<Grade, Double> retVal = new HashMap<>();

        for (Grade g : Grade.values()) {
            int total = totalPerGrade.get(g);

            retVal.put(g, total / (double) students.size());
        }

        return retVal;
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

    public double getAcdi() {
        return acdi;
    }

    public double getPercentMale() {
        return percentMale;
    }

    public double getPercentFemale() {
        return percentFemale;
    }
}
