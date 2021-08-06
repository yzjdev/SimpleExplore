package com.yzj.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipBoardUtil {

	public static void copyText(final CharSequence text,String tip) {
		copyText(text);
		ToastUtil.show("已写入剪切板 "+tip);
	}
	public static void copyText(final CharSequence text) {
		ClipboardManager cm=(ClipboardManager) Utils.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
		cm.setPrimaryClip(ClipData.newPlainText(Utils.getApp().getPackageName(), text));
	}
	
	public static CharSequence getText() {
		ClipboardManager cm=(ClipboardManager) Utils.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clipData=cm.getPrimaryClip();
		if(clipData!=null && clipData.getItemCount()>0){
			CharSequence text=clipData.getItemAt(0).coerceToText(Utils.getApp());
			if(text!=null){
				return text;
			}
		}
		return "";
	}
    
}
