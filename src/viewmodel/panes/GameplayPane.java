package viewmodel.panes;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Exceptions.InvalidMapException;
import model.LevelManager;
import viewmodel.AudioManager;
import viewmodel.MapRenderer;
import viewmodel.SceneManager;
import viewmodel.customNodes.GameplayInfoPane;

import java.util.Optional;

/**
 * Represents the gameplay pane in the game
 */
public class GameplayPane extends BorderPane {

    private final GameplayInfoPane info;
    private VBox canvasContainer;
    private Canvas gamePlayCanvas;
    private HBox buttonBar;
    private Button restartButton;
    private Button quitToMenuButton;

    /**
     * Instantiate the member components and connect and style them. Also set the callbacks.
     * Use 20 for the VBox spacing
     */
    public GameplayPane() {
        //TODO
        super();

        canvasContainer = new VBox(20);
        gamePlayCanvas = new Canvas();
        buttonBar = new HBox();
        restartButton = new Button("Restart");
        quitToMenuButton = new Button("Quit to menu");
        
        info = new GameplayInfoPane(LevelManager.getInstance().currentLevelNameProperty(),  LevelManager.getInstance().curGameLevelExistedDurationProperty(), LevelManager.getInstance().getGameLevel().numPushesProperty(), LevelManager.getInstance().curGameLevelNumRestartsProperty());

        styleComponents();
        connectComponents();
        setCallbacks();
    }

    /**
     * Connects the components together (think adding them into another, setting their positions, etc).
     */
    private void connectComponents() {
        //TODO
        buttonBar.getChildren().addAll(info,restartButton,quitToMenuButton);
        canvasContainer.getChildren().addAll(gamePlayCanvas,buttonBar);
        canvasContainer.setAlignment(Pos.CENTER);
        this.setCenter(canvasContainer);
    }

    /**
     * Apply CSS styling to components.
     */
    private void styleComponents() {
        //TODO
        restartButton.getStyleClass().add("big-button");
        quitToMenuButton.getStyleClass().add("big-button");
        buttonBar.getStyleClass().add("big-hbox");
        canvasContainer.getStyleClass().add("big-vbox");
    }

    /**
     * Set the event handlers for the 2 buttons.
     * <p>
     * Also listens for key presses (w, a, s, d), which move the character.
     * <p>
     * Hint: {@link GameplayPane#setOnKeyPressed(EventHandler)}  is needed.
     * You will need to make the move, rerender the canvas, play the sound (if the move was made), and detect
     * for win and deadlock conditions. If win, play the win sound, and do the appropriate action regarding the timers
     * and generating the popups. If deadlock, play the deadlock sound, and do the appropriate action regarding the timers
     * and generating the popups.
     */
    private void setCallbacks() {
        //TODO
        restartButton.setOnAction(event -> doRestartAction());
        quitToMenuButton.setOnAction(event -> doQuitToMenuAction());
        this.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case A:
                    LevelManager.getInstance().getGameLevel().makeMove('a');
                    break;
                case D:
                    LevelManager.getInstance().getGameLevel().makeMove('d');
                    break;
                case W:
                    LevelManager.getInstance().getGameLevel().makeMove('w');
                    break;
                case S:
                    LevelManager.getInstance().getGameLevel().makeMove('s');
                    break;
            }
            renderCanvas();
            AudioManager.getInstance().playMoveSound();
            if(LevelManager.getInstance().getGameLevel().isWin()){
                AudioManager.getInstance().playWinSound();
                LevelManager.getInstance().resetLevelTimer();
                createLevelClearPopup();
            } else if(LevelManager.getInstance().getGameLevel().isDeadlocked()){
                AudioManager.getInstance().playDeadlockSound();
                LevelManager.getInstance().resetLevelTimer();
                createDeadlockedPopup();
            }
        });
    }

    /**
     * Called when the tries to quit to menu. Show a popup (see the documentation). If confirmed,
     * do the appropriate action regarding the level timer, level number of restarts, and go to the
     * main menu scene.
     */
    private void doQuitToMenuAction() {
        //TODO
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Return to menu?");
        alert.setContentText("Game progress will be lost.");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent()){
            if (option.get() == ButtonType.OK){
                LevelManager.getInstance().resetLevelTimer();
                LevelManager.getInstance().resetNumRestarts();
                SceneManager.getInstance().showMainMenuScene();
            }
        }
    }

    /**
     * Called when the user encounters deadlock. Show a popup (see the documentation).
     * If the user chooses to restart the level, call {@link GameplayPane#doRestartAction()}. Otherwise if they
     * quit to menu, switch to the level select scene, and do the appropriate action regarding
     * the number of restarts.
     */
    private void createDeadlockedPopup() {
        //TODO
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().clear();

        var restartButtonType = new ButtonType("Restart");
        var returnButtonType = new ButtonType("Return");

        alert.setTitle("Confirm");
        alert.setHeaderText("Level deadlocked!");
        alert.getButtonTypes().addAll(restartButtonType,returnButtonType);

        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent()){
            if (option.get() == restartButtonType){
                doRestartAction();
            }else if (option.get() == returnButtonType){
                SceneManager.getInstance().showLevelSelectMenuScene();
                LevelManager.getInstance().resetNumRestarts();
            }
        }

    }

    /**
     * Called when the user clears the level successfully. Show a popup (see the documentation).
     * If the user chooses to go to the next level, set the new level, rerender, and do the appropriate action
     * regarding the timers and num restarts. If they choose to return, show the level select menu, and do
     * the appropriate action regarding the number of level restarts.
     * <p>
     * Hint:
     * Take care of the edge case for when the user clears the last level. In this case, there shouldn't
     * be an option to go to the next level.
     */
    private void createLevelClearPopup() {
        //TODO
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().clear();

        alert.setTitle("Confirm");
        alert.setHeaderText("Level cleared!");

        var nextLevelButtonType = new ButtonType("Next level");
        var returnButtonType = new ButtonType("Return");
        if (LevelManager.getInstance().getNextLevelName() == null){
            alert.getButtonTypes().addAll(returnButtonType);
        } else {
            alert.getButtonTypes().addAll(nextLevelButtonType,returnButtonType);
        }

        var option = alert.showAndWait();
        if (option.isPresent()){
            if (option.get() == nextLevelButtonType){
                try {
                    LevelManager.getInstance().setLevel(LevelManager.getInstance().getNextLevelName());
                    renderCanvas();
                    LevelManager.getInstance().resetLevelTimer();
                    LevelManager.getInstance().resetNumRestarts();
                }catch (InvalidMapException e){
                }
            }else if (option.get() == returnButtonType){
                SceneManager.getInstance().showLevelSelectMenuScene();
                LevelManager.getInstance().resetNumRestarts();
            }
        }

    }

    /**
     * Set the current level to the current level name, rerender the canvas, reset and start the timer, and
     * increment the number of restarts
     */
    private void doRestartAction() {
        //TODO
        try {
            LevelManager.getInstance().setLevel(LevelManager.getInstance().currentLevelNameProperty().getValue());
            renderCanvas();
            LevelManager.getInstance().resetLevelTimer();
            LevelManager.getInstance().startLevelTimer();
            LevelManager.getInstance().incrementNumRestarts();
        } catch (InvalidMapException e) {
            e.printStackTrace();
        }
    }

    /**
     * Render the canvas with updated data
     * <p>
     * Hint: {@link MapRenderer}
     */
    private void renderCanvas() {
        //TODO
        MapRenderer.render(gamePlayCanvas,LevelManager.getInstance().getGameLevel().getMap().getCells());
    }
}
