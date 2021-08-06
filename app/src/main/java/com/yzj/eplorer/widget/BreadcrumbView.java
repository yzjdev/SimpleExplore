package com.yzj.eplorer.widget;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yzj.eplorer.R;
import java.util.Arrays;
import java.util.List;
import com.yzj.utils.ToastUtil;

public class BreadcrumbView extends HorizontalScrollView {

	Context context;
	LinearLayout main;
	Data data;


	public BreadcrumbView(Context context) {
		this(context, null);
	}

	public BreadcrumbView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	TypedArray typedArray;
	public BreadcrumbView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		main = new LinearLayout(context);
		main.setOrientation(LinearLayout.HORIZONTAL);
		main.setGravity(Gravity.CENTER_VERTICAL);
		main.setPadding(dp2px(8),dp2px(4),dp2px(8),dp2px(4));
		addView(main);
		//获取attrId
		TypedValue typedValue = new TypedValue();
		int[] attribute = new int[]{android.R.attr.selectableItemBackground};
        typedArray = getContext().getTheme().obtainStyledAttributes(typedValue.resourceId, attribute);
		
	}

	public int dp2px(float v){
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,v,getResources().getDisplayMetrics());
	}
	Runnable scrollRunnable=new Runnable(){

		@Override
		public void run() {
			fullScroll(View.FOCUS_RIGHT);
		}
	};

	void init() {
		if (data == null)return;
		main.removeAllViews();
		for(int i=0;i<data.datas.size();i++){
			String s=data.datas.get(i);
			final int position=i;
			LinearLayout.LayoutParams tablp=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
			LinearLayout tab=new LinearLayout(context);
			tab.setLayoutParams(tablp);
			tab.setOrientation(LinearLayout.HORIZONTAL);
			tab.setGravity(Gravity.CENTER);
			
			TextView tv=new TextView(context);
			tv.setText(s);
			tv.setBackground(typedArray.getDrawable(0));
			int dp4=dp2px(4);
			tv.setPadding(dp4,dp4/2,dp4,dp4/2);
			tv.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View p1) {
						if(position==data.datas.size()-1){
							ToastUtil.show("不要点我");
							return;
						}
						if(mOnClickListener!=null){
							
							StringBuilder sb=new StringBuilder();
							for(int i=0;i<=position;i++){
								sb.append(data.datas.get(i)).append("/");
							}
							sb.deleteCharAt(sb.length()-1);
							String text=sb.toString().replaceFirst("内部存储",Environment.getExternalStorageDirectory().getPath());
							mOnClickListener.onClick(text,position);
						}
					}
				});
			ImageView iv=new ImageView(context);
			LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(dp2px(20),dp2px(20));
			lp.setMarginEnd(4);
			lp.setMarginStart(4);
			iv.setLayoutParams(lp);
			
			iv.setBackgroundResource(R.drawable.arrow_right);
			
			tv.setTextColor(Color.LTGRAY);
			if (i == data.datas.size()-1) {
				tv.setTextColor(Color.WHITE);
				iv.setVisibility(View.GONE);
			}
			tab.addView(tv);
			tab.addView(iv);
			main.addView(tab);
		}
		post(scrollRunnable);
	}

	public void setData(String path) {
		setData(new Data(path));
	}

	public void setData(Data data) {
		this.data = data;
		init();
	}
	public class Data {
		String path;
		public List<String> datas;
		public Data(String path) {
			this.path = path;
			path = path.replaceFirst(Environment.getExternalStorageDirectory().getPath(), "内部存储");
			if (path.endsWith("/"))
				path = new StringBuilder(path).deleteCharAt(path.length() - 1).toString().trim();
			datas = Arrays.asList(path.split("/"));
		}
	}
	
	private OnClickListener mOnClickListener;
	public void setOnClickListener(OnClickListener listener){
		mOnClickListener=listener;
	}
	public interface OnClickListener{
		void onClick(String path,int position);
	}
}
