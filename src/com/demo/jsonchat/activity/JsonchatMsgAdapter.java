package com.demo.jsonchat.activity;


import java.util.List;

import org.json.JSONException;

import com.demo.jsonchat.R;
import com.demo.jsonchat.service.ChatData;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class JsonchatMsgAdapter extends ArrayAdapter<ChatData> {

	private LayoutInflater mInflater;
	private Context mContext;

	public JsonchatMsgAdapter(Activity context, int textViewResourceId, List<ChatData> list) {
		super(context, textViewResourceId, list);
		this.mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	class ViewHolder {
		TextView name;
		TextView message;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.nameId);
			holder.message = (TextView) convertView.findViewById(R.id.msgId);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ChatData data = getItem(position);

		if(data.getIsMe()){
			holder.name.setText("Me:");
			holder.name.setTextColor(mContext.getResources().getColor(R.color.black));
			holder.message.setTextColor(mContext.getResources().getColor(R.color.black));
			holder.message.setText(data.getInputText());
		}else{
			holder.name.setText("Robot:");
			holder.name.setTextColor(mContext.getResources().getColor(R.color.blue));
			holder.message.setTextColor(mContext.getResources().getColor(R.color.blue));
			try {
				holder.message.setText(data.getJSONString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return convertView;
	}
}
