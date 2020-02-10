package unsw.dungeon;

public class Lit2 implements BombState {
	
	private Bomb bomb;
	
	public Lit2(Bomb bomb) {
		this.bomb = bomb;
	}

	@Override
	public void lit() {
		
		Dungeon dungeon = bomb.getDungeon();
		DungeonController controller = dungeon.getController();
		
		// change the image of the bomb
		controller.changeImage(bomb, "/bomb_lit_3.png");
    	
		bomb.setState(new Lit3(bomb));
	}
}
