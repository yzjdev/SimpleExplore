package com.yzj.eplorer.task;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import com.yzj.eplorer.bean.FileItem;
import com.yzj.utils.SystemUtil;
import com.yzj.utils.UriUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import com.yzj.utils.LogUtil;
import com.yzj.utils.ToastUtil;


public class FileLoadTask extends AsyncTask<Void,Void,Void> {
	//params progress result
	private Activity activity;
	private OnListener mListener;
	private String path;
	private List<FileItem> mDatas=new ArrayList<>();
	public boolean showHiddenFile=false;

	int dirCount,fileCount;//文件夹 文件数量
	
	public FileLoadTask(Activity activity, String path) {
		this.activity = activity;
		this.path = path;
	}

	@Override
	protected Void doInBackground(Void[] p1) {
		if (!mDatas.isEmpty()) mDatas.clear();
		dirCount = 0; fileCount = 0;
		if (SystemUtil.isPrivacyFile(path)) {
			//隐私目录
			handlePrivacy();
		} else {
			//共享目录
			handExternalStorage();
		}
		Collections.sort(mDatas, new FileItem());
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		List<FileItem> dirItems= mDatas.stream().filter(new Predicate<FileItem>(){

				@Override
				public boolean test(FileItem item) {
					return item.isDirectory;
				}
			}).collect(Collectors.toList());
		
		List<FileItem> fileItems= mDatas.stream().filter(new Predicate<FileItem>(){

				@Override
				public boolean test(FileItem item) {
					return !item.isDirectory;
				}
			}).collect(Collectors.toList());
		
		mListener.onSuccess(mDatas, dirItems.size(), fileItems.size());
	}

	
	void handExternalStorage() {
		File[] files= new File(path).listFiles();
		if (files == null) return;
		for (File f:files) {
			boolean isDir=f.isDirectory();
			if (showHiddenFile) {
				if (isDir) dirCount++; else fileCount++;
				mDatas.add(new FileItem(f));
			} else if (!f.isHidden()) {
				if (isDir) dirCount++; else fileCount++;
				mDatas.add(new FileItem(f));
			}
		}
	}

	void handlePrivacy() {
		Uri dirUri=UriUtil.fromPath(path);
		Uri childrenUri= DocumentsContract.buildChildDocumentsUriUsingTree(dirUri, DocumentsContract.getDocumentId(dirUri));
		String[] proj= new String[] {
			DocumentsContract.Document.COLUMN_DOCUMENT_ID,
			DocumentsContract.Document.COLUMN_DISPLAY_NAME,//文件名
			DocumentsContract.Document.COLUMN_MIME_TYPE,
			DocumentsContract.Document.COLUMN_LAST_MODIFIED,//最后修改时间
			DocumentsContract.Document.COLUMN_SIZE//文件大小
		};
		Cursor cursor= activity.getContentResolver().query(childrenUri, proj, null, null, null);
		if (cursor == null) return;
		while (cursor.moveToNext()) {
			Uri uri = DocumentsContract.buildDocumentUriUsingTree(dirUri, cursor.getString(0));
			String name=cursor.getString(1);
			boolean isDir=cursor.getString(2).equals(DocumentsContract.Document.MIME_TYPE_DIR);
			boolean isHidden=name.startsWith(".");
			if (showHiddenFile) {
				if (isDir) dirCount++; else fileCount++;
				mDatas.add(new FileItem(uri, cursor));
			} else if (!isHidden) {
				if (isDir) dirCount++; else fileCount++;
				mDatas.add(new FileItem(uri, cursor));
			}
		}
		cursor.close();
	}

	public FileLoadTask isShowHiddenFile(boolean isShow) {
		showHiddenFile = isShow;
		return this;
	}

	public FileLoadTask setOnListener(OnListener listener)	{
		mListener = listener;
		return this;
	}

	public interface  OnListener {
		public void onSuccess (List<FileItem> datas, int dirCount, int fileCount);
	}

}
