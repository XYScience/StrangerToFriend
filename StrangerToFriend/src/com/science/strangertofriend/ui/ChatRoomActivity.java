package com.science.strangertofriend.ui;

import android.os.Bundle;
import android.view.View;

import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avoscloud.leanchatlib.activity.ChatActivity;

/**
 * @description �������
 * 
 * @author ����Science ������
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
		// toast("���������ת����ͼ���棬ѡȡ��ַ");
	}

	@Override
	protected void onLocationMessageViewClicked(
			AVIMLocationMessage locationMessage) {
		// toast("onLocationMessageViewClicked");
	}

}
