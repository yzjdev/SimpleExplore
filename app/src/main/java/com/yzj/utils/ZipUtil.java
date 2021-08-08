package com.yzj.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
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

	public static int unZip(String zipFile, String destDir) {
		int num=0;
		try {
            //解决zip文件中有中文目录或者中文文件
            ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
            for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry)entries.nextElement();
				String zipEntryName = entry.getName();
				File out=new File(destDir, zipEntryName);
				if (!out.getParentFile().exists())//父目录不存在则创建
					out.getParentFile().mkdirs();
				if (entry.isDirectory())//如果是文件夹则跳过本次循环
					continue;
                BufferedInputStream bis= new BufferedInputStream(zip.getInputStream(entry));
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(out));
                int len;
				byte[] buf=new byte[1024];
                while ((len = bis.read(buf, 0, buf.length)) != -1) {
                    bos.write(buf, 0, len);
					num++;
                }
                bos.flush();
                bis.close();
                bos.close();
            }
        } catch (Exception e) {}
		return num;
    }
}
