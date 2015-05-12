package com.auction.client.fragment;

import java.util.ArrayList;
import java.util.List;

import com.auction.client.Main;
import com.auction.client.R;
import com.auction.client.entity.Item;
import com.auction.client.util.CommonFunction;
import com.auction.common.Constants.State;
import com.auction.config.Common;
import com.auction.connector.HttpCallBack;
import com.auction.database.SharedPreferenceUtil;
import com.auction.view.ImageViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ItemSpace extends Fragment implements HttpCallBack,
		OnClickListener {
	private LinearLayout item_space;
	private LayoutInflater inflater;
	private static Item item;
	private static boolean ischange; // 判断是否重新绑定item
	private static FragmentManager fragmentManager;
	private ImageViewPager imageVP;
	private ItemImgAdapter imageAdapter;
	private TextView remaining_time, max_price;
	private TextView item_name, item_kind, item_desc;
	private List<String> images;
	private TextView title_name;
	private ImageView title_back;
	private CheckBox alarm_checkbox;

	private LinearLayout btLayout;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			System.out.println("handlerfok");
			imageAdapter.notifyDataSetChanged();

		}
	};

	private void runOnUIThread(Runnable runnable) {

		if (runnable != null && handler != null) {
			handler.post(runnable);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		images = new ArrayList<String>();
		fragmentManager = getActivity().getSupportFragmentManager();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		this.inflater = inflater;
		item_space = (LinearLayout) inflater.inflate(R.layout.item_space, null);
		initModule();
		initListener();
		return item_space;

	}

	public void initModule() {
		imageVP = (ImageViewPager) item_space.findViewById(R.id.imgvPager);
		imageAdapter = new ItemImgAdapter();
		imageVP.setAdapter(imageAdapter);

		title_name = (TextView) item_space.findViewById(R.id.title_name);
		alarm_checkbox = (CheckBox) item_space.findViewById(R.id.alarm_checkbox);
		 item_space.findViewById(R.id.title_back).setOnClickListener(this);
		title_name.setText("商品详情");
		 
		max_price = (TextView) item_space.findViewById(R.id.max_price);
		item_name = (TextView) item_space.findViewById(R.id.item_name);
		item_kind = (TextView) item_space.findViewById(R.id.item_kind);
		item_desc = (TextView) item_space.findViewById(R.id.item_desc);
		btLayout = (LinearLayout) item_space.findViewById(R.id.btLayout);
	}

	public void initListener() {
		title_back.setOnClickListener(this);
		alarm_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						int i = 0;
						System.out.println("share ok");
						SharedPreferenceUtil.getInstance(getActivity())
								.putString(SharedPreferenceUtil.ALARM_ITEM,
										"xxxx" + (i++));
						System.out.println(SharedPreferenceUtil.getInstance(
								getActivity()).getString(
								SharedPreferenceUtil.ALARM_ITEM));
					}
 
				});
	}

	public static void bindItem(Item bItem) {
		if (item == null) {
			item = bItem;
			ischange = false;
		} else {
			if (item.getItem_id() == bItem.getItem_id()) {
				ischange = false;
				return;
			} else {
				item = bItem;
				ischange = true;
			}

		}

		Main.changeTouchMode(true);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		if (hidden) {
			System.out.println("hidsdfen");
		} else {
			if (ischange) {
				System.out.println("hahagaibianle");
				handler.sendEmptyMessage(10);
				onRefreshItem();
				// if (item.getOther_img() == 1) {
				// // 加载other url
				// Map<String, String> map = new HashMap<String, String>();
				// map.put("item_id", item.getItem_id() + "");
				// String url = Common.getInstance().OTHERURL;
				// CommonFunction.ToJsonStr(map);
				// ConnectorManage.getInstance(getActivity()).asynPost(url,
				// map, this);
				// } else {
				// return;
				// }
			}
		}
	}

	public void onRefreshItem() {
		if (ischange) {

			remaining_time.setText(item.getEndtime() + "");
			max_price.setText(item.getMax_price() + "");
			item_name.setText(item.getItem_name());
			item_kind.setText(item.getKind_id() + "");
			item_desc.setText(item.getItem_desc());
			images.clear();
			images.add(Common.getInstance().BASE_UPLOAD + item.getItem_img());

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					item_space.notify();
				}
			}).start();
			imageAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onGeneralSuccess(String result, long flag) {
		// TODO Auto-generated method stub
		System.out.println(result);
	}

	@Override
	public void onGeneralError(String e, long flag) {
		// TODO Auto-generated method stub

	}

	public static boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			changeFragment();
		}
		return true;
	}

	// imgAdapter
	class ItemImgAdapter extends PagerAdapter {

		private ImageLoader imageLoader;
		private DisplayImageOptions options;

		public void initImageLoader() {
			imageLoader = ImageLoader.getInstance();
			options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.auction_logo)
					.showImageOnFail(R.drawable.auction_logo)
					.resetViewBeforeLoading(true).cacheOnDisc(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new FadeInBitmapDisplayer(300)).build();

		}

		public ItemImgAdapter() {
			initImageLoader();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {

			View imageLayout = inflater.inflate(R.layout.item_pager_img, null);
			ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);

			imageLoader.displayImage(images.get(position), imageView, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							spinner.setVisibility(View.VISIBLE);
						}
						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) { // 获取图片失败类型
							case IO_ERROR: // 文件I/O错误
								message = "Input/Output error";
								break;
							case DECODING_ERROR: // 解码错误
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED: // 网络延迟
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY: // 内存不足
								message = "Out Of Memory error";
								break;
							case UNKNOWN: // 原因不明
								message = "Unknown error";
								break;
							}
							// Toast.makeText(ImagePagerActivity.this, message,
							// Toast.LENGTH_SHORT).show();

							spinner.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view,Bitmap loadedImage) {
							spinner.setVisibility(View.GONE); // 不显示圆形进度条
						}
					});

			((ViewPager) view).addView(imageLayout, 0); // 将图片增加到ViewPager
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			changeFragment();
			break;
	 
		default:
			break;
		}
	}

	public static void changeFragment() {
		System.out.println("okkk");
		CommonFunction.TabFragment(fragmentManager, State.SELLOFF);
		Main.changeTouchMode(false);
	}

}
