package com.yzj.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.content.res.Resources;

public class UiUtil {

	public static int dp2px(float value){
        float scale =getDm().density;
		
        int ret= (int)(value*scale+0.5f);
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,Resources.getSystem().getDisplayMetrics());
        
    }

    public static int px2dp(float value){
        float scale=getDm().density;
        return (int)(value/scale+0.5f);
    }
    public static int sp2px(float value){
		float scale = getDm().scaledDensity;
        return (int)(value*scale+0.5f);
    }

    public static int px2sp(float value){
        float scale=getDm().scaledDensity;
        return (int)(value/scale+0.5f);
    }
    
	public static int getHeight(){
        return getDm().heightPixels;
    }

    public static int getWidth(){
        return getDm().widthPixels;
    }

    private static DisplayMetrics getDm(){
        return Utils.getApp().getResources().getDisplayMetrics();
    }
    
}
