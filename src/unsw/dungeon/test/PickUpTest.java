package unsw.dungeon.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.junit.Test;

import unsw.dungeon.Bomb;
import unsw.dungeon.CarrySword;
import unsw.dungeon.Dungeon;
import unsw.dungeon.InvincibilityPotion;
import unsw.dungeon.Key;
import unsw.dungeon.Player;
import unsw.dungeon.Sword;
import unsw.dungeon.Treasure;

public class PickUpTest {

	public Dungeon DungeonLoader(String filename) throws FileNotFoundException {
		DungeonLoaderForTest dungeonLoader = new DungeonLoaderForTest(filename);
		Dungeon dungeon = dungeonLoader.load();
		return dungeon;
	}
	
	// carry and pickup key and unlock
	@Test
	public void testUS6() throws FileNotFoundException {
		Dungeon dungeon = this.DungeonLoader("advanced.json");
		Player player = dungeon.getPlayer();
		int initX = player.getX();
		int initY = player.getY();
		Key key1 = new Key(initX+1,initY);
		key1.setID(1);
		dungeon.addEntity(key1);
		player.moveRight();
		
		assertTrue(player.key.hasItem());
		assertEquals(player.key.getItem(), key1.getID());		
	}

	// pickup sword
	@Test
	public void testUS7() throws FileNotFoundException {
		Dungeon dungeon = this.DungeonLoader("advanced.json");
		Player player = dungeon.getPlayer();
		int initX = player.getX();
		int initY = player.getY();
		Sword sword1 = new Sword(initX+1,initY);
		dungeon.addEntity(sword1);
		player.moveRight();
		
		assertTrue(player.sword.hasItem());
		assertEquals(player.sword.getItem(), 5);
	}
	
	// pickup bomb
	@Test
	public void testUS8() throws FileNotFoundException {
		Dungeon dungeon = this.DungeonLoader("advanced.json");
		Player player = dungeon.getPlayer();
		int initX = player.getX();
		int initY = player.getY();
		Bomb bomb1 = new Bomb(dungeon,initX+1,initY+1);
		dungeon.addEntity(bomb1);
		player.moveRight();
		player.pickBomb();
		
		assertTrue(player.bomb.hasItem());
		assertEquals(player.bomb.getItem(), 1);
	}
	
	// light up and drop bombs
	@Test
	public void testUS9() throws FileNotFoundException {
		Dungeon dungeon = this.DungeonLoader("advanced.json");
		Player player = dungeon.getPlayer();
		int initX = player.getX();
		int initY = player.getY();
		Bomb bomb1 = new Bomb(dungeon,initX+1,initY);
		dungeon.addEntity(bomb1);
		player.moveRight();
		
		assertTrue(player.bomb.hasItem());
		assertEquals(player.bomb.getItem(), 1);
		player.moveRight();
		player.useBomb();
		assertFalse(player.bomb.hasItem());
		assertEquals(player.bomb.getItem(), 0);
		
	}
	// pickup invincibility
	@Test
	public void testUS10() throws FileNotFoundException {
		Dungeon dungeon = this.DungeonLoader("advanced.json");
		Player player = dungeon.getPlayer();
		int initX = player.getX();
		int initY = player.getY();
		InvincibilityPotion potion1 = new InvincibilityPotion(initX+1,initY);
		dungeon.addEntity(potion1);
		player.moveRight();
		
		assertTrue(player.invincibility.hasItem());
		assertEquals(player.invincibility.getItem(), 5);
	}
	
	// see and pickup treasure
	@Test
	public void testUS11() throws FileNotFoundException {
		Dungeon dungeon = this.DungeonLoader("advanced.json");
		Player player = dungeon.getPlayer();
		int initX = player.getX();
		int initY = player.getY();
		Treasure treasure1 = new Treasure(initX+1,initY);
		dungeon.addEntity(treasure1);
		player.moveRight();
		
		assertFalse(dungeon.getEntities(initX+1,initY).contains(treasure1));
	}
	
	
	
}
