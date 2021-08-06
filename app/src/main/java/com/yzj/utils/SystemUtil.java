package com.yzj.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.view.inputmethod.InputMethodManager;

public class SystemUtil {
	/**
     * sgf
     * 强制隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘
    }

	public static boolean isM() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

	public static boolean isAboveR() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
	}


	//是否隐私目录
	public static boolean isPrivacyFile(String path) {
		String root=Environment.getExternalStorageDirectory().getAbsolutePath();
		String data= root + "/Android/data";
		String obb=root + "/Android/obb";
		return isAboveR() && (path.startsWith(data) || path.startsWith(obb));
	}

}
