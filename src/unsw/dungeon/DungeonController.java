package unsw.dungeon;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A JavaFX controller for the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonController {

    @FXML
    private GridPane squares;
    
    private List<ImageView> initialEntities;
    private DungeonApplication dungeonApp;
    private Player player;
    private Dungeon dungeon;
    
    // timeline updates Enemy moves after a specific amount of time
    private Timeline timeline;
    
    // list contains all FadeTramsitions in the game
    private List<FadeTransition> fadeAnimations;
    
    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities, DungeonApplication dungeonApp) {
    	this.dungeonApp = dungeonApp;
    	this.dungeon = dungeon;
        dungeon.setController(this);
        this.player = dungeon.getPlayer();
        this.initialEntities = new ArrayList<>(initialEntities);
        this.timeline = new Timeline(
			new KeyFrame(Duration.millis(300), e -> {
				dungeon.updateEnemy();
			})
		);
		this.timeline.setCycleCount(Timeline.INDEFINITE);
		this.fadeAnimations = new ArrayList<FadeTransition>();
    }
    
    // pause all the animations in the dungeon
    public void pauseAllTimelines() {
    	timeline.pause();
    	player.invincibility.getTimeline().pause();
    	for (Bomb bomb : dungeon.getBombs())
    		bomb.pause();
    	for (FadeTransition fadeAnimation : fadeAnimations)
    		fadeAnimation.pause();
    }
    
    // play all the animations in the dungeon
    public void playAllTimelines() {
    	timeline.play();
    	player.invincibility.getTimeline().play();;
    	for (Bomb bomb : dungeon.getBombs()) {
    		if (!(bomb.getState() instanceof Unlit))
    			bomb.light();
    	}
    	for (FadeTransition fadeAnimation : fadeAnimations)
    		fadeAnimation.play();
    }
    
    public List<FadeTransition> getFadeAnimations() {
    	return fadeAnimations;
    }
    
    // change the image of a given entity
    public void changeImage(Entity entity, String imagePath) {
    	Node node = getNodeFromGrid(entity);
    	if (node instanceof ImageView)
    		((ImageView)node).setImage(new Image(imagePath));
    }
    
    // remove an entity from front end
    public void removeEntity(Entity entity) {
    	FadeTransition fadeTransition = newFadeTransition(250, entity, 3);
    	fadeTransition.setOnFinished(e -> {
	    	Node node = getNodeFromGrid(entity);
	    	if (node instanceof ImageView) {
	    		squares.getChildren().remove(node);
	    		initialEntities.remove(node);
	    	}
    	});
    	fadeTransition.play();
    }
    
    // add an entity to front end
    public void addEntity(Entity entity, ImageView view) {
    	trackPosition(entity, view);
    	squares.add(view, entity.getX(), entity.getY());
		initialEntities.add(view);
    }
    
    // return a node corresponding to a given entity
    public Node getNodeFromGrid(Entity entity) {
    	
    	// save the current position of entity
    	int initX = entity.getX();
		int initY = entity.getY();
		// move entity out of the map
		entity.x().set(dungeon.getWidth());
		entity.y().set(dungeon.getWidth());
		
		// return the corresponding node and move entity back
    	for (Node node : squares.getChildren()) {
    		if (GridPane.getColumnIndex(node) == entity.getX() &&
				GridPane.getRowIndex(node) == entity.getY() &&
				this.initialEntities.contains(node)) {
    			entity.x().set(initX);
        		entity.y().set(initY);
    			return node;
    		}
    	}
    	entity.x().set(initX);
		entity.y().set(initY);
    	return null;
    }
    
    // return a new FadeTransition effect of a given entity 
    // with specified duration and cycle
    public FadeTransition newFadeTransition(int duration, Entity entity, int cycleCount) {
    	FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), 
    													   getNodeFromGrid(entity));
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		fadeTransition.setCycleCount(cycleCount);
		fadeAnimations.add(fadeTransition);
		 return fadeTransition;
    }

    @FXML
    public void initialize() {
        Image ground = new Image("/dirt_0_new.png");
        
        // Add the ground first so it is below all other entities
        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                squares.add(new ImageView(ground), x, y);
            }
        }

        for (ImageView entity : initialEntities)
            squares.getChildren().add(entity);
    }

    @FXML
    public void handleKeyPress(KeyEvent event) throws FileNotFoundException {
        switch (event.getCode()) {
        case UP:
            player.moveUp();
            break;
        case DOWN:
            player.moveDown();
            break;
        case LEFT:
            player.moveLeft();
            break;
        case RIGHT:
            player.moveRight();
            break;
        case SPACE:
        	player.useBomb();
            break;
        case ENTER:
        	pauseMenu();;
            break;
        	
        default:
            break;
        }
    }
    
    // start dialog shows the main menu of the dungeon
    public void startMenu() throws FileNotFoundException {
	    	
        Alert alert = new Alert(AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.getIcons().add(new Image(this.getClass().getResource("/human_new.png").toString()));
        alert.setTitle("Dungeon");
        alert.setHeaderText("WELCOME TO DUNGEON !!!");
        //alert.setContentText("Choose your level:");

        ButtonType buttonTypeOne = new ButtonType("NEW GAME");
        ButtonType buttonTypeTwo = new ButtonType("HELP");
        ButtonType buttonTypeCancel = new ButtonType("EXIT", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == buttonTypeCancel){
        	Platform.exit();
        } 
        else if (result.get() == buttonTypeTwo){
        	helpMenu();
        } 
        else {
        	// if User chooses to play a New Game, 
        	// the current map is reset to the first level
        	dungeonApp.resetMap();
        	dungeonApp.loadNewMap();
        }
    }
    
    // dialog shows the user guide of the game
    public void helpMenu() {
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.getIcons().add(new Image(this.getClass().getResource("/human_new.png").toString()));
        alert.setTitle("Dungeon");
        alert.setHeaderText("DUNGEON RULE" + "\n" +
        					"Player has to win every map from low to high levels " +
        					"by completing all required goals");
        alert.setContentText("USER GUIDE" + "\n" +
        					 "<UP ARROW>:    Move up" + "\n" +
        					 "<DOWN ARROW>:  Move down" + "\n" +
        					 "<LEFT ARROW>:  Move left" + "\n" +
        					 "<RIGHT ARROW>: Move right" + "\n" +
        					 "<SPACE>:       Drop a bomb" + "\n" +
        					 "<ENTER>:       Pause the game - Show info of Dungeon, Goals and Items");

        ButtonType buttonTypeOne = new ButtonType("BACK");
        alert.getButtonTypes().setAll(buttonTypeOne);
        alert.show();
        alert.setOnHidden(evt -> {
        	try {
				startMenu();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        });
    }
    
    // dialog pops up when User presses ENTER
    // the stage of the dungeon is paused
    public void pauseMenu() throws FileNotFoundException {
    	
    	// pause all the Timelines in the dungeon
    	pauseAllTimelines();
    	
    	// show the pause menu
    	Alert alert = new Alert(AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.getIcons().add(new Image(this.getClass().getResource("/human_new.png").toString()));
        alert.setTitle("Dungeon");
        // show the current level and goals
        alert.setHeaderText("LEVEL " + (dungeonApp.getCurrentMap() + 1) + "\n" + 
				 			"GOALS: " + "\n" + 
			 				dungeon.getGoals().string());
        // show information about Player
        alert.setContentText("YOUR BAG:" + "\n" +
			 				player.key.string() + "\n" +
			 				player.sword.string() + "\n" +
			 				player.bomb.string() + "\n" +
			 				player.invincibility.string());

        ButtonType buttonTypeOne = new ButtonType("RESUME");
        ButtonType buttonTypeTwo = new ButtonType("RESTART");
        ButtonType buttonTypeThree = new ButtonType("MAIN MENU");
        ButtonType buttonTypeCancel = new ButtonType("EXIT", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        
        // resume the game
        if (result.get() == buttonTypeOne){
        	playAllTimelines();
        }
        // restart the current map
        else if (result.get() == buttonTypeTwo){
        	dungeonApp.loadNewMap();
        }
        // go back to the main menu
        else if (result.get() == buttonTypeThree){
        	startMenu();
        }
        // exit the dungeon
        else {
        	Platform.exit();
        }
    }
    
    // dialog pops up before the game start
    // show the current level and required goals
    public void transitionMenu() {
    	
        Alert alert = new Alert(AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.getIcons().add(new Image(this.getClass().getResource("/human_new.png").toString()));
        alert.setTitle("Dungeon");
        alert.setHeaderText("LEVEL " + (dungeonApp.getCurrentMap() + 1));
        alert.setContentText("GOALS: " + "\n" + 
        					 dungeon.getGoals().string());

        ButtonType buttonTypeOne = new ButtonType("START");
        ButtonType buttonTypeCancel = new ButtonType("EXIT", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == buttonTypeCancel){
        	Platform.exit();
        } 
        else {
        	timeline.play();
        	dungeonApp.play();
        }
    }
    
 // dialog pop up after Player has completed all goals
    public void playerWins() {
    	
    	pauseAllTimelines();
    	    	
        Alert alert = new Alert(AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.getIcons().add(new Image(this.getClass().getResource("/human_new.png").toString()));
        alert.setTitle("Dungeon");
        alert.setHeaderText("CONGRATULATION, YOU WIN !!!");
        alert.setContentText("Level " + (dungeonApp.getCurrentMap() + 1) + " Completed");

        ButtonType buttonTypeOne = new ButtonType("CONTINUE");

        alert.getButtonTypes().setAll(buttonTypeOne);
        alert.show();
        alert.setOnHidden(evt -> {
        	dungeonApp.nextMap();
        	if (dungeonApp.isGameOver())
        		gameOverMenu();
        	dungeonApp.loadNewMap();
        });
    }
    
    // dialog pop up after Player died
    public void playerLoses() {
    	
    	pauseAllTimelines();
    	    	
        Alert alert = new Alert(AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.getIcons().add(new Image(this.getClass().getResource("/human_new.png").toString()));
        alert.setTitle("Dungeon");
        alert.setHeaderText("OOPS, YOU DIED. GAME OVER !!!");
        alert.setContentText("Level " + (dungeonApp.getCurrentMap() + 1) + " Not Completed");

        ButtonType buttonTypeOne = new ButtonType("TRY AGAIN");

        alert.getButtonTypes().setAll(buttonTypeOne);
        alert.show();
        alert.setOnHidden(evt -> {
        	dungeonApp.loadNewMap();
        });
    }
    
    // dialog pops up after Player have won all the maps in the dungeon
    // either go back to the main menu or quit the dungeon
    public void gameOverMenu() {
    	
        Alert alert = new Alert(AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.getIcons().add(new Image(this.getClass().getResource("/human_new.png").toString()));
        alert.setTitle("Dungeon");
        alert.setHeaderText("VICTORY !!!");
        alert.setContentText("You have won all levels of this game, my Lord" + "\n" +
        					 "Go find something else to play !");

        ButtonType buttonTypeOne = new ButtonType("MAIN MENU");
        ButtonType buttonTypeCancel = new ButtonType("EXIT", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == buttonTypeOne) {
        	try {
				startMenu();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        }
        else {
        	Platform.exit();
        }
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the dungeon.
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());
        entity.x().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        });
        entity.y().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        });
    }

}
