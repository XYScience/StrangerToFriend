package com.science.strangertofriend.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.science.strangertofriend.R;
import com.science.strangertofriend.utils.AppContext;

/**
 * @description 基类
 * 
 * @author 幸运Science 陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-4-26
 * 
 */

public class BaseActivity extends Activity {

	private AppContext appContext;// 全局Context
	// 定义一个变量，来标识是否退出
	private static boolean isExit = false;
	private int i = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appContext = (AppContext) getApplication();
		// 网络连接判断
		if (!appContext.isNetworkConnected())
			Toast.makeText(this, R.string.network_not_connected,
					Toast.LENGTH_LONG).show();

		// 沉浸式状态栏设置
		initSystemBar();
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void initSystemBar() {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	// 进度条颜色
	public void colorProgress(SweetAlertDialog pDialog) {
		i++;
		switch (i) {
		case 0:
			pDialog.getProgressHelper().setBarColor(
					getResources().getColor(android.R.color.holo_blue_bright));
			break;

		case 1:
			pDialog.getProgressHelper().setBarColor(
					getResources().getColor(android.R.color.holo_green_light));
			break;
		case 2:
			pDialog.getProgressHelper().setBarColor(
					getResources().getColor(android.R.color.holo_orange_light));
			break;

		case 3:
			pDialog.getProgressHelper().setBarColor(
					getResources().getColor(android.R.color.holo_red_light));
			break;
		}
	}

	// 获取屏幕的宽度
	public int getScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		return screenWidth;
	}

	// 获取屏幕的高度
	public int getScreenHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenHeight = dm.heightPixels;
		return screenHeight;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

}
