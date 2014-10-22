package com.tinybank.app.event;

import com.tinybank.app.bean.User;

public class LoginEvent {

	private User user;
	private boolean success;
	public LoginEvent(User user, boolean success) {
		super();
		this.user = user;
		this.success = success;
	}
	public User getUser() {
		return user;
	}
	public boolean isSuccess() {
		return success;
	}

	
	}
