package com.yzj.eplorer.bean;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import com.yzj.eplorer.R;
import com.yzj.utils.DateTimeUtil;
import com.yzj.utils.DigitsUtil;
import com.yzj.utils.MimeType;
import com.yzj.utils.UriUtil;
import java.io.File;
import java.util.Comparator;
import java.util.zip.ZipEntry;

public class FileItem implements Comparator<FileItem> {
	
	@Override
	public int compare(FileItem f, FileItem ff) {
		if (f.isDirectory && ff.isDirectory) {
            return f.name.compareToIgnoreCase(ff.name);
        } else if (f.isDirectory && !ff.isDirectory) {
            return -1;
        } else if (!f.isDirectory && ff.isDirectory) {
            return 1;
        } else {
            return f.name.compareToIgnoreCase(ff.name);
        }
	}

	public String name,path,info;
	public long lastModified,length;
	public boolean isDirectory;
	public int icon;
	
	public FileItem() {
	}

    public FileItem(File f) {
		path=f.getPath();
		name = f.getName();
		isDirectory=f.isDirectory();
		lastModified=f.lastModified();
		length=f.length();
		info=getInfo();
		icon=getIcon();
	}

    public FileItem(Uri uri,Cursor cursor) {
		path=UriUtil.uriToPath(uri);	
		name=cursor.getString(1);
		isDirectory=cursor.getString(2).equals(DocumentsContract.Document.MIME_TYPE_DIR);	
		lastModified=cursor.getLong(3);
		length=cursor.getLong(4);
		info=getInfo();
		icon=getIcon();
	}
	
	
	int getIcon(){
		int id=-1;
		if (isDirectory) {
			id = R.raw.file_icon_dir;
		} else {
			switch (getExt()) {
				case "txt":
					id = R.raw.file_icon_text;
					break;
				case "zip":
					id = R.raw.file_icon_zip;
					break;
				case "mp3":
					id=R.raw.file_icon_mp3;
					break;
				case "apk":
					id=R.raw.file_icon_apk;
					break;
				case "pdf":
					id=R.raw.file_icon_pdf;
					break;
				case "xml":
					id=R.raw.file_icon_xml;
					break;
				default:
					id = R.raw.file_icon_unknown;
					break;
			}
		}
		return id;
	}
	String getInfo(){
		return isDirectory?calculateLastModified(): calculateLastModified()+"  "+calculateSize();
	}
	
	String calculateSize(){
		return DigitsUtil.computeSize(length);
	}
	
	String calculateLastModified(){
		return DateTimeUtil.toTime(lastModified, "yy-MM-dd HH:mm");
	}
	
	public String getExt(){
		return MimeType.getExt(path);
	}
	
}
