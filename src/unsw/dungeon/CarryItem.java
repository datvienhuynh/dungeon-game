package unsw.dungeon;

public interface CarryItem {
	public void addItem(int i);
	public boolean hasItem();
	public int getItem();
	public void useItem();
	public String string();
}
