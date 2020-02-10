package unsw.dungeon;

import java.util.ArrayList;

public class Goal extends GoalComponent {
	
	private ArrayList<GoalComponent> goalComponents;
	private boolean and;
	
	public Goal(String logic) {
		this.goalComponents = new ArrayList<GoalComponent>();
		if (logic.equals("AND")) 
			this.and = true;
		else 
			this.and = false;
	}
	
	public void add(GoalComponent goal) {
		goalComponents.add(goal);
	}
	
	public void complete(String name) {
		for (GoalComponent goalComponent : goalComponents) {
			goalComponent.complete(name);
		}
	}
	
	public void incomplete(String name) {
		for (GoalComponent goalComponent : goalComponents) {
			goalComponent.incomplete(name);
		}
	}
	
	public boolean isAccomplished() {
		if (and) {
			for (GoalComponent goalComponent : goalComponents) {
				if (!goalComponent.isAccomplished())
					return false;
			}
			return true;
		}
		else {
			for (GoalComponent goalComponent : goalComponents) {
				if (goalComponent.isAccomplished())
					return true;
			}
			return false;
		}
	}
	
	public GoalComponent getSubgoal(int i) {
		return goalComponents.get(i);
	}
	
	public boolean isAND() {
		return and;
	}
	
	public String getName() {
		if (and) return "AND";
		return "OR";
	}
	
	public void print() {
		if (and)
			System.out.println("AND");
		else
			System.out.println("OR");
		for (GoalComponent goalComponent : goalComponents) {
			goalComponent.print();
		}
	}
	
	public String string() {
		String printGoal = "> ";
		if (and)
			printGoal += "AND";
		else
			printGoal += "OR";
		if (isAccomplished())
			printGoal += ": accomplished" + "\n";
		else
			printGoal += ": not accomplished" + "\n";
		for (GoalComponent goalComponent : goalComponents) {
			printGoal += "	" + goalComponent.string() + "\n";
		}
		
		return printGoal;
	}
	
}