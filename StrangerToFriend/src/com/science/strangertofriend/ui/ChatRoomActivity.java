package com.science.strangertofriend.ui;

import android.os.Bundle;
import android.view.View;

import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avoscloud.leanchatlib.activity.ChatActivity;

/**
 * @description 聊天界面
 * 
 * @author 幸运Science 陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-5-18
 * 
 */

public class ChatRoomActivity extends ChatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addLocationBtn.setVisibility(View.VISIBLE);
		// addLocationBtn.setVisibility(View.GONE);
	}

	@Override
	protected void onAddLocationButtonClicked(View v) {
		// toast("这里可以跳转到地图界面，选取地址");
	}

	@Override
	protected void onLocationMessageViewClicked(
			AVIMLocationMessage locationMessage) {
		// toast("onLocationMessageViewClicked");
	}

}
