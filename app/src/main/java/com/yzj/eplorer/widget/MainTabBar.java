package com.yzj.eplorer.widget;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.yzj.eplorer.MainActivity;
import com.yzj.eplorer.R;
import com.yzj.utils.UiUtil;

public class MainTabBar extends PopupWindow implements View.OnClickListener {

	Context context;
	MainActivity mMainActivity;
    View contentView;
    private TextView mBtnCloseCurr;
    private TextView mBtnCloseOther;
    private TextView mBtnCloseAll;
    Paint paint;


    public MainTabBar(MainActivity main) {
        super(main.mContext);
        mMainActivity = main;
        this.context = main.mContext;

        contentView = LayoutInflater.from(context).inflate(R.layout.item_main_popup, null);
        setContentView(contentView);	
		
		contentView.measure(UiUtil.getWidth(), UiUtil.getHeight());
        setWidth(contentView.getMeasuredWidth());
        setHeight(contentView.getMeasuredHeight());
        initView();
		
		setFocusable(true);
        setOutsideTouchable(true);
        
        paint = new Paint();
        setBackgroundDrawable(null);
		
        contentView.setBackgroundDrawable(new Drawable() {
				@Override
				public void draw(Canvas canvas) {
					int shadowWidth = contentView.getPaddingTop();
					paint.setColor(0x33000000);
					paint.setMaskFilter(new BlurMaskFilter(shadowWidth, BlurMaskFilter.Blur.OUTER));
					canvas.drawRoundRect(shadowWidth, shadowWidth,
										 getWidth() - shadowWidth,
										 getHeight() - shadowWidth,
										 shadowWidth, shadowWidth,
										 paint);
					paint.reset();
					paint.setColor(Color.WHITE);
					canvas.drawRoundRect(shadowWidth,
										 shadowWidth,
										 getWidth() - shadowWidth,
										 getHeight() - shadowWidth,
										 shadowWidth, shadowWidth,
										 paint);
				}

				@Override
				public void setAlpha(int alpha) {
				}

				@Override
				public void setColorFilter(ColorFilter colorFilter) {
				}
				
				@Override
				public int getOpacity() {
					return 0;
				}
			});

			
    }

    public void initView() {
        mBtnCloseCurr = contentView.findViewById(R.id.btn_close_curr);
        mBtnCloseCurr.setOnClickListener(this);
        mBtnCloseOther = contentView.findViewById(R.id.btn_close_other);
        mBtnCloseOther.setOnClickListener(this);
        mBtnCloseAll =  contentView.findViewById(R.id.btn_close_all);
        mBtnCloseAll.setOnClickListener(this);
    }



	@Override
	public void onClick(View v) {
		int i = v.getId();
        dismiss();
        if (i == R.id.btn_close_curr) {
			mMainActivity.removeTab();
        } else if (i == R.id.btn_close_other) {
			mMainActivity.removeOtherTab();
        } else if (i == R.id.btn_close_all) {
			mMainActivity.removeAllTab();
        }
	}

	@Override
	public void showAsDropDown(View anchor) {
		super.showAsDropDown(anchor);
	}
}
