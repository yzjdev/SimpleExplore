package com.yzj.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;

public class AppDetail {

    /**
     *PackageInfo
     *string appName 应用名称
     *drawable appIcon 应用图标
     *String appSize 应用大小
     *string packageName 包名
     *int versionCode 版本号
     *string versionName 版本名称
     *long firstInstallTime 第一次安装时间戳
     *long lastUpdateTime 上次更新时间戳
     *string firstInstallDate
     *string lastUpdateDate
     *
     *ApplicationInfo
     *int targetSdkVersion 目标sdk版本
     *int minSdkVersion 最小支持sdk版本
     *string dataDir 数据目录
     *string sourceDir apk源文件路径
     *int uid
     *int flags
     */

	private Context context;
    private String appName;
    private String packageName;
    private Drawable appIcon;
    private long appSizeByte;
    private String appSize;
    private int versionCode;
    private String versionName;

    private Long firstInstallTime;
    private Long lastUpdateTime;

    private String firstInstallDate;
    private String lastUpdateDate;

    private int targetSdkVersion;
    private int minSdkVersion;
    private String dataDir;
    private String sourceDir;
    private int uid;
    private int flags;

    private String mainActivity;
	
	public AppDetail(Context context,String pkgName){
		this.context=context;
		this.packageName = pkgName;
		init();
	}
	
    private void init(){
		try{
            PackageManager pm = context.getPackageManager();
            appName = pm.getPackageInfo(packageName,0).applicationInfo.loadLabel(pm).toString();
            appIcon = pm.getPackageInfo(packageName,0).applicationInfo.loadIcon(pm);
            versionCode = pm.getPackageInfo(packageName,0).versionCode;
            versionName = pm.getPackageInfo(packageName,0).versionName;
            firstInstallTime = pm.getPackageInfo(packageName,0).firstInstallTime;
            lastUpdateTime = pm.getPackageInfo(packageName,0).lastUpdateTime;

            targetSdkVersion = pm.getApplicationInfo(packageName,0).targetSdkVersion;
            minSdkVersion = Build.VERSION.SDK_INT>=Build.VERSION_CODES.M?pm.getApplicationInfo(packageName,0).minSdkVersion:null;
            dataDir = pm.getApplicationInfo(packageName,0).dataDir;
            sourceDir = pm.getApplicationInfo(packageName,0).sourceDir;
            uid = pm.getApplicationInfo(packageName,0).uid;
            flags = pm.getApplicationInfo(packageName,0).flags;


            appSizeByte = new File(sourceDir).length();
            appSize = DigitsUtil.computeSize(appSizeByte);
            //对安装时间戳进行转换
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            firstInstallDate = sdf.format(new Date(firstInstallTime));
            lastUpdateDate = sdf.format(new Date(lastUpdateTime));

            mainActivity = pm.getLaunchIntentForPackage(packageName).getComponent().getClassName();

        }catch(Exception e){}
	}
    
	

    public String getPackageName(){
        return this.packageName;
    }
    public String getMainActivity(){
        return mainActivity;
    }

    public int getFlags(){
        return flags;
    }

    public int getUid(){
        return uid;
    }

    public String getSourceDir(){
        return sourceDir;
    }

    public String getDataDir(){
        return dataDir;
    }

    public int getMinSdkVersion(){
        return minSdkVersion;
    }

    public int getTargetSdkVersion(){
        return targetSdkVersion;
    }

    public String getLastUpdateDate(){
        return lastUpdateDate;
    }

    public String getFirstInstallDate(){
        return firstInstallDate;
    }

    public String getVersionName(){
        return versionName;
    }

    public int getVersionCode(){
        return versionCode;
    }

    public String getAppSize()
    {
        return appSize;
    }

    public Drawable getAppIcon(){
        return appIcon;
    }

    public String getAppName(){
        return appName;
    }  
	
	public String getAll()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            //创建json
            jsonObject.put("appName",appName);
            jsonObject.put("packageName",packageName);
            jsonObject.put("appSize",appSize);
            jsonObject.put("versionCode",versionCode);
            jsonObject.put("versionName",versionName);
            jsonObject.put("firstInstallDate",firstInstallDate);
            jsonObject.put("lastUpdateDate",lastUpdateDate);

            jsonObject.put("targetSdkVersion",targetSdkVersion);
            jsonObject.put("minSdkVersion",minSdkVersion);
            jsonObject.put("dataDir",dataDir);
            jsonObject.put("sourceDir",sourceDir);
            jsonObject.put("uid",uid);
            jsonObject.put("flags",flags);

            jsonObject.put("mainActivity",mainActivity);
            return jsonObject.toString();
        }
        catch(Exception e){}
        return null;
    }
}
