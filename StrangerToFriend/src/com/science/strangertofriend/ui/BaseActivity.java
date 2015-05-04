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
 * @description ����
 * 
 * @author ����Science ������
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-4-26
 * 
 */

public class BaseActivity extends Activity {

	private AppContext appContext;// ȫ��Context
	// ����һ������������ʶ�Ƿ��˳�
	private static boolean isExit = false;
	private int i = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appContext = (AppContext) getApplication();
		// ���������ж�
		if (!appContext.isNetworkConnected())
			Toast.makeText(this, R.string.network_not_connected,
					Toast.LENGTH_LONG).show();

		// ����ʽ״̬������
		initSystemBar();
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void initSystemBar() {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// ͸��״̬��
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// ͸��������
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	// ��������ɫ
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

	// ��ȡ��Ļ�Ŀ��
	public int getScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		return screenWidth;
	}

	// ��ȡ��Ļ�ĸ߶�
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
			Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
					Toast.LENGTH_SHORT).show();
			// ����handler�ӳٷ��͸���״̬��Ϣ
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
