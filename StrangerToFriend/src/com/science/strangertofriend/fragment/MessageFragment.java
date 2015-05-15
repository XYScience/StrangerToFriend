package com.science.strangertofriend.fragment;

import java.util.ArrayList;

import yalantis.com.sidemenu.interfaces.ScreenShotable;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.science.strangertofriend.R;
import com.science.strangertofriend.adapter.MessageListAdapter;
import com.science.strangertofriend.adapter.SwingBottomInAnimationAdapter;

/**
 * @description 消息界面
 * 
 * @author 幸运Science 陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-4-25
 * 
 */

public class MessageFragment extends Fragment implements ScreenShotable,
		OnRefreshListener {

	private View mContainerView;
	private Bitmap mBitmap;
	private View mRootView;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private SwipeMenuListView mMessageList;
	private MessageListAdapter mMessageListAdapter;
	public static ArrayList<String> mUsernameList;

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.mContainerView = view.findViewById(R.id.message_container);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.message_fragment, container,
				false);

		initView();
		initData();
		initSwipeMenu();

		return mRootView;
	}

	@SuppressLint("ResourceAsColor")
	private void initView() {

		// 刷新初始化
		mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView
				.findViewById(R.id.swipe_container);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		mMessageList = (SwipeMenuListView) mRootView
				.findViewById(R.id.message_listView);

	}

	private void initData() {
		mUsernameList = new ArrayList<String>();
		mUsernameList.add("test1");
		mUsernameList.add("test2");
		mUsernameList.add("test3");
		mUsernameList.add("test4");
		mUsernameList.add("test5");
		mMessageListAdapter = new MessageListAdapter(getActivity(),
				mUsernameList);
		// 动态列表
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
				mMessageListAdapter);
		swingBottomInAnimationAdapter.setAbsListView(mMessageList);
		mMessageList.setAdapter(swingBottomInAnimationAdapter);

	}

	private void initSwipeMenu() {
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("Open");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};

		// set creator
		mMessageList.setMenuCreator(creator);

		// step 2. listener item click event
		mMessageList.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
				case 0:
					// open
					open(position);
					break;
				case 1:
					// delete
					// delete(item);
					mUsernameList.remove(position);
					mMessageListAdapter.notifyDataSetChanged();
					break;
				}
			}
		});

		// set SwipeListener
		mMessageList.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// other setting
		// listView.setCloseInterpolator(new BounceInterpolator());

		// test item long click
		mMessageList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getActivity(), position + " long click", 0)
						.show();
				return false;
			}
		});
	}

	// 进入聊天界面
	private void open(int position) {
		Toast.makeText(getActivity(), "This is " + position, Toast.LENGTH_SHORT)
				.show();
	}

	public static void getFriendRequest(String username) {

		mUsernameList.add(username);

	}

	@Override
	public void onRefresh() {

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mSwipeRefreshLayout.setRefreshing(false);
				mMessageListAdapter.notifyDataSetChanged();
			}
		}, 3500);
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
				MessageFragment.this.mBitmap = bitmap;
			}
		};

		thread.start();
	}

	@Override
	public Bitmap getBitmap() {
		return mBitmap;
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

}
