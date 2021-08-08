package com.yzj.utils;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.graphics.drawable.Drawable;

public class ResUtil {
	public static Drawable getSelectableItemBackground(){
		return getTypedArray(android.R.attr.selectableItemBackground).getDrawable(0);
	}
	public static TypedArray getTypedArray(int...ids){
		TypedValue typedValue = new TypedValue();
		return Utils.getApp().getTheme().obtainStyledAttributes(typedValue.resourceId, ids);
	}
}
