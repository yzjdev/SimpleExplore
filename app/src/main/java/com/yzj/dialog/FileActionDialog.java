package com.yzj.dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.view.MotionEvent;
import android.content.DialogInterface;
import android.widget.PopupMenu.OnDismissListener;
import android.graphics.Color;
import com.yzj.utils.DrawableTintUtil;
import com.yzj.eplorer.R;

public class FileActionDialog implements View.OnClickListener{

	private SparseArray<View> mViews=new SparseArray<>();
	private OnItemClickListener listener;
	private View mRootView;

	private AlertDialog dialog;

	//
	public TableRow item1,item2,item3,item4,item5,item6;
	public LinearLayout fun1,fun2,fun3,fun4,fun5,fun6,fun7,fun8,fun9,fun10,fun11,fun12;
	public ImageView icon1,icon2,icon3,icon4,icon5,icon6,icon7,icon8,icon9,icon10,icon11,icon12;
	public TextView title1,title2,title3,title4,title5,title6,title7,title8,title9,title10,title11,title12;
	
	public FileActionDialog(Activity activity) {
		mRootView = View.inflate(activity, R.layout.dialog_file_action, null);
		initView();
		dialog = new AlertDialog.Builder(activity)
			.setView(mRootView)
			.create();

	}
	
	public static FileActionDialog showDialog(Activity activity){
		FileActionDialog dialog= new FileActionDialog(activity);
		dialog.show();
		return dialog;
	}

	private void initView(){
		item1=findView(R.id.item1);
		item2=findView(R.id.item2);
		item3=findView(R.id.item3);
		item4=findView(R.id.item4);
		item5=findView(R.id.item5);
		item6=findView(R.id.item6);

		fun1=findView(R.id.fun1);
		fun2=findView(R.id.fun2);
		fun3=findView(R.id.fun3);
		fun4=findView(R.id.fun4);
		fun5=findView(R.id.fun5);
		fun6=findView(R.id.fun6);
		fun7=findView(R.id.fun7);
		fun8=findView(R.id.fun8);
		fun9=findView(R.id.fun9);
		fun10=findView(R.id.fun10);
		fun11=findView(R.id.fun11);
		fun12=findView(R.id.fun12);

		icon1=findView(R.id.icon1);
		icon2=findView(R.id.icon2);
		icon3=findView(R.id.icon3);
		icon4=findView(R.id.icon4);
		icon5=findView(R.id.icon5);
		icon6=findView(R.id.icon6);
		icon7=findView(R.id.icon7);
		icon8=findView(R.id.icon8);
		icon9=findView(R.id.icon9);
		icon10=findView(R.id.icon10);
		icon11=findView(R.id.icon11);
		icon12=findView(R.id.icon12);


		title1=findView(R.id.title1);
		title2=findView(R.id.title2);
		title3=findView(R.id.title3);
		title4=findView(R.id.title4);
		title5=findView(R.id.title5);
		title6=findView(R.id.title6);
		title7=findView(R.id.title7);
		title8=findView(R.id.title8);
		title9=findView(R.id.title9);
		title10=findView(R.id.title10);
		title11=findView(R.id.title11);
		title12=findView(R.id.title12);

		/*
		 item1.setOnClickListener(this);
		 item2.setOnClickListener(this);
		 item3.setOnClickListener(this);
		 item4.setOnClickListener(this);
		 item5.setOnClickListener(this);
		 item6.setOnClickListener(this);

		 */
		 for(int i=1;i<=12;i++){
			 setIconTint(i,Color.BLACK);
		 }
		fun1.setOnClickListener(this);
		fun2.setOnClickListener(this);
		fun3.setOnClickListener(this);
		fun4.setOnClickListener(this);
		fun5.setOnClickListener(this);
		fun6.setOnClickListener(this);
		fun7.setOnClickListener(this);
		fun8.setOnClickListener(this);
		fun9.setOnClickListener(this);
		fun10.setOnClickListener(this);
		fun11.setOnClickListener(this);
		fun12.setOnClickListener(this);

		/*
		 icon1.setOnClickListener(this);
		 icon2.setOnClickListener(this);
		 icon3.setOnClickListener(this);
		 icon4.setOnClickListener(this);
		 icon5.setOnClickListener(this);
		 icon6.setOnClickListener(this);
		 icon7.setOnClickListener(this);
		 icon8.setOnClickListener(this);
		 icon9.setOnClickListener(this);
		 icon10.setOnClickListener(this);
		 icon11.setOnClickListener(this);
		 icon12.setOnClickListener(this);

		 title1.setOnClickListener(this);
		 title2.setOnClickListener(this);
		 title3.setOnClickListener(this);
		 title4.setOnClickListener(this);
		 title5.setOnClickListener(this);
		 title6.setOnClickListener(this);
		 title7.setOnClickListener(this);
		 title8.setOnClickListener(this);
		 title9.setOnClickListener(this);
		 title10.setOnClickListener(this);
		 title11.setOnClickListener(this);
		 title12.setOnClickListener(this);
		 */
	}

	@Override
	public void onClick(View v) {
		if(listener!=null){
			listener.onClick(v,v.getId());
			dismiss();
		}
	}

	public FileActionDialog setIconTint(int i,int Color){
		switch(i){
			case 1:
				icon1.setImageDrawable(DrawableTintUtil.tintDrawable(icon1.getDrawable(),Color));
				break;
			case 2:
				icon2.setImageDrawable(DrawableTintUtil.tintDrawable(icon2.getDrawable(),Color));
				break;
			case 3:
				icon3.setImageDrawable(DrawableTintUtil.tintDrawable(icon3.getDrawable(),Color));
				break;
			case 4:
				icon4.setImageDrawable(DrawableTintUtil.tintDrawable(icon4.getDrawable(),Color));
				break;
			case 5:
				icon5.setImageDrawable(DrawableTintUtil.tintDrawable(icon5.getDrawable(),Color));
				break;
			case 6:
				icon6.setImageDrawable(DrawableTintUtil.tintDrawable(icon6.getDrawable(),Color));
				break;
			case 7:
				icon7.setImageDrawable(DrawableTintUtil.tintDrawable(icon7.getDrawable(),Color));
				break;
			case 8:
				icon8.setImageDrawable(DrawableTintUtil.tintDrawable(icon8.getDrawable(),Color));
				break;
			case 9:
				icon9.setImageDrawable(DrawableTintUtil.tintDrawable(icon9.getDrawable(),Color));
				break;
			case 10:
				icon10.setImageDrawable(DrawableTintUtil.tintDrawable(icon10.getDrawable(),Color));
				break;
			case 11:
				icon11.setImageDrawable(DrawableTintUtil.tintDrawable(icon11.getDrawable(),Color));
				break;
			case 12:
				icon12.setImageDrawable(DrawableTintUtil.tintDrawable(icon12.getDrawable(),Color));
				break;
		}
		return this;
	}

	public FileActionDialog close(int ...num){
		for(int i:num){
			close(i);
		}
		return this;
	}
	public FileActionDialog close(int i){
		setIconTint(i,Color.GRAY);
		switch(i){
			case 1:
				fun1.setClickable(false);
				title1.setTextColor(Color.GRAY);
				break;

			case 2:
				fun2.setClickable(false);
				title2.setTextColor(Color.GRAY);
				break;

			case 3:
				fun3.setClickable(false);
				title3.setTextColor(Color.GRAY);
				break;

			case 4:
				fun4.setClickable(false);
				title4.setTextColor(Color.GRAY);
				break;

			case 5:
				fun5.setClickable(false);
				title5.setTextColor(Color.GRAY);
				break;

			case 6:
				fun6.setClickable(false);
				title6.setTextColor(Color.GRAY);
				break;

			case 7:
				fun7.setClickable(false);
				title7.setTextColor(Color.GRAY);
				break;

			case 8:
				fun8.setClickable(false);
				title8.setTextColor(Color.GRAY);
				break;

			case 9:
				fun9.setClickable(false);
				title9.setTextColor(Color.GRAY);
				break;

			case 10:
				fun10.setClickable(false);
				title10.setTextColor(Color.GRAY);
				break;

			case 11:
				fun11.setClickable(false);
				title11.setTextColor(Color.GRAY);
				break;

			case 12:
				fun12.setClickable(false);
				title12.setTextColor(Color.GRAY);
				break;
		}
		return this;
	}

	public FileActionDialog activeAll(){
		for(int i=1;i<=12;i++){
			active(i);
		}
		return this;
	}
	public FileActionDialog active(int ...num){
		for(int i:num){
			active(i);
		}
		return this;
	}
	
	public FileActionDialog active(int i){
		setIconTint(i,Color.BLACK);
		switch(i){
			case 1:
				fun1.setClickable(true);
				title1.setTextColor(Color.BLACK);
				break;

			case 2:
				fun2.setClickable(true);
				title2.setTextColor(Color.BLACK);
				break;

			case 3:
				fun3.setClickable(true);
				title3.setTextColor(Color.BLACK);
				break;

			case 4:
				fun4.setClickable(true);
				title4.setTextColor(Color.BLACK);
				break;

			case 5:
				fun5.setClickable(true);
				title5.setTextColor(Color.BLACK);
				break;

			case 6:
				fun6.setClickable(true);
				title6.setTextColor(Color.BLACK);
				break;

			case 7:
				fun7.setClickable(true);
				title7.setTextColor(Color.BLACK);
				break;

			case 8:
				fun8.setClickable(true);
				title8.setTextColor(Color.BLACK);
				break;

			case 9:
				fun9.setClickable(true);
				title9.setTextColor(Color.BLACK);
				break;

			case 10:
				fun10.setClickable(true);
				title10.setTextColor(Color.BLACK);
				break;

			case 11:
				fun11.setClickable(true);
				title11.setTextColor(Color.BLACK);
				break;

			case 12:
				fun12.setClickable(true);
				title12.setTextColor(Color.BLACK);
				break;
		}
		return this;
	}
	

	public void show() {
		if (dialog == null) return;
		dialog.show();
		int w=mRootView.getResources().getDisplayMetrics().widthPixels;
		Window window=dialog.getWindow();
		WindowManager.LayoutParams lp=window.getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.dimAmount = 0;
		lp.width=w*4/5;
		window.setAttributes(lp);
	}

	public FileActionDialog setOnclickListener(OnItemClickListener listener) {
		this.listener=listener;
		return this;
	}

	public FileActionDialog setOnDismissListener(DialogInterface.OnDismissListener listener){
		dialog.setOnDismissListener(listener);
		return this;
	}
	public void dismiss() {
		if (dialog == null) return;
		dialog.dismiss();
	}

	public interface OnItemClickListener {
		void onClick(View view, int id);
	}

	public <T extends View>T findView(int viewId) {
		View view=mViews.get(viewId);
		if (view == null) {
			view = mRootView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T)view;
	}
}

