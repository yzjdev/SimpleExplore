package com.yzj.eplorer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.yzj.base.BaseActivity;
import com.yzj.eplorer.R;
import android.widget.TextView;

public class LogActivity extends BaseActivity {

	TextView logTextView;
	@Override
	public int getLayoutId() {
		return R.layout.activity_log;
	}

	@Override
	public void initView() {
		logTextView=findViewById(R.id.log);
		
	}

	@Override
	public void initData() {
		String log=getIntent().getStringExtra("log");
		logTextView.setText(log);
	}
	
	
	public static void start(Object ...obj){
		StringBuilder builder=new StringBuilder();
		for(Object o:obj){
			builder.append(o).append("\n");
		}
		Intent intent=new Intent(App.getApp(),LogActivity.class);
		intent.putExtra("log",builder.toString().trim());
		App.getApp().startActivity(intent);
	}
}
