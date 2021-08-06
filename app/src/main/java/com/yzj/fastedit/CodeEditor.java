package com.yzj.fastedit;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.yzj.utils.LogUtil;

public class CodeEditor extends FastEdit {

	float lastDist=0;
	float lastX;
	float lastY;
	int lastSize;
	int defaultTextSize=14;

	AutoCompletePanel mAutoCompletePanel;
    public CodeEditor(Context context) {
		this(context, null);
	}
    public CodeEditor(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	void init(Context context, AttributeSet attrs) {
		textSize = defaultTextSize;
		setTextSize(textSize);
	}

	public CodeEditor setLineNumBgColor(int color) {
		lineNumberBgColor = color;
		return this;
	}

	public CodeEditor setCurrLineColor(int color) {
		editLineColor = color;
		return this;
	}

	public CodeEditor setTextColor(int color) {
		textColor = color;
		return this;
	}

	public CodeEditor setSelectColor(int color) {
		selectColor = color;
		return this;
	}

	public CodeEditor setTextListener(TextListener listener) {
		textListener = listener;
		return this;
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		setCursorPos(0);
	}

	@Override
	public void setTextSize(int dip) {
		if (dip < 8 || dip > 36 || dip == defaultTextSize)
			return;
		textSize = dip;
		super.setTextSize(dip);
	}

	int getTextSize() {
		return textSize;
	}


	private boolean onTouchZoom(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            if (e.getPointerCount() == 2) {
                if (lastDist == 0) {
                    float x = e.getX(0) - e.getX(1);
                    float y = e.getY(0) - e.getY(1);
                    lastDist = (float) Math.sqrt(x * x + y * y);
                    lastX = (e.getX(0) + e.getX(1)) / 2;
                    lastY = (e.getY(0) + e.getY(1)) / 2;
                    lastSize = getTextSize();
                }

                float dist = spacing(e);
                if (lastDist != 0) {
                    setTextSize((int) (lastSize * (dist / lastDist)));
                }
                return true;
            }
        }
        lastDist = 0;
        return false;
    }

	private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		onTouchZoom(event);
		return super.onTouchEvent(event);
	}


}
