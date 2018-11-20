package viewmodel.panes;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import viewmodel.Config;
import viewmodel.LevelEditorCanvas;
import viewmodel.SceneManager;
import viewmodel.customNodes.NumberTextField;

import static viewmodel.LevelEditorCanvas.Brush;

/**
 * Represents the level editor in the game
 */
public class LevelEditorPane extends BorderPane {
    private final LevelEditorCanvas levelEditor;
    private VBox leftContainer;
    private Button returnButton;
    private Label rowText;
    private NumberTextField rowField;
    private Label colText;
    private NumberTextField colField;
    private BorderPane rowBox; //holds the rowText and rowField side by side
    private BorderPane colBox; //holds the colText and colField side by side
    private Button newGridButton;
    private ObservableList<Brush> brushList;
    private ListView<Brush> selectedBrush = new ListView<>();
    private Button saveButton;
    private VBox centerContainer;

    /**
     * Instantiate the member components and connect and style them. Also set the callbacks.
     * <p>
     * Hints: {@link LevelEditorPane#rowField} and {@link LevelEditorPane#colField} should be initialized to "5".
     * {@link LevelEditorPane#levelEditor} should be initialized to 5 rows and 5 columns.
     * {@link LevelEditorPane#brushList} should be initialized with all values of the {@link Brush} enum.
     * Use 20 for VBox spacing
     */
    public LevelEditorPane() {
        //TODO
        levelEditor = new LevelEditorCanvas(5,5);
        rowField = new NumberTextField("5");
        colField = new NumberTextField("5");
        rowText = new Label("Rows");
        colText = new Label("Columns");
        returnButton = new Button("Return");
        newGridButton = new Button("New Grid");
        saveButton = new Button("Save");
        brushList = FXCollections.observableArrayList();
        brushList.addAll(LevelEditorCanvas.Brush.values());
        selectedBrush.setFixedCellSize(Config.LIST_CELL_HEIGHT);
        leftContainer = new VBox(20);
        centerContainer = new VBox(20);
        rowBox = new BorderPane();
        colBox = new BorderPane();
        
        styleComponents();
        connectComponents();
        setCallbacks();
    }

    /**
     * Connects the components together (think adding them into another, setting their positions, etc). Reference
     * the other classes in the {@link javafx.scene.layout.Pane} package.
     * <p>
     * Also sets {@link LevelEditorPane#selectedBrush}'s items, and selects the first.
     */
    private void connectComponents() {
        //TODO
        selectedBrush.setItems(brushList);
        /*
        selectedBrush.setCellFactory(param -> {
            var brushCell = new ListCell<Brush>(){
                @Override
                protected void updateItem (Brush item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item.toString());
                }
            };
            return brushCell;
        });*/
        selectedBrush.getSelectionModel().select(0);
        
        rowBox.setLeft(rowText);
        rowBox.setRight(rowField);
        colBox.setLeft(colText);
        colBox.setRight(colField);
        
        leftContainer.getChildren().addAll(returnButton,rowBox,colBox,newGridButton,selectedBrush,saveButton);
        leftContainer.setAlignment(Pos.TOP_CENTER);
        
        centerContainer.getChildren().addAll(levelEditor);
        centerContainer.setAlignment(Pos.CENTER);
        
        this.setLeft(leftContainer);
        this.setCenter(centerContainer);
    }

    /**
     * Apply CSS styling to components.
     */
    private void styleComponents() {
        //TODO
        leftContainer.getStyleClass().add("side-menu");
        returnButton.getStyleClass().add("big-button");
        newGridButton.getStyleClass().add("big-button");
        saveButton.getStyleClass().add("big-button");
        centerContainer.getStyleClass().add("big-vbox");
        
        rowField.getStyleClass().add("text-field");
        colField.getStyleClass().add("text-field");
        
        rowBox.getStyleClass().add("big-hbox");
        colBox.getStyleClass().add("big-hbox");
    }

    /**
     * Sets the event handlers for the 3 buttons and 1 canvas.
     * <p>
     * Hints:
     * The save button should save the current LevelEditorCanvas to file.
     * The new grid button should change the LevelEditorCanvas size based on the entered values
     * The return button should switch back to the main menu scene
     * The LevelEditorCanvas, upon mouse click, should call {@link LevelEditorCanvas#setTile(Brush, double, double)},
     * passing in the currently selected brush and mouse click coordinates
     */
    private void setCallbacks() {
        //TODO
        returnButton.setOnAction(event -> SceneManager.getInstance().showMainMenuScene());
        newGridButton.setOnAction(event -> levelEditor.changeSize(rowField.getValue(), colField.getValue()));
        saveButton.setOnAction(event -> levelEditor.saveToFile());
        levelEditor.setOnMouseClicked(event -> levelEditor.setTile(selectedBrush.getSelectionModel().getSelectedItem(), event.getX(), event.getY()));
    }
}
