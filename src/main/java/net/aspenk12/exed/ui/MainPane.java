package net.aspenk12.exed.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * An abstract class representing shared values of all panes in the MainWindow
 */
public abstract class MainPane extends StackPane {

    /**
     * All MainPanes inherit a standardized vBox for formatting
     */
    protected VBox vBox = new VBox(20);

    protected MainPane() {
        super();
        setPrefSize(200,200);

        vBox.setAlignment(Pos.TOP_CENTER);
        getChildren().add(vBox);

        setAlignment(Pos.CENTER);
    }

    /**
     * Used by multiple panes to select CSVs
     */
    protected final File openFileChooser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        return fileChooser.showOpenDialog(MainWindow.getStage());
    }
}
