package com.yzj.treeadapter;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;

public class TreeHelper {
	/**
	 * 传入我们的普通bean，转化为我们排序后的Node
	 * @param datas
	 * @param defaultExpandLevel
	 * @return
	 */
	public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) {
		List<Node> result = new ArrayList<Node>();
		//将用户数据转化为List<Node>以及设置Node间关系
		List<Node> nodes = convetData2Node(datas);
		//拿到根节点
		List<Node> rootNodes = getRootNodes(nodes);
		//排序
		for (Node node : rootNodes) {
			addNode(result, node, defaultExpandLevel, 1);
		}
		return result;
	}

	public static List<Node> filterVisibleNode(List<Node> nodes) {
		List<Node> result=new ArrayList<>();
		for (Node node:nodes) {
			if (node.isRoot() || node.isParentExpand()) {
				setNodeIcon(node);
				result.add(node);
			}
		}
		return result;
	}

	public static <T> List<Node> convetData2Node(List<T> datas) {
		List<Node> nodes=new ArrayList<>();
		Node node=null;
		for (T t:datas) {
			int id=-1;
			int pid=-1;
			String lable=null;
			Class<? extends Object> clzz = t.getClass();
			Field[] declaredFields= clzz.getDeclaredFields();
			for (Field f:declaredFields) {
				try {
					if (f.getAnnotation(TreeNodeId.class) != null) {
						f.setAccessible(true);
						id = f.getInt(t);
					}
					if (f.getAnnotation(TreeNodePid.class) != null) {
						f.setAccessible(true);
						pid = f.getInt(t);
					}
					if (f.getAnnotation(TreeNodeLable.class) != null) {
						f.setAccessible(true);
						lable = (String)f.get(t);
						if (id != -1 && pid != -1 && lable != null) {
							break;
						}}
				} catch (Exception e) {}

			}
			node = new Node(id, pid, lable);
			nodes.add(node);


		}


		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			for (int j = i + 1; j < nodes.size(); j++) {
				Node m = nodes.get(j);
				if (m.pid == n.id) {
					n.children.add(m);
					m.parent = n;
				} else if (m.id == n.pid) {
					m.children.add(n);
					n.parent = m;
				}
			}
		}


		// 设置图片
		for (Node n : nodes) {
			setNodeIcon(n);
		}
		return nodes;

	}


	private static List<Node> getRootNodes(List<Node> nodes) {
		List<Node> root = new ArrayList<Node>();
		for (Node node : nodes) {
			if (node.isRoot())
				root.add(node);
		}
		return root;
	}

	/**
	 * 把一个节点上的所有的内容都挂上去
	 */
	private static void addNode(List<Node> nodes, Node node, int defaultExpandLevel, int currentLevel) {

		nodes.add(node);
		if (defaultExpandLevel >= currentLevel) {
			node.setExpand(true);
		}

		if (node.isLeaf())
			return;
		for (int i = 0; i < node.children.size(); i++) {
			addNode(nodes, node.children.get(i), defaultExpandLevel, currentLevel + 1);
		}
	}

	public static void setNodeIcon(Node node) {
		if (node.children.size() > 0 && node.isExpand) {
			node.icon = expandIcon;
		} else if (node.children.size() > 0 && !node.isExpand) {
			node.icon = collapseIcon;
		} else
			node.icon = -1;

	}

	
	static int expandIcon;
	static int collapseIcon;
	public static void setExpandIcon(int id){
		expandIcon=id;
	}

	public static void setCollapseIcon(int id){
		collapseIcon=id;
	};
}
