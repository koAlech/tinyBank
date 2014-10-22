package com.tinybank.app.backend;

import android.content.Context;
import android.util.Log;

import com.raweng.built.Built;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.tinybank.app.bean.User;
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

	public static void login(String username, String password) {
		
		final BuiltUser builtUserObject = new BuiltUser();
		builtUserObject.login(username, password,
				new BuiltResultCallBack() {
					@Override
					public void onSuccess() {
						// user has logged in successfully
						// builtUserObject.authtoken contains the session authtoken
						String username = builtUserObject.getUserName();
						String email = builtUserObject.getEmailId();
						String first_name = builtUserObject.getFirstName();
						String last_name = builtUserObject.getLastName();
						
						User user = new User(username, email, first_name, last_name);
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
}
