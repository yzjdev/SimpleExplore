package com.yzj.base;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.yzj.eplorer.R;
import android.support.v4.app.ActivityCompat;
import com.yzj.utils.PermissionUtil;
import android.view.Gravity;

public abstract class BaseActivity extends AppCompatActivity {

	public static final int REQUEST_CODE_PRIVACY=0;
	public static final int REQUEST_CODE_WRITE_AND_READ=1;
	public static final int REQUEST_CODE_MANAGE_ALL_FILE=2;
	public Context mContext;
	public Activity mActivity;
	public Toolbar mToolbar;
	public DrawerLayout mDrawer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutId());
		mContext = this;
		mActivity = this;
		initView();
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
			mToolbar.setSubtitleTextAppearance(mActivity, R.style.Toolbar_SubTitleText);
		}
		if (mToolbar != null && mDrawer != null) {
			ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
			mDrawer.setDrawerListener(toggle);
			toggle.syncState();
		}
		initData();
	}
	
	public void closeStartDrawer(){
		if(mDrawer==null)return;
		mDrawer.closeDrawer(Gravity.START);
	}
	public void closeEndDrawer(){
		if(mDrawer==null)return;
		mDrawer.closeDrawer(Gravity.END);
	}
	public void setTitle(CharSequence title){
		if(getSupportActionBar()!=null) getSupportActionBar().setTitle(title);
	}

	public void setSubTitle(CharSequence title){
		if(getSupportActionBar()!=null){
			getSupportActionBar().setSubtitle(title);
		}
	}
	public void showBack(){
		if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public abstract int getLayoutId();
    public abstract void initView();
	public abstract void initData();
}
