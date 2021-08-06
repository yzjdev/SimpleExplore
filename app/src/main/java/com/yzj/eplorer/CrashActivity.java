package com.yzj.eplorer;

import android.content.Context;
import android.content.Intent;
import com.yzj.base.BaseActivity;
import com.yzj.eplorer.R;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.view.View;
import android.widget.ScrollView;
import com.yzj.utils.ArrayUtil;
import com.yzj.utils.DateTimeUtil;
import android.text.Html;

public class CrashActivity extends BaseActivity {

	ScrollView sv,sv2;
	TextView crashTv,crashTv2;
	CheckBox cb;
	@Override
	public int getLayoutId() {
		return R.layout.activity_crash;
	}

	@Override
	public void initView() {
		mToolbar=findViewById(R.id.toolbar);
		
		crashTv=findViewById(R.id.crash);
		crashTv2=findViewById(R.id.crash2);
		cb=findViewById(R.id.cb);
		sv=findViewById(R.id.sv1);
		sv2=findViewById(R.id.sv2);
		cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton p1, boolean p2) {
					if(p2){
						sv.setVisibility(View.GONE);
						sv2.setVisibility(View.VISIBLE);
			
					}else{
						sv.setVisibility(View.VISIBLE);
						sv2.setVisibility(View.GONE);
						
					}
				}
			});
	}

	@Override
	public void initData() {
		setTitle("Crash日志");
		showBack();
		
		String crash="异常发生时间："+DateTimeUtil.currTime()+"\n"+getIntent().getStringExtra("crash");
		crashTv.setText(crash);
		crashTv2.setText(crash);
	}
	
	public static void start(Object... crash){
		StringBuilder sb=new StringBuilder();
		for(Object o:crash){
			sb.append(o).append("\n");
		}
		Intent it=new Intent(App.getApp(),CrashActivity.class);
		it.putExtra("crash",sb.toString().trim());
		App.getApp().startActivity(it);
		
	}
	
    
}
