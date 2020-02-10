package unsw.dungeon;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DungeonApplication extends Application {
	
	private Stage currentStage;
	private String[] maps = {"marking.json", "maze.json", "boulders.json", "advancednoexit.json"};
	private int currentMap;
	
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Dungeon");
        this.currentStage = primaryStage;
        
        DungeonControllerLoader dungeonLoader = new DungeonControllerLoader(maps[currentMap]);
        DungeonController controller = dungeonLoader.loadController(this);
        controller.startMenu();
    }
    
    public Stage getCurrentStage() {
    	return currentStage;
    }
    
    public String[] getMaps() {
    	return maps;
    }
    
    public int getCurrentMap() {
    	return currentMap;
    }
    
    public void nextMap() {
    	currentMap++;
    }
    
    public void resetMap() {
    	currentMap = 0;
    }
    
    public boolean isGameOver() {
    	return currentMap >= maps.length;
    }
    
    public void play() {
        currentStage.show();
    }
    
    public void loadNewMap() {
    	System.out.println( "Load New Map.." );
        currentStage.close();
        Stage primaryStage = new Stage();
        Platform.runLater(() -> {
       
			try {
				primaryStage.setTitle("Dungeon");
				currentStage = primaryStage;
		        
		        DungeonControllerLoader dungeonLoader = new DungeonControllerLoader(maps[currentMap]);
		        DungeonController controller = dungeonLoader.loadController(this);
		        
		        FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));
		        loader.setController(controller);
		        Parent root = loader.load();
		        Scene scene = new Scene(root);
		        root.requestFocus();
		        primaryStage.setScene(scene);
		        controller.transitionMenu();
			} catch (IOException e) {
				e.printStackTrace();
			}
        });	
    }

    public static void main(String[] args) {
        launch(args);
    }

}
