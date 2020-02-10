package unsw.dungeon;

public class CarrySword implements CarryItem {

	private int count;
	
	public CarrySword() {
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
				throw new Exception("Player has no sword");
			}
		} catch(Exception e) {
			System.out.println(e);
			return;
		}
		this.count--;
	}
	
	public String string() {
		return "Sword x " + count;
	}
	
}
