package com.yzj.fastedit;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.widget.ArrayAdapter;
import com.yzj.eplorer.R;

public class AutoCompletePanel {
    Context context;
	CodeEditor editor;
	ListPopupWindow panel;
	
    public AutoCompletePanel(CodeEditor editor){
		this.editor=editor;
		context=editor.getContext();
		init();
	}
    
	void init(){
		String[] datas={"public","void","function","char"};
		panel=new ListPopupWindow(context);
		panel.setAdapter(new ArrayAdapter<String>(context,R.layout.item_auto_complete_panel,datas));
		panel.setAnchorView(editor);
	}
}
