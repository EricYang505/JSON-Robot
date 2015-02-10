package com.demo.jsonchat.activity;


import java.util.Observable;
import java.util.Observer;

import com.demo.jsonchat.AppContext;
import com.demo.jsonchat.R;
import com.demo.jsonchat.service.ChatData;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends ListActivity implements Observer{
	private JsonchatMsgAdapter mAdapter;
	private EditText mText;
    private ImageButton mSendButton;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		mAdapter = buildViewHolderAdapter(this, R.layout.list_item);
        setListAdapter(mAdapter);        
        mText = (EditText) findViewById(R.id.msgEditTextId);
        mSendButton = (ImageButton) findViewById(R.id.msgImageBtnId);
        AppContext.chatDataManager.addObserver(this);
        if (mSendButton!=null){
        	mSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                	if(mText.getText().length()!=0){
                		sendMessage();
                		mText.setText("");
                	}
                }
            });
        }
	}
	
    @Override
    public void onDestroy(){
    	super.onDestroy();
        AppContext.chatDataManager.deleteObserver(this);
    }
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	default:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
	private JsonchatMsgAdapter buildViewHolderAdapter(Activity context, int textViewResourceId) {
		JsonchatMsgAdapter adapter = new JsonchatMsgAdapter(context, textViewResourceId, AppContext.chatDataManager.getDataList());
		return adapter;
	}
	
    private void sendMessage() {
        ChatData msg = new ChatData.ChatDataBuilder(true).inputText(mText.getText().toString()).build();
        AppContext.chatDataManager.getDataList().add(msg);
        mAdapter.notifyDataSetChanged();
		AppContext.chatDataManager.processChat(mText.getText().toString());
    }

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		this.getListView().post(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
		        mAdapter.notifyDataSetChanged();
			}
		});
	}
}
