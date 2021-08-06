package com.yzj.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {
	
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private static PrefUtil helper;
    private static PrefUtil getInstance(){
        if(helper==null) helper=new PrefUtil();
        return helper;
    }

    private PrefUtil(){
        sp=Utils.getApp().getSharedPreferences("PrefData",Context.MODE_PRIVATE);
        editor=sp.edit();
    }

    //bool float int long string
    public static void writeBool(String key,boolean value){
        getInstance().editor.putBoolean(key,value);
        getInstance().editor.apply();
    }

    public static void writeFloat(String key,float value){
        getInstance().editor.putFloat(key,value);
        getInstance().editor.apply();
    }

    public static void writeInt(String key,int value){
        getInstance().editor.putInt(key,value);
        getInstance().editor.apply();
    }
    public static void writeLong(String key,Long value){
        getInstance().editor.putLong(key,value);
        getInstance().editor.apply();
    }
    public static void writeString(String key,String value){
        getInstance().editor.putString(key,value);
        getInstance().editor.apply();
    }
    public static String readString(String key){
        return getInstance().sp.getString(key, "");
    }

    public static Long readLong(String key){
        return getInstance().sp.getLong(key, 0);
    }

    public static int readInt(String key){
        return getInstance().sp.getInt(key, 0);
    }

    public static float readFloat(String key){
        return getInstance().sp.getFloat(key, 0);
    }

    public static boolean readBool(String key){
        return getInstance().sp.getBoolean(key,false);
	}
    
    
}
