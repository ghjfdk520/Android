package com.auction.client.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.auction.adapter.SelloffAdapter;
import com.auction.client.R;
import com.auction.client.entity.Item;
import com.auction.client.util.CommonFunction;
import com.auction.config.Common;
import com.auction.connector.ConnectorManage;
import com.auction.connector.HttpCallBack;
import com.auction.connector.protocol.ItemHttpProtocol;
import com.auction.view.ItemSpace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class Selloff extends Fragment implements HttpCallBack, OnClickListener,
		SwipeRefreshLayout.OnRefreshListener {
	private long SELECTITEMFLAG;
	private long GETITEMFLAG;

	private PopupWindow pwMyPopWindow;
	private TextView title_name;
	private AutoCompleteTextView keyword;
	private ImageView auction_more;
	private ImageView auction_search;

	private View selloff_normal_bar;
	private View search_bar;
	// 搜索类型
	private int kind_id = 5;
	private boolean isSearch = false; // 是否关键字搜索

	private LayoutInflater inflater;
	private static final int REFRESH_COMPLETE = 0X110;
	private SwipeRefreshLayout swipeLayout;
	private ListView listView;
	private SelloffAdapter selloffAdapter;
	private Map<String, String> map = new HashMap<String, String>();
	private String url;
	private List<Item> itemDate;
	private int lastTime; // 上次刷新时间
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				selloffAdapter.notifyData(itemDate);
				break;

			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		reqItemData();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		url = Common.getInstance().PULLITEM;
		itemDate = new ArrayList<Item>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		this.inflater = inflater;
		View v = inflater.inflate(R.layout.selloff, null);

		init(v);
		initListener();
		// list = new ArrayList<SoftwareClassificationInfo>();
		// list.add(new SoftwareClassificationInfo(1, "asdas"));

		return v;
	}

	// 请求item数据
	public void reqItemData() {
		map.clear();
		map.put("kind_id", kind_id + "");
		CommonFunction.ToJsonStr(map);
		ConnectorManage.getInstance(getActivity()).asynPost(url, map, this);
	}

	// 初始swipe list
	public void init(View v) {
		swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.startLayoutAnimation();
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		listView = (ListView) v.findViewById(R.id.list);
		selloffAdapter = new SelloffAdapter(getActivity(), itemDate);
		listView.setAdapter(selloffAdapter);

		auction_more = (ImageView) v.findViewById(R.id.auction_more);
		auction_more.setOnClickListener(this);
		keyword = (AutoCompleteTextView) v.findViewById(R.id.keyword);
		title_name = (TextView) v.findViewById(R.id.title_name);

		search_bar = v.findViewById(R.id.search_bar);
		selloff_normal_bar = v.findViewById(R.id.selloff_normal_bar);
		v.findViewById(R.id.search_back).setOnClickListener(this);
		v.findViewById(R.id.auction_search).setOnClickListener(this);
		v.findViewById(R.id.title_icon).setOnClickListener(this);
	}

	public void initListener() {
		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				final int totalCount = firstVisibleItem + visibleItemCount;
				int currentPage = totalCount / 20;
				int nextPage = currentPage + 1;

				if (totalCount == totalItemCount && nextPage <= 6) {

				}
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击列表项转入ViewPager显示界面
				ItemSpace.launchActivity(getActivity(), itemDate.get(position));

				// CommonFunction.TabFragment(fragmentManager, State.ITEMSPACE);
			}
		});

		keyword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				SELECTITEMFLAG = ItemHttpProtocol.selectItem(getActivity(),
						Selloff.this, arg0.toString());

			}
		});

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (isSearch) {
			swipeLayout.setRefreshing(false);
			return;
		}
		int nowTime = (int) (System.currentTimeMillis() / 1000);
		if ((nowTime - lastTime) > 10) {
			lastTime = (int) (System.currentTimeMillis() / 1000);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					reqItemData();
				}
			}, 1000);
		} else {
			closeRefreshing();
		}

	}

	public void closeRefreshing() {
		swipeLayout.setRefreshing(false);
	}

	@Override
	public void onGeneralSuccess(String result, long flag) {
		// TODO Auto-generated method stub

		if (SELECTITEMFLAG == flag) {
			System.out.println("SELECTITEMFLAG   " + result);
		}

		swipeLayout.setRefreshing(false);
		itemDate = CommonFunction.ToList(result, itemDate);
		mHandler.sendEmptyMessage(REFRESH_COMPLETE);
	}

	@Override
	public void onGeneralError(String e, long flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.auction_search:
			isSearch = true;
			search_bar.setVisibility(View.VISIBLE);
			selloff_normal_bar.setVisibility(View.GONE);

			break;
		case R.id.auction_more:

			if (pwMyPopWindow != null && pwMyPopWindow.isShowing()) {

				pwMyPopWindow.dismiss();// 关闭
			} else {
				initPopupWindowView();
				pwMyPopWindow.showAsDropDown(auction_more, -40, 0);// 显示
			}
			break;
		case R.id.more_computer_ly:
			kind_id = 0;
			title_name.setText("电脑类");
			reqItemData();
			pwMyPopWindow.dismiss();
			swipeLayout.setRefreshing(true);
			break;
		case R.id.more_costume_ly:
			kind_id = 1;
			reqItemData();
			title_name.setText("服饰类");
			pwMyPopWindow.dismiss();
			swipeLayout.setRefreshing(true);
			break;
		case R.id.more_interest_ly:
			kind_id = 2;
			reqItemData();
			title_name.setText("兴趣类");
			pwMyPopWindow.dismiss();
			swipeLayout.setRefreshing(true);
			break;
		case R.id.more_life_ly:
			kind_id = 3;
			reqItemData();
			title_name.setText("生活类");
			pwMyPopWindow.dismiss();
			swipeLayout.setRefreshing(true);
			break;
		case R.id.more_mobile_ly:
			kind_id = 4;
			reqItemData();
			title_name.setText("手机类");
			pwMyPopWindow.dismiss();
			swipeLayout.setRefreshing(true);
			break;
		case R.id.title_icon:
			if (kind_id == 5)
				return;
			kind_id = 5;
			reqItemData();
			title_name.setText("拍卖系统");
			swipeLayout.setRefreshing(true);
			break;
		case R.id.search_back:
			isSearch = false;
			search_bar.setVisibility(View.GONE);
			selloff_normal_bar.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	public void initPopupWindowView() {

		DisplayMetrics outMetrics = new DisplayMetrics();
		getActivity().getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(outMetrics);
		int screenWidth = outMetrics.widthPixels;
		View customView = inflater.inflate(R.layout.popview_item, null, false);
		// pwMyPopWindow = new PopupWindow(, 400, 360);
		pwMyPopWindow = new PopupWindow(customView,
				outMetrics.widthPixels / 5 * 2, outMetrics.heightPixels / 5 * 2);

		pwMyPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
				R.color.black));// 设置背景图片，不能在布局中设置，要通过代码来设置
		pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上

		// pwMyPopWindow.setAnimationStyle(R.style.);
		customView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (pwMyPopWindow != null && pwMyPopWindow.isShowing()) {
					pwMyPopWindow.dismiss();
					pwMyPopWindow = null;
				}

				return false;
			}
		});
		customView.findViewById(R.id.more_computer_ly).setOnClickListener(this);
		customView.findViewById(R.id.more_costume_ly).setOnClickListener(this);
		customView.findViewById(R.id.more_interest_ly).setOnClickListener(this);
		customView.findViewById(R.id.more_life_ly).setOnClickListener(this);
		customView.findViewById(R.id.more_mobile_ly).setOnClickListener(this);

	}
}
