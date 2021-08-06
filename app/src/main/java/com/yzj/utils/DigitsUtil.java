package com.yzj.utils;

import java.math.BigDecimal;

public class DigitsUtil {
    
    public static String computeSize(long size){
		String a="0B";
		if (size < 1024) {
			a = size + "B";
		} else if (size / 1024 < 1024) {
			a = twoDecimals(size / 1024f) + "K";
		} else if (size / 1024 / 1024 < 1024) {
			a = twoDecimals(size / 1024 / 1024f) + "M";
		} else if (size / 1024 / 1024 / 1024 < 1024) {
			a = twoDecimals(size / 1024 / 1024 / 1024f) + "G";
		}
		return a;
	}
    

	public static double twoDecimals(float num) {
		BigDecimal bd=new BigDecimal(num);
		return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
