package com.yzj.utils;

import android.net.Uri;
import android.os.Environment;
import java.io.File;
import android.os.Build;
import com.yzj.eplorer.LogActivity;
import android.provider.DocumentsContract;

public class UriUtil {
	//请求特定文件夹权限时使用
	public static Uri pathToUri(String path) {
		String s="content://com.android.externalstorage.documents/tree/primary%3A";
		String s2="/document/primary%3A";
		String root=Environment.getExternalStorageDirectory().getAbsolutePath();
		String uriString;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			path = path.replaceFirst(root, "").replaceFirst("/", "").replace("/", "%2F");
			uriString = s + path + s2 + path;
			return Uri.parse(uriString);
		}
		return Uri.fromFile(new File(path));
	}
	//获取列表时使用 适用Android11 Abdroid/data Android/obb
	public static Uri fromPath(String path) {
		String s="content://com.android.externalstorage.documents/tree/primary%3A";
		String s2="/document/primary%3A";
		String root=Environment.getExternalStorageDirectory().getAbsolutePath();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && path.startsWith(root + "/Android/data") || path.startsWith(root + "/Android/obb")) {
			path = path.replaceFirst(root, "").replaceFirst("/", "").replace("/", "%2F");
			String[] arr=path.split("%2F");
			String uriString = s + arr[0] + "%2F" + arr[1] + s2 + path;
			return Uri.parse(uriString);
		} else {
			return Uri.fromFile(new File(path));
		}
	}

	//uri2path 适用Android11
	public static String uriToPath(Uri uri) {
		// content://com.android.externalstorage.documents/tree/primary%3A
		// Android%2Fdata/document/primary%3A
		// Android%2F%data%2F<pkg>%2Ffiles
		if (SystemUtil.isAboveR()) {
			try {
				return Environment.getExternalStorageDirectory() + "/" + DocumentsContract.getDocumentId(uri).replaceFirst("primary:", "");
			} catch (Exception e) {
				return Environment.getExternalStorageDirectory() + "/" + uri.getPath().replaceFirst("/tree/primary:", "");
			}
		}
		return "";
	}

}
