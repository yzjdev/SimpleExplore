package com.yzj.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.v4.provider.DocumentFile;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
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

	private static void copy(InputStream in, OutputStream out) {
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

	public static boolean copy(String old, String dest) {
		List<String> arr=Arrays.asList(old.split("/"));
		List<String> arr2=Arrays.asList(dest.split("/"));
		if(arr.size()<arr2.size()){
			int index=arr.indexOf(getName(old));
			if(arr2.get(index).equals(getName(old)))
				return false;
		}try {
			if (SystemUtil.isPrivacyFile(old)) 
				copyFromPrivacy(old, dest);
			else
				copyTo(old, dest);
			return true;
		} catch (Exception e) {}
		return false;
	}

	//android11从ExternalFiles复制文件
	//param 源文件 不存在则return
	//param 目标文件夹 如果传入的路径存在且为文件则return
	private static void copyFromPrivacy(String old, String dest)throws Exception {
		if (!exists(old)) return;
		if (exists(dest) && isFile(dest))return;
		if (isFile(old)) {
			if (SystemUtil.isPrivacyFile(dest)) {
				createNewFile(dest + "/" + getName(old));
				copy(IOUtil.newInputStream(old), IOUtil.newOutputStream(String.format("%s/%s", dest, getName(old))));
			} else {
				copy(IOUtil.newInputStream(old), IOUtil.newOutputStream(String.format("%s/%s", dest, getName(old))));
			}
		} else {
			if (!exists(dest + "/" + getName(old))) {
				mkdirs(dest + "/" + getName(old));
			}
			Uri dirUri=UriUtil.fromPath(old);
			Uri childrenUri= DocumentsContract.buildChildDocumentsUriUsingTree(dirUri, DocumentsContract.getDocumentId(dirUri));
			String[] proj= new String[] {
				DocumentsContract.Document.COLUMN_DISPLAY_NAME,//文件名
			};
			Cursor cursor= getContentResolver().query(childrenUri, proj, null, null, null);
			if (cursor == null) return;
			while (cursor.moveToNext()) {
				String name=cursor.getString(0);
				copyFromPrivacy(old + "/" + name, dest + "/" + getName(old));
			}
			cursor.close();

		}
	}

	private static void copyTo(String old, String dest)throws Exception {
		if (!exists(old))return;
		if (exists(dest) && isFile(dest))return;

		if (isFile(old)) {
			if (SystemUtil.isPrivacyFile(dest)) {
				createNewFile(dest + "/" + getName(old));
				copy(IOUtil.newInputStream(old), IOUtil.newOutputStream(String.format("%s/%s", dest, getName(old))));	
			} else {
				copy(IOUtil.newInputStream(old), IOUtil.newOutputStream(dest + "/" + getName(old)));
			}
		} else {
			String newFile=dest + "/" + getName(old);
			if (!exists(newFile))
				mkdirs(newFile);
			for (File f:new File(old).listFiles()) {
				copy(f.getPath(), newFile);
			}
		}

	}

	public static boolean isDirectory(String path) {
		if (SystemUtil.isPrivacyFile(path)) {
			return DocumentsContract.Document.MIME_TYPE_DIR.equals(getRawType(UriUtil.fromPath(path)));
		} else {
			return new File(path).isDirectory();
		}
	}

	public static boolean isFile(String path) {
		if (SystemUtil.isPrivacyFile(path)) {
			final String type = getRawType(UriUtil.fromPath(path));
			if (DocumentsContract.Document.MIME_TYPE_DIR.equals(type) || TextUtils.isEmpty(type)) {
				return false;
			} else {
				return true;
			}
		} else {
			return new File(path).isFile();
		}
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
		//return false;
	}

	private static boolean delete(File file) {
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
		if (SystemUtil.isPrivacyFile(old)) {
			try {
				return null != DocumentsContract.renameDocument(getContentResolver(), UriUtil.fromPath(old), newName);
			} catch (FileNotFoundException e) {
			}
		}
		return renameTo(new File(old), newName);
    }

	private static boolean renameTo(File old, String newName) {
		if (!old.exists()) return false;
		return old.renameTo(new File(old.getParent() + "/" + newName));
	}

	//移动文件：源文件完整路径 目标文件夹路径
	public static boolean moveFile(String old, String dest) {
		if(getParent(old).equals(dest))
			return false;
		List<String> arr=Arrays.asList(old.split("/"));
		List<String> arr2=Arrays.asList(dest.split("/"));
		if(arr.size()<arr2.size()){
			int index=arr.indexOf(getName(old));
			if(arr2.get(index).equals(getName(old)))
				return false;
		}
		try {
			if (SystemUtil.isPrivacyFile(old) && SystemUtil.isPrivacyFile(dest)) {
				Uri sourceDocumentUri=UriUtil.fromPath(old);
				Uri sourceParentDocumentUri=UriUtil.fromPath(getParent(old));
				Uri targetParentDocumentUri=UriUtil.fromPath(dest);
				return null != DocumentsContract.moveDocument(getContentResolver(), sourceDocumentUri, sourceParentDocumentUri, targetParentDocumentUri);
			}else if(!SystemUtil.isPrivacyFile(old) && !SystemUtil.isPrivacyFile(dest)){
				return new File(old).renameTo(new File(dest,getName(old)));
			}else{
				if(copy(old,dest))
					return delete(old);
			}
		} catch (FileNotFoundException e) {}
		return false;
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


    private static String getRawType(Uri self) {
        return queryForString(self, DocumentsContract.Document.COLUMN_MIME_TYPE, null);
    }

    private static String queryForString(Uri self, String column, String defaultValue) {
        final ContentResolver resolver = Utils.getApp().getContentResolver();
        Cursor c = null;
        try {
            c = resolver.query(self, new String[] { column }, null, null, null);
            if (c.moveToFirst() && !c.isNull(0)) {
                return c.getString(0);
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        } finally {
            closeQuietly(c);
        }
    }

	public static ContentResolver getContentResolver() {
		return Utils.getApp().getContentResolver();
	}
	public static boolean exists(String path) {
		if (SystemUtil.isPrivacyFile(path)) {
			return exists(UriUtil.fromPath(path));
		} else {
			return new File(path).exists();
		}
	}
	private static boolean exists(Uri self) {
        final ContentResolver resolver =getContentResolver();

        Cursor c = null;
        try {
            c = resolver.query(self, new String[] {DocumentsContract.Document.COLUMN_DOCUMENT_ID }, null, null, null);
            return c.getCount() > 0;
        } catch (Exception e) {
            return false;
        } finally {
            closeQuietly(c);
        }
    }

    private static int queryForInt(Uri self, String column, int defaultValue) {
        return (int) queryForLong(self, column, defaultValue);
    }

    private static long queryForLong(Uri self, String column, long defaultValue) {
        final ContentResolver resolver = Utils.getApp().getContentResolver();

        Cursor c = null;
        try {
            c = resolver.query(self, new String[] { column }, null, null, null);
            if (c.moveToFirst() && !c.isNull(0)) {
                return c.getLong(0);
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        } finally {
            closeQuietly(c);
        }
    }

    private static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }
}
