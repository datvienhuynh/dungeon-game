package unsw.dungeon;

public class Boulder extends Entity {
		
	public Boulder(int x, int y) {
		super(x, y);
		this.setMoveThrough(false);
		this.setBePicked(false);
	}

}
