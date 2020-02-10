package unsw.dungeon;

public abstract class GoalComponent {
	
	public void add(GoalComponent goal) {
		throw new UnsupportedOperationException();
	}
	public void complete(String name) {
		throw new UnsupportedOperationException();
	}
	public void incomplete(String name) {
		throw new UnsupportedOperationException();
	}
	public boolean isAccomplished () {
		throw new UnsupportedOperationException();
	}
	public GoalComponent getSubgoal(int i) {
		throw new UnsupportedOperationException();
	}
	public String getName() {
		throw new UnsupportedOperationException();
	}
	public abstract void print();
	public String string() {
		throw new UnsupportedOperationException();
	}
	
}
