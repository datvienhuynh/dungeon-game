package unsw.dungeon.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.junit.Test;

import unsw.dungeon.Boulder;
import unsw.dungeon.Door;
import unsw.dungeon.Dungeon;
import unsw.dungeon.FloorSwitch;
import unsw.dungeon.GoalComponent;
import unsw.dungeon.Player;
import unsw.dungeon.Subgoal;

public class MoveBoulderTest {

	public Dungeon DungeonLoader(String filename) throws FileNotFoundException {
		DungeonLoaderForTest dungeonLoader = new DungeonLoaderForTest(filename);
		Dungeon dungeon = dungeonLoader.load();
		return dungeon;
	}

	@Test
	public void testUS12() throws FileNotFoundException {
		
		Dungeon dungeon = this.DungeonLoader("advancednoexit.json");
		Player player = dungeon.getPlayer();
		Boulder boulder1 = (Boulder)dungeon.getEntities(1, 4).get(0);
		FloorSwitch floorSwitch = (FloorSwitch)dungeon.getEntities(1, 3).get(0);
		
		// test pushing a boulder to a wall
		player.moveRight();
		for (int i = 0; i < 3; i++)
			player.moveDown();
		assertEquals(player.getX(), 2);
		assertEquals(player.getY(), 4);
		assertEquals(boulder1.getX(), 1);
		assertEquals(boulder1.getY(), 4);
		player.moveLeft();
		// player and boulder stay at the current squares
		assertEquals(player.getX(), 2);
		assertEquals(player.getY(), 4);
		assertEquals(boulder1.getX(), 1);
		assertEquals(boulder1.getY(), 4);
		
		// test pushing the boulder on top of a floor switch
		player.moveDown(); player.moveLeft();
		assertEquals(player.getX(), 1);
		assertEquals(player.getY(), 5);
		assertEquals(boulder1.getX(), 1);
		assertEquals(boulder1.getY(), 4);
		player.moveUp();
		// boulder moves up by one square
		assertEquals(player.getX(), 1);
		assertEquals(player.getY(), 4);
		assertEquals(boulder1.getX(), 1);
		assertEquals(boulder1.getY(), 3);
		// boulder and switch are now in the same square
		assertEquals(boulder1.getX(), floorSwitch.getX());
		assertEquals(boulder1.getY(), floorSwitch.getY());
		
		// test pushing a boulder to a door
		for (int i = 0; i < 11; i++)
			player.moveDown();
		for (int i = 0; i < 6; i++)
			player.moveRight();
		player.moveUp();
		Boulder boulder2 = (Boulder)dungeon.getEntities(7, 12).get(0);
		assertTrue(dungeon.getEntities(7, 11).get(0) instanceof Door);
		assertEquals(player.getX(), 7);
		assertEquals(player.getY(), 13);
		assertEquals(boulder2.getX(), 7);
		assertEquals(boulder2.getY(), 12);
		player.moveUp();
		// player and boulder stay at the current squares
		assertEquals(player.getX(), 7);
		assertEquals(player.getY(), 13);
		assertEquals(boulder2.getX(), 7);
		assertEquals(boulder2.getY(), 12);
	}
	
	@Test
	public void testUS13() throws FileNotFoundException {
		
		Dungeon dungeon = this.DungeonLoader("advancednoexit.json");
		Player player = dungeon.getPlayer();
		GoalComponent goals = dungeon.getGoals();
		
		assertFalse(goals.isAccomplished());
		
		// move boulders to fill all switches
		player.moveRight();
		for (int i = 0; i < 4; i++)
			player.moveDown();
		player.moveLeft();
		player.moveUp();
		
		for (int i = 0; i < 11; i++)
			player.moveDown();
		for (int i = 0; i < 6; i++)
			player.moveRight();
		player.moveUp(); player.moveLeft();
		player.moveUp(); player.moveRight();
		
		// goal 'boulders' completed
		GoalComponent boulders = goals.getSubgoal(0);
		assertEquals(boulders.getName(), "boulders");
		assertTrue(boulders.isAccomplished());
	}
	
}
