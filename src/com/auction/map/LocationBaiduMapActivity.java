package com.auction.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auction.client.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
 

public class LocationBaiduMapActivity extends Activity implements
		OnGetGeoCoderResultListener, OnClickListener {

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	private BaiduMap mBaiduMap = null;
	private MapView mMapView = null;
	private GeoCoder mSearch = null;
	private BitmapDescriptor mBD;

	private LatLng mCurrenPoint;
	private String address;
	private ImageView poslocation_bt;
	private TextView address_text;
	private long currentTime;
	boolean isFirstLoc = true;
    private LatLng clickPoint;
	private Handler mHandler = new Handler(Looper.getMainLooper());

	public static void actionActivity(Activity fromActivity) {
		Intent i = new Intent(fromActivity, LocationBaiduMapActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		fromActivity.startActivityForResult(i, 1);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	 	setContentView();
		 init();
		 initListeners();
	}

	public void setContentView() {
		// TODO Auto-generated method stub
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.map_home);
	}

	public void init() {
		// TODO Auto-generated method stub

		mMapView = (MapView) findViewById(R.id.bmapView);
		mSearch = GeoCoder.newInstance();
		poslocation_bt = (ImageView) findViewById(R.id.poslocation_bt);
		mMapView.showZoomControls(false);
		mMapView.showScaleControl(false);
		if (mMapView != null) {
			mBaiduMap = mMapView.getMap();
			if (mBaiduMap != null) {
				// 初始化地图
				MapStatus mMapStatus = new MapStatus.Builder().zoom(17).build();
				// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
				MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
						.newMapStatus(mMapStatus);
				// 改变地图状态
				mBaiduMap.setMapStatus(mMapStatusUpdate);
			}
		}
	 
		address_text = (TextView) findViewById(R.id.address_text);
		
		
		mBaiduMap
		.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, null));
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	public void initListeners() {
		// TODO Auto-generated method stub
		mSearch.setOnGetGeoCodeResultListener(this);
		poslocation_bt.setOnClickListener(this);
		findViewById(R.id.select).setOnClickListener(this);;
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				clearOverlay();
			}
		});
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onMapClick(LatLng latlng) {
				// TODO Auto-generated method stub
				clearOverlay();
				drawOerlay(latlng, R.drawable.iconfont_coordinate);
				clickPoint = latlng;
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(latlng));
			}
		});
		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng latlng) {
				// TODO Auto-generated method stub
				clearOverlay();
				drawOerlay(latlng, R.drawable.iconfont_coordinate);
				clickPoint = latlng;
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(latlng));
			}
		});
	}

 

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，   未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}

		address = result.getAddress();
		changeSelectAddress();
	}

	private void drawOerlay(LatLng latlng, int id) {
		mBD = BitmapDescriptorFactory.fromResource(id);
		OverlayOptions oOption = new MarkerOptions().position(latlng).icon(mBD)
				.zIndex(9).draggable(false);
		mBaiduMap.addOverlay(oOption);
	}

	 

	public void setPosLocation(LatLng latlng) {
		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latlng));
	}

	// 清除overlay
	public void clearOverlay() {
		if (mBaiduMap != null) {
			mBaiduMap.clear();
		}
	}

	public void changeSelectAddress() {
		runOnUIThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				address_text.setText(address);

			}
		});
	}

	private void runOnUIThread(Runnable runnable) {
		if (runnable != null && mHandler != null) {
			mHandler.post(runnable);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.poslocation_bt:
			isFirstLoc = true;
			break;
		case R.id.select:
			finishActivity();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			System.out.println("hahhahah");
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
	
	protected void finishActivity(){
		Intent intent = new Intent();
		intent.putExtra("address", address);
		intent.putExtra("lat", clickPoint.latitude);
		intent.putExtra("lng", clickPoint.longitude);
		setResult(0x00f, intent);
		finish();
	}
}
