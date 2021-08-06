package com.yzj.base;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.util.SparseArray;
import android.widget.TextView;
import android.widget.ImageView;
import com.yzj.utils.ImageUtil;
import java.io.InputStream;
import android.net.Uri;

public class BaseViewHolder extends RecyclerView.ViewHolder{
    private SparseArray<View> mViews;
	private View mView;
    public BaseViewHolder(View v){
		super(v);
		mViews=new SparseArray<>();
		mView=v;
	}
    
	public BaseViewHolder setImageRes(int viewId,int resId){
		ImageView iv=findViewById(viewId);
		iv.setImageResource(resId);
		return this;
	}
	

	public BaseViewHolder setImageWithGlide(int viewId,int resId){
		ImageView iv=findViewById(viewId);
		ImageUtil.setImageWithGilde(iv,resId);
		return this;
	}
	public BaseViewHolder setImageWithGlide(int viewId,String path){
		ImageView iv=findViewById(viewId);
		ImageUtil.setImageFromPathWithGlide(iv,path);
		return this;
	}
	public BaseViewHolder setImageWithGlide(int viewId,Object obj){
		ImageView iv=findViewById(viewId);
		ImageUtil.setImageWithGilde(iv,obj);
		return this;
	}

	
	
	public BaseViewHolder setImageWithSvg(int viewId,int resId){
		ImageView iv=findViewById(viewId);
		ImageUtil.setImageFromRawWithSvg(iv,resId);
		return this;
	}
	
	public BaseViewHolder setImageWithSvg(int viewId,String path){
		ImageView iv=findViewById(viewId);
		ImageUtil.setImageFromPathWithSvg(iv,path);
		return this;
	}
	
	
	
	public BaseViewHolder setText(int viewId,CharSequence text){
		TextView tv=findViewById(viewId);
		tv.setText(text);
		return this;
	}
	
	public <T extends View> T findViewById(int viewId){
		View view=mViews.get(viewId);
		if(view==null){
			view=mView.findViewById(viewId);
			mViews.put(viewId,view);
		}
		return (T)view;
	}
}
