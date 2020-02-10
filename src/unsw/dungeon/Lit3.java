package unsw.dungeon;

public class Lit3 implements BombState {

	private Bomb bomb;
	
	public Lit3(Bomb bomb) {
		this.bomb = bomb;
	}

	@Override
	public void lit() {
		
		Dungeon dungeon = bomb.getDungeon();
		DungeonController controller = dungeon.getController();
		
		// change the image of the bomb
		controller.changeImage(bomb, "/bomb_lit_4.png");
    	
		bomb.setState(new Explode(bomb));
	}
	
}
