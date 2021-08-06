package com.yzj.utils;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.LinearLayout;
import java.lang.reflect.Field;
import android.widget.TextView;

public class ReflexUtil {
	public static int getIdFromInternalR(String idName){
        try {
            Class clasz = Class.forName("com.android.internal.R$id");
            Field field = clasz.getDeclaredField(idName);
            field.setAccessible(true);
            return field.get(null);
        } catch (Exception e) {
        }
        return 0;
    }
}
