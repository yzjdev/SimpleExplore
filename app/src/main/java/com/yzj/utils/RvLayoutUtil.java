package com.yzj.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public class RvLayoutUtil {
    
	public static LinearLayoutManager setLinearLayout(RecyclerView recyclerView){
		LinearLayoutManager ll;
		recyclerView.setLayoutManager(ll=new LinearLayoutManager(Utils.getApp()));
		return ll;
	}

	public static void setGridLayout(RecyclerView recyclerView,int column){
		recyclerView.setLayoutManager(new GridLayoutManager(Utils.getApp(),column));
	}

	//direction: horizontal 0 vertical 1
	public static void setStaggeredVerticalLayout(RecyclerView recyclerView,int column){
		recyclerView.setLayoutManager(new StaggeredGridLayoutManager(column,1));
	}
    public static void setStaggeredHorizontalLayout(RecyclerView recyclerView,int column){
		recyclerView.setLayoutManager(new StaggeredGridLayoutManager(column,0));
	}
}
