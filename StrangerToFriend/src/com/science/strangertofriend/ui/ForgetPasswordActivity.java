package com.science.strangertofriend.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.science.strangertofriend.R;
import com.science.strangertofriend.utils.AVService;
import com.science.strangertofriend.widget.MyDialog;

/**
 * @description ÕÒ»ØÃÜÂë
 * 
 * @author ÐÒÔËScience ³ÂÍÁŸö
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-4-29
 * 
 */

public class ForgetPasswordActivity extends Activity {

	private EditText mEmailForFind;
	private Button mFindPassword;
	private MyDialog mMyDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);

		// ³Á½þÊ½×´Ì¬À¸ÉèÖÃ
		initSystemBar();
		initComponent();
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void initSystemBar() {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// Í¸Ã÷×´Ì¬À¸
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// Í¸Ã÷µ¼º½À¸
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	private void initComponent() {
		mEmailForFind = (EditText) findViewById(R.id.email_for_find);
		mFindPassword = (Button) findViewById(R.id.find_password);
		mFindPassword.setOnClickListener(findPasswordListener);
		mMyDialog = new MyDialog(ForgetPasswordActivity.this);
	}

	OnClickListener findPasswordListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String email = mEmailForFind.getText().toString();
			if (!email.isEmpty()) {
				RequestPasswordResetCallback callback = new RequestPasswordResetCallback() {
					public void done(AVException e) {
						if (e == null) {
							Toast.makeText(ForgetPasswordActivity.this,
									R.string.forget_password_send_email,
									Toast.LENGTH_LONG).show();
							Intent LoginIntent = new Intent(
									ForgetPasswordActivity.this,
									LoginActivity.class);
							startActivity(LoginIntent);
							ForgetPasswordActivity.this.finish();
						} else {
							mMyDialog
									.errorDialog(
											"·¢ËÍÊ§°Ü",
											ForgetPasswordActivity.this
													.getResources()
													.getString(
															R.string.forget_password_email_error));
						}
					}
				};
				AVService.requestPasswordReset(email, callback);
			} else {
				Toast.makeText(ForgetPasswordActivity.this,
						R.string.error_register_email_address_null,
						Toast.LENGTH_LONG).show();
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			ForgetPasswordActivity.this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
