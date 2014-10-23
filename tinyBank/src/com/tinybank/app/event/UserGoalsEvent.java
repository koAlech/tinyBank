package com.tinybank.app.event;

import java.util.ArrayList;

import com.tinybank.app.bean.Goal;

public class UserGoalsEvent {
	private boolean success;
	private ArrayList<Goal> goals;
	public UserGoalsEvent(boolean success, ArrayList<Goal> goals) {
		super();
		this.success = success;
		this.goals = goals;
	}
	public boolean isSuccess() {
		return success;
	}
	public ArrayList<Goal> getGoals() {
		return goals;
	}
}
