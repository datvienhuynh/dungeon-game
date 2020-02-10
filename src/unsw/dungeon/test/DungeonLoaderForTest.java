package unsw.dungeon.test;

import java.io.FileNotFoundException;

import unsw.dungeon.Bomb;
import unsw.dungeon.Boulder;
import unsw.dungeon.Door;
import unsw.dungeon.DungeonLoader;
import unsw.dungeon.Enemy;
import unsw.dungeon.Entity;
import unsw.dungeon.Exit;
import unsw.dungeon.FloorSwitch;
import unsw.dungeon.InvincibilityPotion;
import unsw.dungeon.Key;
import unsw.dungeon.Sword;
import unsw.dungeon.Treasure;
import unsw.dungeon.Wall;

public class DungeonLoaderForTest extends DungeonLoader {
	
	public DungeonLoaderForTest(String filename) throws FileNotFoundException {
		super(filename);
	}

	public void onLoad(Entity player) {}
	public void onLoad(Wall wall) {}
	public void onLoad(Boulder boulder) {}
	public void onLoad(Exit Exit) {}
	public void onLoad(FloorSwitch FloorSwitch) {}
	public void onLoad(Door door) {}
	public void onLoad(Enemy enemy) {}
	public void onLoad(Treasure treasure) {}
	public void onLoad(Bomb bomb) {}
	public void onLoad(Sword sword) {}
	public void onLoad(Key key) {}
	public void onLoad(InvincibilityPotion InvincibilityPotion) {}

}
