package unsw.dungeon;

public class FloorSwitch extends Entity {
		
	public FloorSwitch(int x, int y) {
		super(x, y);
		this.setMoveThrough(true);
		this.setBePicked(false);
	}

}
