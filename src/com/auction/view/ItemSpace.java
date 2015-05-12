package com.auction.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.auction.adapter.ItemImgAdapter;
import com.auction.client.R;
import com.auction.client.entity.Item;
import com.auction.client.util.CommonFunction;
import com.auction.client.util.DialogUtil;
import com.auction.client.util.JsonUtil;
import com.auction.config.Common;
import com.auction.connector.HttpCallBack;
import com.auction.connector.protocol.ItemHttpProtocol;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.auction.common.Constants;
import com.auction.database.SharedPreferenceUtil;

public class ItemSpace extends Activity implements HttpCallBack,
		OnClickListener {
	private long GETIMAGESFLAG;
	private long GETITEMFLAG;
	private long BIDITEMFLAG;
	private long BIDRECORDFLAG;
	private ProgressDialog mProgressDialog;
	private static Item item;
	private Dialog dialog;
	private boolean isExists; // 判断是否已经添加到闹钟
	private boolean isCheckbox; // 不存在的话是否勾选了闹钟
	private ImageViewPager imageVP;
	private ItemImgAdapter imageAdapter;
	private static TextView remaining_time, max_price;
	private TextView item_name, item_kind, item_desc;
	private List<String> images;
	private List<Map<String, Object>> recordMap;
	int i = 0;
	private ImageView title_back;
	private CheckBox alarm_checkbox;
	private TextView title_name;
	private LinearLayout btLayout;
	private LinearLayout time_ly;
	private TextView bidNum;
	private int user_id;
	private boolean isBid = false;
	private String kindStr[] = { "电脑用品", "服装", "生活", "兴趣", "手机" };
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == Constants.UPDATE_PRICE) {
				max_price.setText(msg.arg1);
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_space);
		initModule();
		initData();
		initListener();
	}

	public static void bindItem(Item bItem) {

		item = bItem;
	}

	public static void launchActivity(Activity fromActivity, Item bitem) {
		Intent i = new Intent(fromActivity, ItemSpace.class);
		item = bitem;
		fromActivity.startActivity(i);
	}

	public static void launchActivity(Activity fromActivity, String itemStr) {
		Intent i = new Intent(fromActivity, ItemSpace.class);
		item = CommonFunction.toItem(itemStr);
		fromActivity.startActivity(i);
	}

	public void initListener() {
		findViewById(R.id.title_back).setOnClickListener(this);
		findViewById(R.id.bid_icon).setOnClickListener(this);
		alarm_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						isCheckbox = isChecked;
						if (isCheckbox) {
							Toast.makeText(ItemSpace.this, "关注拍卖品", 2000)
									.show();
						} else {
							Toast.makeText(ItemSpace.this, "取消关注", 2000).show();
						}

					}

				});
	}

	public void initModule() {
		images = new ArrayList<String>();
		user_id = Common.getInstance().user.getUser_id();
		imageVP = (ImageViewPager) findViewById(R.id.imgvPager);
		max_price = (TextView) findViewById(R.id.max_price);
		item_name = (TextView) findViewById(R.id.item_name);
		item_kind = (TextView) findViewById(R.id.item_kind);
		item_desc = (TextView) findViewById(R.id.item_desc);
		btLayout = (LinearLayout) findViewById(R.id.btLayout);
		time_ly = (LinearLayout) findViewById(R.id.time_ly);
		title_name = (TextView) findViewById(R.id.title_name);
		alarm_checkbox = (CheckBox) findViewById(R.id.alarm_checkbox);
		bidNum = (TextView) findViewById(R.id.bidNum);
		if (user_id == item.getOwner_id()) {
			alarm_checkbox.setVisibility(View.GONE);
			btLayout.setVisibility(View.GONE);
		}
		if (item.getEndtime() < System.currentTimeMillis() / 1000) {
			btLayout.setVisibility(View.GONE);
		}
	}

	public void initData() {

		CommonFunction.Timer(item.getEndtime(), time_ly);
		max_price.setText(item.getMax_price() + "");
		item_name.setText(item.getItem_name());
		item_kind.setText(kindStr[item.getKind_id()]);
		item_desc.setText(item.getItem_desc());
		String imgUrls[] = item.getItem_img().split(",");

		for (String url : imgUrls) {
			images.add(Common.getInstance().BASE_UPLOAD + url);
		}

		imageAdapter = new ItemImgAdapter(images, getLayoutInflater());
		imageVP.setAdapter(imageAdapter);
		btLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		isExists = CommonFunction.checkItemInAlarm(this, item);
		alarm_checkbox.setChecked(isExists);
		BIDRECORDFLAG = ItemHttpProtocol.bidRecord(this, this,
				item.getItem_id());
	}

	public void reqItem() {
		int item_id = getIntent().getIntExtra("item_id", 0);
		GETITEMFLAG = ItemHttpProtocol.getItem(this, this, item_id);
	}

	public void showDialog() {
		dialog = DialogUtil.showBIDialog(this, item.getMax_price());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onGeneralSuccess(String result, long flag) {
		// TODO Auto-generated method stub
		if (GETITEMFLAG == flag) {

		} else if (BIDRECORDFLAG == flag) {
			// bidNum.setText("已经有"+images.size()+"参与");
			recordMap = JsonUtil.jsonToList(result);
			bidNum.setText(recordMap.size() + "次出手记录");
		} else if (BIDITEMFLAG == flag) {
			isBid = false;
			closeLoading();
			CommonFunction.Toast(getApplicationContext(), "竞拍成功");
			BIDRECORDFLAG = ItemHttpProtocol.bidRecord(this, this,
					item.getItem_id());
		}
	}

	@Override
	public void onGeneralError(String e, long flag) {
		// TODO Auto-generated method stub
		closeLoading();
		isBid = false;
		CommonFunction.Toast(getApplicationContext(), "出现错误，请重新操作");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isCheckbox = alarm_checkbox.isChecked();
		if (!isExists && isCheckbox) {
			CommonFunction.saveItemAlarm(this, item);
			SharedPreferenceUtil.getInstance(this).putBoolean(
					SharedPreferenceUtil.REINSTALL_ALARM_ITEM, true);
		} else if (isExists && !isCheckbox) {
			CommonFunction.cancleAlarm(this, item);
			isCheckbox = false;
			SharedPreferenceUtil.getInstance(this).putBoolean(
					SharedPreferenceUtil.REINSTALL_ALARM_ITEM, true);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		finish();
		return false;
	}

	public void bidItem(View v) {
		showLoading("请稍后...");
		if (!isBid) {
			isBid = true;
			EditText et = (EditText) v.getTag();
			int temp = Integer.parseInt(et.getText().toString());
			item.setMax_price(temp);
			dialog.dismiss();
			max_price.setText(et.getText().toString());

			BIDITEMFLAG = ItemHttpProtocol.bidItem(this, this,
					item.getItem_id(), Common.getInstance().user.getUser_id(),
					temp);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.bid_icon:
			Intent intent = new Intent();
			intent.setClass(this, BidRecord.class);
			if (recordMap != null)
				BidRecord.appendList(recordMap);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void showLoading(String message) {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(this, "", message, false,
					false);
		} else {
			mProgressDialog.setMessage(message);
			mProgressDialog.show();
		}
	}

	public void closeLoading() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
}
