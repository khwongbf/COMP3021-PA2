package viewmodel;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.PopupWindow;
import javafx.stage.Window;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import static viewmodel.Config.LEVEL_EDITOR_TILE_SIZE;

/**
 * Extends the Canvas class to provide functionality for generating maps and saving them.
 */
public class LevelEditorCanvas extends Canvas {
    private int rows;
    private int cols;

    private Brush[][] map;

    //Stores the last location the player was standing at
    private int oldPlayerRow = -1;
    private int oldPlayerCol = -1;

    /**
     * Call the super constructor. Also resets the map to all {@link Brush#TILE}.
     * Hint: each square cell in the grid has size {@link Config#LEVEL_EDITOR_TILE_SIZE}
     *
     * @param rows The number of rows in the map
     * @param cols The number of tiles in the map
     */
    public LevelEditorCanvas(int rows, int cols) {
        //TODO
        super(rows * LEVEL_EDITOR_TILE_SIZE, cols *LEVEL_EDITOR_TILE_SIZE);
        resetMap(rows, cols);
    }

    /**
     * Setter function. Also resets the map
     *
     * @param rows The number of rows in the map
     * @param cols The numbers of cols in the map
     */
    public void changeSize(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        resetMap(rows, cols);
    }

    /**
     * Assigns {@link LevelEditorCanvas#map} to a new instance, sets all the values to {@link Brush#TILE}, and
     * renders the canvas with the updated map.
     *
     * @param rows The number of rows in the map
     * @param cols The numbers of cols in the map
     */
    private void resetMap(int rows, int cols) {
        //TODO
        map = new Brush[rows][cols];
        for (int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                map[i][j]= Brush.TILE;
            }
        }
        renderCanvas();
    }

    /**
     * Render the map using {@link MapRenderer}
     */
    private void renderCanvas() {
        //TODO
        MapRenderer.render(this, map);
    }

    /**
     * Sets the applicable {@link LevelEditorCanvas#map} cell to the brush the user currently has selected.
     * In other words, when the user clicks somewhere on the canvas, we translate that into updating one of the
     * tiles in our map.
     * <p>
     * There can only be 1 player on the map. As such, if the user clicks a new position using the player brush,
     * the old location must have the player removed, leaving behind either a tile or a destination underneath,
     * whichever the player was originally standing on.
     * <p>
     * Hint:
     * Don't forget to update the player ({@link Brush#PLAYER_ON_DEST} or {@link Brush#PLAYER_ON_TILE})'s last
     * known position.
     * <p>
     * Finally, render the canvas.
     *
     * @param brush The currently selected brush
     * @param x     Mouse click coordinate x
     * @param y     Mouse click coordinate y
     */
    public void setTile(Brush brush, double x, double y) {
        //TODO
        int row = Double.valueOf(y/LEVEL_EDITOR_TILE_SIZE).intValue();
        int col = Double.valueOf(x/LEVEL_EDITOR_TILE_SIZE).intValue();

        // Remove the original player location
        switch (brush){
            case PLAYER_ON_TILE:
            case PLAYER_ON_DEST:
                for (int i =0; i < map.length; i++){
                    for (int j = 0; j < map[i].length; j++){
                        switch (map[i][j]){
                            case PLAYER_ON_TILE:
                                map[i][j] = Brush.TILE;

                                break;
                            case PLAYER_ON_DEST:
                                map[i][j] = Brush.DEST;
                                break;
                        }
                    }
                }
                break;
        }

        map[row][col] = brush;

        renderCanvas();

    }

    /**
     * Saves the current map to file. Should prompt the user for the save directory before saving the file to
     * the selected location.
     */
    public void saveToFile() {
        //TODO
        var file = getTargetSaveDirectory();
        if (file != null){
            try(var printWriter = new PrintWriter(file)){
                file.createNewFile();
                printWriter.println(rows);
                printWriter.println(cols);
                for (var i = 0 ; i < rows; i++){
                    for (var j = 0; j< cols; j++){
                        
                    }
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Hint: {@link FileChooser} is needed. Also add an extension filter with the following information:
     * description: "Normal text file"
     * extension: "*.txt"
     *
     * @return The directory the user chose to save the map in.
     */
    private File getTargetSaveDirectory() {
        //TODO
        var fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Normal text file", "*.txt"));
        return fileChooser.showSaveDialog(new PopupWindow() {
        });//NOTE: You may also need to modify this line
    }

    /**
     * Check if the map is valid for saving.
     * Conditions to check:
     * 1. Map must at least have 3 rows and 3 cols
     * 2. Must have a player
     * 3. Balanced number of crates and destinations
     * 4. At least 1 crate and destination
     * <p>
     * Show an Alert if there's an error.
     *
     * @return If the map is invalid
     */
    private boolean isInvalidMap() {
        //TODO
        var invalidCondition = new boolean[4];
        invalidCondition[0] = rows < 3 && cols < 3;

        int playerCount = 0;
        int crateCount = 0;
        int destCount = 0;

        for (var i: map){
            for (var j:i){
                switch (j){
                    case PLAYER_ON_TILE:
                        playerCount++;
                        break;
                    case PLAYER_ON_DEST:
                        destCount++;
                        playerCount++;
                        break;
                    case CRATE_ON_TILE:
                        crateCount++;
                        break;
                    case CRATE_ON_DEST:
                        crateCount++;
                        destCount++;
                        break;
                    case DEST:
                        destCount++;
                        break;
                }
            }
        }
        invalidCondition[1] = (playerCount != 1);
        invalidCondition[2] = (crateCount != destCount);
        invalidCondition[3] = (crateCount < 1 || destCount < 1);


        return !(Arrays.equals(new boolean[4], invalidCondition));//NOTE: You may also need to modify this line
    }

    /**
     * Represents the currently selected brush when the user is making a new map
     */
    public enum Brush {
        TILE("Tile", '.'),
        PLAYER_ON_TILE("Player on Tile", '@'),
        PLAYER_ON_DEST("Player on Destination", '&'),
        CRATE_ON_TILE("Crate on Tile", 'c'),
        CRATE_ON_DEST("Crate on Destination", '$'),
        WALL("Wall", '#'),
        DEST("Destination", 'C');

        private final String text;
        private final char rep;

        Brush(String text, char rep) {
            this.text = text;
            this.rep = rep;
        }

        public static Brush fromChar(char c) {
            for (Brush b : Brush.values()) {
                if (b.getRep() == c) {
                    return b;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return text;
        }

        char getRep() {
            return rep;
        }
    }


}

