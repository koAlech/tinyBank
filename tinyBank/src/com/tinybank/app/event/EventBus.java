package com.tinybank.app.event;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class EventBus {

	private static final Bus bus = new Bus(ThreadEnforcer.MAIN);
	
	public static void post(Context context, Object event) {
		try {
			bus.post(event);
		} catch (Exception e) {
		}
	}
	
	public static void postOnMain(final Context context, final Object event) {
		Handler mainThread = new Handler(Looper.getMainLooper());
		mainThread.post(new Runnable() {
			@Override
			public void run() {
				try {
					bus.post(event);
				} catch (Exception e) {
				}
			}
		});
	}

	public static void register(Object object) {
		bus.register(object);
	}

	public static void unregister(Object object) {
		bus.unregister(object);
	}

	@Override
	public String toString() {
		return bus.toString();
	}
	
}
