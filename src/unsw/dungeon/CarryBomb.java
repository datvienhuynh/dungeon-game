package unsw.dungeon;

public class CarryBomb implements CarryItem {

	private int count;
	
	public CarryBomb() {
		this.count = 0;
	}

	@Override
	public boolean hasItem() {
		if (count > 0) return true;
		return false;
	}

	@Override
	public int getItem() {
		return count;
	}

	public void addItem(int i) {
		this.count = i;
	}

	public void useItem() {
		try {
			if (count == 0) {
				throw new Exception("Player has no bomb");
			}
			count--;
		} catch(Exception e) {
			System.out.println(e);
			return;
		}
	}
	
	public String string() {
		return "Bomb x " + count;
	}
	
}
