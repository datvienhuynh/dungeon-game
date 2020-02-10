package unsw.dungeon;

import java.util.LinkedList;
import java.util.Queue;

public class Hunt implements EnemyBehaviour {
	
	private Enemy enemy;
	
	public Hunt(Enemy enemy) {
		this.enemy = enemy;
	}

	@Override
	public void move(Dungeon dungeon) {
				
		IntArray nextMove = decideMove(dungeon);
		
		// change enemy's position and update the game
		enemy.x().set(nextMove.srcX);
		enemy.y().set(nextMove.srcY);
	}
	
	/*
	 * returns the next square (adjacent up, down, left or right)
	 * an enemy will move to
	 * implements the breadth first search to find the shortest path
	 * from an enemy to the player
	 * 
	 */
	public IntArray decideMove(Dungeon dungeon) {
		
		Player player = dungeon.getPlayer();
		int width = dungeon.getWidth();
		int height = dungeon.getHeight();
		int enemyX = enemy.getX();
		int enemyY = enemy.getY();
		int playerX = player.getX();
		int playerY = player.getY();
		
		// implements BFS algorithm to find a path from enemy to player
		IntArray visited[][] = new IntArray[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				visited[i][j] = new IntArray(-1, -1, -1, -1);
			}
		}
		 
		boolean found = false;
		
		visited[enemyX][enemyY].set(enemyX, enemyY, enemyX, enemyY);
		
		Queue<IntArray> q = new LinkedList<IntArray>();
		q.add(visited[enemyX][enemyY]);
		
		while (!(found) && !(q.isEmpty())) {
			
			IntArray v = q.remove();

			if (v.srcX == playerX && v.srcY == playerY)
				found = true;
			else {
				if (dungeon.canMoveTo(v.srcX, v.srcY - 1)) {
					if (visited[v.srcX][v.srcY - 1].srcX == -1) {
						visited[v.srcX][v.srcY - 1].set
						(v.srcX, v.srcY - 1, v.srcX, v.srcY);
						q.add(visited[v.srcX][v.srcY - 1]);
					}
				}
				if (dungeon.canMoveTo(v.srcX, v.srcY + 1)) {
					if (visited[v.srcX][v.srcY + 1].srcX == -1) {
						visited[v.srcX][v.srcY + 1].set
						(v.srcX, v.srcY + 1, v.srcX, v.srcY);
						q.add(visited[v.srcX][v.srcY + 1]);
					}
				}
				if (dungeon.canMoveTo(v.srcX - 1, v.srcY)) {
					if (visited[v.srcX - 1][v.srcY].srcX == -1) {
						visited[v.srcX - 1][v.srcY].set
						(v.srcX - 1, v.srcY, v.srcX, v.srcY);
						q.add(visited[v.srcX - 1][v.srcY]);
					}
				}
				if (dungeon.canMoveTo(v.srcX + 1, v.srcY)) {
					if (visited[v.srcX + 1][v.srcY].srcX == -1) {
						visited[v.srcX + 1][v.srcY].set
						(v.srcX + 1, v.srcY, v.srcX, v.srcY);
						q.add(visited[v.srcX + 1][v.srcY]);
					}
				}
			}
		}
		
		int nextX; int nextY;
		
		// if found, return the first move in the path
		if (found) {
			
			nextX = playerX;
			nextY = playerY;

			while (visited[nextX][nextY].destX != enemyX || 
				   visited[nextX][nextY].destY != enemyY) {
				
				int tmpX = nextX; int tmpY = nextY;
				nextX = visited[tmpX][tmpY].destX;
				nextY = visited[tmpX][tmpY].destY;
			}

			return visited[nextX][nextY];
		}
		// otherwise, moving based on positions of enemy and player
		else {
			if (enemyX > playerX && dungeon.canMoveTo(enemyX - 1, enemyY)) {
				nextX = enemyX - 1;
				nextY = enemyY;
			}
			else if (enemyX < playerX && dungeon.canMoveTo(enemyX + 1, enemyY)) {
				nextX = enemyX + 1;
				nextY = enemyY;
			}
			else if (enemyY > playerY && dungeon.canMoveTo(enemyX, enemyY - 1)) {
				nextX = enemyX;
				nextY = enemyY - 1;
			}
			else if (enemyY < playerY && dungeon.canMoveTo(enemyX, enemyY + 1)) {
				nextX = enemyX;
				nextY = enemyY + 1;
			}
			else if (dungeon.canMoveTo(enemyX + 1, enemyY)) {
				nextX = enemyX + 1;
				nextY = enemyY;
			}
			else if (dungeon.canMoveTo(enemyX, enemyY + 1)) {
				nextX = enemyX;
				nextY = enemyY + 1;
			}
			else if (dungeon.canMoveTo(enemyX - 1, enemyY)) {
				nextX = enemyX - 1;
				nextY = enemyY;
			}
			else {
				nextX = enemyX;
				nextY = enemyY - 1;
			}
			
			IntArray nextMove = new IntArray(nextX, nextY, 0, 0);
			return nextMove;
		}
	}

}
