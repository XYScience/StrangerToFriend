package com.science.strangertofriend.ui;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.LogInCallback;
import com.science.strangertofriend.MainActivity;
import com.science.strangertofriend.R;
import com.science.strangertofriend.widget.MyDialog;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @description 登陆界面
 * 
 * @author 幸运Science 陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-4-26
 * 
 */

public class LoginActivity extends BaseActivity {

	private CircleImageView mCameraAvatar;
	private EditText mUser, mPassword;
	private Button mLoginButton;
	private TextView mForgetPassword, mRegisterNow;
	private String mUsernameString, mPasswordString;
	private MyDialog mMyDialog;
	private String mObjectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// AVUser currentUser = AVUser.getCurrentUser();
		// if (currentUser != null) {
		// Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		// startActivity(intent);
		// LoginActivity.this.finish();
		// } else {
		setContentView(R.layout.login_layout);
		initComponent();
		addListener();
		// }
	}

	private void initComponent() {
		mCameraAvatar = (CircleImageView) findViewById(R.id.camera_avatar);
		mUser = (EditText) findViewById(R.id.username);
		mPassword = (EditText) findViewById(R.id.password);
		mLoginButton = (Button) findViewById(R.id.login);
		mForgetPassword = (TextView) findViewById(R.id.forget_password);
		mRegisterNow = (TextView) findViewById(R.id.register_now);
		mMyDialog = new MyDialog(this);
	}

	public void addListener() {

		mUser.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					findGenderCallback();
				}
			}
		});

		mForgetPassword.setOnClickListener(new ForgetPasswordListener());
		mRegisterNow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
			}
		});
		mLoginButton.setOnClickListener(new LoginListener());
	}

	// 根据当前输入用户名查找回调
	private void findGenderCallback() {
		mUsernameString = mUser.getText().toString();
		FindCallback<AVObject> findCallback = new FindCallback<AVObject>() {
			public void done(List<AVObject> avObjects, AVException e) {
				if (e == null) {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = avObjects;
					mUsernameHandler.sendMessage(msg);
				} else {
					Toast.makeText(LoginActivity.this, "请检查网络！",
							Toast.LENGTH_LONG).show();
				}
			}
		};

		AVQuery<AVObject> query = new AVQuery<AVObject>("Gender");
		if (isEmail(mUsernameString)) {
			query.whereEqualTo("email", mUsernameString);
		} else {
			query.whereEqualTo("username", mUsernameString);
		}
		query.findInBackground(findCallback);
	}

	@SuppressLint("HandlerLeak")
	private Handler mUsernameHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				List<AVObject> responseList = (List<AVObject>) msg.obj;
				if (responseList != null && responseList.size() != 0) {
					mObjectId = responseList.get(responseList.size() - 1)
							.getObjectId();
					byteToDrawable();
				}
				break;
			}
		}
	};

	private void byteToDrawable() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				AVQuery<AVObject> query = new AVQuery<AVObject>("Gender");
				AVObject gender = null;
				try {
					gender = query.get(mObjectId);
				} catch (AVException e) {
					e.printStackTrace();
				}
				// Retrieving the file
				AVFile imageFile = (AVFile) gender.get("gender");
				imageFile.getDataInBackground(new GetDataCallback() {
					@SuppressWarnings("deprecation")
					public void done(byte[] data, AVException e) {
						if (data != null) {
							// Success; data has the file
							Bitmap bitmap;
							bitmap = BitmapFactory.decodeByteArray(data, 0,
									data.length);
							// LayoutParams laParams = (LayoutParams)
							// mCameraAvatar
							// .getLayoutParams();
							// laParams.width = (getScreenWidth() / 3) + 20;
							// laParams.height = (getScreenHeight() / 3) + 20;
							// mCameraAvatar.setLayoutParams(laParams);
							mCameraAvatar.setImageDrawable(new BitmapDrawable(
									bitmap));
							mCameraAvatar
									.setMaxHeight((getScreenHeight() / 3) + 20);
							mCameraAvatar
									.setMaxWidth((getScreenWidth() / 3) + 20);
							mCameraAvatar
									.setMinimumHeight((getScreenWidth() / 3) + 20);
							mCameraAvatar
									.setMinimumWidth((getScreenWidth() / 3) + 20);
						} else {
							// Failed
						}
					}
				});
			}

		}).start();
	}

	/**
	 * 判断邮箱是否合法
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (null == email || "".equals(email))
			return false;
		// Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
		Matcher m = p.matcher(email);
		return m.matches();
	}

	class LoginListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			mUsernameString = mUser.getText().toString();
			mPasswordString = mPassword.getText().toString();
			if (mUsernameString.isEmpty()) {
				Toast.makeText(LoginActivity.this,
						R.string.error_register_user_name_null,
						Toast.LENGTH_LONG).show();
				return;
			}
			if (mPasswordString.isEmpty()) {
				Toast.makeText(LoginActivity.this,
						R.string.error_register_password_null,
						Toast.LENGTH_LONG).show();
				return;
			}
			progressDialog();
		}
	}

	// 加载提示框
	public void progressDialog() {

		final SweetAlertDialog pDialog = new SweetAlertDialog(this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("拼命加载中、、");
		pDialog.show();
		pDialog.setCancelable(false);
		new CountDownTimer(800 * 4, 800) {
			public void onTick(long millisUntilFinished) {
				// you can change the progress bar color by ProgressHelper
				// every 800 millis
				colorProgress(pDialog);
			}

			public void onFinish() {
				i = -1;
				login();
				pDialog.dismiss();
			}
		}.start();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void login() {
		// 登陆查询
		AVUser.logInInBackground(mUsernameString, mPasswordString,
				new LogInCallback() {
					public void done(AVUser user, AVException e) {
						if (user != null) {
							mMyDialog.successDialog("登陆成功!");
							Intent mainIntent = new Intent(LoginActivity.this,
									MainActivity.class);
							startActivity(mainIntent);
							LoginActivity.this.finish();
						} else {
							mMyDialog
									.errorDialog(
											"登陆失败",
											LoginActivity.this
													.getResources()
													.getString(
															R.string.error_login_error));
						}
					}
				});
	}

	class ForgetPasswordListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent forgetPasswordIntent = new Intent(LoginActivity.this,
					ForgetPasswordActivity.class);
			startActivity(forgetPasswordIntent);
		}

	}

}
