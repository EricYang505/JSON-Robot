package com.demo.jsonchat.service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;
import android.util.Patterns;

import com.demo.jsonchat.AppContext;
import com.demo.jsonchat.service.ChatData.ChatDataBuilder;

public class JSONProcessTask implements Runnable{
	private final static String TAG = "JSONProcessTask";
	private String message;
	private Pattern mentionPattern = Pattern.compile("@([\\w]+)");
	private Pattern emotionPattern = Pattern.compile("\\(([\\p{ASCII}&&[^\\()]]{1,15})\\)");
	private Pattern titlePattern = Pattern.compile("<title>(.*?)</title>");

	public JSONProcessTask(String message){
		this.message = message;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ChatDataBuilder dataBuilder = new ChatData.ChatDataBuilder(false);
		processMention(message, dataBuilder);
		processEmotion(message, dataBuilder);
		processLink(message, dataBuilder);
		AppContext.chatDataManager.getDataList().add(dataBuilder.build());
	}
	
	private void processMention(String message, ChatDataBuilder dataBuilder) {
		Matcher mentionMatcher = mentionPattern.matcher(message);
		while (mentionMatcher.find()) {
			String mention = mentionMatcher.group(1);
	        dataBuilder.mention(mention);
		}
	}
	
	private void processEmotion(String message, ChatDataBuilder dataBuilder) {
		Matcher emotionMatcher = emotionPattern.matcher(message);
		while (emotionMatcher.find()) {
			String emotion = emotionMatcher.group(1);
			dataBuilder.emotion(emotion);
		}
	}
	
	private void processLink(String message, ChatDataBuilder dataBuilder) {
		Matcher linkMatcher = Patterns.WEB_URL.matcher(message);
		while(linkMatcher.find()){
		    String url = linkMatcher.group();
			String title = processTitle(url);
			dataBuilder.link(url, title);
		}
	}
	
	private String parseTitle(String htmlText){
		Matcher titleMatcher = titlePattern.matcher(htmlText);
		if(titleMatcher.find()){
			return titleMatcher.group(1);
		}
		return "";
	}
	
	private String processTitle(String urlStr){
		HttpResponse response = null;
        HttpGet httpGet = null;
        HttpClient mHttpClient = null;
        String s = "";
        try {
            if(mHttpClient == null){
                mHttpClient = new DefaultHttpClient();
            }
            httpGet = new HttpGet(urlStr);
            response = mHttpClient.execute(httpGet);
            s = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            Log.w(TAG, e.toString());
        } catch (Exception e){
        	Log.w(TAG, e.toString());
        }
        return parseTitle(s);
	}
}


