/**
 *
 */
package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;

/**
 * A dungeon in the interactive dungeon player.
 *
 * A dungeon can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 *
 * @author Robert Clifton-Everest
 *
 */
public class Dungeon {

    private int width, height;
    private List<Entity> entities;
    private Player player;
    private GoalComponent goals;
    private DungeonController controller;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.entities = new ArrayList<>();
        this.player = null;
        this.goals = null;
        this.controller = null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public GoalComponent getGoals() {
    	return goals;
    }
    
    public void setGoals(GoalComponent goals) {
    	this.goals = goals;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    
    public void addNewEntity(Entity entity, ImageView view) {
    	entities.add(entity);
    	controller.addEntity(entity, view);
    }
    
    public void removeEntity(Entity entity) {
    	entities.remove(entity);
		controller.removeEntity(entity);
    }
    
    public DungeonController getController() {
    	return controller;
    }
    
    public void setController(DungeonController controller) {
    	this.controller = controller;
    }
    
    // return a list of all enemies
    public List<Enemy> getEnemies() {
    	List<Enemy> enemies = new ArrayList<Enemy>();
    	for (Entity entity : entities) {
    		if (entity instanceof Enemy)
    			enemies.add((Enemy)entity);
    	}
    	return enemies;
    }
    
    // return a list of all bombs
    public List<Bomb> getBombs() {
    	List<Bomb> bombs = new ArrayList<Bomb>();
    	for (Entity entity : entities) {
    		if (entity instanceof Bomb)
    			bombs.add((Bomb)entity);
    	}
    	return bombs;
    }
    
    // return a list of all exploding bombs
    public List<Bomb> getExplosions() {
    	List<Bomb> explosions = new ArrayList<Bomb>();
    	for (Entity entity : entities) {
    		if (entity instanceof Bomb) {
    			if (((Bomb)entity).isExplode()) 
    				explosions.add((Bomb)entity);
    		}
    	}
    	return explosions;
    }
    
    // return a list of all entities at a given position
    public List<Entity> getEntities(int x, int y) {
    	List<Entity> findEntities = new ArrayList<Entity>();
    	for (Entity entity : entities) {
    		if (entity.getX() == x && entity.getY() == y) {
    			findEntities.add(entity);
    		}
    	}
    	return findEntities;
    }
    
    // return a list of all entities at a given square
    // and top, down, left, right adjacent to it
    public List<Entity> getAdjEntities(int x, int y) {
    	List<Entity> findEntities = new ArrayList<Entity>();
    	for (Entity entity : entities) {
    		if ((entity.getX() == x && entity.getY() == y) 	   ||
				(entity.getX() == x - 1 && entity.getY() == y) ||
				(entity.getX() == x + 1 && entity.getY() == y) ||
				(entity.getX() == x && entity.getY() == y - 1) ||
				(entity.getX() == x && entity.getY() == y + 1)) {
    			findEntities.add(entity);
    		}
    	}
    	return findEntities;
    }
    
    public List<Entity> getAllEntities() {
    	return entities;
    }
    
    // check if it is possible to move into a square
    public boolean canMoveTo(int x, int y) {
    	if (x >= width || y >= height || 
    		x < 0 || y < 0) {
    		return false;
    	}
    	for (Entity entity : getEntities(x, y)) {
	    	if (!entity.canMoveThrough()) {
	    		return false;
	    	}
    	}
    	return true;
    }
    
    // check before Player moves into a square of given x, y
    // handle cases when Player moves into Boulder and Door
    public boolean checkMove(int x, int y, String direction) {
    	
    	int a = 0, b = 0;
    	if (direction.equals("UP")) {
    		a = 0; b = -1;
    	}
    	else if (direction.equals("DOWN")) {
    		a = 0; b = 1;
    	}
    	else if (direction.equals("LEFT")) {
    		a = -1; b = 0;
    	}
    	else if (direction.equals("RIGHT")) {
    		a = 1; b = 0;
    	}
    		
    	for (Entity entity : getEntities(x, y)) {
    		// if Players moving to a new key
	        if (entity instanceof Key && 
	        	canMoveTo(x, y) == true &&
	        	player.key.hasItem() == true) {  
	        	// drop the old key at the previous square
	        	player.dropKey();
	        }
    		// if Players moving to a boulder
	        if (entity instanceof Boulder && 
	        	canMoveTo(x + a, y + b) == true) {  
	        	entity.x().set(entity.getX() + a);
	        	entity.y().set(entity.getY() + b);
	        }
	        // if Players moving to a door, check keys id
	        if (entity instanceof Door) {
	        	Door door = (Door)entity;
	        	if (door.getID() == player.key.getItem()) {
	        		controller.changeImage(door, "/open_door.png");
	        		door.open();
	        		player.useKey();
	        	}
	        }
    	}
    	
    	if (canMoveTo(x, y)) return true;
    	return false;
    }
    
    // update states of Player, Enemy, Boulder, GoalComponent
    // after Player has made a move
    public void updateDungeon() {
    	    	
    	// if Players moving to an enemy, check Sword and Invincibility
    	for (Entity entity : getEntities(player.getX(), player.getY())) {
	        if (entity instanceof Enemy) {
	        	// if Player has no weapons, he loses
	        	if (!player.invincibility.hasItem() &&
	        		!player.sword.hasItem()) {
	        		playerLoses();
	        	}
	        	// only use Sword if Player has no Invincibility
	        	else if (!player.invincibility.hasItem()) {
	        		player.sword.useItem();
	        		removeEntity(entity);
	        	}
	        	else
	        		removeEntity(entity);
	        }
        }
    	
    	// for each bomb exploding
    	for (Bomb explodeBomb : getExplosions()) {
    		// for all entities adjacent to the explosion
	    	for (Entity entity : getAdjEntities(explodeBomb.getX(), 
	    										explodeBomb.getY())) {
	    		// kill Player if he is not invincible
	    		if (entity instanceof Player) {
	    			if (!((Player)entity).invincibility.hasItem()) 
	    				playerLoses();
	    		}
	    		// destroy Enemy and Boulder
	    		else if (entity instanceof Enemy ||
	    				 entity instanceof Boulder) {
	    			removeEntity(entity);
	    		}
	    	}
    	}
    	
    	updateGoals();
    }
    
    // update GoalComponent goals of Dungeon
    public void updateGoals() {
    	
    	boolean enemies = true;
    	boolean boulder = true;
    	boolean treasure = true;
    	
    	for (Entity entity : entities) {
    		// if player can move into exit, he wins
    		if (entity instanceof Exit &&
    			entity.getX() == player.getX() &&
    			entity.getY() == player.getY()) {
    			goals.complete("exit"); 
    		}
    		// check if any enemy still exists
    		if (entity instanceof Enemy) {  
	        	enemies = false;
	        }
	        // check if every switch has a boulder on it
	        if (entity instanceof FloorSwitch) {
	        	boolean isBoulderOn = false;
	        	for (Entity check : getEntities(entity.getX(), entity.getY())) {
	        		if (check instanceof Boulder) {
	        			isBoulderOn = true;
	        		}
	        	}
	        	if (!isBoulderOn) boulder = false;
	        }
	        // check if all treasures are collected
	        if (entity instanceof Treasure) {  
	        	treasure = false;
	        }
        }
    	
    	// update base goals that have been accomplished
    	if (enemies) goals.complete("enemies");
    	if (boulder) goals.complete("boulders");
    	else goals.incomplete("boulders");
    	if (treasure) goals.complete("treasure");
    	
    	// if all goals are accomplished, player wins
    	if (goals.isAccomplished()) {
    		playerWins(); 
    	}
    	
    	// if exit is the last goal, open exit
    	if ((enemies) && (boulder) && (treasure)) {
    		for (Entity entity : entities) {
        		if (entity instanceof Exit) {
        			((Exit)entity).open();
        		}
        	}
    	}
    	// otherwise, exit should be closed
    	else {
    		for (Entity entity : entities) {
        		if (entity instanceof Exit) {
        			((Exit)entity).close();
        		}
        	}
    	}
    }
    
    // decide how enemies will move
    public void updateEnemy() {
    	for (Enemy enemy : getEnemies()) {
	    	if (player.invincibility.hasItem())
	    		enemy.runAway();
	    	else
	    		enemy.hunt();
    	}
    	updateDungeon();
    }
    
    public void playerWins() {
    	controller.playerWins();
    	System.out.println("CONGRATULATION! YOU WON!!!");
    }
    
    public void playerLoses() {
    	controller.playerLoses();
    	System.out.println("PLAYER DIED! GAME OVER!!!");
    }
  
}
