package com.yzj.eplorer.widget;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.yzj.eplorer.R;
import com.yzj.shadowviewhelper.ShadowProperty;
import com.yzj.shadowviewhelper.ShadowViewDrawable;
import com.yzj.utils.AnimUtil;
import com.yzj.utils.UiUtil;
import android.util.SparseArray;
import android.widget.TextView;
import android.widget.ImageView;

public class FixSnackBar extends LinearLayout {

	private OnActionClickListener mOnActionClickListener;
	private int duration=100;
	private View view;
	public int[] ids={R.id.icon,R.id.title,R.id.action};
	private SparseArray<View> mCacheView=new SparseArray<>();
	private Context context;
	
	public FixSnackBar(Context context) {
		this(context, null);
	}
	public FixSnackBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setOrientation(LinearLayout.VERTICAL);
		
		view = View.inflate(context, R.layout.snackbar, null);
		getActionView().setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					if(mOnActionClickListener!=null){
						mOnActionClickListener.onClick(FixSnackBar.this);
					}
				}
			});
		addView(view);
		setVisibility(View.GONE);
		UiUtil.setShadowTop(this);
	}
	
	public interface OnActionClickListener{
		void onClick(FixSnackBar snackbar);
	}
	public FixSnackBar setOnActionClickListener(OnActionClickListener listener){
		mOnActionClickListener=listener;
		return this;
	}

	public void show() {
		AnimUtil.animHeightToView((Activity)context, this, true, duration);
	}

	public void hide() {
		AnimUtil.animHeightToView((Activity)context, this, false, duration);
	}

	public boolean isVisible() {
		return getVisibility() == View.VISIBLE;
	}

	public void toggle() {
		if (isVisible()) {
			hide();
		} else {
			show();
		}
	}

	@Override
	public void setVisibility(int visibility) {
		if (visibility != View.VISIBLE) {
			setTag(null);
		}
		super.setVisibility(visibility);
	}
	
	public ImageView getIconView(){
		return findView(ids[0]);
	}
	public TextView getTitleView(){
		return findView(ids[1]);
	}
	public ImageView getActionView(){
		return findView(ids[2]);
	}
	public FixSnackBar setTitle(CharSequence text){
		TextView tv=findView(ids[1]);
		tv.setText(text);
		return this;
	}

	public FixSnackBar setActionIcon(int resId){
		ImageView iv=findView(ids[2]);
		iv.setBackgroundResource(resId);
		return this;
	}
	
	public FixSnackBar setIcon(int resId){
		ImageView iv=findView(ids[0]);
		iv.setBackgroundResource(resId);
		return this;
	}

	public <T extends View> T findView(int viewId){
		View v=mCacheView.get(viewId);
		if(v==null){
			v=view.findViewById(viewId);
			mCacheView.put(viewId,v);
		}
		return (T)v;
	}
}
