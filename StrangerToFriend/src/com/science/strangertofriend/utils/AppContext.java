package com.science.strangertofriend.utils;

import java.util.List;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.ChatManagerAdapter;
import com.avoscloud.leanchatlib.model.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * @description ȫ��Ӧ�ó����ࣺ���ڱ���͵���ȫ��Ӧ�����ü�������������
 * 
 * @author ����Science ������
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-4-26
 * 
 */

public class AppContext extends Application {

	public static boolean isThisLocation = true;

	@Override
	public void onCreate() {
		super.onCreate();
		// U need your AVOS key and so on to run the code.
		AVOSCloud.initialize(getApplicationContext(),
				"naxbv0f9j653brj453n6yzcvlwx44oeuuw1uve2bvzipd3gu",
				"hf1nu0zrbbwupc18c363kwuluu00gf7ujaku0bfr5boapqbc");
		// ���ñ�������ͳ��
		AVAnalytics.enableCrashReport(this.getApplicationContext(), true);
		AVOSCloud.setDebugLogEnabled(true);
		ChatManager.setDebugEnabled(true);// tag leanchatlib

		final ChatManager chatManager = ChatManager.getInstance();
		chatManager.init(this);
		chatManager.setChatManagerAdapter(new ChatManagerAdapter() {
			@Override
			public UserInfo getUserInfoById(String userId) {
				UserInfo userInfo = new UserInfo();
				userInfo.setUsername(userId);
				userInfo.setAvatarUrl("http://ac-x3o016bx.clouddn.com/86O7RAPx2BtTW5zgZTPGNwH9RZD5vNDtPm1YbIcu");
				return userInfo;
			}

			@Override
			public void cacheUserInfoByIdsInBackground(List<String> userIds)
					throws Exception {
			}

			// �������������� leanchat Ӧ���е� ChatManagerAdapterImpl.java
			@Override
			public void shouldShowNotification(Context context, String selfId,
					AVIMConversation conversation, AVIMTypedMessage message) {
				Toast.makeText(context, "�յ���һ����Ϣ����δ����Ӧ�ĶԻ������Դ���ϵͳ֪ͨ��",
						Toast.LENGTH_LONG).show();
			}
		});

		initImageLoader(this);
	}

	/**
	 * ��������Ƿ����
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				// .memoryCache(new WeakMemoryCache())
				.denyCacheImageMultipleSizesInMemory()
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
	}

}
