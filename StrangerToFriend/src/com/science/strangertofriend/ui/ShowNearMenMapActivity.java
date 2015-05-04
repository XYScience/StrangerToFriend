package com.science.strangertofriend.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.science.strangertofriend.R;
import com.science.strangertofriend.bean.LocationMenList;
import com.science.strangertofriend.utils.AVService;

/**
 * @description ��ͼ��ʾ��������
 * 
 * @author ����Science ������
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-5-1
 * 
 */

public class ShowNearMenMapActivity extends BaseActivity {

	private MapView mMapView = null;
	private BaiduMap mBaiduMap;

	private Context context;
	private String mUserObjectId, mUsername, mGender;
	private String mOtherUsername, mOtherLatitude, mOtherLongtitude;

	// ��λ���
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn = true;
	private double mLatitude;
	private double mLongtitude;
	private ImageView mMapLocation;

	// ���������
	private BitmapDescriptor mMarkDescriptor;
	// �б�����
	private ArrayList<LocationMenList> mLocationMenList;

	// �Զ��嶨λͼ��
	// private BitmapDescriptor mIconLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getApplicationContext());

		setContentView(R.layout.near_men_map);

		this.context = this;

		initComponent();

		// ��ʼ����λ
		initLocation();

		initMarker();

		setMarkerClickListener();

	}

	private void initComponent() {
		// ��ȡ��ͼ�ؼ�����
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// ��ͼ����200m
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(16.0f);
		mBaiduMap.setMapStatus(mapStatusUpdate);
		// ��ȡ��λ�ҵ�λ�õ�ͼ��
		mMapLocation = (ImageView) findViewById(R.id.map_location);

		mLocationMenList = new ArrayList<LocationMenList>();

		AVUser currentUser = AVUser.getCurrentUser();
		if (currentUser != null) {
			mUserObjectId = currentUser.getObjectId();
			mUsername = currentUser.getUsername();
			mGender = currentUser.getString("gender");
		} else {
			Toast.makeText(context, "����û�е�½�", Toast.LENGTH_LONG).show();
		}
	}

	private void initLocation() {

		mLocationClient = new LocationClient(getApplicationContext());
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);

		LocationClientOption option = new LocationClientOption();
		// option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ
		// option.setNeedDeviceDirect(true);//���صĶ�λ��������ֻ���ͷ�ķ���
		option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
		option.setIsNeedAddress(true);// ���صĶ�λ���������ַ��Ϣ
		option.setOpenGps(true);
		option.setScanSpan(3600000);// ���÷���λ����ļ��ʱ��Ϊ1000ms
		mLocationClient.setLocOption(option);

		// ��λ���ҵ�λ��
		mMapLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				centerToMyLocation();
			}
		});

		// mIconLocation = BitmapDescriptorFactory
		// .fromResource(R.drawable.navi_map_gps_locked);
	}

	private void initMarker() {

		mMarkDescriptor = BitmapDescriptorFactory
				.fromResource(R.drawable.maker);
	}

	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			MyLocationData data = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(data);

			// �����Զ���ͼ��
			// MyLocationConfiguration configuration = new
			// MyLocationConfiguration(
			// LocationMode.NORMAL, true, mIconLocation);
			// mBaiduMap.setMyLocationConfigeration(configuration);

			// ���¾�γ��
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();

			if (isFirstIn) {
				AVService.myLocation(mUserObjectId, mUsername, mGender,
						location.getLatitude(), location.getLongitude());

				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstIn = false;

				Toast.makeText(context, location.getAddrStr(),
						Toast.LENGTH_LONG).show();

				findMenCallback();

			}
		}
	}

	private void findMenCallback() {

		AVQuery<AVObject> query = new AVQuery<AVObject>("MyLocation");
		query.whereEqualTo("userObjectId", mUserObjectId);
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> avObjects, AVException e) {
				if (e == null) {
					for (AVObject avo : avObjects) {
						mLocationMenList.add(new LocationMenList(avo
								.getString("userObjectId"), avo
								.getString("username"), avo
								.getDouble("latitude"), avo
								.getDouble("longtitude")));
					}
					mMenListHandler.obtainMessage(1).sendToTarget();
				} else {
					Log.e("1111111", "111111111111111111:" + e.getMessage());
				}
			}
		});

	}

	@SuppressLint("HandlerLeak")
	private Handler mMenListHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				addOverLays(mLocationMenList);
				break;
			}
		}
	};

	// ��Ӹ�����
	private void addOverLays(List<LocationMenList> avObjects) {

		mBaiduMap.clear();
		LatLng latLng = null;
		Marker marker = null;
		OverlayOptions options;

		for (LocationMenList menList : avObjects) {

			// ��γ��
			latLng = new LatLng(menList.getLatitude(), menList.getLongtitude());
			// ͼ��
			options = new MarkerOptions().position(latLng)
					.icon(mMarkDescriptor).zIndex(5);
			marker = (Marker) mBaiduMap.addOverlay(options);

			Bundle bundle = new Bundle();
			bundle.putSerializable("menList", (Serializable) menList);
			marker.setExtraInfo(bundle);

			TextView textView = new TextView(context);
			textView.setBackgroundResource(R.drawable.location_tips);
			textView.setPadding(23, 20, 20, 40);
			textView.setTextColor(Color.WHITE);
			textView.setText(menList.getUsername());

			InfoWindow infoWindow;
			infoWindow = new InfoWindow(
					BitmapDescriptorFactory.fromView(textView), latLng, -45,
					new OnInfoWindowClickListener() {

						@Override
						public void onInfoWindowClick() {
							Toast.makeText(context, "��������", Toast.LENGTH_LONG)
									.show();
						}
					});
			mBaiduMap.showInfoWindow(infoWindow);
		}

	}

	private void setMarkerClickListener() {

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {

				Bundle extraInfo = marker.getExtraInfo();
				final LocationMenList menList = (LocationMenList) extraInfo
						.getSerializable("menList");

				InfoWindow infoWindow;
				final LatLng latLng = marker.getPosition();
				TextView textView = new TextView(context);
				textView.setBackgroundResource(R.drawable.location_tips);
				textView.setPadding(23, 20, 20, 40);
				textView.setTextColor(Color.WHITE);
				textView.setText(menList.getUsername());

				infoWindow = new InfoWindow(BitmapDescriptorFactory
						.fromView(textView), latLng, -45,
						new OnInfoWindowClickListener() {

							@Override
							public void onInfoWindowClick() {
								Toast.makeText(context,
										"��������" + menList.getUserObjectId(),
										Toast.LENGTH_LONG).show();
							}
						});
				mBaiduMap.showInfoWindow(infoWindow);

				return true;
			}
		});
	}

	/**
	 * ��λ���ҵ�λ��
	 */
	private void centerToMyLocation() {
		LatLng latLng = new LatLng(mLatitude, mLongtitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.map_common:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;
		case R.id.map_site:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.map_traffic:
			if (mBaiduMap.isTrafficEnabled()) {
				mBaiduMap.setTrafficEnabled(false);
				item.setTitle("ʵʱ��ͼ(off)");
			} else {
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle("ʵʱ��ͼ(on)");
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// ��ʼ��λ
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// ֹͣ��λ
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			ShowNearMenMapActivity.this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
