package com.science.strangertofriend.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SendCallback;
import com.science.strangertofriend.R;
import com.science.strangertofriend.utils.AVService;
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
		// �������ͳ�ʼ��
		// initPush();
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
		AVQuery<AVUser> query = AVUser.getQuery();
		query.whereEqualTo("username", receiveUser);
		query.findInBackground(new FindCallback<AVUser>() {

			@Override
			public void done(List<AVUser> list, AVException arg1) {
				if (list != null && list.size() != 0) {
					friendValidationPassthrough(sendUsername, receiveUser, list
							.get(list.size() - 1).getString("installationId"));
				}
			}
		});
	}

	private void friendValidationPassthrough(final String sendUsername,
			final String receiveUser, String receiveUserID) {

		AVPush push = new AVPush();
		// ����Ƶ��
		push.setChannel("public");
		// ������Ϣ
		push.setMessage(sendUsername + "�������Ϊ����");
		// ���ò�ѯ����
		push.setQuery(AVInstallation.getQuery().whereEqualTo("installationId",
				receiveUserID));
		// ����
		push.sendInBackground(new SendCallback() {
			@Override
			public void done(AVException e) {
				Toast toast = null;
				if (e == null) {
					toast = Toast.makeText(FriendInformationAddActivity.this,
							"���ͳɹ�", Toast.LENGTH_SHORT);
					findFriendObjId(receiveUser, sendUsername);
				} else {
					toast = Toast.makeText(FriendInformationAddActivity.this,
							"����ʧ�ܣ���������", Toast.LENGTH_SHORT);
				}
				// callback ������ UI �̡߳�
				toast.show();
			}
		});
	}

	private void findFriendObjId(final String receiveUser,
			final String sendUsername) {
		AVQuery<AVObject> queryFriend = new AVQuery<AVObject>("Gender");
		queryFriend.whereEqualTo("username", receiveUser);
		queryFriend.findInBackground(new FindCallback<AVObject>() {

			@Override
			public void done(List<AVObject> list, AVException arg1) {
				if (list != null && list.size() != 0) {
					findFriendAvaterUrl(receiveUser, sendUsername,
							list.get(list.size() - 1).getObjectId(), 1);
				}
			}
		});

		AVQuery<AVObject> queryCurrentUser = new AVQuery<AVObject>("Gender");
		queryCurrentUser.whereEqualTo("username", sendUsername);
		queryCurrentUser.findInBackground(new FindCallback<AVObject>() {

			@Override
			public void done(List<AVObject> list, AVException arg1) {
				if (list != null && list.size() != 0) {
					findFriendAvaterUrl(receiveUser, sendUsername,
							list.get(list.size() - 1).getObjectId(), 2);
				}
			}
		});
	}

	private void findFriendAvaterUrl(final String receiveUser,
			final String sendUsername, final String objId, final int id) {

		new Thread(new Runnable() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void run() {

				// ȡ�÷���ʱ��
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				String sendTime = format.format(date);

				AVQuery<AVObject> query = new AVQuery<AVObject>("Gender");
				AVObject avObj = null;
				try {
					avObj = query.get(objId);
				} catch (AVException e) {
					e.printStackTrace();
				}
				// Retrieving the file
				switch (id) {
				case 1:
					AVFile imageFileFriend = (AVFile) avObj.get("gender");
					String avaterUrlFriend = imageFileFriend.getThumbnailUrl(
							false, 150, 150);
					// ������Ϣ
					AVService.messageList(receiveUser, avaterUrlFriend,
							sendUsername, sendTime, receiveUser + "�������Ϊ����");
					// �������ͨѶ¼
					AVService.addressList(receiveUser, sendUsername,
							avaterUrlFriend, sendTime);
					break;

				case 2:
					AVFile imageFileCurrent = (AVFile) avObj.get("gender");
					String avaterUrlCurrent = imageFileCurrent.getThumbnailUrl(
							false, 150, 150);
					AVService.messageList(sendUsername, avaterUrlCurrent,
							receiveUser, sendTime, sendUsername + "�������Ϊ����");
					AVService.addressList(sendUsername, receiveUser,
							avaterUrlCurrent, sendTime);
					break;
				}

			}

		}).start();
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
