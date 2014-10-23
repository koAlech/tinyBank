package com.tinybank.app.event;

import java.util.ArrayList;

import com.tinybank.app.bean.Feed;

public class UserFeedsEvent {
	private boolean success;
	private ArrayList<Feed> feeds;
	public UserFeedsEvent(boolean success, ArrayList<Feed> feeds) {
		super();
		this.success = success;
		this.feeds = feeds;
	}
	public boolean isSuccess() {
		return success;
	}
	public ArrayList<Feed> getFeeds() {
		return feeds;
	}
}
