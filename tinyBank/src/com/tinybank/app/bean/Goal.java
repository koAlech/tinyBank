package com.tinybank.app.bean;

public class Goal {
	private String uid;
	private String username;
	private String date;
	private String description;
	private Double goalTarget;
	private Double goalBalance;
	public Goal(String uid, String username, String date, String description,
			Double goalTarget, Double goalBalance) {
		super();
		this.uid = uid;
		this.username = username;
		this.date = date;
		this.description = description;
		this.goalTarget = goalTarget;
		this.goalBalance = goalBalance;
	}
	public String getUid() {
		return uid;
	}
	public String getUsername() {
		return username;
	}
	public String getDate() {
		return date;
	}
	public String getDescription() {
		return description;
	}
	public Double getGoalTarget() {
		return goalTarget;
	}
	public Double getGoalBalance() {
		return goalBalance;
	}
	
}
