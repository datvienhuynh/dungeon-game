package unsw.dungeon;

public class Door extends Entity {
	
	private int id;
	
	 public Door(int x, int y) {
        super(x, y);
        this.setMoveThrough(false);
        this.setBePicked(false);
	 }
	 
	 public int getID() {
		 return id;
	 }
	 
	 public void setID(int id) {
		 this.id = id;
	 }
	 
	 public void open() {
		 this.setMoveThrough(true);
	 }

}
