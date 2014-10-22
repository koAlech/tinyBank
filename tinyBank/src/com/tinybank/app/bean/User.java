package com.tinybank.app.bean;


public class User {
	private String username;
	private Boolean parent;
	private String email;
	private String first_name;
	private String last_name;
	public User(String username, Boolean parent, String email,
			String first_name, String last_name) {
		super();
		this.username = username;
		this.parent = parent;
		this.email = email;
		this.first_name = first_name;
		this.last_name = last_name;
	}
	public String getUsername() {
		return username;
	}
	public Boolean getParent() {
		return parent;
	}
	public String getEmail() {
		return email;
	}
	public String getFirst_name() {
		return first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	
}
