package com.science.strangertofriend.receiver;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.science.strangertofriend.fragment.MessageFragment;

/**
 * @description
 * 
 * @author ����Science ������
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-5-8
 * 
 */

public class CustomReceiver extends BroadcastReceiver {

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d("StrangerToFriend",
				"onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {

		case PushConsts.GET_MSG_DATA:
			// ��ȡ͸������
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");

			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");

			// smartPush��������ִ���ýӿڣ�actionid��ΧΪ90000-90999���ɸ���ҵ�񳡾�ִ��
			boolean result = PushManager.getInstance().sendFeedbackMessage(
					context, taskid, messageid, 90001);
			System.out.println("��������ִ�ӿڵ���" + (result ? "�ɹ�" : "ʧ��"));

			if (payload != null) {
				String friend = new String(payload);// ������
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				String sendTime = format.format(date);
				Log.e("StrangerToFriend", "Got Payload:" + friend);
				MessageFragment.getRequestData(friend, friend + "�������Ϊ����",
						sendTime);
				// DecodeGameActivity.tLogView.append(data + "\n");
			}
			break;
		case PushConsts.GET_CLIENTID:
			// ��ȡClientID(CID)
			// ������Ӧ����Ҫ��CID�ϴ��������������������ҽ���ǰ�û��ʺź�CID���й������Ա��պ�ͨ���û��ʺŲ���CID������Ϣ����
			String cid = bundle.getString("clientid");
			// if (DecodeGameActivity.tView != null)
			// DecodeGameActivity.tView.setText(cid);
			break;
		case PushConsts.THIRDPART_FEEDBACK:
			/*
			 * String appid = bundle.getString("appid"); String taskid =
			 * bundle.getString("taskid"); String actionid =
			 * bundle.getString("actionid"); String result =
			 * bundle.getString("result"); long timestamp =
			 * bundle.getLong("timestamp");
			 * 
			 * Log.d("StrangerToFriend", "appid = " + appid);
			 * Log.d("StrangerToFriend", "taskid = " + taskid);
			 * Log.d("StrangerToFriend", "actionid = " + actionid);
			 * Log.d("StrangerToFriend", "result = " + result);
			 * Log.d("StrangerToFriend", "timestamp = " + timestamp);
			 */
			break;
		default:
			break;
		}
	}
	// public void getData(UsernameCallBack usernameCallBack) {
	// usernameCallBack.getUsername(data);
	// }
	//
	// public interface UsernameCallBack {
	// public void getUsername(String username);
	// }
}
