package com.tinyband.app.backend;

import android.content.Context;

import com.raweng.built.Built;
import com.tinybank.app.bean.User;

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
	public static User login(String username, String password) {
		User user = null;
		
		return user;
	}
}
