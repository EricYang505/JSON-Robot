package com.demo.jsonchat.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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
		BufferedReader bufferReader = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(urlStr);
			HttpResponse response = client.execute(request);

			InputStream in = response.getEntity().getContent();
			bufferReader = new BufferedReader(
					new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = bufferReader.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			return parseTitle(sb.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.w(TAG, e.toString());
		}catch (IOException e) {
			// TODO Auto-generated catch block
			Log.w(TAG, e.toString());
		}finally {
            if(bufferReader != null){
                try {
					bufferReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.w(TAG, e.toString());
				}
            }
        }
		return "";
	}
}


