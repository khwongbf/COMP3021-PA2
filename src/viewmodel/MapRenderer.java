package viewmodel;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.Map.Cell;
import model.Map.Occupant.Player;
import model.Map.Occupiable.DestTile;
import model.Map.Occupiable.Occupiable;
import model.Map.Occupiable.Tile;

import java.net.URISyntaxException;

import static viewmodel.Config.LEVEL_EDITOR_TILE_SIZE;

/**
 * Renders maps onto canvases
 */
public class MapRenderer {
    private static Image wall = null;
    private static Image crateOnTile = null;
    private static Image crateOnDest = null;

    private static Image playerOnTile = null;
    private static Image playerOnDest = null;

    private static Image dest = null;
    private static Image tile = null;

    static {
        try {
            wall = new Image(MapRenderer.class.getResource("/assets/images/wall.png").toURI().toString());
            crateOnTile = new Image(MapRenderer.class.getResource("/assets/images/crateOnTile.png").toURI().toString());
            crateOnDest = new Image(MapRenderer.class.getResource("/assets/images/crateOnDest.png").toURI().toString());
            playerOnTile = new Image(MapRenderer.class.getResource("/assets/images/playerOnTile.png").toURI().toString());
            playerOnDest = new Image(MapRenderer.class.getResource("/assets/images/playerOnDest.png").toURI().toString());
            dest = new Image(MapRenderer.class.getResource("/assets/images/dest.png").toURI().toString());
            tile = new Image(MapRenderer.class.getResource("/assets/images/tile.png").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Render the map onto the canvas. This method can be used in Level Editor
     * <p>
     * Hint: set the canvas height and width as a multiple of the rows and cols
     *
     * @param canvas The canvas to be rendered onto
     * @param map    The map holding the current state of the game
     */
    static void render(Canvas canvas, LevelEditorCanvas.Brush[][] map) {
        //TODO
        canvas.setHeight(map.length * LEVEL_EDITOR_TILE_SIZE);
        canvas.setWidth(map[0].length * LEVEL_EDITOR_TILE_SIZE);
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                switch (map[i][j]){
                    case TILE:
                        canvas.getGraphicsContext2D().drawImage(tile,i*LEVEL_EDITOR_TILE_SIZE, j*LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE,LEVEL_EDITOR_TILE_SIZE);
                        break;
                    case WALL:
                        canvas.getGraphicsContext2D().drawImage(wall,i*LEVEL_EDITOR_TILE_SIZE, j*LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE,LEVEL_EDITOR_TILE_SIZE);
                        break;
                    case DEST:
                        canvas.getGraphicsContext2D().drawImage(dest,i*LEVEL_EDITOR_TILE_SIZE, j*LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE,LEVEL_EDITOR_TILE_SIZE);
                        break;
                    case CRATE_ON_DEST:
                        canvas.getGraphicsContext2D().drawImage(crateOnDest,i*LEVEL_EDITOR_TILE_SIZE, j*LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE,LEVEL_EDITOR_TILE_SIZE);
                        break;
                    case CRATE_ON_TILE:
                        canvas.getGraphicsContext2D().drawImage(crateOnTile,i*LEVEL_EDITOR_TILE_SIZE, j*LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE,LEVEL_EDITOR_TILE_SIZE);
                        break;
                    case PLAYER_ON_DEST:
                        canvas.getGraphicsContext2D().drawImage(playerOnDest,i*LEVEL_EDITOR_TILE_SIZE, j*LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE,LEVEL_EDITOR_TILE_SIZE);
                        break;
                    case PLAYER_ON_TILE:
                        canvas.getGraphicsContext2D().drawImage(playerOnTile,i*LEVEL_EDITOR_TILE_SIZE, j*LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE,LEVEL_EDITOR_TILE_SIZE);
                        break;
                }
            }
        }
    }

    /**
     * Render the map onto the canvas. This method can be used in GamePlayPane and LevelSelectPane
     * <p>
     * Hint: set the canvas height and width as a multiple of the rows and cols
     *
     * @param canvas The canvas to be rendered onto
     * @param map    The map holding the current state of the game
     */
    public static void render(Canvas canvas, Cell[][] map) {
        //TODO
        canvas.setHeight(map.length * LEVEL_EDITOR_TILE_SIZE);
        canvas.setWidth(map[0].length * LEVEL_EDITOR_TILE_SIZE);
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                if (map[i][j] instanceof Occupiable){
                    if (map[i][j] instanceof DestTile) {
                        if (((DestTile) map[i][j]).getOccupant().isPresent()) {
                            if (((DestTile) map[i][j]).getOccupant().get() instanceof Player) {
                                canvas.getGraphicsContext2D().drawImage(playerOnDest, i * LEVEL_EDITOR_TILE_SIZE, j * LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE);
                            } else {
                                canvas.getGraphicsContext2D().drawImage(crateOnDest, i * LEVEL_EDITOR_TILE_SIZE, j * LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE);
                            }
                        } else {
                            canvas.getGraphicsContext2D().drawImage(dest, i * LEVEL_EDITOR_TILE_SIZE, j * LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE);
                        }
                    } else {
                        if (((Tile) map[i][j]).getOccupant().isPresent()) {
                            if (((Tile) map[i][j]).getOccupant().get() instanceof Player) {
                                canvas.getGraphicsContext2D().drawImage(playerOnTile, i * LEVEL_EDITOR_TILE_SIZE, j * LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE);
                            } else {
                                canvas.getGraphicsContext2D().drawImage(crateOnTile, i * LEVEL_EDITOR_TILE_SIZE, j * LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE);
                            }
                        } else {
                            canvas.getGraphicsContext2D().drawImage(tile, i * LEVEL_EDITOR_TILE_SIZE, j * LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE);
                        }
                    }
                } else {
                    canvas.getGraphicsContext2D().drawImage(wall, i * LEVEL_EDITOR_TILE_SIZE, j * LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE, LEVEL_EDITOR_TILE_SIZE);
                }
            }
        }
    }
}
