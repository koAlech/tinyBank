package com.tinybank.app.event;


public class BankAccountEvent {
	private boolean success;
	private String bank_account_id;
	private String bank_type;
	private String bank_name;
	private Double balance;
	public BankAccountEvent(boolean success, String bank_account_id,
			String bank_type, String bank_name, Double balance) {
		super();
		this.success = success;
		this.bank_account_id = bank_account_id;
		this.bank_type = bank_type;
		this.bank_name = bank_name;
		this.balance = balance;
	}
	public boolean isSuccess() {
		return success;
	}
	public String getBank_account_id() {
		return bank_account_id;
	}
	public String getBank_type() {
		return bank_type;
	}
	public String getBank_name() {
		return bank_name;
	}
	public Double getBalance() {
		return balance;
	}
}
