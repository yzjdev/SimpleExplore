package com.yzj.dialog;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import com.yzj.widget.FixProgressBar;

public class LoadingDialog {
	public AlertDialog dialog;
	Activity activity;
	public LoadingDialog(Activity activity) {
		this.activity = activity;
		dialog = new AlertDialog.Builder(activity)
			.setView(new FixProgressBar(activity).setColor(Color.WHITE)).create();
	}

	public boolean isShowing() {
		if (dialog == null)return false;
		return dialog.isShowing();
	}

	public void dismiss() {
		if (dialog == null)return;
		dialog.dismiss();
	}
	public static LoadingDialog showDialog(Activity activity) {
		LoadingDialog dialog= new LoadingDialog(activity);
		dialog.dialog.show();
		dialog.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		return dialog;
	}
}
