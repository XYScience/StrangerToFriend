package com.science.strangertofriend.fragment;

import yalantis.com.sidemenu.interfaces.ScreenShotable;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.science.strangertofriend.R;
import com.science.strangertofriend.ui.ShowNearMenMapActivity;

/**
 * @description 摇一摇界面
 * 
 * @author 幸运Science 陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-4-25
 * 
 */

public class ShakeFragment extends Fragment implements ScreenShotable {

	private View mContainerView;
	private Bitmap mBitmap;
	private Button mToNearMapButton;
	private View mRootView;

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.mContainerView = view.findViewById(R.id.shake_container);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.shake_fragment, container, false);

		initComponent();

		return mRootView;
	}

	private void initComponent() {
		mToNearMapButton = (Button) mRootView.findViewById(R.id.to_near_map);
		mToNearMapButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						ShowNearMenMapActivity.class);
				startActivity(intent);
			}
		});
		if (getActivity().getIntent().getStringExtra("username") != null) {
			mToNearMapButton.setText(getActivity().getIntent().getStringExtra(
					"username"));
		}
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
				ShakeFragment.this.mBitmap = bitmap;
			}
		};

		thread.start();
	}

	@Override
	public Bitmap getBitmap() {
		return mBitmap;
	}
}
