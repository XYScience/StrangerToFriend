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
 * @author 幸运Science 陈土燊
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
			// 获取透传数据
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");

			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");

			// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
			boolean result = PushManager.getInstance().sendFeedbackMessage(
					context, taskid, messageid, 90001);
			System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

			if (payload != null) {
				String friend = new String(payload);// 发送者
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				String sendTime = format.format(date);
				Log.e("StrangerToFriend", "Got Payload:" + friend);
				MessageFragment.getRequestData(friend, friend + "已添加您为好友",
						sendTime);
				// DecodeGameActivity.tLogView.append(data + "\n");
			}
			break;
		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
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
