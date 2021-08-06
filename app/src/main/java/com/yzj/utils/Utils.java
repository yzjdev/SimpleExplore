package com.yzj.utils;
import android.app.Application;

public class Utils {
    private static Application mApp;
    
    public static void init(Application app){
		mApp=app;
		CrashHandler.getInstance().init(app);
	}
	
	public static Application getApp(){
		return mApp;
	}
}
