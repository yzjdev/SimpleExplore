package com.yzj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	//压缩单个文件
	public static boolean compressFile(String sourceFile, String destFile)throws IOException {
		File file=new File(sourceFile);
		if (!file.exists()) return false;
		ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(destFile));
		recursiveCompression(file, file.getName(), zos);
		zos.close();
		return true;
	}

	//压缩文件列表
	public static boolean compressList(List<String> sourceList, String destFile) throws IOException {
		ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(destFile));
		for (String sourceFile:sourceList) {
			File file =new File(sourceFile);
			if (!file.exists()) return false;
			recursiveCompression(file, file.getName(), zos);
		}
		zos.close();
		return true;
	}

	//递归压缩
	private static void recursiveCompression(File sourceFile, String filename, ZipOutputStream zos)throws IOException {
		if (sourceFile.isDirectory()) {
			filename = filename.endsWith("/") ?filename: filename + "/";
			zos.putNextEntry(new ZipEntry(filename));
			zos.closeEntry();
			for (File child:sourceFile.listFiles()) {
				recursiveCompression(child, filename + child.getName(), zos);
			}
			return;
		}
		FileInputStream fis=new FileInputStream(sourceFile);
		zos.putNextEntry(new ZipEntry(filename));
		byte[] bytes=new byte[1024];
		int len;
		while ((len = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, len);
		}
		fis.close();
	}

    //解压文件
	public static boolean unCompress(String zipFile, String destDir) {

		try {
			byte[] buffer=new byte[1024];
			ZipInputStream zis=new ZipInputStream(new FileInputStream(zipFile));
			ZipEntry zipEntry=zis.getNextEntry();
			while (zipEntry != null) {
				File file=new File(destDir, zipEntry.getName());
				if (zipEntry.isDirectory()) {
					if (!file.exists()) file.mkdirs();
				} else {
					FileOutputStream fos=new FileOutputStream(file);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
				}
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
