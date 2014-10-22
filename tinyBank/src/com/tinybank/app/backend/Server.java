package com.tinybank.app.backend;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.raweng.built.Built;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltObject;
import com.raweng.built.BuiltQuery;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.raweng.built.QueryResult;
import com.raweng.built.QueryResultsCallBack;
import com.tinybank.app.bean.User;
import com.tinybank.app.event.BankAccountEvent;
import com.tinybank.app.event.EventBus;
import com.tinybank.app.event.LoginEvent;

public class Server {

	private static Context context;

	public static void connect(Context context) {

		if (context != null) {
			Server.context = context;
			try {
				Built.initializeWithApiKey(context, "blt5d6ff099d763bf99", "tinybank");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void login(String email, String password) {
		
		final BuiltUser builtUserObject = new BuiltUser();
		builtUserObject.login(email, password,
				new BuiltResultCallBack() {
					@Override
					public void onSuccess() {
						// user has logged in successfully
						// builtUserObject.authtoken contains the session authtoken
						String username = builtUserObject.getUserName();
						String email = builtUserObject.getEmailId();
						String first_name = builtUserObject.getFirstName();
						String last_name = builtUserObject.getLastName();
						String type = (String)builtUserObject.get("user_type");
						//TODO REMOVE
						if (type == null) {
							if (username.equals("alechko")) {
								type = "parent";
							} else {
								type = "tiny";
							}
						}
							
						boolean isParent = (type.equals("parent") ? true : false);
						User user = new User(username, isParent, email, first_name, last_name);
						EventBus.postOnMain(context, new LoginEvent(user, true));
						
					}

					@Override
					public void onError(BuiltError builtErrorObject) {
						Log.i("error: ", "" + builtErrorObject.getErrorMessage());
						Log.i("error: ", "" + builtErrorObject.getErrorCode());
						Log.i("error: ", "" + builtErrorObject.getErrors());
						EventBus.postOnMain(context, new LoginEvent(null, false));
					}

					@Override
					public void onAlways() {
					}
				});
	}
	
	public static void getBankAccount(String username) {

	    BuiltQuery query = new BuiltQuery("bank_account");
	    query.where("username", username);
	    
	    query.exec(new QueryResultsCallBack() {
	    	
		    @Override
		    public void onSuccess(QueryResult queryResultObject) {
			    List<BuiltObject> objects = queryResultObject.getResultObjects();
			    
			    for (Object object : objects) {
			    	
			    	BuiltObject bankAccount = (BuiltObject) object;
			    	String bank_account_id = (String)bankAccount.get("bank_account_id");
			    	String bank_type = (String)bankAccount.get("bank_type");
			    	Double balance = (Double)bankAccount.get("bank_account_balance");
			    	String bank_name = (String)bankAccount.get("bank_name");
			    	EventBus.postOnMain(context, new BankAccountEvent(true, bank_account_id, bank_type, bank_name, balance));
				}
		    }
		    
		    @Override
		    public void onError(BuiltError builtErrorObject) {
			    // query failed
			    // the message, code and details of the error
			    Log.i("error: ", "" + builtErrorObject.getErrorMessage());
			    Log.i("error: ", "" + builtErrorObject.getErrorCode());
			    Log.i("error: ", "" + builtErrorObject.getErrors());
			    EventBus.postOnMain(context, new BankAccountEvent(false, null, null, null, null));
		    }
		    
		    @Override
		    public void onAlways() {
		    }
	    });
	}
}
