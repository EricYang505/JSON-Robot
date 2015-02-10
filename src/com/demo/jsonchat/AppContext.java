package com.demo.jsonchat;

import com.demo.jsonchat.service.ChatDataManager;

import android.app.Application;

public class AppContext extends Application{
	public static ChatDataManager chatDataManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		chatDataManager = new ChatDataManager();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		chatDataManager = null;
	}
}
