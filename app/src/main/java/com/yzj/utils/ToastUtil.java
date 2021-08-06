package com.yzj.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;
import com.yzj.eplorer.R;
import android.view.Gravity;

public class ToastUtil {
	
    private Toast toast;
    private ToastUtil(Context context, Object str){
		Toast toast=new Toast(context);
		TextView tv=new TextView(Utils.getApp());
		ShapeUtil.set(tv,UiUtil.dp2px(4),"#eeeeee");
		int padding=24;
		tv.setPadding(padding,padding,padding,padding);
		tv.setTextColor(Color.BLACK);
		tv.setText(str+"");
		tv.setGravity(Gravity.CENTER_VERTICAL);
		Drawable drawable=ContextCompat.getDrawable(Utils.getApp(),R.mipmap.ic_launcher);
		int size=UiUtil.dp2px(24);
		drawable.setBounds(0,0,size,size);
		tv.setCompoundDrawablePadding(10);
		tv.setCompoundDrawables(drawable,null,null,null);

		toast.setView(tv);
		toast.setDuration(Toast.LENGTH_SHORT);
		this.toast=toast;
	}

	public static void show(Object ...str){
		new ToastUtil(Utils.getApp(), ArrayUtil.toString(str)).show();
	}

	private void show(){
		if(toast!=null) toast.show();
	}
    
    
}
