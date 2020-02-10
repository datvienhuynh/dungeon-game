package unsw.dungeon;

public class InvincibilityPotion extends Entity {
	
	public InvincibilityPotion(int x, int y) {
        super(x, y);
        this.setMoveThrough(true);
        this.setBePicked(true);
    }

}
