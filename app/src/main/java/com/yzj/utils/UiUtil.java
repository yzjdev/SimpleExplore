package com.yzj.utils;

import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import com.yzj.shadowviewhelper.ShadowProperty;
import com.yzj.shadowviewhelper.ShadowViewDrawable;
import android.view.View;

public class UiUtil {

	public static int dp2px(float value){
        float scale =getDm().density;
        return (int)(value*scale+0.5f);
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
	
	public static void setShadowLeft(View view){
		setShadow(view,ShadowProperty.LEFT);
	}
	public static void setShadowTop(View view){
		setShadow(view,ShadowProperty.TOP);
	}
	public static void setShadowRight(View view){
		setShadow(view,ShadowProperty.RIGHT);
	}

	public static void setShadowBottom(View view){
		setShadow(view,ShadowProperty.BOTTOM);
	}
	

	public static void setShadowAll(View view){
		setShadow(view,ShadowProperty.LEFT|ShadowProperty.TOP|ShadowProperty.RIGHT|ShadowProperty.BOTTOM);
	}
	
	public static void setShadow(View view,int slide){
		// all sides shadow
        ShadowProperty sp = new ShadowProperty()
			.setShadowColor(0x77000000)
			.setShadowDy(UiUtil.dp2px( 0.5f))
			.setShadowRadius(UiUtil.dp2px(2))
			.setShadowSide(slide);
        ShadowViewDrawable sd = new ShadowViewDrawable(sp, Color.WHITE, 0, 0);
        ViewCompat.setBackground(view, sd);
        ViewCompat.setLayerType(view, ViewCompat.LAYER_TYPE_SOFTWARE, null);
	}
    
}
