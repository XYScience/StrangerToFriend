package com.science.strangertofriend.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.avos.avoscloud.AVOSCloud;

/**
 * @description 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * 
 * @author 幸运Science 陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-4-26
 * 
 */

public class AppContext extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// U need your AVOS key and so on to run the code.
		AVOSCloud.initialize(getApplicationContext(),
				"naxbv0f9j653brj453n6yzcvlwx44oeuuw1uve2bvzipd3gu",
				"hf1nu0zrbbwupc18c363kwuluu00gf7ujaku0bfr5boapqbc");

	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

}
