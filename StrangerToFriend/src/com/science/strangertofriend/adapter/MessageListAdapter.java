package com.science.strangertofriend.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.science.strangertofriend.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @description 消息列表适配器
 * 
 * @author 幸运Science 陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-5-14
 * 
 */

public class MessageListAdapter extends BaseAdapter {

	private ArrayList<String> mUsernameList;
	private Context mContext;
	private LayoutInflater mInflater;

	public MessageListAdapter(Context context, ArrayList<String> username) {
		mInflater = LayoutInflater.from(context);
		mUsernameList = username;
		mContext = context;
	}

	@SuppressLint({ "InflateParams", "SimpleDateFormat" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_list_message, null);
			new ViewHolder(convertView);
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();

		// viewHolder.avater.setImageDrawable();
		viewHolder.username.setText(mUsernameList.get(position));// 请求好友
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		String sendtime = format.format(date);
		viewHolder.time.setText(sendtime);// 获取短信的接收时间

		return convertView;
	}

	class ViewHolder {
		CircleImageView avater;
		TextView username;
		TextView message;
		TextView time;

		public ViewHolder(View view) {
			avater = (CircleImageView) view.findViewById(R.id.item_avater);
			username = (TextView) view.findViewById(R.id.username);
			message = (TextView) view.findViewById(R.id.message);
			time = (TextView) view.findViewById(R.id.time);
			view.setTag(this);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return mUsernameList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

}
