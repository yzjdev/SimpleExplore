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
        sb.append("\n-----------------------???????????????????????????--------------------------\n");
		sb.append("\n-----------------------" + DateTimeUtil.toTime(System.currentTimeMillis()) + "--------------------------\n");
		sb.append("\n" + e.getMessage() + "\n");
        sb.append(collectExceptionInfos(e));

		// ??????????????????????????????
        //?????????????????????,?????????????????????????????????
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
        // ?????????????????????????????????????????????writer???
        while (mThrowable != null) {
            mThrowable.printStackTrace(mPrintWriter);
            // ?????? ??????????????????????????????
            mPrintWriter.append("\r\n");
            mThrowable = mThrowable.getCause();
        }
        // ????????????
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
