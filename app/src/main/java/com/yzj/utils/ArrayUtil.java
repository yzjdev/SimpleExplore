package com.yzj.utils;

public class ArrayUtil {

	public static String toString(Object ...obj) {
		StringBuilder sb=new StringBuilder();
		for(Object o:obj){
			sb.append(o).append("  ");
		}
		return sb.toString().trim();
	}
    
}
