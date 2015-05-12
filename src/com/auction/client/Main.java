package com.auction.client;

//
//

import com.auction.client.fragment.MenuFragment;
import com.auction.client.util.CommonFunction;
import com.auction.common.Constants.State;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class Main extends SlidingFragmentActivity {
	static SlidingMenu mSlidingMenu;
	private MenuFragment mf;
	private View rootView;
	private State state = State.SELLOFF;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		CommonFunction.TabFragment(getSupportFragmentManager(), state);
		initFragment();
		rootView = getLayoutInflater().inflate(R.layout.main, null);

	}

	private void initFragment() {
		mSlidingMenu = getSlidingMenu();
		setBehindContentView(R.layout.frame_menu);
		mf = new MenuFragment();

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_menu, mf).commit();
		mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置slidingmeni从哪侧出现
		changeTouchMode(false);
		int menuSize = (int) (getResources().getDisplayMetrics().widthPixels * 0.2f);
		mSlidingMenu.setBehindOffset(menuSize);

		mSlidingMenu.setFadeEnabled(true);
		// mSlidingMenu.attachToActivity(this, SlidingMendu.SLIDING_CONTENT);
		mSlidingMenu.setFadeDegree(0.5f);
		mSlidingMenu.setMenu(R.layout.frame_menu);
		mSlidingMenu.setBackgroundColor(Color.BLACK);
		// 导航打开监听事件
		mSlidingMenu.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
			}
		});
		// 导航关闭监听事件
		mSlidingMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
			}
		});
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// mSlidingMenu.setBehindCanvasTransformer(new
		// SlidingMenu.CanvasTransformer() {
		// @Override
		// public void transformCanvas(Canvas canvas, float percentOpen) {
		// float scale = (float) (percentOpen * 0.25 + 0.75);
		// canvas.scale(scale, scale, -canvas.getWidth() / 2,
		// canvas.getHeight() / 2);
		// }
		// });
		//
		// mSlidingMenu.setAboveCanvasTransformer(new
		// SlidingMenu.CanvasTransformer() {
		// @Override
		// public void transformCanvas(Canvas canvas, float percentOpen) {
		// float scale = (float) (1 - percentOpen * 0.25);
		// canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
		// }
		// });

	}

	// 影藏slidingmenu 或者显示
	public static void changeTouchMode(boolean state) {
		if (state) {
			mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		} else {
			mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次返回登陆页",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0x1111) {
			CommonFunction.mb.onActivityResult(requestCode, resultCode, data);
		}
	}

}
