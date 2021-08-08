package com.yzj.dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.yzj.utils.UiUtil;
import android.support.v7.app.AlertDialog.Builder;
import android.content.DialogInterface.OnClickListener;

public class EditTextDialog extends AlertDialog.Builder{
	EditText et;
	public EditTextDialog(Context context){
		super(context);
		et=new EditText(context);
		setView(et);
	}

	@Override
	public EditTextDialog setTitle(CharSequence text) {
		super.setTitle(text);
		return this;
	}

	public EditTextDialog setMessage(CharSequence message) {
		et.setText(message);
		return this;
	}

	@Override
	public EditTextDialog setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
		super.setPositiveButton(text, listener);
		return this;
	}

	@Override
	public EditTextDialog setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
		super.setNegativeButton(text, listener);
		return this;
	}

	
	
	@Override
	public AlertDialog show() {
		super.show();
		FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams) et.getLayoutParams();
		lp.setMarginEnd(UiUtil.dp2px(24));
		lp.setMarginStart(UiUtil.dp2px(24));

		new Handler().postDelayed(new Runnable(){
				@Override
				public void run() {
					InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
				}
			},200);
		return this.create();
	}
	
	public EditTextDialog setSelection(int start,int end){
		et.setSelection(start,end);
		return this;
	}
	
	public String getMessage(){
		return et.getText().toString();
	}
	
}
