package com.demo.jsonchat.model;

import java.util.ArrayList;
import java.util.List;

public class MessageModel {
	
	private List<String> mentions = new ArrayList<String>();
	
	private List<String> emoticons = new ArrayList<String>();
	
	private List<LinkModel> links = new ArrayList<LinkModel>();

	public List<String> getMentions() {
		return mentions;
	}

	public void setMentions(List<String> mentions) {
		this.mentions = mentions;
	}

	public List<String> getEmoticons() {
		return emoticons;
	}

	public void setEmoticons(List<String> emoticons) {
		this.emoticons = emoticons;
	}

	public List<LinkModel> getLinks() {
		return links;
	}

	public void setLinks(List<LinkModel> links) {
		this.links = links;
	}
}
