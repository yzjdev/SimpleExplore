package com.yzj.eplorer;

import android.app.Application;
import com.yzj.utils.Utils;

public class App extends Application {

	private static App sApp;

	@Override
	public void onCreate() {
		super.onCreate();
		sApp = this;
		Utils.init(this);
	}

	public static App getApp() {
		return sApp;
	}

}
