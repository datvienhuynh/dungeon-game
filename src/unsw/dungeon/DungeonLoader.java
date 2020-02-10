package unsw.dungeon;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Loads a dungeon from a .json file.
 *
 * By extending this class, a subclass can hook into entity creation. This is
 * useful for creating UI elements with corresponding entities.
 *
 * @author Robert Clifton-Everest
 *
 */
public abstract class DungeonLoader {

    private JSONObject json;

    public DungeonLoader(String filename) throws FileNotFoundException {
        json = new JSONObject(new JSONTokener(new FileReader("dungeons/" + filename)));
    }

    /**
     * Parses the JSON to create a dungeon.
     * @return
     */
    public Dungeon load() {
        int width = json.getInt("width");
        int height = json.getInt("height");

        Dungeon dungeon = new Dungeon(width, height);

        JSONArray jsonEntities = json.getJSONArray("entities");

        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(dungeon, jsonEntities.getJSONObject(i));
        }
        
        // add dungeon to all Enemy objects
        for (Entity entity : dungeon.getAllEntities()) {
        	if (entity instanceof Enemy) {
        		((Enemy)entity).addDungeon(dungeon);
        	}
        }
        // set ids of keys
        int i = 1;
        for (Entity findKey : dungeon.getAllEntities()) {
        	if (findKey instanceof Key) {
        		((Key)findKey).setID(i);
        		i++;
        	}
        }
        // set ids of doors
        i = 1;
        for (Entity findDoor : dungeon.getAllEntities()) {
        	if (findDoor instanceof Door) {
        		((Door)findDoor).setID(i);
        		i++;
        	}
        }
        
        JSONObject jsonGoals = json.getJSONObject("goal-condition");
        dungeon.setGoals(getJSONGoals(jsonGoals));

        return dungeon;
    }
    
    // recursion reads a JSONObject into a goal composite
    private GoalComponent getJSONGoals(JSONObject json) {
    	
    	String goalName = json.getString("goal");
    	if ((goalName.equals("exit")) 	  ||
			(goalName.equals("enemies"))  ||
			(goalName.equals("boulders")) ||
			(goalName.equals("treasure"))) {
    		Subgoal subgoal = new Subgoal(goalName);
    		return subgoal;
    	}
    	
		Goal goal = new Goal(goalName);
		JSONArray jsonSubgoals = json.getJSONArray("subgoals");
		for (int i = 0; i < jsonSubgoals.length(); i++) {
			goal.add(getJSONGoals(jsonSubgoals.getJSONObject(i)));
		}
		return goal;
    }

    private void loadEntity(Dungeon dungeon, JSONObject json) {
        String type = json.getString("type");
        int x = json.getInt("x");
        int y = json.getInt("y");

        Entity entity = null;
        switch (type) {
        case "player":
            Player player = new Player(dungeon, x, y);
            dungeon.setPlayer(player);
            onLoad(player);
            entity = player;
            break;
        case "wall":
            Wall wall = new Wall(x, y);
            onLoad(wall);
            entity = wall;
            break;
        case "boulder":
            Boulder boulder = new Boulder(x, y);
            onLoad(boulder);
            entity = boulder;
            break;
        case "exit":
            Exit exit = new Exit(x, y);
            onLoad(exit);
            entity = exit;
            break;
        case "switch":
            FloorSwitch floorswitch = new FloorSwitch(x, y);
            onLoad(floorswitch);
            entity = floorswitch;
            break;
        case "door":
            Door door = new Door(x, y);
            onLoad(door);
            entity = door;
            break;
        case "enemy":
            Enemy enemy = new Enemy(x, y);
            onLoad(enemy);
            entity = enemy;
            break;
        case "sword":
            Sword sword = new Sword(x, y);
            onLoad(sword);
            entity = sword;
            break;
        case "key":
            Key key = new Key(x, y);
            onLoad(key);
            entity = key;
            break;
        case "treasure":
        	Treasure treasure = new Treasure(x, y);
            onLoad(treasure);
            entity = treasure;
            break;
        case "bomb":
        	Bomb bomb = new Bomb(dungeon, x, y);
            onLoad(bomb);
            entity = bomb;
            break;
        case "invincibility":
        	InvincibilityPotion invincibilityPotion = new InvincibilityPotion(x, y);
            onLoad(invincibilityPotion);
            entity = invincibilityPotion;
            break;
        }
        dungeon.addEntity(entity);
    }

    public abstract void onLoad(Entity player);
    public abstract void onLoad(Wall wall);
    public abstract void onLoad(Boulder boulder);
    public abstract void onLoad(Exit Exit);
    public abstract void onLoad(FloorSwitch FloorSwitch);
    public abstract void onLoad(Door door);
    public abstract void onLoad(Enemy enemy);
    public abstract void onLoad(Treasure treasure);
    public abstract void onLoad(Bomb bomb);
    public abstract void onLoad(Sword sword);
    public abstract void onLoad(Key key);
    public abstract void onLoad(InvincibilityPotion InvincibilityPotion);

}
