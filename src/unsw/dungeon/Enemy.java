package unsw.dungeon;

public class Enemy extends Entity {
	
	private Dungeon dungeon;
	
	EnemyBehaviour hunt;
	EnemyBehaviour runAway;
	
	public Enemy(int x, int y) {
        super(x, y);
        this.hunt = new Hunt(this);
        this.runAway = new RunAway(this);
        this.setMoveThrough(true);
        this.setBePicked(false);
    }
	
	public void addDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}
	
	public void hunt() {
		hunt.move(dungeon);
	}
	
	public void runAway() {
		runAway.move(dungeon);
	}

}
