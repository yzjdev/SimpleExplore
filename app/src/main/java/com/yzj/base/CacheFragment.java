package com.yzj.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


abstract class CacheFragment extends Fragment {

    protected View mRootView = null;
    protected boolean mViewCreated = false;

    private SparseArray<View> mCacheViews = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        if (mRootView == null) {
            final int layoutId = getLayoutRes();
            if (layoutId > 0) {
                mRootView = inflater.inflate(getLayoutRes(), container, false);
            }
        }
        mViewCreated = true;
        return mRootView;
    }

	
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
        if (mCacheViews != null) {
            mCacheViews.clear();
            mCacheViews = null;
        }
        mViewCreated = false;
    }

    public View getRootView() {
        return mRootView;
    }

    public final <V extends View> V getView(int id) {
        if (mCacheViews == null) {
            mCacheViews = new SparseArray<>();
        }
        View view = mCacheViews.get(id);
        if (view == null) {
            view = findViewById(id);
            if (view != null) {
                mCacheViews.put(id, view);
            }
        }
        return (V) view;
    }

    public final <V extends View> V findViewById(int id) {
        if (mRootView == null) {
            return null;
        }
        return mRootView.findViewById(id);
    }

    protected abstract int getLayoutRes();
}
