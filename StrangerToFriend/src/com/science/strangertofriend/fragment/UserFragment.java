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

import com.science.strangertofriend.R;

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

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.mContainerView = view.findViewById(R.id.user_container);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.user_fragment, container,
				false);
		return rootView;
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
