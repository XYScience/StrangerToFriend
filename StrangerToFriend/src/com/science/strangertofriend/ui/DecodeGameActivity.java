package com.science.strangertofriend.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @description
 * 
 * @author 幸运Science 陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-5-7
 * 
 */

public class DecodeGameActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		Button button = new Button(this);
		setContentView(ll);
		ll.addView(button);
		ll.setGravity(Gravity.CENTER);
		button.setText("add friend");
		button.setBackgroundColor(0xffcccccc);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addFriend();
			}
		});
	}

	private void addFriend() {

		new SweetAlertDialog(DecodeGameActivity.this,
				SweetAlertDialog.WARNING_TYPE)
				.setTitleText("确定添加为好友?")
				.setCancelText("取消")
				.setConfirmText("确认")
				.showCancelButton(true)
				.setCancelClickListener(
						new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								// reuse previous dialog instance, keep
								// widget user state, reset them if you need
								sDialog.setTitleText("已取消!")
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
										.setTitleText("邂逅相遇,适我愿兮");
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
									}
								}.start();
							}
						}).show();
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
