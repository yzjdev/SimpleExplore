package com.yzj.eplorer.widget;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yzj.utils.UiUtil;
import java.lang.reflect.Field;
import android.support.design.widget.TabLayout;

public class FixTabLayout extends TabLayout {

	public FixTabLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void addTab(TabLayout.Tab tab, int position, boolean setSelected) {
		super.addTab(tab, position, setSelected);
		setTabWidth(position);
	}

	void setTabWidth(int position) {
		try {
			int dp10=UiUtil.dp2px(10);
			LinearLayout mTabStrip=(LinearLayout) getChildAt(0);
			//changeTabStrip(mTabStrip);
			View tabView=mTabStrip.getChildAt(position);
			tabView.setPadding(0, 0, 0, 0);
			Field textViewField=tabView.getClass().getDeclaredField("mTextView");
			textViewField.setAccessible(true);
			TextView tv= (TextView) textViewField.get(tabView);
			int w=tv.getWidth();
			int h=tv.getHeight();
			if (w == 0 || h == 0) {
				tv.measure(0, 0);
				w = tv.getMeasuredWidth();
				h = tv.getMeasuredHeight();
			}
			LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams) tabView.getLayoutParams();
			lp.width = w;
			lp.leftMargin = dp10;
			lp.rightMargin = dp10;
			tabView.setLayoutParams(lp);
			tabView.invalidate();
		} catch (Exception e) {}
	}
}
