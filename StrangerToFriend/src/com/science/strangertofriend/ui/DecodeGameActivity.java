package com.science.strangertofriend.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.igexin.sdk.PushManager;
import com.science.strangertofriend.R;
import com.science.strangertofriend.utils.GetuiSdkHttpPost;

/**
 * @description 解密游戏界面
 * 
 * @author 幸运Science 陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-5-7
 * 
 */

public class DecodeGameActivity extends BaseActivity {

	/**
	 * 第三方应用Master Secret，修改为正确的值
	 */
	private static final String MASTERSECRET = "LkJJyZCzgJAlYOpgSlzz62";
	// SDK参数，会自动从Manifest文件中读取，第三方无需修改下列变量，请修改AndroidManifest.xml文件中相应的meta-data信息。
	// 修改方式参见个推SDK文档
	private String appkey = "";
	private String appsecret = "";
	private String appid = "";
	private SimpleDateFormat formatter = null;
	private Date curDate = null;
	public Button button;
	public static TextView tLogView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.decode_game);

		button = (Button) findViewById(R.id.button);
		tLogView = (EditText) findViewById(R.id.tvlog);
		tLogView.setInputType(InputType.TYPE_NULL);
		tLogView.setSingleLine(false);
		tLogView.setHorizontallyScrolling(false);
		// 推送初始化
		initPush();

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addFriend();
			}
		});

	}

	@SuppressLint("SimpleDateFormat")
	private void initPush() {

		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 从AndroidManifest.xml的meta-data中读取SDK配置信息
		String packageName = getApplicationContext().getPackageName();
		ApplicationInfo appInfo;
		try {
			appInfo = getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_META_DATA);
			if (appInfo.metaData != null) {

				appid = appInfo.metaData.getString("PUSH_APPID");
				appsecret = appInfo.metaData.getString("PUSH_APPSECRET");
				appkey = (appInfo.metaData.get("PUSH_APPKEY") != null) ? appInfo.metaData
						.get("PUSH_APPKEY").toString() : null;
			}

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		// SDK初始化，第三方程序启动时，都要进行SDK初始化工作
		Log.d("GetuiSdkDemo", "initializing sdk...");
		PushManager.getInstance().initialize(this.getApplicationContext());
	}

	// 添加等待
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
										friendValidation();
									}
								}.start();
							}
						}).show();
	}

	// 发送好友验证
	private void friendValidation() {

		Intent intent = getIntent();
		final String username = intent.getStringExtra("username");
		AVQuery<AVObject> query = new AVQuery<AVObject>("ClientID");
		query.whereEqualTo("username", username);
		query.findInBackground(new FindCallback<AVObject>() {

			@Override
			public void done(List<AVObject> list, AVException e) {
				if (list != null && list.size() != 0) {
					friendValidationPassthrough(username,
							list.get(list.size() - 1).getString("clientID"));
				}
			}
		});

		/**
		 * 通知栏消息
		 */
		// !!!!!!注意：以下为个推服务端API1.0接口，仅供测试。不推荐在现网系统使用1.0版服务端接口，请参考最新的个推服务端API接口文档，使用最新的2.0版接口
		// Map<String, Object> param = new HashMap<String, Object>();
		// param.put("action", "pushSpecifyMessage"); //
		// pushSpecifyMessage为接口名，注意大小写
		// /*---以下代码用于设定接口相应参数---*/
		// param.put("appkey", appkey);
		// param.put("type", 2); // 推送类型： 2为消息
		// param.put("pushTitle", "通知栏测试"); // pushTitle请填写您的应用名称
		//
		// // 推送消息类型，有TransmissionMsg、LinkMsg、NotifyMsg三种，此处以LinkMsg举例
		// param.put("pushType", "LinkMsg");
		//
		// param.put("offline", true); // 是否进入离线消息
		//
		// param.put("offlineTime", 72); // 消息离线保留时间
		// param.put("priority", 1); // 推送任务优先级
		//
		// List<String> cidList = new ArrayList<String>();
		// cidList.add(PushManager.getInstance().getClientid(this)); //
		// 您获取的ClientID
		// param.put("tokenMD5List", cidList);
		//
		// // 生成Sign值，用于鉴权，需要MasterSecret，请务必填写
		// param.put("sign", GetuiSdkHttpPost.makeSign(MASTERSECRET, param));
		//
		// // LinkMsg消息实体
		// Map<String, Object> linkMsg = new HashMap<String, Object>();
		// linkMsg.put("linkMsgIcon", "push.png"); // 消息在通知栏的图标
		// linkMsg.put("linkMsgTitle", "通知栏测试"); // 推送消息的标题
		// linkMsg.put("linkMsgContent", "您收到一条测试消息，点击访问www.igetui.com！"); //
		// 推送消息的内容
		// linkMsg.put("linkMsgUrl", "http://www.igetui.com/"); // 点击通知跳转的目标网页
		// param.put("msg", linkMsg);
		//
		// GetuiSdkHttpPost.httpPost(param);
	}

	private void friendValidationPassthrough(String username, String clientID) {
		/**
		 * 透传消息
		 */
		// !!!!!!注意：以下为个推服务端API1.0接口，仅供测试。不推荐在现网系统使用1.0版服务端接口，请参考最新的个推服务端API接口文档，使用最新的2.0版接口
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("action", "pushmessage"); // pushmessage为接口名，注意全部小写
		/*---以下代码用于设定接口相应参数---*/
		param.put("appkey", appkey);
		param.put("appid", appid);
		// 注：透传内容后面需用来验证接口调用是否成功，假定填写为hello girl~
		param.put("data", "正在请求" + username + "加为好友");

		curDate = new Date(System.currentTimeMillis());
		param.put("time", formatter.format(curDate)); // 当前请求时间，可选
		param.put("clientid", clientID); // 您获取的ClientID
											// 红米1c6850e1f444df6faf7d34d23a2b8216

		param.put("expire", 3600); // 消息超时时间，单位为秒，可选

		// 生成Sign值，用于鉴权
		param.put("sign", GetuiSdkHttpPost.makeSign(MASTERSECRET, param));

		GetuiSdkHttpPost.httpPost(param);
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
