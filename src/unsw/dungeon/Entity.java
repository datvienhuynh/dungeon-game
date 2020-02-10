package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class Entity {

    // IntegerProperty is used so that changes to the entities position can be
    // externally observed.
    private IntegerProperty x, y;
    
    private boolean canMoveThrough = true;
    private boolean canBePicked = true;

    /**
     * Create an entity positioned in square (x,y)
     * @param x
     * @param y
     */
    public Entity(int x, int y) {
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
    }

    public IntegerProperty x() {
        return x;
    }

    public IntegerProperty y() {
        return y;
    }

    public int getY() {
        return y().get();
    }

    public int getX() {
        return x().get();
    }
    
    public boolean canMoveThrough() {
    	return this.canMoveThrough;
    }
    
    public void setMoveThrough(boolean bool) {
    	this.canMoveThrough = bool;
    }
    
    public boolean canBePicked() {
    	return this.canBePicked;
    }
    
    public void setBePicked(boolean bool) {
    	this.canBePicked = bool;
    }
    
}
