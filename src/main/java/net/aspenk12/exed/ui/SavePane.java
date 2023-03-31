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

        for (Course c : Course.getCourses().values()) {
            System.out.println(Course.getCourses().values());
            //leave row 1 empty for aesthetics and stuff
            Row labelRow = sheet.createRow(0);
            labelRow.createCell(0).setCellValue("Student ID");
            labelRow.createCell(1).setCellValue("Email");
            labelRow.createCell(2).setCellValue("First Name");
            labelRow.createCell(3).setCellValue("Last Name");
            labelRow.createCell(4).setCellValue("Gender");
            labelRow.createCell(5).setCellValue("Grade");
            labelRow.createCell(6).setCellValue("Bid");


            for (int i = 0; i < c.getStudents().size(); i++) {
                Student student = c.getStudents().get(i);


                //note we start on the fourth row here
                Row row = sheet.createRow(i + 3);
                row.createCell(0).setCellValue(c.courseId);
                row.createCell(1).setCellValue(student.profile.getId());
                row.createCell(2).setCellValue(student.application.email);
                row.createCell(3).setCellValue(student.profile.getFirstName());
                row.createCell(4).setCellValue(student.profile.getLastName());
                row.createCell(5).setCellValue(student.profile.getGender().name);
                row.createCell(6).setCellValue(student.profile.getGrade().gradeNum);
                //System.out.println(i+"i");
                Pick p = student.application.getPick(c);
                row.createCell(6).setCellValue(p.bid);
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
