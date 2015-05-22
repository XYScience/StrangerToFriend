package com.science.strangertofriend.fragment;

import yalantis.com.sidemenu.interfaces.ScreenShotable;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.avos.avoscloud.AVUser;
import com.science.strangertofriend.R;
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
	private TextView mMySign;
	private TextView mUserAcount;
	private TextView mUserPosition;
	private Button mLogout;

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
		mMySign = (TextView) mRootView.findViewById(R.id.my_sign);
		mUserAcount = (TextView) mRootView.findViewById(R.id.user_acount);
		mUserPosition = (TextView) mRootView.findViewById(R.id.user_position);
		mLogout = (Button) mRootView.findViewById(R.id.logout);
	}

	private void initData() {

		mAvatar.setImageBitmap((Bitmap) getActivity().getIntent()
				.getParcelableExtra("avater"));
		mUsername.setText(AVUser.getCurrentUser().getUsername().toString());
		mUserAcount.setText(AVUser.getCurrentUser().getEmail().toString());

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

	}

	private void initListener() {

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
