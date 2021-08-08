package com.yzj.treeadapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import java.util.ArrayList;
import android.view.LayoutInflater;

public abstract class TreeAdapter<T,VH extends TreeViewHolder> extends RecyclerView.Adapter<VH> {

	List<Node> mNodes;
	List<Node> mAllNodes;
	
	private OnTreeNodeClickListener onTreeNodeClickListener;

	public interface OnTreeNodeClickListener{
		void onClick(Node node,int position);
	}
	public void setOnTreeNodeClickListener(OnTreeNodeClickListener onTreeNodeClickListener){
		this.onTreeNodeClickListener=onTreeNodeClickListener;
	}
	
	
	public TreeAdapter(List<T> datas,int defaultExpandLevel){
		mAllNodes=TreeHelper.getSortedNodes(datas,defaultExpandLevel);
		mNodes=TreeHelper.filterVisibleNode(mAllNodes);
		
	}
	
	@Override
	public VH onCreateViewHolder(ViewGroup parent, int viewType) {
		View view=null;
		LayoutInflater inflater=LayoutInflater.from(parent.getContext());
		switch(viewType){
			case Node.Type.PARENT:
				view=inflater.inflate(getParentLayoutId(),parent,false);
				break;
			case Node.Type.CHILD:
				view=inflater.inflate(getChildLayoutId(),parent,false);
				break;
			default:
				view=inflater.inflate(getParentLayoutId(),parent,false);
				break;
		}
		return (VH)new TreeViewHolder(view);
	}

	@Override
	public void onBindViewHolder(VH holder, final int position) {
		final Node n=mNodes.get(position);
		convert(holder,n);
		holder.itemView.setPadding(n.getLevel()*30,3,3,3);
		
		holder.itemView.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					expandOrCollapse(position);
					if(onTreeNodeClickListener!=null){
						onTreeNodeClickListener.onClick(n,position);
					}
				}
			});
	}

	@Override
	public int getItemViewType(int position) {
		return mNodes.get(position).type;
	}

	
	@Override
	public int getItemCount() {
		return mNodes==null?0:mNodes.size();
	}

	public void expandOrCollapse(int position){
		Node n=mNodes.get(position);
		if(n!=null && !n.isLeaf()){
			n.setExpand(!n.isExpand);
			mNodes=TreeHelper.filterVisibleNode(mAllNodes);
			notifyDataSetChanged();
		}
	}
	
	public abstract void convert(VH holder,Node node);
	public abstract int getParentLayoutId();
	public abstract int getChildLayoutId();
}
