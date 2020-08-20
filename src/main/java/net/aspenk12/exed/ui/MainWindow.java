package net.aspenk12.exed.ui;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * The main window of the application, contains all functioning panes n stuff
 * @author Alex Appleby
 */
public class MainWindow {
    private static Stage mainStage;

    public static void launch(){
        mainStage = new Stage();
        //mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.setResizable(false);

        HBox hLayout = new HBox();
        hLayout.getChildren().add(new CoursePane());
        hLayout.getChildren().add(new Separator(Orientation.VERTICAL));
        hLayout.getChildren().add(new StudentPane());
        hLayout.getChildren().add(new Separator(Orientation.VERTICAL));
        hLayout.getChildren().add(new ApplicationPane());

        Scene scene = new Scene(hLayout);
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static Stage getStage(){
        return mainStage;
    }

    private MainWindow(){}
}
