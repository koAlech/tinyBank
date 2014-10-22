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
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.squareup.otto.Subscribe;
import com.tinybank.app.R;
import com.tinybank.app.backend.Server;
import com.tinybank.app.event.EventBus;
import com.tinybank.app.event.LoginEvent;

public class LoginActivity extends Activity {

//	@InjectView(R.id.logo) ImageView logo;
	@InjectView(R.id.userName) EditText userName;
	@InjectView(R.id.password) EditText password;
	@InjectView(R.id.createAccount) TextView createAccount;
	
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
		            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		            Server.login("alik.hochner@gmail.com", "gordon");
		            //Server.login(userName.getText().toString(), password.getText().toString());
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
	public void onLoginFinished(LoginEvent loginEvent) {
		//Log.e("tinybank", "login success=" + loginEvent.isSuccess());
		//Log.e("tinybank", "login user=" + loginEvent.getUser());
		if (loginEvent.isSuccess()) {
			//Toast.makeText(getApplicationContext(), "login =>"+loginEvent.getUser(), Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
			startActivity(intent);
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
