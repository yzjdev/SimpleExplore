package com.yzj.eplorer;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import com.yzj.base.BaseActivity;
import com.yzj.fastedit.CodeEditor;
import com.yzj.utils.FileUtil;
import android.view.MenuItem;
import com.yzj.utils.ToastUtil;

public class EditorActivity extends BaseActivity {


	Menu menu;
	CodeEditor editor;
	String path;
	String text="";
	@Override
	public int getLayoutId() {
		return R.layout.activity_editor;
	}

	@Override
	public void initView() {
		mToolbar = findViewById(R.id.toolbar);
		editor = findViewById(R.id.editor);
	}

	@Override
	public void initData() {
		path = getIntent().getStringExtra("path");
		setTitle(FileUtil.getName(path));
		text = FileUtil.readText(path);
		editor.setText(text);

	}

	public void test(View v) {
		if (menu != null) {
			MenuItem item= menu.findItem(R.id.action_save);
			if (item.isEnabled()) {
				item.setEnabled(false);
				item.getIcon().setAlpha(128);
			} else {
				item.setEnabled(true);
				item.getIcon().setAlpha(255);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		getMenuInflater().inflate(R.menu.editor_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_undo:
				if (editor.canUndo())
					editor.undo();
				break;
			case R.id.action_redo:
				if (editor.canRedo())
					editor.redo();
				break;
			case R.id.action_save:
				break;
			default:
		}
		return super.onOptionsItemSelected(item);
	}



    public static void openFile(Context context, String path) {
		Intent intent=new Intent(context, EditorActivity.class);
		intent.putExtra("path", path);
		context.startActivity(intent);
	}

    public static void openFile(String path) {
		Intent intent=new Intent(App.getApp(), EditorActivity.class);
		intent.putExtra("path", path);
		App.getApp().startActivity(intent);
	}

}
