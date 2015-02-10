package com.demo.jsonchat.service;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.demo.jsonchat.model.LinkModel;
import com.demo.jsonchat.model.MessageModel;


public class ChatData
{
	private final String inputText;
    private final MessageModel message;
    private final Boolean isMe;
 
    private ChatData(ChatDataBuilder builder) {
        this.isMe = builder.isMe;
        this.message = builder.message;
        this.inputText = builder.inputText;
    }
 
    //NO setter for immutable
	public Boolean getIsMe() {
		return isMe;
	}

	public MessageModel getMessage() {
		return message;
	}

    public String getJSONString() throws JSONException {
    	JSONObject obj = new JSONObject();
    	if(message == null){
    		return obj.toString();
    	}
		if (message.getEmoticons().size() > 0){
			JSONArray jsonArray = new JSONArray();
			for (String emotion : message.getEmoticons()) {
				jsonArray.put(emotion);
			}
			obj.put("emoticons", jsonArray);
		}
		if (message.getMentions().size() > 0){
			JSONArray jsonArray = new JSONArray();
			for (String mention : message.getMentions()) {
				jsonArray.put(mention);
			}
			obj.put("mentions", jsonArray);
		}
		if (message.getLinks().size() > 0){
			JSONArray jsonArray = new JSONArray();
			for (LinkModel link : message.getLinks()) {
				JSONObject linkJson = new JSONObject();
				linkJson.put("url", link.getUrl());
				linkJson.put("title", link.getTitle());
				jsonArray.put(linkJson);
			}
			obj.put("links", jsonArray);
		}
    	return "Return (string):\n" + obj.toString();
    }
 
    public String getInputText() {
		return inputText;
	}

	public static class ChatDataBuilder
    {
        private MessageModel message;
        private String inputText;
        private final Boolean isMe;
 
        public ChatDataBuilder(Boolean isMe) {
            this.isMe = isMe;
            message = new MessageModel();
        }
        public ChatDataBuilder mention(String mention) {
            this.message.getMentions().add(mention);
            return this;
        }
        public ChatDataBuilder emotion(String emotion) {
            this.message.getEmoticons().add(emotion);
            return this;
        }
        public ChatDataBuilder link(String url, String title) {
        	LinkModel model = new LinkModel();
        	model.setTitle(title);
        	model.setUrl(url);
            this.message.getLinks().add(model);
            return this;
        }
        public ChatDataBuilder inputText(String text){
        	this.inputText = text;
        	return this;
        }
        public ChatData build() {
        	ChatData chatData =  new ChatData(this);
            return chatData;
        }

    }
}
