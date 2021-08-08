package com.yzj.eplorer;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import com.yzj.base.BaseActivity;
import com.yzj.base.BasePagerAdapter;
import com.yzj.dialog.EditTextDialog;
import com.yzj.eplorer.R;
import com.yzj.eplorer.fragment.FileListFragment;
import com.yzj.eplorer.widget.BreadcrumbView;
import com.yzj.eplorer.widget.FixTabLayout;
import com.yzj.eplorer.widget.MainTabBar;
import com.yzj.utils.Attrs;
import com.yzj.utils.ClipBoardUtil;
import com.yzj.utils.FileUtil;
import com.yzj.utils.PermissionUtil;
import com.yzj.utils.PrefUtil;
import com.yzj.utils.SystemUtil;
import com.yzj.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import com.yzj.utils.UriUtil;
import com.yzj.eplorer.widget.FixSnackBar;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, BreadcrumbView.OnClickListener, TabLayout.OnTabSelectedListener {

	
	String requestPath;
	public int currPos=0;
	Menu mMenu;
	MainTabBar mainTabBar;
	List<Fragment> mDatas=new ArrayList<>();
	FixTabLayout mTab;
	ViewPager mPager;
	PagerAdapter mPagerAdapter;
	NavigationView mNavgationView;
	public BreadcrumbView mBreadcrumbView;
	public FixSnackBar mSnackbar;
	@Override
	public int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	public void initView() {
		mToolbar = findViewById(R.id.toolbar);
		mDrawer = findViewById(R.id.drawer);
		mPager = findViewById(R.id.pager);
		mTab = findViewById(R.id.tab);
		mBreadcrumbView = findViewById(R.id.breadcrumb_view);
		mNavgationView=findViewById(R.id.main_nav);
		mSnackbar=findViewById(R.id.snackbar);
	}

	@Override
	public void initData() {
		PermissionUtil.requestManageAllFile(this);
		mainTabBar = new MainTabBar(this);
		mTab.setTabMode(TabLayout.MODE_SCROLLABLE);

		String lastWindows= PrefUtil.readString("last_windows");
		if (!TextUtils.isEmpty(lastWindows)) {
			for (String path:lastWindows.split("\n")) {
				mDatas.add(FileListFragment.newInstance(path));
			}
		} else {
			mDatas.add(FileListFragment.newInstance());
			mDatas.add(FileListFragment.newInstance());
		}

		mPager.setOffscreenPageLimit(mDatas.size());
		mPagerAdapter = new PagerAdapter();
		mPager.setAdapter(mPagerAdapter);
		mTab.setupWithViewPager(mPager);
		mTab.setOnTabSelectedListener(this);
		mBreadcrumbView.setOnClickListener(this);
		mNavgationView.setNavigationItemSelectedListener(this);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem menuItem) {
		closeStartDrawer();
		switch(menuItem.getItemId()){
			case R.id.qqfile:
				requestPath=Attrs.PATH_QQ;
				getCurrentFragment().loadFile(requestPath);
				break;
			case R.id.exit:
				finish();
				break;
			default:
		}
		return true;
	}
	
	
	//导航栏
	@Override
	public void onClick(String path, int position) {
		getCurrentFragment().loadFile(path);
	}


	@Override
	public void onTabSelected(TabLayout.Tab tab) {

		currPos = tab.getPosition();
		if (getCurrentFragment() != null) {
			getCurrentFragment().setSubTitle();
			getCurrentFragment().setBreadcrumb();
		}
	}

	@Override
	public void onTabUnselected(TabLayout.Tab tab) {
	}

	@Override
	public void onTabReselected(TabLayout.Tab tab) {
		LinearLayout tabStrip=(LinearLayout) mTab.getChildAt(0);
		View v=tabStrip.getChildAt(tab.getPosition());
		mainTabBar.showAsDropDown(v);
	}


	public int getItemCount() {
		return mPagerAdapter.getCount();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		mMenu = menu;
		menu.findItem(R.id.action_show_hidden_file).setChecked(PrefUtil.readBool(Attrs.KEY_SHOW_HIDDEN_FILE));
		return true;
	}

	EditTextDialog dialog;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_multi_select:
				mSnackbar.toggle();
				break;
			case R.id.action_new_file:
				dialog = new EditTextDialog(mActivity);

				dialog.setTitle("新建")
					.setPositiveButton("文件夹", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface p1, int p2) {
							if (FileUtil.mkdirs(getCurrentFragment().currentPath + "/" + dialog.getMessage())) {
								getCurrentFragment().refresh();
							} else {
								ToastUtil.show("文件已存在");
							}
						}
					})
					.setNegativeButton("文件", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface p1, int p2) {
							if(FileUtil.createNewFile(getCurrentFragment().currentPath + "/" + dialog.getMessage())){
								getCurrentFragment().refresh();
							}else{
								ToastUtil.show("文件已存在");
							}
						}
					})
					.show();
				break;
			case R.id.action_show_hidden_file:
				item.setChecked(!item.isChecked());
				PrefUtil.writeBool(Attrs.KEY_SHOW_HIDDEN_FILE, item.isChecked());
				refresh();
				break;
			case R.id.action_refresh:
				getCurrentFragment().refresh();
				break;
			case R.id.action_copy_path:
				ClipBoardUtil.copyText(getCurrentFragment().currentPath, getCurrentFragment().currentPath);
				break;
			case R.id.action_new_task:
				addTab();
				break;
			case R.id.action_exit:
				finish();
				break;
		}
		return true;
	}

	public void refresh() {
		if (getCurrentFragment() != null)
			getCurrentFragment().refresh();
	}


	public void addTab() {
		if (mTab.getVisibility() == View.GONE) {
			mTab.setVisibility(View.VISIBLE);
		}
		mPagerAdapter.addData(FileListFragment.newInstance());
		setCurrentItem(mPagerAdapter.getCount());
	}

	public void removeTab() {
		if (getItemCount() == 1) {
			mTab.setVisibility(View.GONE);
		}
		mPagerAdapter.removeData(currPos);
		setCurrentItem(currPos);
	}

	public void removeOtherTab() {
		mPagerAdapter.removeOther(currPos);
		setCurrentItem(currPos);
	}

	public void removeAllTab() {
		mPagerAdapter.clear();
		mTab.setVisibility(View.GONE);
	}

	public void setCurrentItem(final int pos) {
		mPager.post(new Runnable() {
				@Override
				public void run() {
					mPager.setCurrentItem(pos);
				}
			});
	}

	public FileListFragment getFragment(int pos) {
		return (FileListFragment)mPagerAdapter.getItem(pos);
	}

	public FileListFragment getCurrentFragment() {
		return getFragment(currPos);
	}


	long start=0;
	@Override
	public void onBackPressed() {
		if (getCurrentFragment().navigateUp())
			return;
		if (System.currentTimeMillis() - start > 2000) {
			ToastUtil.show("再按一次退出");
			start = System.currentTimeMillis();
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PRIVACY) {
			PermissionUtil.savePermission(data);
			if(TextUtils.isEmpty(requestPath)){
				requestPath=UriUtil.uriToPath(data.getData());
			}
			getCurrentFragment().loadFile(requestPath);
			requestPath=null;
		}
	}


	@Override
	protected void onPause() {
		super.onPause();
		StringBuilder sb=new StringBuilder();
		for (int i=0;i < getItemCount();i++) {
			sb.append(getFragment(i).currentPath).append("\n");
		}
		PrefUtil.writeString("last_windows", sb.toString().trim());

	}




	class PagerAdapter extends BasePagerAdapter {
		public PagerAdapter() {
			super(getSupportFragmentManager(), mDatas);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "窗口" + (position + 1);
		}

		@Override
		public void setData(List<Fragment> fragments) {
			super.setData(fragments);
			mPager.setOffscreenPageLimit(getCount());
		}
	}

}
