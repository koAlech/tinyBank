package com.tinybank.app.event;

public class FeedUpdateEvent {
	private double amount;
	private boolean success;
	public FeedUpdateEvent(double amount, boolean success) {
		super();
		this.amount = amount;
		this.success = success;
	}
	public double getAmount() {
		return amount;
	}
	public boolean isSuccess() {
		return success;
	}
}
