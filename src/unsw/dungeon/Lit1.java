package unsw.dungeon;

public class Lit1 implements BombState {
	
	private Bomb bomb;
	
	public Lit1(Bomb bomb) {
		this.bomb = bomb;
	}

	@Override
	public void lit() {
		
		Dungeon dungeon = bomb.getDungeon();
		DungeonController controller = dungeon.getController();
		
		// change the image of the bomb
		controller.changeImage(bomb, "/bomb_lit_2.png");
    	
		bomb.setState(new Lit2(bomb));
	}

}
