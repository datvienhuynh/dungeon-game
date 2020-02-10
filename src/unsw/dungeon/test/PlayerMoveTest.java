package unsw.dungeon.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.junit.Test;

import unsw.dungeon.Bomb;
import unsw.dungeon.Door;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Enemy;
import unsw.dungeon.FloorSwitch;
import unsw.dungeon.GoalComponent;
import unsw.dungeon.Player;

public class PlayerMoveTest {
		
	public Dungeon DungeonLoader(String filename) throws FileNotFoundException {
		DungeonLoaderForTest dungeonLoader = new DungeonLoaderForTest(filename);
		Dungeon dungeon = dungeonLoader.load();
		return dungeon;
	}

	@Test
	public void testUS1MoveInDirections() throws FileNotFoundException {
		
		Dungeon dungeon = this.DungeonLoader("advancednoexit.json");
		Player player = dungeon.getPlayer();
		int initX = player.getX();
		int initY = player.getY();
		
		// test moving down
		player.moveDown();
		assertEquals(player.getX(), initX);
		assertEquals(player.getY(), initY + 1);
		
		// test moving right
		player.moveRight();
		assertEquals(player.getX(), initX + 1);
		assertEquals(player.getY(), initY + 1);
		
		// test moving up
		player.moveUp();
		assertEquals(player.getX(), initX + 1);
		assertEquals(player.getY(), initY);
		
		// test moving left
		player.moveLeft();
		assertEquals(player.getX(), initX);
		assertEquals(player.getY(), initY);
		
		// test moving into a Wall
		player.moveLeft();
		assertEquals(player.getX(), initX);
		assertEquals(player.getY(), initY);
		
		// test moving onto FloorSwitch
		FloorSwitch floorSwitch = (FloorSwitch)dungeon.getEntities(1, 3).get(0);
		player.moveDown();
		player.moveDown();
		assertEquals(player.getX(), floorSwitch.getX());
		assertEquals(player.getY(), floorSwitch.getY());
	}
	
	@Test
	public void testUS2MoveToExit() throws FileNotFoundException {
		
		//test a map with Exit
		Dungeon dungeon = this.DungeonLoader("advancedexit.json");
		Player player = dungeon.getPlayer();
		GoalComponent goals = dungeon.getGoals();
		int initX = player.getX();
		int initY = player.getY();
		
		// check if goals have not been yet accomplished
		GoalComponent exit = goals.getSubgoal(0);
		GoalComponent treasure = goals.getSubgoal(1);
		assertEquals(exit.getName(), "exit");
		assertEquals(treasure.getName(), "treasure");
		assertFalse(goals.isAccomplished());
		
		// test moving into Exit before completing other goals
		player.moveUp();
		assertEquals(player.getX(), initX);
		assertEquals(player.getY(), initY);
		
		// collect Treasure
		player.moveDown(); player.moveDown();
		assertTrue(treasure.isAccomplished());
		assertFalse(exit.isAccomplished());
		
		// test moving into Exit after completing other goals
		player.moveUp(); player.moveUp(); player.moveUp();
		assertTrue(exit.isAccomplished());
		assertTrue(goals.isAccomplished());
		
		// test a map without Exit
		dungeon = this.DungeonLoader("advancednoexit.json");
		player = dungeon.getPlayer();
		goals = dungeon.getGoals();
		
		// check if goals have not been yet accomplished
		GoalComponent boulders = goals.getSubgoal(0);
		treasure = goals.getSubgoal(1);
		assertEquals(boulders.getName(), "boulders");
		assertEquals(treasure.getName(), "treasure");
		assertFalse(goals.isAccomplished());
		
		// collect Treasure
		player.moveRight(); player.moveRight();
		assertTrue(treasure.isAccomplished());
		
		// push Boulder onto Switch
		player.moveLeft();
		for (int i = 0; i < 4; i++) {
			player.moveDown();
		}
		player.moveLeft(); player.moveUp();
		for (int i = 0; i < 11; i++)
			player.moveDown();
		for (int i = 0; i < 6; i++)
			player.moveRight();
		player.moveUp(); player.moveLeft();
		player.moveUp(); player.moveRight();
		
		assertTrue(boulders.isAccomplished());
		assertTrue(goals.isAccomplished());
	}
	
	@Test
	public void testUS3MoveToDoor() throws FileNotFoundException {
		
		Dungeon dungeon = this.DungeonLoader("advancedexit.json");
		Player player = dungeon.getPlayer();
		Door door = (Door)dungeon.getEntities(6, 1).get(0);
		
		// try moving through a locked door without a key
		assertFalse(player.key.hasItem());
		assertFalse(door.canMoveThrough());
		for (int i = 0; i < 5; i++) 
			player.moveRight();
		// player can not move into it
		assertTrue(player.getX() != door.getX());
		assertEquals(player.getX(), door.getX() - 1);
		assertEquals(player.getY(), door.getY());
		
		// collect a key
		player.moveLeft(); player.moveDown();
		assertTrue(player.key.hasItem());
		// the key player is carrying has different ID with the door
		assertTrue(player.key.getItem() != door.getID());
		// try moving through the door with wrong key
		player.moveUp(); player.moveRight(); player.moveRight();
		assertTrue(player.getX() != door.getX());
		assertEquals(player.getX(), door.getX() - 1);
		assertEquals(player.getY(), door.getY());
		
		// collect another key
		player.moveLeft(); player.moveDown(); player.moveRight();
		assertTrue(player.key.hasItem());
		// the key player is carrying has same ID with the door
		assertTrue(player.key.getItem() == door.getID());
		// try moving through the door with the corresponding key
		player.moveUp(); player.moveRight();
		assertEquals(player.getX(), door.getX());
		assertEquals(player.getY(), door.getY());
		// the door is unlocked now
		assertTrue(door.canMoveThrough());
		// move away from the door, it remains unlocked
		player.moveRight();
		assertTrue(player.getX() != door.getX());
		assertTrue(door.canMoveThrough());
	}
	
	@Test
	public void testUS4MoveToEnemy() throws FileNotFoundException {
		
		Dungeon dungeon = this.DungeonLoader("advancednoexit.json");
		Player player = dungeon.getPlayer();
		ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
		
		// try moving through an enemy without sword or invincibility
		assertFalse(player.sword.hasItem());
		assertFalse(player.invincibility.hasItem());
		player.moveRight(); 
		for (int i = 0; i < 5; i++) 
			player.moveDown(); 
		assertFalse(dungeon.getAllEntities().contains(player));
		assertEquals(testOut.toString(), "PLAYER DIED! GAME OVER!!!\n");
		
		// reload dungeon
		dungeon = this.DungeonLoader("advancednoexit.json");
		player = dungeon.getPlayer();
		
		// pick up a sword
		player.moveRight(); 
		for (int i = 0; i < 4; i++) 
			player.moveDown(); 
		player.moveRight(); 
		// test if player has a sword and how many remaining hits
		assertTrue(player.sword.hasItem());
		assertEquals(player.sword.getItem(), 5);
		// try moving through an enemy with sword
		Enemy enemy = (Enemy)dungeon.getEntities(2, 6).get(0);
		player.moveLeft(); player.moveDown();
		// the enemy has been killed and does not exist in the dungeon
		assertFalse(dungeon.getAllEntities().contains(enemy));
		// the number of remaining sword hits decreases by 1
		assertEquals(player.sword.getItem(), 4);
		// test destroying more enemies by sword
		enemy = (Enemy)dungeon.getEntities(2, 7).get(0);
		player.moveDown();
		assertFalse(dungeon.getAllEntities().contains(enemy));
		assertEquals(player.sword.getItem(), 3);
		enemy = (Enemy)dungeon.getEntities(2, 8).get(0);
		player.moveDown();
		assertFalse(dungeon.getAllEntities().contains(enemy));
		assertEquals(player.sword.getItem(), 2);
		enemy = (Enemy)dungeon.getEntities(2, 9).get(0);
		player.moveDown();
		assertFalse(dungeon.getAllEntities().contains(enemy));
		assertEquals(player.sword.getItem(), 1);
		enemy = (Enemy)dungeon.getEntities(2, 10).get(0);
		player.moveDown();
		assertFalse(dungeon.getAllEntities().contains(enemy));
		assertEquals(player.sword.getItem(), 0);
		
		// try moving through an enemy with no remaining hits
		enemy = (Enemy)dungeon.getEntities(2, 11).get(0);
		player.moveDown();
		assertFalse(dungeon.getAllEntities().contains(player));
		assertEquals(testOut.toString(), "PLAYER DIED! GAME OVER!!!\n"
									   + "PLAYER DIED! GAME OVER!!!\n");
		
		// reload dungeon
		dungeon = this.DungeonLoader("advancednoexit.json");
		player = dungeon.getPlayer();
		
		// pick up an invincibility potion
		player.moveRight(); 
		for (int i = 0; i < 3; i++) 
			player.moveDown(); 
		player.moveRight(); player.moveRight(); player.moveDown(); 
		assertTrue(player.invincibility.hasItem());
		// try moving through an enemy with invincibility
		enemy = (Enemy)dungeon.getEntities(2, 6).get(0);
		player.moveDown(); player.moveLeft(); player.moveLeft();
		// the enemy has been killed and does not exist in the dungeon
		assertFalse(dungeon.getAllEntities().contains(enemy));
	}
	
	@Test
	public void testUS5MoveToBomb() throws FileNotFoundException {
		
		Dungeon dungeon = this.DungeonLoader("advancednoexit.json");
		Player player = dungeon.getPlayer();
		ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        
	    // test moving to an unlit bomb to pick up it
        Bomb bomb = (Bomb)dungeon.getEntities(5, 5).get(0);
		player.moveRight(); 
		for (int i = 0; i < 3; i++) 
			player.moveDown(); 
		for (int i = 0; i < 3; i++) 
			player.moveRight(); 
		player.moveDown(); 
		assertEquals(player.getX(), 5);
		assertEquals(player.getY(), 5);
		// The unlit bomb is picked up and does not exist on map
		assertFalse(dungeon.getAllEntities().contains(bomb));
		assertTrue(player.bomb.hasItem());
	
		// test moving to a lit bomb before it explodes
		// drop a bomb
		player.useBomb();
		bomb = (Bomb)dungeon.getEntities(player.getX(), player.getY()).get(1);
		assertEquals(player.getX(), bomb.getX());
		assertEquals(player.getY(), bomb.getY());
		// test moving to a bomb during its explosion
		for (int i = 0; i < 3; i++)
			bomb.lit();
		assertFalse(dungeon.getAllEntities().contains(player));
		assertEquals(testOut.toString(), "PLAYER DIED! GAME OVER!!!\n");
		
		// load new dungeon
		dungeon = this.DungeonLoader("advancednoexit.json");
		player = dungeon.getPlayer();
		
		// pick up an invincibility potion
		player.moveRight(); 
		for (int i = 0; i < 3; i++) 
			player.moveDown(); 
		player.moveRight(); player.moveRight(); player.moveDown();
		assertTrue(player.invincibility.hasItem());
		// pick up and drop a bomb
		player.moveRight(); 
		player.useBomb();
		bomb = (Bomb)dungeon.getEntities(player.getX(), player.getY()).get(1);
		// test surviving a bomb explosion with invincibility
		for (int i = 0; i < 3; i++)
			bomb.lit();
		assertTrue(bomb.isExplode());
		assertTrue(dungeon.getAllEntities().contains(player));
	}
	
}
