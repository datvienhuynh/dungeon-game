package unsw.dungeon;

public class Exit extends Entity {
		
	public Exit(int x, int y) {
		super(x, y);
		this.setMoveThrough(false);
		this.setBePicked(false);
	}
	 
	public void open() {
		this.setMoveThrough(true);
	}
	
	public void close() {
		this.setMoveThrough(false);
	}

}
