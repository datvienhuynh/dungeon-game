package unsw.dungeon;

public class Key extends Entity {
	
	private int id;
	
	public Key(int x, int y) {
        super(x, y);
        this.id = 0;
        this.setMoveThrough(true);
        this.setBePicked(true);
    }
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}

}
