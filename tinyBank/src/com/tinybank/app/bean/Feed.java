package com.tinybank.app.bean;

public class Feed {
	private String uid;
	private String username;
	private String date;
	private String type; //deposit / badge / ....
	private String description;
	private Double amount;
	private String status;
	private boolean liked;
	
	public Feed(String uid, String username, String date, String type,
			String description, Double amount, String status, boolean liked) {
		super();
		this.uid = uid;
		this.username = username;
		this.date = date;
		this.type = type;
		this.description = description;
		this.amount = amount;
		this.status = status;
		this.liked = liked;
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
	public String getType() {
		return type;
	}
	public String getDescription() {
		return description;
	}
	public Double getAmount() {
		return amount;
	}
	public String getStatus() {
		return status;
	}
	public boolean isLiked() {
		return liked;
	}
	
	
}
