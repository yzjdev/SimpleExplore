package com.yzj.utils;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import com.yzj.utils.DateTimeUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import android.content.Intent;
import com.yzj.eplorer.CrashActivity;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private static volatile CrashHandler INSTANCE;
	private Context mContext;
	private ConcurrentHashMap<String,String> infos=new ConcurrentHashMap<>();

	private String logSavePath() {
		return Environment.getExternalStorageDirectory().getPath() + "/log";
	}


	public static CrashHandler getInstance() {
		if (INSTANCE == null) {
			synchronized (CrashHandler.class) {
				if (INSTANCE == null) {
					INSTANCE = new CrashHandler();
				}
			}
		}
		return INSTANCE;
	}


	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		if (!uncautchCrashException(e) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(t, e);
		} else {
			Process.killProcess(Process.myPid());
		}
	}


	private boolean uncautchCrashException(Throwable e) {
		if (e == null)
			return false;
		try {
			collectDeviceInfos(e);
			CrashActivity.start(mContext, saveCrashLog(e));
		} catch (Exception ee) {}
		return true;
	}

	void collectDeviceInfos(Throwable e)throws Exception {
		Field[] fileds=Build.class.getDeclaredFields();
		for (Field filed:fileds) {
			filed.setAccessible(true);
			infos.put(filed.getName(), filed.get("").toString());

			PackageManager pm=mContext.getPackageManager();
			PackageInfo pi=pm.getPackageInfo(mContext.getPackageName(), pm.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		}
	}


	String saveCrashLog(Throwable e)throws Exception {
		StringBuilder sb=new StringBuilder();
        sb.append("\n-----------------------获取捕获异常的信息--------------------------\n");
		sb.append("\n-----------------------" + DateTimeUtil.toTime(System.currentTimeMillis()) + "--------------------------\n");
		sb.append("\n" + e.getMessage() + "\n");
        sb.append(collectExceptionInfos(e));

		// 保存文件，设置文件名
        //用于格式化日期,作为日志文件名的一部分
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time = formatter.format(new Date());
		String logName="CrashLog.txt";
        File file = new File(mContext.getExternalCacheDir(),"log");
		if (!file.exists()) 
			file.mkdirs();
		FileOutputStream fos = new FileOutputStream(new File(file, logName));
		fos.write(sb.toString().getBytes());
		fos.flush();
		fos.close();
		return sb.toString().trim();
	}

	private String collectExceptionInfos(Throwable ex) {
        Writer mWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mWriter);
        ex.printStackTrace(mPrintWriter);
        ex.printStackTrace();
        Throwable mThrowable = ex.getCause();
        // 迭代栈队列把所有的异常信息写入writer中
        while (mThrowable != null) {
            mThrowable.printStackTrace(mPrintWriter);
            // 换行 每个个异常栈之间换行
            mPrintWriter.append("\r\n");
            mThrowable = mThrowable.getCause();
        }
        // 记得关闭
		mPrintWriter.flush();
        mPrintWriter.close();
        return mWriter.toString();
    }



	StringBuilder getDeviceInfo() {
		StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        return sb;
	}
}
