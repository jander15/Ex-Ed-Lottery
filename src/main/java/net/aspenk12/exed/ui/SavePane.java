package net.aspenk12.exed.ui;

import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import net.aspenk12.exed.alg.data.CourseData;
import net.aspenk12.exed.alg.members.Algorithm;
import net.aspenk12.exed.alg.members.Course;
import net.aspenk12.exed.alg.members.Student;
import org.apache.pdfbox.pdmodel.PDDocument;
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

        Workbook workbook = new XSSFWorkbook();

        PDDocument doc = new PDDocument();
        for (Course c : Course.getCourses().values()) {
            c.writeSheet(workbook);

            CourseData courseData = new CourseData(c);
            courseData.calculateAll();
            try {
                doc.addPage(courseData.createPage(doc));
            } catch (IOException e) {
                ErrorAlert.throwErrorWindow(e);
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(f);
            workbook.write(fos);

            statusText.setText("Status: Unreviewed course rosters saved to: " + f.getAbsolutePath());
            statusText.setFill(Color.GREEN);

            String pdfLoc = f.getParent() + "/data.pdf";

            doc.save(pdfLoc);

            System.out.println(pdfLoc);
        } catch (IOException e) {
            ErrorAlert.throwErrorWindow(e);
            statusText.setText("Status: Failed to write file");
        }
    }
}
