package unsw.dungeon;

import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity {

    private Dungeon dungeon;
    
    public CarryKey key;
    public CarrySword sword;
    public CarryBomb bomb;
    public CarryInvincibility invincibility;
    
    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.setBePicked(false);
        this.dungeon = dungeon;
        this.key = new CarryKey();
        this.sword = new CarrySword();
        this.bomb = new CarryBomb();
        this.invincibility = new CarryInvincibility(this);
    }
    
    public Dungeon getDungeon() {
    	return dungeon;
    }
    
    public DungeonController getController() {
    	return dungeon.getController();
    }

    public void moveUp() throws FileNotFoundException {
        if (getY() > 0 && 
        	dungeon.checkMove(getX(), getY() - 1, "UP") == true) {
            y().set(getY() - 1);
            pickItem(getX(), getY());
            dungeon.updateDungeon();
        }
    }

    public void moveDown() throws FileNotFoundException {
        if (getY() < dungeon.getHeight() - 1 && 
        	dungeon.checkMove(getX(), getY() + 1, "DOWN") == true) {
            y().set(getY() + 1);
            pickItem(getX(), getY());
            dungeon.updateDungeon();
        }
    }

    public void moveLeft() throws FileNotFoundException {
        if (getX() > 0 && 
        	dungeon.checkMove(getX() - 1, getY(), "LEFT") == true) {
            x().set(getX() - 1);
            pickItem(getX(), getY());
            dungeon.updateDungeon();
        }
    }

    public void moveRight() throws FileNotFoundException {
        if (getX() < dungeon.getWidth() - 1 && 
        	dungeon.checkMove(getX() + 1, getY(), "RIGHT") == true) {
            x().set(getX() + 1);
            pickItem(getX(), getY());
            dungeon.updateDungeon();
        }
    }
    
    public void pickItem(int x, int y) {
    	for (Entity entity : dungeon.getEntities(x, y)) {
    		if (entity.canBePicked()) {
				if (entity instanceof Key) 
					pickKey((Key)entity);
				else if (entity instanceof Sword) 
					pickSword();
				else if (entity instanceof Bomb) 
					pickBomb();
				else if (entity instanceof InvincibilityPotion) 
					pickInvincibility();
				dungeon.removeEntity(entity);
    		}
    	}
    }
    
    public void pickKey(Key key) {
    	this.key.addItem(key.getID());
    }
    public void dropKey() {
    	Key oldKey = new Key(getX(), getY());
    	oldKey.setID(key.getItem());
    	dungeon.addNewEntity(oldKey, new ImageView("/key.png"));
    }
    public void useKey() {
    	key.useItem();
    }
    
    public void pickSword() {
    	sword.addItem(5);
    }
	
    public void useSword() {
    	sword.useItem();
    }
    
    public void pickBomb() {
    	bomb.addItem(1);
    }
    public void useBomb() {
    	if (!bomb.hasItem()) return;
    	bomb.useItem();
    	Bomb bomb1 = new Bomb(dungeon, getX(),getY());
    	ImageView view = new ImageView(new Image("/bomb_unlit.png"));
    	dungeon.addNewEntity(bomb1, view);
    	bomb1.light();
    }
    
    public void pickInvincibility() {
    	this.invincibility.addItem(5);
    }
    public void useInvincibility() {
    	this.invincibility.useItem();
    }
    
}
