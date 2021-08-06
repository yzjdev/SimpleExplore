package com.yzj.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import com.yzj.eplorer.MainActivity;

public class PermissionUtil {

	//Android11 请求指定目录权限
    public static void requestPermissionWithSAF(Activity activity, String path, int requestCode) {
		Uri uri=UriUtil.pathToUri(path);
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
		intent.addFlags(
			Intent.FLAG_GRANT_READ_URI_PERMISSION
			| Intent.FLAG_GRANT_WRITE_URI_PERMISSION
			| Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
			| Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
        }
		activity.startActivityForResult(intent, requestCode);
	}

	//保存已请求权限
	public static void savePermission(Intent data) {
		final int takeFlags = data.getFlags()
			& (Intent.FLAG_GRANT_READ_URI_PERMISSION
			| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		Utils.getApp().getContentResolver().takePersistableUriPermission(data.getData(), takeFlags);
	}

	//请求读写权限
	public static void requestWriteAndReadPermisson(Activity activity, int requestCode) {
		ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
	}

	public static void requestManageAllFile(final Activity activity) {
		//判断是否需要所有文件权限
		if (SystemUtil.isAboveR() && !Environment.isExternalStorageManager()) {
			new AlertDialog.Builder(activity)
				.setTitle("提示")
				.setCancelable(false)
				.setMessage("授权以提升文件操作效率")
				.setPositiveButton("确定", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2) {   
						Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
						activity.startActivity(intent);	
					}
				})
				.setNegativeButton("取消", null)
				.show();
        }
	}


	//某一路径是否已授权
	public static boolean isPathGrant(String path) {
		for (UriPermission persistedUriPermission : Utils.getApp().getContentResolver().getPersistedUriPermissions()) {
			if (persistedUriPermission.isReadPermission() && UriUtil.uriToPath(persistedUriPermission.getUri()).equals(path)) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkPrivacyPermission(final Activity activity, String path) {
		String data=Environment.getExternalStorageDirectory() + "/Android/data";
		String obb=Environment.getExternalStorageDirectory() + "/Android/obb";
		final StringBuilder sb=new StringBuilder();
		if (path.startsWith(data)) 
			sb.append(data);
		else if (path.startsWith(obb)) 
			sb.append(obb);
		if (SystemUtil.isAboveR() && (path.startsWith(data) && !isPathGrant(data)) || (path.startsWith(obb) && !isPathGrant(obb))) {
			new AlertDialog.Builder(activity)
				.setTitle("提示")
				.setMessage("授权后访问" + path)
				.setPositiveButton("确定", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface p1, int p2) {
						requestPermissionWithSAF(activity, sb.toString().trim(), MainActivity.REQUEST_CODE_PRIVACY);
					}
				})
				.setNegativeButton("取消", null)
				.show();
			return false;
		} else {
			return true;
		}
	}

}
