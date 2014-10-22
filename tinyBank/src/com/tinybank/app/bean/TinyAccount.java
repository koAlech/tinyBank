package com.tinybank.app.bean;

public class TinyAccount {
	private String username;
	private Double balance;
	public TinyAccount(String username, Double balance) {
		super();
		this.username = username;
		this.balance = balance;
	}
	public String getUsername() {
		return username;
	}
	public Double getBalance() {
		return balance;
	}
	
}
