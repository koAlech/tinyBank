package com.tinybank.app.backend;

import android.content.Context;
import android.util.Log;

import com.raweng.built.Built;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.BuiltUser;
import com.tinybank.app.bean.User;

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

	public static User login(String username, String password) {
		User user = null;

		BuiltUser builtUserObject = new BuiltUser();
		builtUserObject.login(username, password,
				new BuiltResultCallBack() {
					@Override
					public void onSuccess() {
						// user has logged in successfully
						// builtUserObject.authtoken contains the session
						// authtoken
					}

					@Override
					public void onError(BuiltError builtErrorObject) {
						// login failed
						// the message, code and details of the error
						Log.i("error: ",
								"" + builtErrorObject.getErrorMessage());
						Log.i("error: ", "" + builtErrorObject.getErrorCode());
						Log.i("error: ", "" + builtErrorObject.getErrors());
					}

					@Override
					public void onAlways() {
						// write code here that you want to execute
						// regardless of success or failure of the operation
					}
				});

		return user;
	}
}
