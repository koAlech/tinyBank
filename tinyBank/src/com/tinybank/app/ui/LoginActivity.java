package com.tinybank.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnLongClick;

import com.squareup.otto.Subscribe;
import com.tinybank.app.R;
import com.tinybank.app.backend.Server;
import com.tinybank.app.bean.User;
import com.tinybank.app.event.BankAccountEvent;
import com.tinybank.app.event.EventBus;
import com.tinybank.app.event.LoginEvent;

public class LoginActivity extends Activity {

//	@InjectView(R.id.logo) ImageView logo;
	@InjectView(R.id.userName) EditText userName;
	@InjectView(R.id.password) EditText password;
	@InjectView(R.id.createAccount) TextView createAccount;
	@InjectView(R.id.main_loading) ViewGroup spinner;
	
	@OnLongClick(R.id.logo) boolean speedLogin() {
		if (userName.getText().toString().equals("shira@tinybank.com")) {
			userName.setText("roni@gmail.com");
		} else {
			userName.setText("shira@tinybank.com");
		}
		password.setText("gordon");
		password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		password.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT);
		return true;
	}
	
	//private User user = null;
	
	private String name = null;
	private Double balance = null;
	private String bank_name = null;
	private String bank_account_id = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.register(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.login);
		ButterKnife.inject(this);
		Server.connect(getApplicationContext());
		
		//userName.setSelected(false);
		userName.setText(getString(R.string.userName));
		userName.setCursorVisible(false);
		userName.setTextColor(getResources().getColor(R.color.light_gray));
		userName.setOnFocusChangeListener(new OnFocusChangeListener() {          
	        public void onFocusChange(View v, boolean hasFocus) {
	            if(hasFocus) {
	            	if (TextUtils.equals(userName.getText(), getString(R.string.userName))) {
						userName.setText("");
						userName.setCursorVisible(true);
						userName.setTextColor(getResources().getColor(R.color.black));
					} 
	            } else {
	            	if (TextUtils.isEmpty(userName.getText()) || TextUtils.equals(userName.getText(), getString(R.string.userName))) {
	            		userName.setText(getString(R.string.userName));
						userName.setCursorVisible(false);
						userName.setTextColor(getResources().getColor(R.color.light_gray));
	            	}
	            }
	        }
	    });
		userName.setFocusable(false);
		userName.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	        	password.setFocusable(true);
	        	password.setFocusableInTouchMode(true);
	        	userName.setFocusable(true);
	        	userName.setFocusableInTouchMode(true);
	            return false;
	        }
	    });
		//password.setSelected(false);
		password.setText(getString(R.string.password));
		password.setInputType(InputType.TYPE_CLASS_TEXT);
		password.setCursorVisible(false);
		password.setTextColor(getResources().getColor(R.color.light_gray));
		password.setOnFocusChangeListener(new OnFocusChangeListener() {          
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					if (TextUtils.equals(password.getText(), getString(R.string.password))) {
						password.setText("");
						password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
						password.setCursorVisible(true);
						password.setTextColor(getResources().getColor(R.color.black));
					} 
				} else {
					if (TextUtils.isEmpty(password.getText()) || TextUtils.equals(password.getText(), getString(R.string.password))) {
						password.setText(getString(R.string.password));
						password.setInputType(InputType.TYPE_CLASS_TEXT);
						password.setCursorVisible(false);
						password.setTextColor(getResources().getColor(R.color.light_gray));
					}
				}
			}
		});
		password.setFocusable(false);
		password.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	        	password.setFocusable(true);
	        	password.setFocusableInTouchMode(true);
	        	userName.setFocusable(true);
	        	userName.setFocusableInTouchMode(true);
	            return false;
	        }
	    });
		password.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					spinner.setVisibility(View.VISIBLE);
		            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//					TODO		            
//		            Server.login("shira@tinybank.com", "gordon");
//		            Server.login("roni@gmail.com", "gordon");
		            Server.login(userName.getText().toString(), password.getText().toString());
//		            Server.login("roni@gmail.com", "gordon");
		            return true;  
		        }
				return false;
			}
		});
		createAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "We Only had 24 Hours", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	@Subscribe
	public void onBankAccountFinished(BankAccountEvent bankAccountEvent) {
		spinner.setVisibility(View.INVISIBLE);
		if (bankAccountEvent.isSuccess()) {
			balance = bankAccountEvent.getBalance();
			bank_name = bankAccountEvent.getBank_name();
			bank_account_id = bankAccountEvent.getBank_account_id();
			Intent intent = new Intent(getApplicationContext(), TinyAccountActivity.class);
			intent.putExtra("name", name);
			intent.putExtra("balance", balance);
			intent.putExtra("bank_name", bank_name);
			intent.putExtra("bank_account_id", bank_account_id);
			startActivity(intent);
			finish();
		}
	}
	
	@Subscribe
	public void onLoginFinished(LoginEvent loginEvent) {
		if (loginEvent.isSuccess()) {
			User user = loginEvent.getUser();
			name = user.getFirst_name();
			if (user.getParent()) {
				Server.getBankAccount(user.getUsername());
			} else {
				spinner.setVisibility(View.INVISIBLE);
				Intent intent = new Intent(getApplicationContext(), ChildFeedActivity.class);
    			intent.putExtra("name", name);
    			startActivity(intent);
    			finish();
			}
		} else {
			userName.setText("");
			password.setText("");
			userName.requestFocus();
			Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		EventBus.unregister(this);
		super.onDestroy();
	}
}
