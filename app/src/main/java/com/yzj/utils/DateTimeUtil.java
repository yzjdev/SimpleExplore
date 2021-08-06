package com.yzj.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    
    public static String toTime(long time) {
        return toTime(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String toTime(long time, String format) {
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        Date date=new Date(time);
        return sdf.format(date);
    }

    public static String currTime() {
        return currTime("yyyy-MM-dd HH:mm:ss");
    }

    public static String currTime(String format) {
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        Date date=new Date(System.currentTimeMillis());
        return sdf.format(date);
    }
    
}
