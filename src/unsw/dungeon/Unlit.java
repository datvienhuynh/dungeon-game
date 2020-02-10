package unsw.dungeon;

public class Unlit implements BombState {
	
	private Bomb bomb;
	
	public Unlit(Bomb bomb) {
		this.bomb = bomb;
	}

	@Override
	public void lit() {
		
		Dungeon dungeon = bomb.getDungeon();
		DungeonController controller = dungeon.getController();
		
		// change the image of the bomb
		controller.changeImage(bomb, "/bomb_lit_1.png");    	
    	
		this.bomb.setState(new Lit1(bomb));
		this.bomb.setBePicked(false);
	}
	
	
}
