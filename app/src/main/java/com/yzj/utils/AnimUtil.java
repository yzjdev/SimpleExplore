package com.yzj.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

public class AnimUtil {

	// 高度渐变的动画；
	public static void animHeightToView(final View v, final int start, final int end, final boolean isToShow, long duration) {   
		ValueAnimator va = ValueAnimator.ofInt(start, end);
		final ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					int h = animation.getAnimatedValue();
					layoutParams.height = h;
					v.setLayoutParams(layoutParams);
					v.requestLayout();
				}
			});

		va.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationStart(Animator animation) {
					if (isToShow) {
						v.setVisibility(View.VISIBLE);
					}
					super.onAnimationStart(animation);
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					if (!isToShow) {
						v.setVisibility(View.GONE);
					}
				}
			});

		va.setDuration(duration);
		va.start();
	}

	public static void animHeightToView(Activity activity, final View v, final boolean isToShow, final long duration) {
		if (isToShow) {
			// 显示：通过上下文对象context获取可见度属性为 gone 的 view 的高度；
			Display display = activity.getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			v.measure(size.x, size.y);
			int end= v.getMeasuredHeight();
			animHeightToView(v, 0, end, isToShow, duration);
		} else {
			// 隐藏：从当前高度变化到0，最后设置不可见；
			animHeightToView(v, v.getMeasuredHeight(), 0, isToShow, duration);
		}
	}
}
