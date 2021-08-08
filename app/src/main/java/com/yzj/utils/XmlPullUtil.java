package com.yzj.utils;

import com.yzj.utils.IOUtil;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class XmlPullUtil {
	
	public static XmlPullParser newPullParser(String path) {
		XmlPullParser pullParser=null;
		try {
			XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
			pullParser = factory.newPullParser();
			pullParser.setInput(IOUtil.newBufferedReader(path));
		} catch (Exception e) {}
		return pullParser;
	}
	
	public static boolean checkIsVector(String path) {
		try {
			XmlPullParser pullParser=newPullParser(path);
			int eventType = pullParser.getEventType();
			boolean isStart=false;
			boolean isEnd=false;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = pullParser.getName();
				switch (eventType) {
					case XmlPullParser.START_TAG:
						if ("vector".equals(nodeName)) 
							isStart = true;
						break;
					case XmlPullParser.END_TAG:
						if ("vector".equals(nodeName)) 
							isEnd = true;
						break;
					default:
				}
				eventType = pullParser.next();//读取下一个标签
			}
			if(isStart && isEnd)
				return true;
		} catch (Exception e) {}
		return false;
	}
	
	
	public static boolean svg2xml(String svgPath) {
		try {
			XmlPullParser pullParser=newPullParser(svgPath);
			int eventType = pullParser.getEventType();
			//文档的末尾
			//遍历内部的内容
			StringBuilder stringBuilder = new StringBuilder();
			String viewBox="";
			String width="";
			String height="";
			List<String> fills=new ArrayList<>();
			List<String> ds=new ArrayList<>();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = pullParser.getName();
				switch (eventType) {
					case XmlPullParser.START_TAG:
						if (nodeName.equals("svg")) {
							viewBox = pullParser.getAttributeValue(null, "viewBox");
							width = pullParser.getAttributeValue(null, "width");
							height = pullParser.getAttributeValue(null, "height");
						} else if ("path".equals(nodeName)) {
							fills.add(pullParser.getAttributeValue(null, "fill"));
							ds.add(pullParser.getAttributeValue(null, "d"));
						}
						break;
					case XmlPullParser.END_TAG:
						if ("svg".equals(nodeName)) {
							stringBuilder.append(String.format("viewBox=%s;\nwidth=%s;\nheight=%s;\nfill=%s;\nd=%s", viewBox, width, height, fills, ds));
						}
						break;
					default:
				}
				eventType = pullParser.next();//读取下一个标签
			}
			//转为xml
			StringBuilder sb=new StringBuilder();
			String[] arr=viewBox.split(" ");
			sb.append(String.format(
						  "<vector xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
						  "	android:width=\"%sdp\"\n" +
						  "	android:height=\"%sdp\"\n" +
						  "	android:viewportHeight=\"%s\"\n" +
						  "	android:viewportWidth=\"%s\"\n" +
						  "	android:tint=\"?android:colorControlNormal\">\n"
						  , width, height, arr[2], arr[3]));

			for (int i=0;i < ds.size();i++) {
				sb.append(String.format(
							  "	<path\n" +
							  "		android:fillColor=\"%s\"\n" +
							  "		android:pathData=\"%s\"/>\n"
							  , fills.get(i) == null ?"#FF000000": fills.get(i), ds.get(i)));
			}
			sb.append("</vector>");
			FileUtil.writeText(svgPath.substring(0, svgPath.lastIndexOf(".")) + ".xml", sb.toString().trim());
			return true;
		} catch (Exception e) {
		}
		return false;
	}
}
