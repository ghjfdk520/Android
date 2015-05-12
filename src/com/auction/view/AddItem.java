package com.auction.view;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.auction.client.R;
import com.auction.client.fragment.ManagerBid;
import com.auction.client.util.CommonFunction;
import com.auction.client.util.PhotoUtil;
import com.auction.client.util.UploadImg;

import com.auction.common.Constants;
import com.auction.config.Common;
import com.auction.connector.FormFile;
import com.auction.connector.SocketHttpRequester;
import com.auction.thread.OtherThread;

import android.R.integer;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class AddItem extends Activity implements OnClickListener{
	
	private List<String> imgUrls;
	private LinearLayout viewPagerContainer;
	private ViewPager viewPager;
	private List<ImageView> imageViews;
	private MaPagerAdapter pagerAdapter;
	private Spinner Tspinner, Cspinner;
	private List<Bitmap> imgList;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item);
		initModule();

	}

	private void initModule() {
		CommonFunction.BitmapStack.clear();
		imgList = new ArrayList<Bitmap>();
		Common.getInstance().additem.setOwner_id(Common.getInstance().user
				.getUser_id());
		Tspinner = (Spinner) findViewById(R.id.time_spinner);
		Cspinner = (Spinner) findViewById(R.id.class_spinner);

		imageViews = new ArrayList<ImageView>();
		ImageView iv = new ImageView(this);
		iv.setImageResource(R.drawable.additem_bt);
		iv.setBackgroundResource(R.drawable.additem_bt_bg);

		imageViews.add(iv);

		viewPagerContainer = (LinearLayout) findViewById(R.id.container);
		viewPager = (ViewPager) findViewById(R.id.item_img);
		viewPager.setOffscreenPageLimit(4);
		viewPager.setPageMargin(10);
		pagerAdapter = new MaPagerAdapter();
		viewPager.setAdapter(pagerAdapter);
		MyOnPageChangeListener myOnPageChangeListener = new MyOnPageChangeListener();
		viewPager.setOnPageChangeListener(myOnPageChangeListener);

		viewPagerContainer.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// dispatch the events to the ViewPager, to solve the problem
				// that we can swipe only the middle view.
				return viewPager.dispatchTouchEvent(event);
			}
		});
		Tspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Common.getInstance().additem.setEndtime(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		Cspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Common.getInstance().additem.setKind_id(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});
	}

	class SpinnerSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	public void onbtclick(View v) {
		upload();
		Intent intent = new Intent(this, AddItem_desc.class);
        
		startActivityForResult(intent, Constants.REQUEST_CODE_RETURN);

	}

	public void upload() {
		Map<String, String> map = new HashMap<String, String>();
		JSONObject json = new JSONObject();

		try {

			if (CommonFunction.BitmapStack.size() > 1) {
				json.put("otherImg", 1);
			} else {
				json.put("otherImg", 0);
			}
			json.put("owner_id", Common.getInstance().user.getUser_id());
			
		} catch (Exception e) {
			// TODO: handle exception
		}
        
		map.put("inputStr", json.toString());
		String url = Common.getInstance().uploadImg;
		Upload up = new Upload(url, CommonFunction.BitmapStack.getLast(), map);
		 
		up.start();
	}
	
	public void uploadOther() {
		int item_id = Integer.parseInt(Common.getInstance().tempStr);
		Common.getInstance().additem.setItem_id(item_id);
		if (item_id != 0 && CommonFunction.BitmapStack.size() >= 1) {
			Map<String, String> map = new HashMap<String, String>();
			JSONObject json = new JSONObject();
			try {
				json.put("item_id", item_id);
				json.put("owner_id", Common.getInstance().user.getUser_id());
			} catch (Exception e) {
				// TODO: handle exception
			}

			map.put("inputStr", json.toString());
			String url = Common.getInstance().uploadOther;
			OtherThread ot = new OtherThread(url, CommonFunction.BitmapStack, map);
			ot.start();
 			
		}
	}
	
	private void runOnUIThread( Runnable runnable )
	{

		if ( runnable != null && mMainHandler != null )
		{
			mMainHandler.post( runnable );
		}
	}

	class MaPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			// TODO Auto-generated method stub
			return (view == obj);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			ImageView imageView = imageViews.get(position);
			imageView.setTag(position);
			container.addView(imageView);

			if (position == 0) {
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						PhotoUtil.chooseImg(AddItem.this);

					}
				});
			}
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView((ImageView) object);
		}

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// to refresh frameLayout
			if (viewPagerContainer != null) {
				viewPagerContainer.invalidate();
			}

			if (position == 0) {
				CommonFunction.log("viewpager", "0");
			} else {
				CommonFunction.log("viewpager", "qitat");
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (requestCode == Constants.REQUEST_CODE_RETURN) {
			ManagerBid.changeList();
			finish();
		}
		PhotoUtil.dialog.dismiss();
		ImageView vp = new ImageView(this);
		Bitmap temp = null;
		if (data != null && requestCode == Constants.REQUEST_CODE_PHOTOGRAPH) { // 拍照上传时
			Bundle extras = data.getExtras();

			if (extras != null) {
				temp = (Bitmap) extras.get("data");
			} else {
				Toast.makeText(this, "未找到图片", Toast.LENGTH_LONG).show();
			}
		}
		if (requestCode == Constants.REQUEST_CODE_LOCPIC && data != null) {
			/**
			 * 当选择的图片不为空的话，在获取到图片的途径
			 */
			Uri uri = data.getData();
			try {
				String[] pojo = { MediaStore.Images.Media.DATA };

				Cursor cursor = this.getContentResolver().query(uri, pojo,
						null, null, null);
				if (cursor != null) {
					ContentResolver cr = this.getContentResolver();
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);
					Log.d("path", path);
					/***
					 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
					 * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
					 */
					if (path.endsWith("jpg") || path.endsWith("png")) {
						// picPath = path;
						temp = BitmapFactory.decodeStream(cr
								.openInputStream(uri));

					} else {

					}
				} else {

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		CommonFunction.BitmapStack.add(temp);
		LayoutParams params = new LayoutParams(70, 150);
		vp.setLayoutParams(params);
		vp.setBackgroundDrawable(new BitmapDrawable(temp));
		imageViews.add(vp);
		pagerAdapter.notifyDataSetChanged();
		viewPager.setCurrentItem(imageViews.size());
	}

	public class Upload extends Thread {
		private String url;
		private Bitmap bitmap;
		private Map<String, String> map;

		public Upload(String url, Bitmap bit, Map<String, String> map) {
			// TODO Auto-generated constructor stub
			this.url = url;
			this.bitmap = bit;
			this.map = map;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray();

			try {
				System.out.println("sdfsdfsdf");
				FormFile formfile = new FormFile("file", stream.toByteArray(),
						"file", "application/octet-stream");
				String tempStr = SocketHttpRequester.post(url, map, formfile);
				CommonFunction.BitmapStack.removeLast();
				uploadOther();
			} catch (Exception e) {
				// TODO: handle exception
				CommonFunction.log(e);
			}
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
