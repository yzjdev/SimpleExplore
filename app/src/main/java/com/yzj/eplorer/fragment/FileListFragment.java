package com.yzj.eplorer.fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Xml;
import android.view.View;
import com.yzj.base.BaseAdapter;
import com.yzj.base.LazyFragment;
import com.yzj.dialog.FileActionDialog;
import com.yzj.dialog.LoadingDialog;
import com.yzj.eplorer.EditorActivity;
import com.yzj.eplorer.LogActivity;
import com.yzj.eplorer.MainActivity;
import com.yzj.eplorer.R;
import com.yzj.eplorer.adapter.FileListAdapter;
import com.yzj.eplorer.bean.FileItem;
import com.yzj.eplorer.task.FileLoadTask;
import com.yzj.utils.Attrs;
import com.yzj.utils.FileUtil;
import com.yzj.utils.IOUtil;
import com.yzj.utils.MimeType;
import com.yzj.utils.PermissionUtil;
import com.yzj.utils.PrefUtil;
import com.yzj.utils.RvLayoutUtil;
import com.yzj.utils.ToastUtil;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Stack;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.util.ArrayList;
import com.yzj.utils.XmlPullUtil;

public class FileListFragment extends LazyFragment implements FileListAdapter.OnChildClickListener,FileActionDialog.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener,FileLoadTask.OnListener,BaseAdapter.OnItemClickListener,BaseAdapter.OnItemLongClickListener {

	public static final int SUCCESS=0;
	public static final int FAILED_DEL=1;

	MainActivity activity;
	boolean flagBack=false;
	boolean flagRefresh=false;
	boolean showHidden=false;
	public String currentPath=Attrs.PATH_ROOT;
	Stack<Integer> mLastPosition=new Stack<>();

	List<FileItem> mDatas;
	FileListAdapter mAdapter;
	SwipeRefreshLayout swipeRefresh;
	RecyclerView rv;

	LinearLayoutManager layoutManager;

	public static FileListFragment newInstance() {
		return new FileListFragment();
	}

	public static FileListFragment newInstance(String path) {
		return new FileListFragment(path);
	}

	private FileListFragment() {
	}

	private FileListFragment(String path) {
		currentPath = path;
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.fragment_file_list;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		activity = (MainActivity) getActivity();
		rv = getView(R.id.rv);
		swipeRefresh = getView(R.id.refresh);
		swipeRefresh.setOnRefreshListener(this);

		layoutManager = RvLayoutUtil.setLinearLayout(rv);
		mAdapter = new FileListAdapter();
		mAdapter.setOnItemClickListener(this);
		mAdapter.setOnItemLongClickListener(this);
		mAdapter.setOnChildClickListener(R.id.fileicon, this);
		rv.setAdapter(mAdapter);
		loadFile(currentPath);//加载文件列表

	}

	@Override
	public void onRefresh() {
		refresh();
	}


	@Override
	public void onChildClick(View view, int position) {
		if (position == 0)return;
		ToastUtil.show(mDatas.get(position).name);
	}


	//列表点击事件
	@Override
	public void onItemClick(View v, int position) {
		if (position == 0) {
			navigateUp();
			return;
		}
		final FileItem data=mDatas.get(position);
		final String path=data.path;
		if (data.isDirectory) {
			if (!PermissionUtil.checkPrivacyPermission(getActivity(), path))return;
			mLastPosition.push(getFirstItemPosition());
			loadFile(path);
		} else {
			String ext=data.getExt();
			if (MimeType.isTextFile(path) || MimeType.isCodeFile(path)) {
				EditorActivity.openFile(getActivity(), path);
			}
			if (ext.equals("apk")) {
				new AlertDialog.Builder(getActivity())
					.setTitle("提示")
					.setMessage("这是一个Apk文件")
					.setPositiveButton("安装", null)
					.setNegativeButton("查看", null)
					.setNeutralButton("功能", null)
					.show();
			}
			if (ext.equals("svg")) {
				final String[] items={"查看图像","编辑代码","Svg转Xml"};
				new AlertDialog.Builder(getActivity())
					.setTitle("打开方式...")
					.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface p1, int i) {
							p1.dismiss();
							if (i == 0) {

							} else if (i == 1) {
								EditorActivity.openFile(path);
							} else if (i == 2) {
								if(XmlPullUtil.svg2xml(path)){
									ToastUtil.show("已完成");
									refresh();
								}
								else
									ToastUtil.show("转换失败");
								
							}
						}
					}).show();
			}
		}
	}

	FileActionDialog fileActionDialog;
	FileItem data;
	//列表长按事件
	@Override
	public boolean onItemLongClick(View v, int position) {
		data = mDatas.get(position);
		fileActionDialog = FileActionDialog.showDialog(getActivity());
		fileActionDialog.setOnclickListener(this);
		return true;
	}


	LoadingDialog loadingDialog;
	@Override
	public void onClick(View view, int id) {
		switch (id) {
			case R.id.fun5:
				new AlertDialog.Builder(getActivity())
					.setTitle("删除")
					.setMessage("是否删除文件" + data.name)
					.setPositiveButton("确认", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface p1, int p2) {
							loadingDialog = LoadingDialog.showDialog(getActivity());
							new Thread(){
								public void run() {
									if (FileUtil.delete(data.path)) {
										mHandler.sendEmptyMessage(SUCCESS);
									} else {
										mHandler.sendEmptyMessage(FAILED_DEL);
									}
								}
							}.start();
						}
					})
					.setNegativeButton("取消", null)
					.show();
				break;
		}
	}

	Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (loadingDialog.isShowing())
				loadingDialog.dismiss();

			switch (msg.what) {
				case SUCCESS:
					refresh();
					break;
				case FAILED_DEL:
					ToastUtil.show("删除失败");
					break;
				default:
			}
		}

	};

	public void setBreadcrumb() {
		activity.mBreadcrumbView.setData(currentPath);
	}
	int mDirCount=0;
	int mFileCount=0;
	//加载列表
	public void loadFile(final String path) {
		currentPath = path;
		if(!PermissionUtil.checkPrivacyPermission(getActivity(),currentPath)){
			return;
		}
		setBreadcrumb();
		showHidden = PrefUtil.readBool(Attrs.KEY_SHOW_HIDDEN_FILE);
		new FileLoadTask(getActivity(), path)
			.isShowHiddenFile(showHidden)
			.setOnListener(this)
			.execute();
	}

	//列表加载完成回调
	@Override
	public void onSuccess(List<FileItem> datas, int dirCount, int fileCount) {
		mDatas = datas;
		mDirCount = dirCount;
		mFileCount = fileCount;
		mAdapter.setData(mDatas);
		setSubTitle();
		if ((flagBack || flagRefresh) && mLastPosition.size() > 0) {
			int p=mLastPosition.pop();
			if (p >= mDatas.size()) p = mDatas.size() - 1;
			moveToPosition(p);
			flagBack = false;
			flagRefresh = false;
		}
		if (swipeRefresh.isRefreshing())
			swipeRefresh.setRefreshing(false);
	}

	public void moveToPosition(int pos) {
		layoutManager.scrollToPositionWithOffset(pos, 0);
	}

	public void setSubTitle() {
		((MainActivity)getActivity()).setSubTitle("文件夹 " + mDirCount + "  文件 " + mFileCount);
	}

	public void refresh() {
		flagRefresh = true;
		mLastPosition.push(getFirstItemPosition());
		loadFile(currentPath);
	}

	public int getFirstItemPosition() {
		return layoutManager.findFirstVisibleItemPosition();
	}

	public boolean navigateUp() {
		if (canNavigateUp()) {
			flagBack = true;
			loadFile(new File(currentPath).getParent());
			return true;
		}
		return false;
	}

	public boolean canNavigateUp() {
		return currentPath.replaceFirst(Attrs.PATH_ROOT, "内部存储").split("/").length > 1;
	}

	@Override
	protected void onVisible(boolean isFirstVisible) {
		super.onVisible(isFirstVisible);
		if (showHidden != PrefUtil.readBool(Attrs.KEY_SHOW_HIDDEN_FILE)) {
			refresh();
		}
	}


}
