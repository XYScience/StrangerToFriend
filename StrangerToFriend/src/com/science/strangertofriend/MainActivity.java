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

import com.avos.avoscloud.AVAnalytics;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.science.strangertofriend.fragment.AddressListFragment;
import com.science.strangertofriend.fragment.MessageFragment;
import com.science.strangertofriend.fragment.ShakeFragment;
import com.science.strangertofriend.fragment.UserFragment;
import com.science.strangertofriend.utils.AppContext;

/**
 * @description
 * 
 * @author 幸运Science·陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @date 2015-4-25
 */

public class MainActivity extends ActionBarActivity implements
		ViewAnimator.ViewAnimatorListener {

	private AppContext appContext;// 全局Context

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mActionBarDrawerToggle;
	private List<SlideMenuItem> mMenuList = new ArrayList<>();
	private ShakeFragment mShakeFragment;
	private UserFragment mUserFragment;
	private MessageFragment mMessageFragment;
	private AddressListFragment mAddressListFragment;
	@SuppressWarnings("rawtypes")
	private ViewAnimator mViewAnimator;
	private LinearLayout mLinearLayout;
	// 定义一个变量，来标识是否退出
	private static boolean isExit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 跟踪统计应用的打开情况
		AVAnalytics.trackAppOpened(getIntent());
		appContext = (AppContext) getApplication();
		// 网络连接判断
		if (!appContext.isNetworkConnected())
			Toast.makeText(this, R.string.network_not_connected,
					Toast.LENGTH_LONG).show();

		// 沉浸式状态栏设置
		initSystemBar();
		initComponent();
		setActionBar();
		createMenuList();
		// list 为菜单每个项的内容
		// contentFragment 为主体显示继承自Fragment 并实现了ScreenShotable接口
		// 最后一个参数为ViewAnimator.ViewAnimatorListener
		// 接口,其中一个方法便是addViewToContainer
		// 在ViewAnimator 中创建view 并添加到 linearLayout 菜单中.
		mViewAnimator = new ViewAnimator<>(this, mMenuList, mShakeFragment,
				mDrawerLayout, this);
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void initSystemBar() {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		// 创建状态栏的管理实例
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// 激活状态栏设置
		tintManager.setStatusBarTintEnabled(true);
		// 激活导航栏设置
		tintManager.setNavigationBarTintEnabled(true);
		// 设置一个颜色给系统栏
		tintManager.setTintColor(Color.parseColor("#e793c1"));
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
		toolbar.setTitle("解密陌生人");
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
	}

	// 摇一摇视图切换动画实现
	public ScreenShotable replaceShakeFragment(ScreenShotable screenShotable,
			int topPosition) {

		CircularRevealAnima(screenShotable, topPosition);

		mShakeFragment = new ShakeFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mShakeFragment).commit();

		return mShakeFragment;
	}

	// 用户信息视图切换动画实现
	public ScreenShotable replaceUserFragment(ScreenShotable screenShotable,
			int topPosition) {

		CircularRevealAnima(screenShotable, topPosition);

		mUserFragment = new UserFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mUserFragment).commit();

		return mUserFragment;
	}

	// 消息视图切换动画实现
	public ScreenShotable replaceMessageFragment(ScreenShotable screenShotable,
			int topPosition) {

		CircularRevealAnima(screenShotable, topPosition);

		mMessageFragment = new MessageFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mMessageFragment).commit();

		return mMessageFragment;
	}

	// 通讯录视图切换动画实现
	public ScreenShotable replaceAddressListFragment(
			ScreenShotable screenShotable, int topPosition) {

		CircularRevealAnima(screenShotable, topPosition);

		mAddressListFragment = new AddressListFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mAddressListFragment).commit();

		return mAddressListFragment;
	}

	@SuppressWarnings("deprecation")
	private void CircularRevealAnima(ScreenShotable screenShotable,
			int topPosition) {
		View view = findViewById(R.id.content_frame);
		int finalRadius = Math.max(view.getWidth(), view.getHeight());
		// 创建圆形动画
		SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
				view, 0, topPosition, 0, finalRadius);
		animator.setInterpolator(new AccelerateInterpolator());
		animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);
		// 由于圆形动画是一点点的扩大的,其没有全部覆盖的部分应该为上一个视图的内容,
		// 因此我们需要将前面的视图截图保存下来,可将下面代码屏蔽可明白其意义.
		findViewById(R.id.content_overlay).setBackgroundDrawable(
				new BitmapDrawable(getResources(), screenShotable.getBitmap()));
		animator.start();
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
		default:
			return screenShotable;
		}
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

	// 添加到菜单的LinearLayout 中去
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
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
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
