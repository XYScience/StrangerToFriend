package com.science.strangertofriend.fragment;

import yalantis.com.sidemenu.interfaces.ScreenShotable;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.science.strangertofriend.R;
import com.science.strangertofriend.widget.DampView;

/**
 * @description �û���Ϣ
 * 
 * @author ����Science ������
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

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.mContainerView = view.findViewById(R.id.user_container);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.user_fragment, container, false);

		// ��ʼ��
		initComponent();

		return mRootView;
	}

	private void initComponent() {
		// �����������
		mUserBackgroundImg = (ImageView) mRootView
				.findViewById(R.id.user_background_img);
		DampView view = (DampView) mRootView.findViewById(R.id.dampview);
		view.setImageView(mUserBackgroundImg);
	}

	@Override
	public void takeScreenShot() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				Bitmap bitmap = Bitmap.createBitmap(mContainerView.getWidth(),
						mContainerView.getHeight(), Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(bitmap);
				mContainerView.draw(canvas);
				UserFragment.this.mBitmap = bitmap;
			}
		};

		thread.start();
	}

	@Override
	public Bitmap getBitmap() {
		return mBitmap;
	}

}
