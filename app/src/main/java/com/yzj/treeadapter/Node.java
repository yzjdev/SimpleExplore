package com.yzj.treeadapter;

import java.util.ArrayList;
import java.util.List;

public class Node {
	
	public int type=Type.PARENT;
	
	public int id;
	public int pid=0;
	public String name;
	public int level;
	public boolean isExpand=false;
	public int icon;
	public List<Node> children=new ArrayList<>();
	public Node parent;
	
	public Node(int id,int pid,String name){
		this.id=id;
		this.pid=pid;
		this.name=name;
	}
	
	//是否根节点
	public boolean isRoot(){
		return parent==null;
	}
	//父节点是否展开
	public boolean isParentExpand(){
		if(parent==null)
			return false;
		return parent.isExpand;
	}
	//是否叶子节点
	public boolean isLeaf(){
		return children.size()==0;
	}
	public int getLevel(){
		return parent==null ?0:parent.getLevel()+1;
	}

	public void setExpand(boolean isExpand){
		this.isExpand=isExpand;
		if(!isExpand){
			for(Node node:children){
				node.setExpand(isExpand);
			}
		}
	}
	
	class Type{

		public static final int PARENT=0;
		public static final int CHILD=1;
	}
}
