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
				Built.initializeWithApiKey(context, "blt5d6ff099d763bf99",
						"tinybank");
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
						// builtUserObject.authtoken contains the session
						// authtoken
						String username = (String) builtUserObject.get("username");
						String email = (String) builtUserObject.get("email");
						String first_name = (String) builtUserObject.get("first_name");
						String last_name = (String) builtUserObject.get("last_name");
						Boolean active = (Boolean) builtUserObject.get("active");
						
						User user = new User(username, email, first_name, last_name, active);
						EventBus.postOnMain(context, new LoginEvent(user, true));
						
					}

					@Override
					public void onError(BuiltError builtErrorObject) {
						// login failed
						// the message, code and details of the error
						Log.i("error: ",
								"" + builtErrorObject.getErrorMessage());
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
