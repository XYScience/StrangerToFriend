package com.science.strangertofriend.fragment;

import java.util.List;

import yalantis.com.sidemenu.interfaces.ScreenShotable;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.science.strangertofriend.R;
import com.science.strangertofriend.ui.AlterActivity;
import com.science.strangertofriend.ui.LoginActivity;
import com.science.strangertofriend.utils.AVService;
import com.science.strangertofriend.widget.DampView;

/**
 * @description 用户信息
 * 
 * @author 幸运Science 陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-4-25
 * 
 */

public class UserFragment extends Fragment implements ScreenShotable {

	private View mContainerView;
	private Bitmap mBitmap;
	private ImageView mUserBackgroundImg;
	private View mRootView;

	private ImageView mAvatar;
	private TextView mUsername;
	private ImageView mGender;
	private TextView mMyStatement;
	private TextView mUserAcount;
	private TextView mUserPosition;
	private TextView mUserBirth;
	private TextView mUserHome;
	private TextView mUserConstellation;
	private TextView mUserInlove;
	private Button mLogout;
	private ImageView mAlterPic;

	public int i = -1;

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.mContainerView = view.findViewById(R.id.user_container);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.user_fragment, container, false);

		// 初始化
		initView();
		initData();
		initListener();

		return mRootView;
	}

	private void initView() {
		// 背景下拉变大
		mUserBackgroundImg = (ImageView) mRootView
				.findViewById(R.id.user_background_img);
		DampView view = (DampView) mRootView.findViewById(R.id.dampview);
		view.setImageView(mUserBackgroundImg);

		mAvatar = (ImageView) mRootView.findViewById(R.id.avatar);
		mUsername = (TextView) mRootView.findViewById(R.id.username);
		mGender = (ImageView) mRootView.findViewById(R.id.gender);
		mMyStatement = (TextView) mRootView.findViewById(R.id.my_sign);
		mUserAcount = (TextView) mRootView.findViewById(R.id.user_acount);
		mUserPosition = (TextView) mRootView.findViewById(R.id.user_position);
		mUserBirth = (TextView) mRootView.findViewById(R.id.user_birth_content);
		mUserHome = (TextView) mRootView.findViewById(R.id.user_home_content);
		mUserConstellation = (TextView) mRootView
				.findViewById(R.id.user_constellation);
		mUserInlove = (TextView) mRootView.findViewById(R.id.user_inlove);
		mAlterPic = (ImageView) mRootView
				.findViewById(R.id.user_message_alter_pic);

		mLogout = (Button) mRootView.findViewById(R.id.logout);
	}

	private void initData() {

		mUsername.setText(AVUser.getCurrentUser().getUsername().toString());
		mUserAcount.setText(AVUser.getCurrentUser().getEmail().toString());

		AVQuery<AVObject> queryGender = new AVQuery<AVObject>("Gender");
		queryGender.whereEqualTo("username", AVUser.getCurrentUser()
				.getUsername().toString());
		queryGender.findInBackground(findGenderCallback());

		switch (AVUser.getCurrentUser().get("gender").toString()) {
		case "男":
			mGender.setImageDrawable(getActivity().getResources().getDrawable(
					R.drawable.user_boy));
			break;

		case "女":
			mGender.setImageDrawable(getActivity().getResources().getDrawable(
					R.drawable.user_girl));
			break;
		}

		AVQuery<AVObject> query = new AVQuery<AVObject>("UserInformation");
		query.whereEqualTo("username", mUsername.getText().toString());
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> avObjects, AVException e) {
				if (e == null) {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = avObjects;
					mHandler.sendMessage(msg);
				} else {
					// Toast.makeText(AlterActivity.this, "请检查网络！",
					// Toast.LENGTH_LONG).show();
				}
			}
		});

		AVQuery<AVObject> queryLocation = new AVQuery<AVObject>("MyLocation");
		queryLocation.whereEqualTo("username", mUsername.getText().toString());
		queryLocation.orderByDescending("updatedAt");// 按照时间降序
		queryLocation.setLimit(1);// 最大1个
		queryLocation.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> avObjects, AVException e) {
				if (e == null) {
					mUserPosition.setText(avObjects.get(0)
							.getString("location"));
				} else {
					// Toast.makeText(AlterActivity.this, "请检查网络！",
					// Toast.LENGTH_LONG).show();
					mUserPosition.setText("未定位");
				}
			}
		});

	}

	// 查找回调
	public FindCallback<AVObject> findGenderCallback() {

		FindCallback<AVObject> findCallback = new FindCallback<AVObject>() {
			public void done(List<AVObject> avObjects, AVException e) {
				if (e == null) {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = avObjects;
					mUsernameHandler.sendMessage(msg);
				} else {
					Toast.makeText(getActivity(), "请检查网络！", Toast.LENGTH_LONG)
							.show();
				}
			}
		};
		return findCallback;
	}

	@SuppressLint("HandlerLeak")
	private Handler mUsernameHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				List<AVObject> responseList = (List<AVObject>) msg.obj;
				if (responseList != null && responseList.size() != 0) {
					String objectId = responseList.get(responseList.size() - 1)
							.getObjectId();
					byteToDrawable(objectId);
				}
				break;
			}
		}
	};

	public void byteToDrawable(final String objectId) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				AVQuery<AVObject> query = new AVQuery<AVObject>("Gender");
				AVObject gender = null;
				try {
					gender = query.get(objectId);
				} catch (AVException e) {
					e.printStackTrace();
				}
				// Retrieving the file
				AVFile imageFile = (AVFile) gender.get("avater");

				Message msg = new Message();
				msg.what = 1;
				msg.obj = imageFile.getUrl();
				mHandlerLoad.sendMessage(msg);
			}

		}).start();
	}

	// 子线程Handler刷新UI界面
	@SuppressLint("HandlerLeak")
	private Handler mHandlerLoad = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				@SuppressWarnings("deprecation")
				DisplayImageOptions options = new DisplayImageOptions.Builder()
						.showStubImage(R.drawable.default_load)
						.showImageForEmptyUri(R.drawable.default_load)
						.showImageOnFail(R.drawable.default_load)
						.cacheInMemory(true).cacheOnDisc(true)
						.bitmapConfig(Bitmap.Config.RGB_565).build();
				ImageLoader.getInstance().displayImage((String) msg.obj,
						mAvatar, options);
			}
		};
	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
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
			mUserBirth.setText(responseList.get(responseList.size() - 1)
					.getString("birth"));
			mUserHome.setText(responseList.get(responseList.size() - 1)
					.getString("hometown"));
			mUserInlove.setText(responseList.get(responseList.size() - 1)
					.getString("inlove"));
			mUserConstellation.setText(responseList
					.get(responseList.size() - 1).getString("constellation"));
			mMyStatement.setText(responseList.get(responseList.size() - 1)
					.getString("personalStatement"));
		}
	}

	private void initListener() {

		mAlterPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AlterActivity.class);
				intent.putExtra("birth", mUserBirth.getText().toString());
				intent.putExtra("hometown", mUserHome.getText().toString());
				intent.putExtra("inlove", mUserInlove.getText().toString());
				intent.putExtra("constellation", mUserConstellation.getText()
						.toString());
				intent.putExtra("personalStatement", mMyStatement.getText()
						.toString());
				startActivityForResult(intent, 1);
			}
		});

		// 退出登录
		mLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutDialog();
			}
		});
	}

	// 退出当前账号提示
	private void logoutDialog() {
		final SweetAlertDialog nAlertDialog = new SweetAlertDialog(
				getActivity(), SweetAlertDialog.PROGRESS_TYPE);
		nAlertDialog.setTitleText("正在努力退出").setContentText("请稍后");
		nAlertDialog.show();
		nAlertDialog.setCancelable(false);
		new CountDownTimer(800 * 4, 800) {
			public void onTick(long millisUntilFinished) {
				colorProgress(nAlertDialog);
			}

			public void onFinish() {
				i = -1;
				nAlertDialog.dismiss();
				AVService.logout();
				Intent loginIntent = new Intent(getActivity(),
						LoginActivity.class);
				startActivity(loginIntent);
				getActivity().finish();
			}
		}.start();
	}

	// 进度条颜色
	private void colorProgress(SweetAlertDialog pDialog) {
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if (resultCode == -1) {
				mMyStatement.setText(data.getStringExtra("personalStatement"));
				mUserBirth.setText(data.getStringExtra("birth"));
				mUserHome.setText(data.getStringExtra("hometown"));
				mUserConstellation
						.setText(data.getStringExtra("constellation"));
				mUserInlove.setText(data.getStringExtra("inlove"));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void takeScreenShot() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap = Bitmap.createBitmap(mContainerView.getWidth(),
						mContainerView.getHeight(), Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(bitmap);
				mContainerView.draw(canvas);
				UserFragment.this.mBitmap = bitmap;
			}
		}, 0);
		// Thread thread = new Thread() {
		// @Override
		// public void run() {
		// Bitmap bitmap = Bitmap.createBitmap(mContainerView.getWidth(),
		// mContainerView.getHeight(), Bitmap.Config.ARGB_8888);
		// Canvas canvas = new Canvas(bitmap);
		// mContainerView.draw(canvas);
		// UserFragment.this.mBitmap = bitmap;
		// }
		// };
		// thread.start();
	}

	@Override
	public Bitmap getBitmap() {
		return mBitmap;
	}

}
