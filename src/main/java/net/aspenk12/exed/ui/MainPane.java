package net.aspenk12.exed.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
}
