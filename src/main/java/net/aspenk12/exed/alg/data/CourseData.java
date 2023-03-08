package net.aspenk12.exed.alg.data;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.alg.members.Student;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

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

    private double percentFull;

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
        //calcDemographicExpenditure();
        calcAcdi();
        calcGenderDist();
        applicantGradeMap = calcGradeDist(course.getApplicants());
        studentGradeMap = calcGradeDist(new HashSet<>(course.getStudents()));
        calcPercentFull();
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
        int memberSum = 0;
        List<Student> members = course.getStudents();
        for (Student s : members) {
            memberSum += s.application.getPick(course).bid;
        }

        avgPointExpenditure = memberSum / ((double) members.size());
    }

    /*test*/ void calcDemographicExpenditure(){ //calculate bids to guarantee placement for each demographic------------------
        for (Grade grade : Grade.values()) {
            //submap in demographicExpenditureMap, maps gender to minimum point expenditure.
            Map<Gender, Integer> map = new HashMap<>();
            for (Gender gender : Gender.values()) {
                boolean full = (course.spotMap.getRemainingSpots() == 0);
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

        //Get the set of all students applying to the course we're analyzing.
        //Note the unordered nature of this collection.
        Set<Student> applicants = course.getApplicants();

        //Initialize an integer to contain the sum of all ACDI values
        int acdiSum = 0;

        //loop through every student applying to this course
        for (Student s : applicants) {
            //index is the 'order' of this particular pick. 0 = 1st, 1 = 2nd, etc. etc.
            int index = s.application.getPick(course).index;
            //refer to the ACDI class to get proper weighting, then add it to the sum
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

    /*test*/ void calcPercentFull(){
        percentFull = course.getStudents().size() / (double) course.spotMap.maxSpots;
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

    private BufferedImage makeGenderPieChart(){
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("% Male", percentMale);
        dataset.setValue("% Female", percentFemale);

        JFreeChart chart = ChartFactory.createPieChart(
                "Gender Distribution",   // chart title
                dataset,          // data
                true,             // include legend
                false,
                false);

        PiePlot<String> piePlot = (PiePlot<String>)chart.getPlot();
        piePlot.setSectionPaint("% Male", Color.BLUE);
        piePlot.setSectionPaint("% Female", Color.PINK);
        piePlot.setBackgroundAlpha(0);
        piePlot.setOutlineVisible(false);

        return chart.createBufferedImage(250, 200);
    }

    private BufferedImage makeGradePieChart(){
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("% 9th", applicantGradeMap.get(Grade.FRESHMAN));
        dataset.setValue("% 10th", applicantGradeMap.get(Grade.SOPHOMORE));
        dataset.setValue("% 11th", applicantGradeMap.get(Grade.JUNIOR));
        dataset.setValue("% 12th", applicantGradeMap.get(Grade.SENIOR));


        JFreeChart chart = ChartFactory.createPieChart(
                "Grade Distribution",   // chart title
                dataset,          // data
                true,             // include legend
                false,
                false);

        PiePlot<String> piePlot = (PiePlot<String>)chart.getPlot();
        piePlot.setBackgroundAlpha(0);
        piePlot.setOutlineVisible(false);

        return chart.createBufferedImage(250, 200);
    }

    /**
     * @return the PDF page for this specific course
     */
    public PDPage createPage(PDDocument doc) throws IOException {
        //instantiate a new blank PDF Page
        PDPage page = new PDPage();
        //instantiate a new contentStream to write content to the page,
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);

        //begin writing text
        contentStream.beginText();
        //set the font and font size
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
        //create a new line
        contentStream.newLineAtOffset(25, 730);
        //write the course name and ID at the top of the page.
        contentStream.showText(course.courseName + " <" + course.courseId + ">");

        //... more boilerplate formatting code omitted

        contentStream.setFont(PDType1Font.HELVETICA, 14);
        contentStream.newLineAtOffset(0, -25);
        contentStream.showText("Teacher(s): " + course.teachers);

        contentStream.newLineAtOffset(0, -25);
        contentStream.showText("Total Spots: " + course.spotMap.maxSpots);

        contentStream.newLineAtOffset(0, -25);

        String spotsFilled = String.format("Spots Filled: %d — %.1f%%", course.getStudents().size(), percentFull * 100.0);

        contentStream.showText(spotsFilled);

        contentStream.newLineAtOffset(30, -50);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);

        contentStream.showText("Minimum Bids to Guarantee Placement");
        contentStream.setFont(PDType1Font.HELVETICA, 14);

        contentStream.newLineAtOffset(40, -30);
        contentStream.setFont(PDType1Font.COURIER_BOLD, 14);

        for (Grade grade : Grade.values()) {
            for (Gender gender : Gender.values()) {
                int points = getDemographicExpenditureMap().get(grade).get(gender);

                contentStream.showText(String.valueOf(grade.gradeNum));
                contentStream.newLineAtOffset(20, 0);

                contentStream.showText(gender.name + ":");

                contentStream.newLineAtOffset(15, 0);
                contentStream.showText(String.valueOf(points));

                contentStream.newLineAtOffset(40, 0);
            }
            contentStream.newLineAtOffset(-150, -25);
        }

        contentStream.setFont(PDType1Font.COURIER_BOLD, 16);

        contentStream.newLineAtOffset(-40, -30);

        contentStream.showText(String.format("ACDI: %.3f", acdi));

        contentStream.newLineAtOffset(0, -30);
        contentStream.showText(String.format("Avg Bid: %.2f", avgBid));

        contentStream.newLineAtOffset(0, -30);
        contentStream.showText(String.format("Avg Expenditure: %.2f", avgPointExpenditure));

        contentStream.endText();

        //create pie charts
        PDImageXObject genderChart = JPEGFactory.createFromImage(doc, makeGenderPieChart());
        PDImageXObject gradeChart = JPEGFactory.createFromImage(doc, makeGradePieChart());

        //draw pie charts
        contentStream.drawImage(genderChart, 320, 550);
        contentStream.drawImage(gradeChart, 320, 325);

        contentStream.close();
        return page;
    }

}
