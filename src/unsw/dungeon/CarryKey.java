package unsw.dungeon;

public class CarryKey implements CarryItem {
	
	private int id;
	
	public CarryKey() {
		this.id = 0;
	}

	@Override
	public boolean hasItem() {
		if (id != 0) return true;
		return false;
	}

	@Override
	public int getItem() {
		return id;
	}

	public void addItem(int i) {
		this.id = i;
	}
	
	public void useItem() {
		this.id = 0;
	}
	
	public String string() {
		if (id == 0)
			return "No Key";
		else 
			return "Key ID: " + id;
	}
}
