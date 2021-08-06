package com.yzj.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import java.io.InputStream;
import java.io.IOException;
import android.net.Uri;
import java.io.File;

public class ImageUtil {

	public static RequestOptions newRequestOpt() {
		return new RequestOptions().centerCrop();
	}
    //通过资源id设置图片 glide
	//implementation 'com.github.bumptech.glide:glide:4.6.1'
    public static void setImageWithGilde(ImageView iv, int imgId) {
        Glide.with(iv.getContext()).load(imgId).apply(newRequestOpt()).into(iv);
    }

    //通过路径设置图片
	//glide implementation 'com.github.bumptech.glide:glide:4.6.1'
    public static void setImageFromPathWithGlide(ImageView iv, String path) {
		Glide.with(iv.getContext()).load(UriUtil.fromPath(path)).apply(newRequestOpt()).into(iv);
    }

	//implementation 'com.github.bumptech.glide:glide:4.6.1'
    public static void setImageWithGilde(ImageView iv, Object obj) {
        Glide.with(iv.getContext()).load(obj).apply(newRequestOpt()).into(iv);
    }


    //androidsvg
	//implementation 'com.caverock:androidsvg-aar:1.4'
    public static void setImageFromRawWithSvg(ImageView iv, int resId) {
        iv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        try {
            SVG svg= SVG.getFromResource(iv.getContext(), resId);
            PictureDrawable drawable= new PictureDrawable(svg.renderToPicture());
            iv.setImageDrawable(drawable);
        } catch (SVGParseException e) {}
    }


    //androidsvg 文件路径
	//implementation 'com.caverock:androidsvg-aar:1.4'
    public static void setImageFromPathWithSvg(ImageView iv, String path) {
		try {
			setImageFromInputStreamWithSvg(iv, IOUtil.newInputStream(path));
		} catch (IOException e) {}
	}
	//androidsvg
	//implementation 'com.caverock:androidsvg-aar:1.4'
    public static void setImageFromInputStreamWithSvg(ImageView iv, InputStream in) {
        iv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        try {
            SVG svg= SVG.getFromInputStream(in);
            PictureDrawable drawable= new PictureDrawable(svg.renderToPicture());
            iv.setImageDrawable(drawable);
		} catch (Exception e) {}
    }


	public static Drawable getDrawableFromSvg(int resId) {
		try {
            SVG svg= SVG.getFromResource(Utils.getApp(), resId);
            PictureDrawable drawable= new PictureDrawable(svg.renderToPicture());
			return drawable;
        } catch (SVGParseException e) {}
		return null;
	}


    //给图片着色
    public static void setImageTint(ImageView iv, int imgId, int tintColor) {
        Drawable up = ContextCompat.getDrawable(iv.getContext(), imgId);
        Drawable drawableUp= DrawableCompat.wrap(up);
        DrawableCompat.setTint(drawableUp, tintColor);
        iv.setImageDrawable(drawableUp);
    }



    //给图片着色
    public static Drawable setImageTint(Drawable drawable, int tintColor) {
        Drawable drawableUp= DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawableUp, tintColor);
		return drawableUp;
    }


}
