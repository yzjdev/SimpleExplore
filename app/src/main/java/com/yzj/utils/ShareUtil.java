package com.yzj.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import android.support.v4.content.FileProvider;

public class ShareUtil {
	public static void share(Context context, String file)
    {
        try{
			Uri uri=FileProvider.getUriForFile(context,"com.yzj.hg.fileprovider",new File(file));
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);      
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType("*/*");   
            context.startActivity(Intent.createChooser(intent, "分享到"));
        }catch(Exception e){
		}
    }

    public static void shareText(Context context, String text)
    {
        try{
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, text); 
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType("text/plain");
            context.startActivity(Intent.createChooser(intent, "分享到"));
        }catch(Exception e){}
    }
    
    
}
