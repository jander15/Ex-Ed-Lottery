package net.aspenk12.exed.ui;

import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import net.aspenk12.exed.alg.containers.Pick;
import net.aspenk12.exed.alg.data.CourseData;
import net.aspenk12.exed.alg.members.Algorithm;
import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.alg.members.Student;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Pane that saves the course rosters after the lottery is finished.
 */

public class SavePane extends MainPane {

    public SavePane() {
        super("Save the Course Rosters","Save", "Status: Waiting to specify a save location");
    }

    @Override
    protected void run(){
        int year = Calendar.getInstance().get(Calendar.YEAR);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("exed-course-rosters-" + year + "-UNREVIEWED.xlsx");

        File f = fileChooser.showSaveDialog(MainWindow.getStage());

        //System.out.println(f);

        Workbook workbook = new XSSFWorkbook();

        //PDDocument doc = new PDDocument();

        Sheet sheet = workbook.createSheet("All Courses");

        Row labelRow = sheet.createRow(0);
        labelRow.createCell(0).setCellValue("Code");
        labelRow.createCell(1).setCellValue("Student ID");
        labelRow.createCell(2).setCellValue("Email");
        labelRow.createCell(3).setCellValue("First Name");
        labelRow.createCell(4).setCellValue("Last Name");
        labelRow.createCell(5).setCellValue("Gender");
        labelRow.createCell(6).setCellValue("Grade");
        labelRow.createCell(7).setCellValue("Bid");
        labelRow.createCell(8).setCellValue("ExEd Points");
        labelRow.createCell(9).setCellValue("TieBreaker Number");

        int count=2;

        for (Course c : Course.getCourses().values()) {
            System.out.println(c.courseName);

            //System.out.println(c.getStudents().size());

            for (int i = 0; i < c.getStudents().size(); i++) {
                Student student = c.getStudents().get(i);

                //note we start on the third row here
                Row row = sheet.createRow(count);
                count++;
                row.createCell(0).setCellValue(c.courseId);
                row.createCell(1).setCellValue(student.profile.getId());
                row.createCell(2).setCellValue(student.profile.getEmail());
                row.createCell(3).setCellValue(student.profile.getFirstName());
                row.createCell(4).setCellValue(student.profile.getLastName());
                row.createCell(5).setCellValue(student.profile.getGender().name);
                row.createCell(6).setCellValue(student.profile.getGrade().gradeNum);
                if(!c.courseId.equals("zz2ndLotto")&&!c.courseId.equals("zzNoSignUp")&&student.profile.getPlacedCourse().isEmpty()) {
                    Pick p = student.application.getPick(c);
                    row.createCell(7).setCellValue(p.bid);
                    row.createCell(10).setCellValue(p.index + 1);
                }
                if(!c.courseId.equals("zzNoSignUp")&&student.profile.getPlacedCourse().isEmpty()){
                List<Pick> picks = student.application.getPicks();
                    for (int j = 0; j < picks.size(); j++) {
                        row.createCell(11+2*j).setCellValue(picks.get(j).course.courseName);
                        row.createCell(12+2*j).setCellValue(picks.get(j).bid);
                    }
                    }
                row.createCell(8).setCellValue(student.profile.getPoints());
                row.createCell(9).setCellValue(student.profile.getLottoNumber());

            }
        }

            //CourseData courseData = new CourseData(c);
            //courseData.calculateAll();
            //try {
                //doc.addPage(courseData.createPage(doc));

            //} catch (IOException e) {

               // ErrorAlert.throwErrorWindow(e);




        try {

            FileOutputStream fos = new FileOutputStream(f);
            workbook.write(fos);

            statusText.setText("Status: Unreviewed course rosters saved to: " + f.getAbsolutePath());
            statusText.setFill(Color.GREEN);



            String pdfLoc = f.getParent() + "/data.pdf";
            System.out.println(pdfLoc);
            //doc.save(pdfLoc);

            //System.out.println(pdfLoc);
        } catch (IOException e) {
            ErrorAlert.throwErrorWindow(e);
            statusText.setText("Status: Failed to write file");
        }
    }
}
