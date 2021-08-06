package com.yzj.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.v4.provider.DocumentFile;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.yzj.eplorer.LogActivity;


/**
 读写文本 复制 删除 重命名 移动
 */
public class FileUtil {

	public static byte[] buffer = new byte[1024];

	public static File newFile(String path) {
		return new File(path);
	}

	public static String readText(String path) {
		StringBuilder sb=new StringBuilder();
		try {
			BufferedReader reader=IOUtil.newBufferedReader(path);
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {}
		return sb.toString().trim();
	}

	public static boolean writeText(String path, String text) {
		try {
			BufferedWriter writer=IOUtil.newBufferedWriter(path);
			writer.write(text);
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {}
		return false;
	}

    public static byte[] readBytes(InputStream input) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			int len;
			while (-1 != (len = input.read(buffer, 0, buffer.length))) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			byte[] ret = baos.toByteArray();
			baos.close();
			return ret;
		} catch (IOException e) {}
		return null;
    }

    //读取asset文件
    public static byte[] readAsset(String name)  {
        try {
			return readBytes(Utils.getApp().getAssets().open(name));
		} catch (IOException e) {}
		return null;
    }


    //读取raw文件
    public static byte[] readRaw(int resId)  {
		return readBytes(Utils.getApp().getResources().openRawResource(resId));
    }

	//根据路径读取文件
    public static byte[] read(String filePathName)  {
		try {
			return readBytes(IOUtil.newInputStream(filePathName));
		} catch (IOException e) {}
		return null;
    }

	public static void copy(InputStream in, OutputStream out) {
		BufferedInputStream bis;
		BufferedOutputStream bos;
		try {
			bis = new BufferedInputStream(in);
			bos = new BufferedOutputStream(out);
			int len;
			while ((len = bis.read(buffer, 0, buffer.length)) != -1) {
				bos.write(buffer, 0, len);
			}
			bos.flush();
			bis.close();
			bos.close();
		} catch (IOException e) {}
	}

	public static void copy(String old, String dest) {
		try {
			copy(IOUtil.newInputStream(old), IOUtil.newOutputStream(dest));
		} catch (IOException e) {}
	}
	//复制asset文件到sd卡
    public static void copyAssetsFile(String fileName, String destFileName) {
		try {
			copy(Utils.getApp().openFileInput(fileName), IOUtil.newOutputStream(destFileName));
		} catch (IOException e) {}
    }

	public static void copyRawFile(int rawId, String dest) {
		try {
			copy(Utils.getApp().getResources().openRawResource(rawId), IOUtil.newOutputStream(dest));
		} catch (IOException e) {}
	}

	//删除文件
	public static boolean delete(String path) {
		if (SystemUtil.isPrivacyFile(path)) {
			return DocumentFile.fromSingleUri(Utils.getApp(), UriUtil.fromPath(path)).delete();
		} else {
			return delete(new File(path));
		}
	}

	public static boolean delete(File file) {
		if (file.isFile())
			return file.delete();
		else {
			for (File f:file.listFiles()) {
				if (!delete(f)) return false;
			}
			return file.delete();
		}
	}

    //重命名文件：源文件完整路径 新名称
    public static boolean renameTo(String old, String newName) {
		return renameTo(new File(old), newName);
    }

	public static boolean renameTo(File old, String newName) {
		if (!old.exists()) return false;
		return old.renameTo(new File(old.getParent() + "/" + newName));
	}

	//移动文件：源文件完整路径 目标文件夹路径
	public static boolean moveFile(String old, String dest) {
		return moveFile(new File(old), new File(dest));
	}
	public static boolean moveFile(File oldFile, File destDir) {
		if (!oldFile.exists()) return false;
		if (destDir.isFile()) return false;
		if (!destDir.exists())
			destDir.mkdirs();
		return oldFile.renameTo(new File(destDir, oldFile.getName()));
	}

	public static boolean createNewFile(String path) {
		try {
			if (SystemUtil.isPrivacyFile(path)) {
				if (DocumentFile.fromSingleUri(Utils.getApp(), UriUtil.fromPath(path)).exists())
					return false;
				Uri dirUri=UriUtil.fromPath(getParent(path));
				ContentResolver content=Utils.getApp().getContentResolver();
				String mimeType=DocumentsContract.Document.COLUMN_DISPLAY_NAME;
				Uri uri=DocumentsContract.createDocument(content, dirUri, mimeType, getName(path));
				return uri != null;
			} else {
				return new File(path).createNewFile();
			}
		} catch (IOException e) {}
		return false;
	}

	public static boolean mkdirs(String path) {
		if (SystemUtil.isPrivacyFile(path)) {
			if (DocumentFile.fromSingleUri(Utils.getApp(), UriUtil.fromPath(path)).exists())
				return false;
			try {
				Uri dirUri=UriUtil.fromPath(getParent(path));
				ContentResolver content=Utils.getApp().getContentResolver();
				String mimeType=DocumentsContract.Document.MIME_TYPE_DIR;
				Uri uri=DocumentsContract.createDocument(content, dirUri, mimeType, getName(path));
				return uri != null;
			} catch (Exception e) {
				mkdirs(getParent(path));
			}
			return mkdirs(path);
		} else {
			return new File(path).mkdirs();
		}
	}

	public static String getParent(String path) {
		if (SystemUtil.isPrivacyFile(path)) {
			return path.substring(0, path.lastIndexOf("/"));
		} else {
			return new File(path).getParent();
		}
	}

	public static String getName(String path) {
		if (SystemUtil.isPrivacyFile(path)) {
			return path.substring(path.lastIndexOf("/") + 1);
		} else {
			return new File(path).getName();
		}
	}
}
