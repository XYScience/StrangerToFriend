package com.science.strangertofriend.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.igexin.sdk.PushManager;
import com.science.strangertofriend.R;
import com.science.strangertofriend.utils.GetuiSdkHttpPost;

/**
 * @description ������Ϸ����
 * 
 * @author ����Science ������
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-5-7
 * 
 */

public class DecodeGameActivity extends BaseActivity {

	/**
	 * ������Ӧ��Master Secret���޸�Ϊ��ȷ��ֵ
	 */
	private static final String MASTERSECRET = "LkJJyZCzgJAlYOpgSlzz62";
	// SDK���������Զ���Manifest�ļ��ж�ȡ�������������޸����б��������޸�AndroidManifest.xml�ļ�����Ӧ��meta-data��Ϣ��
	// �޸ķ�ʽ�μ�����SDK�ĵ�
	private String appkey = "";
	private String appsecret = "";
	private String appid = "";
	private SimpleDateFormat formatter = null;
	private Date curDate = null;
	public Button button;
	public static TextView tLogView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.decode_game);

		button = (Button) findViewById(R.id.button);
		tLogView = (EditText) findViewById(R.id.tvlog);
		tLogView.setInputType(InputType.TYPE_NULL);
		tLogView.setSingleLine(false);
		tLogView.setHorizontallyScrolling(false);
		// ���ͳ�ʼ��
		initPush();

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addFriend();
			}
		});

	}

	@SuppressLint("SimpleDateFormat")
	private void initPush() {

		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// ��AndroidManifest.xml��meta-data�ж�ȡSDK������Ϣ
		String packageName = getApplicationContext().getPackageName();
		ApplicationInfo appInfo;
		try {
			appInfo = getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_META_DATA);
			if (appInfo.metaData != null) {

				appid = appInfo.metaData.getString("PUSH_APPID");
				appsecret = appInfo.metaData.getString("PUSH_APPSECRET");
				appkey = (appInfo.metaData.get("PUSH_APPKEY") != null) ? appInfo.metaData
						.get("PUSH_APPKEY").toString() : null;
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		// SDK��ʼ������������������ʱ����Ҫ����SDK��ʼ������
		Log.d("GetuiSdkDemo", "initializing sdk...");
		PushManager.getInstance().initialize(this.getApplicationContext());
	}

	// ��ӵȴ�
	private void addFriend() {

		new SweetAlertDialog(DecodeGameActivity.this,
				SweetAlertDialog.WARNING_TYPE)
				.setTitleText("ȷ�����Ϊ����?")
				.setCancelText("ȡ��")
				.setConfirmText("ȷ��")
				.showCancelButton(true)
				.setCancelClickListener(
						new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								// reuse previous dialog instance, keep
								// widget user state, reset them if you need
								sDialog.setTitleText("��ȡ��!")
										.setConfirmText("OK")
										.showCancelButton(false)
										.setCancelClickListener(null)
										.setConfirmClickListener(null)
										.changeAlertType(
												SweetAlertDialog.SUCCESS_TYPE);
							}
						})
				.setConfirmClickListener(
						new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(final SweetAlertDialog sDialog) {
								sDialog.dismiss();

								final SweetAlertDialog nAlertDialog = new SweetAlertDialog(
										DecodeGameActivity.this,
										SweetAlertDialog.PROGRESS_TYPE)
										.setTitleText("��������,����Ը��");
								nAlertDialog.show();
								nAlertDialog.setCancelable(false);
								new CountDownTimer(800 * 4, 800) {
									public void onTick(long millisUntilFinished) {
										// you can change the progress bar color
										// by ProgressHelper
										// every 800 millis
										colorProgress(nAlertDialog);
									}

									public void onFinish() {
										i = -1;
										nAlertDialog.dismiss();
										friendValidation();
									}
								}.start();
							}
						}).show();
	}

	// ���ͺ�����֤
	private void friendValidation() {

		Intent intent = getIntent();
		final String username = intent.getStringExtra("username");
		AVQuery<AVObject> query = new AVQuery<AVObject>("ClientID");
		query.whereEqualTo("username", username);
		query.findInBackground(new FindCallback<AVObject>() {

			@Override
			public void done(List<AVObject> list, AVException e) {
				if (list != null && list.size() != 0) {
					friendValidationPassthrough(username,
							list.get(list.size() - 1).getString("clientID"));
				}
			}
		});

		/**
		 * ֪ͨ����Ϣ
		 */
		// !!!!!!ע�⣺����Ϊ���Ʒ����API1.0�ӿڣ��������ԡ����Ƽ�������ϵͳʹ��1.0�����˽ӿڣ���ο����µĸ��Ʒ����API�ӿ��ĵ���ʹ�����µ�2.0��ӿ�
		// Map<String, Object> param = new HashMap<String, Object>();
		// param.put("action", "pushSpecifyMessage"); //
		// pushSpecifyMessageΪ�ӿ�����ע���Сд
		// /*---���´��������趨�ӿ���Ӧ����---*/
		// param.put("appkey", appkey);
		// param.put("type", 2); // �������ͣ� 2Ϊ��Ϣ
		// param.put("pushTitle", "֪ͨ������"); // pushTitle����д����Ӧ������
		//
		// // ������Ϣ���ͣ���TransmissionMsg��LinkMsg��NotifyMsg���֣��˴���LinkMsg����
		// param.put("pushType", "LinkMsg");
		//
		// param.put("offline", true); // �Ƿ����������Ϣ
		//
		// param.put("offlineTime", 72); // ��Ϣ���߱���ʱ��
		// param.put("priority", 1); // �����������ȼ�
		//
		// List<String> cidList = new ArrayList<String>();
		// cidList.add(PushManager.getInstance().getClientid(this)); //
		// ����ȡ��ClientID
		// param.put("tokenMD5List", cidList);
		//
		// // ����Signֵ�����ڼ�Ȩ����ҪMasterSecret���������д
		// param.put("sign", GetuiSdkHttpPost.makeSign(MASTERSECRET, param));
		//
		// // LinkMsg��Ϣʵ��
		// Map<String, Object> linkMsg = new HashMap<String, Object>();
		// linkMsg.put("linkMsgIcon", "push.png"); // ��Ϣ��֪ͨ����ͼ��
		// linkMsg.put("linkMsgTitle", "֪ͨ������"); // ������Ϣ�ı���
		// linkMsg.put("linkMsgContent", "���յ�һ��������Ϣ���������www.igetui.com��"); //
		// ������Ϣ������
		// linkMsg.put("linkMsgUrl", "http://www.igetui.com/"); // ���֪ͨ��ת��Ŀ����ҳ
		// param.put("msg", linkMsg);
		//
		// GetuiSdkHttpPost.httpPost(param);
	}

	private void friendValidationPassthrough(String username, String clientID) {
		/**
		 * ͸����Ϣ
		 */
		// !!!!!!ע�⣺����Ϊ���Ʒ����API1.0�ӿڣ��������ԡ����Ƽ�������ϵͳʹ��1.0�����˽ӿڣ���ο����µĸ��Ʒ����API�ӿ��ĵ���ʹ�����µ�2.0��ӿ�
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("action", "pushmessage"); // pushmessageΪ�ӿ�����ע��ȫ��Сд
		/*---���´��������趨�ӿ���Ӧ����---*/
		param.put("appkey", appkey);
		param.put("appid", appid);
		// ע��͸�����ݺ�����������֤�ӿڵ����Ƿ�ɹ����ٶ���дΪhello girl~
		param.put("data", "��������" + username + "��Ϊ����");

		curDate = new Date(System.currentTimeMillis());
		param.put("time", formatter.format(curDate)); // ��ǰ����ʱ�䣬��ѡ
		param.put("clientid", clientID); // ����ȡ��ClientID
											// ����1c6850e1f444df6faf7d34d23a2b8216

		param.put("expire", 3600); // ��Ϣ��ʱʱ�䣬��λΪ�룬��ѡ

		// ����Signֵ�����ڼ�Ȩ
		param.put("sign", GetuiSdkHttpPost.makeSign(MASTERSECRET, param));

		GetuiSdkHttpPost.httpPost(param);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			DecodeGameActivity.this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
