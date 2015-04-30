package com.science.strangertofriend;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

import java.util.ArrayList;
import java.util.List;

import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.avos.avoscloud.AVAnalytics;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.science.strangertofriend.fragment.AddressListFragment;
import com.science.strangertofriend.fragment.MessageFragment;
import com.science.strangertofriend.fragment.SetFragment;
import com.science.strangertofriend.fragment.ShakeFragment;
import com.science.strangertofriend.fragment.UserFragment;
import com.science.strangertofriend.utils.AppContext;

/**
 * @description
 * 
 * @author ����Science��������
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @date 2015-4-25
 */

public class MainActivity extends ActionBarActivity implements
		ViewAnimator.ViewAnimatorListener {

	private AppContext appContext;// ȫ��Context

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mActionBarDrawerToggle;
	private List<SlideMenuItem> mMenuList = new ArrayList<>();
	private ShakeFragment mShakeFragment;
	private UserFragment mUserFragment;
	private MessageFragment mMessageFragment;
	private AddressListFragment mAddressListFragment;
	private SetFragment mSetFragment;
	@SuppressWarnings("rawtypes")
	private ViewAnimator mViewAnimator;
	private LinearLayout mLinearLayout;
	// ����һ������������ʶ�Ƿ��˳�
	private static boolean isExit = false;
	private int i = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ����ͳ��Ӧ�õĴ����
		AVAnalytics.trackAppOpened(getIntent());
		appContext = (AppContext) getApplication();
		// ���������ж�
		if (!appContext.isNetworkConnected())
			Toast.makeText(this, R.string.network_not_connected,
					Toast.LENGTH_LONG).show();

		// ����ʽ״̬������
		initSystemBar();
		initComponent();
		setActionBar();
		createMenuList();
		// list Ϊ�˵�ÿ���������
		// contentFragment Ϊ������ʾ�̳���Fragment ��ʵ����ScreenShotable�ӿ�
		// ���һ������ΪViewAnimator.ViewAnimatorListener
		// �ӿ�,����һ����������addViewToContainer
		// ��ViewAnimator �д���view ����ӵ� linearLayout �˵���.
		mViewAnimator = new ViewAnimator<>(this, mMenuList, mShakeFragment,
				mDrawerLayout, this);
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void initSystemBar() {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// ͸��״̬��
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// ͸��������
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		// ����״̬���Ĺ���ʵ��
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// ����״̬������
		tintManager.setStatusBarTintEnabled(true);
		// �����������
		tintManager.setNavigationBarTintEnabled(true);
		// ����һ����ɫ��ϵͳ��
		tintManager.setTintColor(Color.parseColor("#dd8fb9"));
	}

	private void initComponent() {

		mShakeFragment = new ShakeFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mShakeFragment).commit();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setScrimColor(Color.TRANSPARENT);
		mLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
		mLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawerLayout.closeDrawers();
			}
		});
	}

	private void setActionBar() {

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("����İ����");
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mActionBarDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout,/* DrawerLayout object */
		toolbar, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close)/* "close drawer" description */
		{
			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				mLinearLayout.removeAllViews();
				mLinearLayout.invalidate();
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				if (slideOffset > 0.6 && mLinearLayout.getChildCount() == 0) {
					mViewAnimator.showMenuContent();
				}
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};
		mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
	}

	private void createMenuList() {

		SlideMenuItem slideMenuItemClose = new SlideMenuItem("Close",
				R.drawable.close_drawer);
		mMenuList.add(slideMenuItemClose);
		SlideMenuItem slideMenuItemShake = new SlideMenuItem("Shake",
				R.drawable.shake);
		mMenuList.add(slideMenuItemShake);
		SlideMenuItem slideMenuItemUser = new SlideMenuItem("User",
				R.drawable.user);
		mMenuList.add(slideMenuItemUser);
		SlideMenuItem slideMenuItemMessage = new SlideMenuItem("Message",
				R.drawable.message);
		mMenuList.add(slideMenuItemMessage);
		SlideMenuItem slideMenuItemAddress = new SlideMenuItem("Address",
				R.drawable.address);
		mMenuList.add(slideMenuItemAddress);
		SlideMenuItem slideMenuItemSet = new SlideMenuItem("Set",
				R.drawable.set);
		mMenuList.add(slideMenuItemSet);
		SlideMenuItem slideMenuItemQuit = new SlideMenuItem("Quit",
				R.drawable.quit);
		mMenuList.add(slideMenuItemQuit);
	}

	// ҡһҡ��ͼ�л�����ʵ��
	public ScreenShotable replaceShakeFragment(ScreenShotable screenShotable,
			int topPosition) {

		CircularRevealAnima(screenShotable, topPosition);

		mShakeFragment = new ShakeFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mShakeFragment).commit();

		return mShakeFragment;
	}

	// �û���Ϣ��ͼ�л�����ʵ��
	public ScreenShotable replaceUserFragment(ScreenShotable screenShotable,
			int topPosition) {

		CircularRevealAnima(screenShotable, topPosition);

		mUserFragment = new UserFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mUserFragment).commit();

		return mUserFragment;
	}

	// ��Ϣ��ͼ�л�����ʵ��
	public ScreenShotable replaceMessageFragment(ScreenShotable screenShotable,
			int topPosition) {

		CircularRevealAnima(screenShotable, topPosition);

		mMessageFragment = new MessageFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mMessageFragment).commit();

		return mMessageFragment;
	}

	// ͨѶ¼��ͼ�л�����ʵ��
	public ScreenShotable replaceAddressListFragment(
			ScreenShotable screenShotable, int topPosition) {

		CircularRevealAnima(screenShotable, topPosition);

		mAddressListFragment = new AddressListFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mAddressListFragment).commit();

		return mAddressListFragment;
	}

	// ������ͼ�л�����ʵ��
	public ScreenShotable replaceSetFragment(ScreenShotable screenShotable,
			int topPosition) {

		CircularRevealAnima(screenShotable, topPosition);

		mSetFragment = new SetFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mSetFragment).commit();

		return mSetFragment;
	}

	@Override
	public ScreenShotable onSwitch(Resourceble slideMenuItem,
			ScreenShotable screenShotable, int topPosition) {

		switch (slideMenuItem.getName()) {
		case "Shake":
			return replaceShakeFragment(screenShotable, topPosition);
		case "User":
			return replaceUserFragment(screenShotable, topPosition);
		case "Message":
			return replaceMessageFragment(screenShotable, topPosition);
		case "Address":
			return replaceAddressListFragment(screenShotable, topPosition);
		case "Set":
			return replaceSetFragment(screenShotable, topPosition);
		case "Quit":
			quitApp();
			return screenShotable;
		default:
			return screenShotable;
		}
	}

	@SuppressWarnings("deprecation")
	private void CircularRevealAnima(ScreenShotable screenShotable,
			int topPosition) {
		View view = findViewById(R.id.content_frame);
		int finalRadius = Math.max(view.getWidth(), view.getHeight());
		// ����Բ�ζ���
		SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
				view, 0, topPosition, 0, finalRadius);
		animator.setInterpolator(new AccelerateInterpolator());
		animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);
		// ����Բ�ζ�����һ���������,��û��ȫ�����ǵĲ���Ӧ��Ϊ��һ����ͼ������,
		// ���������Ҫ��ǰ�����ͼ��ͼ��������,�ɽ�����������ο�����������.
		findViewById(R.id.content_overlay).setBackgroundDrawable(
				new BitmapDrawable(getResources(), screenShotable.getBitmap()));
		animator.start();
	}

	@Override
	public void disableHomeButton() {
		getSupportActionBar().setHomeButtonEnabled(false);
	}

	@Override
	public void enableHomeButton() {
		getSupportActionBar().setHomeButtonEnabled(true);
		mDrawerLayout.closeDrawers();
	}

	// ��ӵ��˵���LinearLayout ��ȥ
	@Override
	public void addViewToContainer(View view) {
		mLinearLayout.addView(view);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mActionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mActionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// �˳�APP
	private void quitApp() {
		new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
				.setTitleText("���Ҫ�˳�ô?")
				.setContentText("��������!")
				.setCancelText("����һ��")
				.setConfirmText("�����˳�")
				.showCancelButton(true)
				.setCancelClickListener(
						new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								// reuse previous dialog instance, keep
								// widget user state, reset them if you need
								sDialog.setTitleText("�Ѿ��ɹ�ȡ��")
										.setConfirmText("ȷ��")
										.setContentText("��ӭ����")
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
							public void onClick(SweetAlertDialog sDialog) {
								sDialog.dismiss();

								final SweetAlertDialog nAlertDialog = new SweetAlertDialog(
										MainActivity.this,
										SweetAlertDialog.PROGRESS_TYPE);
								nAlertDialog.setTitleText("�����˳�..")
										.setContentText("�´��ټ����ǵ�(��o��)�!");
								nAlertDialog.show();
								nAlertDialog.setCancelable(false);
								new CountDownTimer(800 * 4, 800) {
									public void onTick(long millisUntilFinished) {
										// you can change the progress bar color
										// by ProgressHelper
										// every 800 millis
										i++;
										switch (i) {
										case 0:
											nAlertDialog
													.getProgressHelper()
													.setBarColor(
															getResources()
																	.getColor(
																			android.R.color.holo_blue_bright));
											break;

										case 1:
											nAlertDialog
													.getProgressHelper()
													.setBarColor(
															getResources()
																	.getColor(
																			android.R.color.holo_green_light));
											break;
										case 2:
											nAlertDialog
													.getProgressHelper()
													.setBarColor(
															getResources()
																	.getColor(
																			android.R.color.holo_orange_light));
											break;

										case 3:
											nAlertDialog
													.getProgressHelper()
													.setBarColor(
															getResources()
																	.getColor(
																			android.R.color.holo_red_light));
											break;
										}
									}

									public void onFinish() {
										i = -1;
										nAlertDialog.dismiss();
										MainActivity.this.finish();
										System.exit(0);
									}
								}.start();
							}
						}).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
					Toast.LENGTH_SHORT).show();
			// ����handler�ӳٷ��͸���״̬��Ϣ
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

}
