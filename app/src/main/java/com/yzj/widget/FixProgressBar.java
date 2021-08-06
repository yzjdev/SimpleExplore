package com.yzj.widget;
import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class FixProgressBar extends ProgressBar {
	public FixProgressBar(Context context){
		this(context,null);
	}
	public FixProgressBar(Context context, AttributeSet attrs){
		super(context,attrs);
	}
	
	public FixProgressBar setColor(int color){
		getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
		return this;
	}
}
