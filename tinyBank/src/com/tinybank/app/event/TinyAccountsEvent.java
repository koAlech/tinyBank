package com.tinybank.app.event;

import java.util.ArrayList;

import com.tinybank.app.bean.TinyAccount;

public class TinyAccountsEvent {
	private boolean success;
	private ArrayList<TinyAccount> tinyAccounts;
	public TinyAccountsEvent(boolean success,
			ArrayList<TinyAccount> tinyAccounts) {
		super();
		this.success = success;
		this.tinyAccounts = tinyAccounts;
	}
	public boolean isSuccess() {
		return success;
	}
	public ArrayList<TinyAccount> getTinyAccounts() {
		return tinyAccounts;
	}
}
