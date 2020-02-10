package unsw.dungeon;

public class Wall extends Entity {
	
    public Wall(int x, int y) {
        super(x, y);
        this.setMoveThrough(false);
        this.setBePicked(false);
    }

}
