package com.yzj.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;

public class DrawableTintUtil {

	/**
	 * Drawable 颜色转化类
	 *
	 * @param drawable 
	 * @param color资源
	 * @return 改变颜色后的Drawable
	 */
	public static Drawable tintDrawable(@NonNull Drawable drawable, int color) {
		Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
		DrawableCompat.setTint(wrappedDrawable, color);
		return wrappedDrawable;
	}

	/**
	 * Drawable 颜色转化类
	 *
	 * @param drawable 源Drawable
	 * @param ColorStateList
	 * @return 改变颜色后的Drawable
	 */
	public static Drawable tintListDrawable(@NonNull Drawable drawable, ColorStateList colors) {
		Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
		DrawableCompat.setTintList(wrappedDrawable, colors);
		return wrappedDrawable;
	}
	}
