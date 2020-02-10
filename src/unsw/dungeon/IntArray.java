package unsw.dungeon;

public class IntArray {
	
	public int srcX;
	public int srcY;
	public int destX;
	public int destY;
	
	public IntArray(int srcX, int srcY, int destX, int destY) {
		this.srcX = srcX;
		this.srcY = srcY;
		this.destX = destX;
		this.destY = destY;
	}
	
	public void set(int srcX, int srcY, int destX, int destY) {
		this.srcX = srcX;
		this.srcY = srcY;
		this.destX = destX;
		this.destY = destY;
	}
}
