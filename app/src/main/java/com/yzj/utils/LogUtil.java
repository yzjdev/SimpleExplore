package com.yzj.utils;

import android.content.Context;
import android.util.Log;
import com.yzj.eplorer.LogActivity;

public class LogUtil {
    
	public static void d(Context context, Object ...obj){
		String tag=context==null?"aaa": context.getClass().getName();
		Log.d(tag, DateTimeUtil.currTime("MM-dd HH:mm:ss")+"   "+ArrayUtil.toString(obj));
	}
	
	
    public static void d(Object ...obj){
		d(null,obj);
	}
    
}
