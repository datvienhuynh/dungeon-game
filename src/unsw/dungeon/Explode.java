package unsw.dungeon;

public class Explode implements BombState {

	private Bomb bomb;
	
	public Explode(Bomb bomb) {
		this.bomb = bomb;
	}

	@Override
	public void lit() {
		Dungeon dungeon = bomb.getDungeon();
		dungeon.removeEntity(bomb);
	}
	
}
