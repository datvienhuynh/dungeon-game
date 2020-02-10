package unsw.dungeon;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Bomb extends Entity {
	
	// Timeline transforms states of Bomb
	private Timeline timeline;
	
	private BombState state;
	private Dungeon dungeon;
	
	public Bomb(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
        this.setState(new Unlit(this));
        this.setMoveThrough(true);
        this.setBePicked(true);
        this.timeline = new Timeline(
			new KeyFrame(Duration.millis(500), e -> {
				this.lit();
			}
		));
        this.timeline.setCycleCount(5);
    }
	
	// start the timeline
	public void light() {
		timeline.play();
	}
	
	// pause the timeline of Bomb
	public void pause() {
		timeline.pause();
	}
	
	public BombState getState() {
		return state;
	}
	
	public void setState(BombState state) {
		this.state = state;
	}
	
	public Dungeon getDungeon() {
		return dungeon;
	}
	
	public boolean isExplode() {
		if (state instanceof Explode) 
			return true;
		return false;
	}
	
	// change the state of Bomb and update the game
	public void lit() {
		state.lit();
		dungeon.updateDungeon();
	}

}
