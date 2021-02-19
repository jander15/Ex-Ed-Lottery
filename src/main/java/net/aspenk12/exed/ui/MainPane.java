package net.aspenk12.exed.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class representing shared values of all panes in the MainWindow
 */
public abstract class MainPane extends StackPane {

    private static final Background disabledBackground;
    private static final Background enabledBackground;

    protected Text statusText;

    //consider translating all this to css
    static {
        Insets insets = new Insets(0);
        CornerRadii radii = CornerRadii.EMPTY;

        disabledBackground = new Background(new BackgroundFill(Color.GAINSBORO, radii, insets));
        enabledBackground = new Background(new BackgroundFill(Color.WHITESMOKE, radii, insets));
    }

    /**
     * Ordered list of MainPanes to control the transition between panes.
     */
    private static final List<MainPane> mainPanes = new ArrayList<>();
    private static int currentActivePane = 0;

    private boolean active;

    /**
     * All MainPanes inherit a standardized vBox for formatting
     */
    protected VBox vBox = new VBox(20);

    private final Button mainButton;
    private final MainText mainText;

    //adds a bit of empty space to the very top of the vbox
    private final Region spacer = new Region();

    protected MainPane(String title, String buttonText, String initialStatusMessage) {
        super();

        mainText = new MainText(title);

        setPrefSize(300,300);
        setAlignment(Pos.CENTER);

        mainButton = new Button(buttonText);
        mainButton.onActionProperty().setValue(e -> onClick());

        spacer.setPrefHeight(40);

        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().addAll(mainText, spacer, mainButton);

        getChildren().add(vBox);

        statusText = new Text(initialStatusMessage);
        statusText.setFill(Color.RED);
        statusText.setTextAlignment(TextAlignment.CENTER);
        statusText.wrappingWidthProperty().bind(widthProperty());
        statusText.setVisible(false);

        vBox.getChildren().add(statusText);

        setActive(mainPanes.isEmpty());

        mainPanes.add(this);
    }

    private void setActive(boolean active){
        this.active = active;

        mainText.setActive(active);

        if(active){
            setBackground(enabledBackground);
            statusText.setVisible(true);
        } else {
            setBackground(disabledBackground);
        }

        //maybe change this to get rid of the negative
        mainButton.setDisable(!active);
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

    private void onClick(){
        run();

        //progress to the next main pane
        currentActivePane++;
        setActive(false);

        if(currentActivePane < mainPanes.size()){
            mainPanes.get(currentActivePane).setActive(true);
        }
    }

    /**
     * Overridden in each sublcass of MainPane,
     * specifies what the main button should do when it's clicked.
     */
    protected abstract void run();

    private static class MainText extends Text{
        private static Font font = Font.loadFont(
                MainText.class.getResource("/Karrik-Regular.ttf").toExternalForm(), 22);

        public MainText(String text) {
            super(text);
            setFont(font);
            setFill(Color.DARKGRAY);
        }

        private void setActive(boolean active){
            Color color = active ? Color.RED: Color.DARKGRAY;
            setFill(color);
        }
    }
}
