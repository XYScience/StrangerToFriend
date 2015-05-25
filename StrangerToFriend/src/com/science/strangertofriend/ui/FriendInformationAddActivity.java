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
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.igexin.sdk.PushManager;
import com.science.strangertofriend.MainActivity;
import com.science.strangertofriend.R;
import com.science.strangertofriend.utils.GetuiSdkHttpPost;
import com.science.strangertofriend.widget.DampView;

/**
 * @description ������Ϸ����ѽ���
 * 
 * @author ����Science ������
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-5-7
 * 
 */

public class FriendInformationAddActivity extends BaseActivity {

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

	private ImageView mUserBackgroundImg;
	private ImageView mBackImg;

	private ImageView mAvatar;
	private TextView mUsername;
	private ImageView mGender;
	public Button mAddButton;
	private TextView mMyStatement;
	private TextView mUserAcount;
	private TextView mUserPosition;
	private TextView mUserBirth;
	private TextView mUserHome;
	private TextView mUserInlove;
	private TextView mUserConstellation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			setContentView(R.layout.friend_information_add_kitkat);
		} else {
			setContentView(R.layout.friend_information_add);
		}

		initView();
		initData();
		// ���ͳ�ʼ��
		initPush();
		initListener();

	}

	private void initView() {

		mAddButton = (Button) findViewById(R.id.user_add);
		// �����������
		mUserBackgroundImg = (ImageView) findViewById(R.id.user_background_img);
		DampView view = (DampView) findViewById(R.id.dampview);
		view.setImageView(mUserBackgroundImg);

		mBackImg = (ImageView) findViewById(R.id.back);
		mAvatar = (ImageView) findViewById(R.id.avatar);
		mUsername = (TextView) findViewById(R.id.username);
		mGender = (ImageView) findViewById(R.id.gender);
		mMyStatement = (TextView) findViewById(R.id.friend_mystatement);
		mUserAcount = (TextView) findViewById(R.id.friend_number_content);
		mUserPosition = (TextView) findViewById(R.id.friend_position_content);
		mUserBirth = (TextView) findViewById(R.id.friend_birth_content);
		mUserHome = (TextView) findViewById(R.id.friend_home_content);
		mUserConstellation = (TextView) findViewById(R.id.friend_constellation);
		mUserInlove = (TextView) findViewById(R.id.friend_inlove);
	}

	private void initData() {

		mUsername.setText(getIntent().getStringExtra("receiveUser"));
		mUserPosition.setText(getIntent().getIntExtra("distance", 0) + "m");

		// ���Һ���ͷ��
		AVQuery<AVObject> queryGender = new AVQuery<AVObject>("Gender");
		queryGender.whereEqualTo("username",
				getIntent().getStringExtra("receiveUser"));
		queryGender.findInBackground(findGenderCallback(this, mAvatar));

		// ���Һ�����Ϣ
		AVQuery<AVObject> query = new AVQuery<AVObject>("UserInformation");
		query.whereEqualTo("username", getIntent()
				.getStringExtra("receiveUser"));
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> avObjects, AVException e) {
				if (e == null) {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = avObjects;
					mFriendHandler.sendMessage(msg);
				} else {
					// Toast.makeText(AlterActivity.this, "�������磡",
					// Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mFriendHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				showOldInformation((List<AVObject>) msg.obj);
				break;
			default:
				break;
			}
		}
	};

	// ��ʾ����д����
	private void showOldInformation(List<AVObject> responseList) {
		if (responseList != null && responseList.size() != 0) {
			switch (responseList.get(responseList.size() - 1).getString(
					"gender")) {
			case "��":
				mGender.setImageDrawable(getResources().getDrawable(
						R.drawable.user_boy));
				break;

			case "Ů":
				mGender.setImageDrawable(getResources().getDrawable(
						R.drawable.user_girl));
				break;
			}
			mMyStatement.setText(responseList.get(responseList.size() - 1)
					.getString("personalStatement"));
			mUserAcount.setText(responseList.get(responseList.size() - 1)
					.getString("email"));
			mUserBirth.setText(responseList.get(responseList.size() - 1)
					.getString("birth"));
			mUserHome.setText(responseList.get(responseList.size() - 1)
					.getString("hometown"));
			mUserInlove.setText(responseList.get(responseList.size() - 1)
					.getString("inlove"));
			mUserConstellation.setText(responseList
					.get(responseList.size() - 1).getString("constellation"));
		}
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

	private void initListener() {

		mBackImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FriendInformationAddActivity.this.finish();
			}
		});

		mAddButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addFriend();
			}
		});

		mMyStatement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMyStatement.getText().toString() != null) {

					new SweetAlertDialog(FriendInformationAddActivity.this,
							SweetAlertDialog.CUSTOM_IMAGE_TYPE)
							.setTitleText("Ta��ǩ��")
							.setContentText(mMyStatement.getText().toString())
							.setCustomImage(R.drawable.add_friend_statement)
							.show();
				}
			}
		});
	}

	// ��ӵȴ�
	private void addFriend() {

		new SweetAlertDialog(FriendInformationAddActivity.this,
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
										FriendInformationAddActivity.this,
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

		final String receiveUser = getIntent().getStringExtra("receiveUser");
		final String sendUsername = getIntent().getStringExtra("sendUsername");
		AVQuery<AVObject> query = new AVQuery<AVObject>("ClientID");
		query.whereEqualTo("username", receiveUser);
		query.findInBackground(new FindCallback<AVObject>() {

			@Override
			public void done(List<AVObject> list, AVException e) {
				if (list != null && list.size() != 0) {
					friendValidationPassthrough(sendUsername,
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

	private void friendValidationPassthrough(String sendUsername,
			String clientID) {
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
		param.put("data", sendUsername);

		curDate = new Date(System.currentTimeMillis());
		param.put("time", formatter.format(curDate)); // ��ǰ����ʱ�䣬��ѡ
		param.put("clientid", clientID); // ������֤��Ϣ��clientID�ĺ���
		// ����1c6850e1f444df6faf7d34d23a2b8216

		param.put("expire", 3600); // ��Ϣ��ʱʱ�䣬��λΪ�룬��ѡ

		// ����Signֵ�����ڼ�Ȩ
		param.put("sign", GetuiSdkHttpPost.makeSign(MASTERSECRET, param));

		GetuiSdkHttpPost.httpPost(param);

		Intent intentMain = new Intent(FriendInformationAddActivity.this,
				MainActivity.class);
		startActivity(intentMain);
		FriendInformationAddActivity.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			FriendInformationAddActivity.this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
