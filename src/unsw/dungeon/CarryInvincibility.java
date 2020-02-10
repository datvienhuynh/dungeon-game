package unsw.dungeon;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class CarryInvincibility implements CarryItem {
	
	private Player player;
	private int count;
	
	private Timeline timeline;	
	private FadeTransition fadeTransition;
	
	public CarryInvincibility(Player player) {
		this.player = player;
		this.count = 0;
		this.timeline = new Timeline(
			new KeyFrame(Duration.millis(1000), e -> {
				useItem();
			})
		);
		this.timeline.setCycleCount(Timeline.INDEFINITE);
	}

	@Override
	public boolean hasItem() {
		if (count > 0) return true;
		return false;
	}

	@Override
	public int getItem() {
		return count;
	}
	
	// run the timeline and fade transition 
	// right after collecting a potion
	public void addItem(int i) {
		count = i;
		fadeTransition = player.getController().newFadeTransition
						 (500, player, Animation.INDEFINITE);
		fadeTransition.play();
		timeline.play();
	}
	
	public void useItem() {
		try {
			if (count == 0) {
				timeline.stop();
				fadeTransition.stop();
				player.getController().getFadeAnimations().remove(fadeTransition);
				throw new Exception("Player is not in Invincibility Mode");
			}
		} catch(Exception e) {
			System.out.println(e);
			return;
		}
		this.count--;
	}
	
	public Timeline getTimeline() {
		return timeline;
	}
	
	public String string() {
		if (count == 0)
			return "No Invincibility Potion";
		else
			return "Invincibility Potion";
	}

}
