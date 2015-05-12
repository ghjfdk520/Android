//package com.auction.test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.auction.client.R;
//import com.auction.client.fragment.MenuFragment;
//import com.auction.client.fragment.selloff;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
//import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//
//
//
//public class MainTest extends SlidingFragmentActivity{
//	private MenuFragment mf;
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main);	
//		getSupportFragmentManager().beginTransaction()
//		.replace(R.id.main, new selloff()).commit();
//		initFragment();
//		
//	}
//
//	private void initFragment(){
//		SlidingMenu mSlidingMenu = getSlidingMenu();
//		setBehindContentView(R.layout.frame_menu);
//		 mf  = new MenuFragment();
//
//		getSupportFragmentManager().beginTransaction()
//		.replace(R.id.frame_menu, mf).commit();
//		mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置slidingmeni从哪侧出现
//		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 只有在边上才可以打开
//		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 偏移量
//		mSlidingMenu.setFadeEnabled(true);
//		mSlidingMenu.setFadeDegree(0.5f);
//		mSlidingMenu.setMenu(R.layout.frame_menu);
//		Bundle mBundle = null;
//		// 导航打开监听事件
//		mSlidingMenu.setOnOpenListener(new OnOpenListener() {
//			@Override
//			public void onOpen() {
//			}
//		});
//		// 导航关闭监听事件
//		mSlidingMenu.setOnClosedListener(new OnClosedListener() {
//
//			@Override
//			public void onClosed() {
//			}
//		});
//	}
//}
