package com.yzj.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

public class MimeType {

	public static boolean isTextFile(String path) {
		String[] types=new String[]{"txt"};
		return Arrays.asList(types).contains(getExt(path));
	}

	public static boolean isImageFile(String path) {
		String[] types={"png","jpg","jpeg","gif","tiff","tif"};
		return Arrays.asList(types).contains(getExt(path));
	}

	public static boolean isAPKFile(String path) {
        return getExt(path).equalsIgnoreCase("apk");

    }

    public static boolean isArchiveFile(String path) {
		String[] types={"zip","apk"};
		return Arrays.asList(types).contains(getExt(path));
	}

	public boolean isPdfFile(String file) {
        return getExt(file).equalsIgnoreCase("pdf");
    }

	public static boolean isVideoFile(String path) {
		String[] types={"mp4","avi","m4v","3gp","webm"};
		return Arrays.asList(types).contains(getExt(path));
	}


    public static boolean isAudioFile(String path) {
		String[] types={"mp3","wav","ogg","wma","aac","m4a"};
		return Arrays.asList(types).contains(getExt(path));
	}

	public static boolean isWordFile(String path) {
    	String[] types={"doc","docx"};
		return Arrays.asList(types).contains(getExt(path));
	}

	public static boolean isPowerPointFile(String path) {
    	String[] types={"ppt","pptx"};
		return Arrays.asList(types).contains(getExt(path));
	}

	public static boolean isExcelFile(String path) {
    	String[] types={"xls","xlsx"};
		return Arrays.asList(types).contains(getExt(path));
	}

	public static boolean isCertificateFile(String path) {
        return getExt(path).equalsIgnoreCase("ca");
    }

	public static boolean isCodeFile(String path) {
		String[] types={"myu","iyu","java","kt","html","css","js","php","py","lua","xml","json","smali"};
		return Arrays.asList(types).contains(getExt(path));
	}

	public static String getExt(String path) {
		int index=path.lastIndexOf(".");
		if(index==-1){
			return "";
		}else{
			return path.substring(index+1);
		}
	}

}
