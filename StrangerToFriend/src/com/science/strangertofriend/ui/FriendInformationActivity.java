package com.science.strangertofriend.ui;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.science.strangertofriend.R;
import com.science.strangertofriend.widget.DampView;

/**
 * @description 好友资料
 * 
 * @author 幸运Science 陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-5-22
 * 
 */

public class FriendInformationActivity extends BaseActivity {

	private ImageView mUserBackgroundImg;
	private ImageView mBackImg;

	private ImageView mAvatar;
	private TextView mUsername;
	private ImageView mGender;
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
			setContentView(R.layout.friend_information_kitkat);
		} else {
			setContentView(R.layout.friend_information);
		}

		// 初始化
		initView();
		initData();
		initListener();
	}

	private void initView() {
		// 背景下拉变大
		mUserBackgroundImg = (ImageView) findViewById(R.id.user_background_img);
		DampView view = (DampView) findViewById(R.id.dampview);
		view.setImageView(mUserBackgroundImg);

		mBackImg = (ImageView) findViewById(R.id.back);

		mAvatar = (ImageView) findViewById(R.id.avatar);
		mUsername = (TextView) findViewById(R.id.username);
		mGender = (ImageView) findViewById(R.id.gender);
		mMyStatement = (TextView) findViewById(R.id.my_sign);
		mUserAcount = (TextView) findViewById(R.id.user_number_content);
		mUserPosition = (TextView) findViewById(R.id.user_position_content);
		mUserBirth = (TextView) findViewById(R.id.user_birth_content);
		mUserHome = (TextView) findViewById(R.id.user_home_content);
		mUserConstellation = (TextView) findViewById(R.id.user_constellation);
		mUserInlove = (TextView) findViewById(R.id.user_inlove);
	}

	private void initData() {

		mUsername.setText(getIntent().getStringExtra("username"));

		AVQuery<AVObject> query = new AVQuery<AVObject>("UserInformation");
		query.whereEqualTo("username", getIntent().getStringExtra("username"));
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> avObjects, AVException e) {
				if (e == null) {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = avObjects;
					mFriendHandler.sendMessage(msg);
				} else {
					// Toast.makeText(AlterActivity.this, "请检查网络！",
					// Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mFriendHandler = new Handler() {
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

	// 显示已填写内容
	private void showOldInformation(List<AVObject> responseList) {
		if (responseList != null && responseList.size() != 0) {
			switch (responseList.get(responseList.size() - 1).getString(
					"gender")) {
			case "男":
				mGender.setImageDrawable(getResources().getDrawable(
						R.drawable.user_boy));
				break;

			case "女":
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
				FriendInformationActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			FriendInformationActivity.this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
