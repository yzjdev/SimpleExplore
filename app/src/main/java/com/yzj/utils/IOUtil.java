package com.yzj.utils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public class IOUtil {

	public static BufferedInputStream newBufferedInputStream(String path) throws IOException{
		return new BufferedInputStream(newInputStream(path));
	}

	public static BufferedOutputStream newBufferedOutputStream(String path)throws Exception{
		return new BufferedOutputStream(newOutputStream(path));
	}
	
    public static BufferedReader newBufferedReader(String path)throws IOException {
		return new BufferedReader(new InputStreamReader(newInputStream(path)));
	}

	public static BufferedWriter newBufferedWriter(String path) throws IOException {
		return new BufferedWriter(new OutputStreamWriter(newOutputStream(path)));
	}

	//获取输入流
	public static InputStream newInputStream(String path)throws IOException {
		if (SystemUtil.isPrivacyFile(path)) {
			return Utils.getApp().getContentResolver().openInputStream(UriUtil.fromPath(path));
		} else {
			return Files.newInputStream(Paths.get(path));
		}
	}

	//获取输出流
	public static OutputStream newOutputStream(String path)throws IOException {
		if (SystemUtil.isPrivacyFile(path)) {
			return Utils.getApp().getContentResolver().openOutputStream(UriUtil.fromPath(path));
		} else {
			return Files.newOutputStream(Paths.get(path));
		}
	}

	public static void closeQuietly(AutoCloseable closeable) {
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
