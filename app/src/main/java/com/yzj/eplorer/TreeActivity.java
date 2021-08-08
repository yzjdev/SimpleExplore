package com.yzj.eplorer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.yzj.base.BaseActivity;
import com.yzj.eplorer.R;
import com.yzj.treeadapter.Node;
import com.yzj.treeadapter.TreeAdapter;
import com.yzj.treeadapter.TreeHelper;
import com.yzj.treeadapter.TreeNodeId;
import com.yzj.treeadapter.TreeNodeLable;
import com.yzj.treeadapter.TreeNodePid;
import com.yzj.treeadapter.TreeViewHolder;
import java.util.ArrayList;
import java.util.List;
import com.yzj.utils.ResUtil;

public class TreeActivity extends BaseActivity {

	List<FileBean> mDatas=new ArrayList<>();
	RecyclerView rv;
	SimpleTreeAdapter adapter;
	@Override
	public int getLayoutId() {
		return R.layout.activity_tree;
	}

	@Override
	public void initView() {
		rv=findViewById(R.id.rv);
		initData();
		TreeHelper.setExpandIcon(R.drawable.down);
		TreeHelper.setCollapseIcon(R.drawable.right);
		adapter = new SimpleTreeAdapter<FileBean>(mDatas, 10);
		rv.setLayoutManager(new LinearLayoutManager(this));
		rv.setAdapter(adapter);
	}

	@Override
	public void initData() {

		// id , pid , label , 其他属性
		mDatas.add(new FileBean(1, 0, "文件管理系统"));
		mDatas.add(new FileBean(2, 1, "游戏"));
		mDatas.add(new FileBean(3, 1, "文档"));
		mDatas.add(new FileBean(4, 1, "程序"));
		mDatas.add(new FileBean(5, 2, "war3"));
		mDatas.add(new FileBean(6, 2, "刀塔传奇"));

		mDatas.add(new FileBean(7, 4, "面向对象"));
		mDatas.add(new FileBean(8, 4, "非面向对象"));

		mDatas.add(new FileBean(9, 7, "C++"));
		mDatas.add(new FileBean(10, 7, "JAVA"));
		mDatas.add(new FileBean(11, 7, "Javascript"));
		mDatas.add(new FileBean(12, 8, "C"));
		mDatas.add(new FileBean(13, 0, "文件管理系统"));

		mDatas.add(new FileBean(14, 13, "C++"));
		mDatas.add(new FileBean(15, 13, "JAVA"));
		mDatas.add(new FileBean(16, 13, "Javascript"));
		mDatas.add(new FileBean(17, 13, "C"));
	
	}
	
	
	class FileBean{
		@TreeNodeId
		public int id;

		@TreeNodePid
		public int pid;

		@TreeNodeLable
		public String label;

		public long length;
		public String desc;

		public FileBean(int id,int pid,String label){

			this.id=id;
			this.pid=pid;
			this.label=label;
		}
	}
	class SimpleTreeAdapter<T> extends TreeAdapter<T,TreeViewHolder> {
		
		public SimpleTreeAdapter(List<T> datas,int defaultExpandLevel){
			super(datas,defaultExpandLevel);
		}
		
		@Override
		public void convert(TreeViewHolder holder, Node node) {
			holder.itemView.setBackground(ResUtil.getSelectableItemBackground());
			ImageView icon=holder.itemView.findViewById(R.id.icon);

			TextView name=holder.itemView.findViewById(R.id.name);
			if (node.icon == -1){
				icon.setVisibility(View.GONE);
			} else{
				icon.setVisibility(View.VISIBLE);
				icon.setImageResource(node.icon);
			}
			name.setText(node.name);
		}

		@Override
		public int getParentLayoutId() {
			return R.layout.item_tree_parent;
		}

		@Override
		public int getChildLayoutId() {
			return R.layout.item_tree_child;
		}

		
	}
	public static void start(Activity activity){
		Intent intent=new Intent(activity,TreeActivity.class);
		activity.startActivity(intent);
	}
}
