package com.yzj.base;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.List;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {
	
	private int childViewId;
	private OnChildClickListener mOnChildClickListener;
	private OnItemClickListener mClickListener;
	private OnItemLongClickListener mLongClickListener;
	
	private List<T> mDatas;
	private int mLayoutId;
	
	public BaseAdapter(int layoutId){
		mLayoutId=layoutId;
	}
	
	
	public BaseAdapter(int layoutId,List<T> datas){
		mLayoutId=layoutId;
		mDatas=datas;
	}

	@Override
	public VH onCreateViewHolder(ViewGroup parent, int viewType) {
		View view=LayoutInflater.from(parent.getContext()).inflate(mLayoutId,parent,false);
		return (VH)new BaseViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(final BaseViewHolder holder, int position) {
		try {
			convert(holder, mDatas.get(position));
		} catch (Exception e) {}
		if (mOnChildClickListener != null) {
			holder.findViewById(childViewId).setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						mOnChildClickListener.onChildClick(v,holder.getLayoutPosition());
					}
				});
		}

		holder.itemView.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					mClickListener.onItemClick(v,holder.getLayoutPosition());
				}
			});

		holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
				@Override
				public boolean onLongClick(View v) {
					if(mLongClickListener!=null)
						return mLongClickListener.onItemLongClick(v,holder.getLayoutPosition());
					else
						return true;
				}
			});
		
	}

	@Override
	public int getItemCount() {
		return mDatas==null?0:mDatas.size();
	}
	

	public void clear(){
		mDatas.clear();
		notifyDataSetChanged();
	}

	public void setData(List<T> datas){
		mDatas=datas;
		notifyDataSetChanged();
	}

	public List<T> getData(){
		return mDatas;
	}
	
	public abstract void convert(BaseViewHolder holder,T item) throws Exception;
    
	public void setOnChildClickListener(int viewId,OnChildClickListener listener){
		childViewId=viewId;
		mOnChildClickListener=listener;
	}

	public void setOnItemClickListener(OnItemClickListener listener){
		mClickListener=listener;
	}
	public void setOnItemLongClickListener(OnItemLongClickListener listener){
		mLongClickListener=listener;
	}
	
	

	public interface OnChildClickListener{
		void onChildClick(View view,int position);
	}
	public interface OnItemClickListener{
		void onItemClick(View v,int position);
	}
	
	public interface OnItemLongClickListener{
		boolean onItemLongClick(View v,int position);
	}
	
}
