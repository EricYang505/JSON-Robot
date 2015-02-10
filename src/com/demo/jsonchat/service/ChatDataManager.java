package com.demo.jsonchat.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Queue;


public class ChatDataManager extends Observable{
	private static int TASK_IDLE = 0;
	private static int TASK_RUNNING = 1;
	private int taskStatus = TASK_IDLE;

	private List<ChatData> dataList = new ArrayList<ChatData>();
	private Queue<JSONProcessTask> taskQueue = new LinkedList<JSONProcessTask>();

	public ChatDataManager(){
        ChatData data = new ChatData.ChatDataBuilder(true).inputText("Hello, I am JSON Robot!").build();
		dataList.add(data);
	}
	
	public List<ChatData> getDataList() {
		return dataList;
	}

	public void setDataList(List<ChatData> dataList) {
		this.dataList = dataList;
	}

	public void processChat(String text){
		taskQueue.add(new JSONProcessTask(text));
		if (taskStatus == TASK_IDLE) {
			taskStatus = TASK_RUNNING;
			Thread thread = new Thread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (!taskQueue.isEmpty()) {
						taskQueue.poll().run();
				        setChanged();
				        notifyObservers();
				        taskStatus = TASK_IDLE;
					}
				}
			});
			thread.start();
		}
	}
}
