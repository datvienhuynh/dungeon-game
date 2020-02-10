package unsw.dungeon;

public class Subgoal extends GoalComponent {

	private String name;
	private boolean accomplished;
	
	public Subgoal(String name) {
		this.name = name;
		this.accomplished = false;
	}
	
	public void complete() {
		accomplished = true;
	}
	
	public void complete(String name) {
		if (name.equals(getName())) {
			accomplished = true;
		}
	}
	
	public void incomplete(String name) {
		if (name.equals(getName())) {
			accomplished = false;
		}
	}
	
	public boolean isAccomplished() {
		return accomplished;
	}
	
	public String getName() {
		return name;
	}
	
	public void print() {
		System.out.println("\n-----------");
		System.out.println(name);
	}
	
	public String string() {
		String subgoal = "- " + name + ": ";
		if (isAccomplished())
			subgoal += "accomplished";
		else 
			subgoal += "not accomplished";
		
		return subgoal;
	}
	
}