package unsw.dungeon;

public class RunAway implements EnemyBehaviour {
	
	private Enemy enemy;
	
	public RunAway(Enemy enemy) {
		this.enemy = enemy;
	}

	@Override
	public void move(Dungeon dungeon) {
		
		Player player = dungeon.getPlayer();
		int enemyX = enemy.getX();
		int enemyY = enemy.getY();
		int playerX = player.getX();
		int playerY = player.getY();
				
		if (enemyX > playerX && dungeon.canMoveTo(enemyX + 1, enemyY)) {
			enemy.x().set(enemyX + 1);
			enemy.y().set(enemyY);
		}
		else if (enemyX < playerX && dungeon.canMoveTo(enemyX - 1, enemyY)) {
			enemy.x().set(enemyX - 1);
			enemy.y().set(enemyY);
		}
		else if (enemyY > playerY && dungeon.canMoveTo(enemyX, enemyY + 1)) {
			enemy.x().set(enemyX);
			enemy.y().set(enemyY + 1);
		}
		else if (enemyY < playerY && dungeon.canMoveTo(enemyX, enemyY - 1)) {
			enemy.x().set(enemyX);
			enemy.y().set(enemyY - 1);
		}
		else if (dungeon.canMoveTo(enemyX + 1, enemyY)) {
			enemy.x().set(enemyX + 1);
			enemy.y().set(enemyY);
		}
		else if (dungeon.canMoveTo(enemyX, enemyY + 1)) {
			enemy.x().set(enemyX);
			enemy.y().set(enemyY + 1);
		}
		else if (dungeon.canMoveTo(enemyX - 1, enemyY)) {
			enemy.x().set(enemyX - 1);
			enemy.y().set(enemyY);
		}
		else if (dungeon.canMoveTo(enemyX, enemyY - 1)){
			enemy.x().set(enemyX);
			enemy.y().set(enemyY - 1);
		}
	}

}
