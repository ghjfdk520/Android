package com.auction.client.fragment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.auction.client.AddItem;
import com.auction.client.R;
import com.auction.client.entity.Item;
import com.auction.client.util.CommonFunction;
import com.auction.client.util.DialPhone;
import com.auction.client.util.JsonUtil;
import com.auction.config.Common;
import com.auction.connector.ConnectorManage;
import com.auction.connector.HttpCallBack;
import com.auction.connector.protocol.ItemHttpProtocol;
import com.auction.view.BidRecord;
import com.auction.view.ItemSpace;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerBid extends Fragment implements HttpCallBack,
		OnClickListener {
	private ListAdapter listAdapter;
	private View v;
	private ListView itemList;
	private List<Item> itemData;
	private List<Map<String, Object>> itemBidData;
	private Map<String, String> map = new HashMap<String, String>();
	private String url;
	private TextView title_name;
	private ImageView title_add;

	private long GETITEMBIDFLAG = 0;
	private long DELITEM = 0;
	private long FINDUSERITEMFLAG = 0;
	private long GETUSERFLAG;
	private ProgressDialog mProgressDialog;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			listAdapter.notifyDataSetChanged();

		}
	};

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		map.put("user_id", Common.getInstance().user.getUser_id() + "");

		url = Common.getInstance().FINDUSERITEM;
		itemData = new LinkedList<Item>();
		itemBidData = new LinkedList<Map<String, Object>>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.manage_item, null);
		initView();
		initListener();
		FINDUSERITEMFLAG = ItemHttpProtocol.getUserItem(getActivity(), this);
		return v;
	}

	public void initView() {

		title_name = (TextView) v.findViewById(R.id.title_name);
		title_name.setText("管理");
		title_add = (ImageView) v.findViewById(R.id.title_add);
		title_add.setVisibility(View.VISIBLE);
		itemList = (ListView) v.findViewById(R.id.itemList);
		listAdapter = new ListAdapter();
		itemList.setAdapter(listAdapter);
	}

	public void initListener() {
		title_add.setOnClickListener(this);
		itemList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ItemSpace.launchActivity(getActivity(), itemData.get(arg2));
			}
		});
	 

	}

	private void reqItemData() {
		CommonFunction.ToJsonStr(map);
		ConnectorManage.getInstance(getActivity()).asynPost(url, map, this);
	}

	private void reqBidDate(int item_id, int flag) {
		map.clear();
		map.put("item_id", item_id + "");
		if (flag == 0) {
			url = Common.getInstance().GETITEMBID;
			CommonFunction.ToJsonStr(map);
			GETITEMBIDFLAG = ConnectorManage.getInstance(getActivity()).Post(
					url, map, this);
		} else if (flag == 1) {
			url = Common.getInstance().DELITEM;
			CommonFunction.ToJsonStr(map);
			DELITEM = ConnectorManage.getInstance(getActivity()).Post(url, map,
					this);
		}

	}

	public static void changeList() {
		System.out.println("hahahhsdfd");
	}

	class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			ViewHolder holder;
			if (view == null) {
				view = LayoutInflater.from(getActivity()).inflate(
						R.layout.manager_list_item, parent, false);
				holder = new ViewHolder();
				holder.item_name = (TextView) view.findViewById(R.id.item_name);
				holder.winner_phone= (ImageView) view
						.findViewById(R.id.winner_phone);
				holder.undercarriage = (Button) view
						.findViewById(R.id.undercarriage);
				holder.bid_record = (ImageView) view
						.findViewById(R.id.bid_record);
				holder.finish_icon = (ImageView) view
						.findViewById(R.id.finish_icon);
				
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			Long l = System.currentTimeMillis() / 1000;
			Item item = itemData.get(position);
			if(item.getEndtime()<l){
				holder.winner_phone.setVisibility(View.VISIBLE);
				holder.finish_icon.setVisibility(View.VISIBLE);
			}else{
				holder.winner_phone.setVisibility(View.GONE);
				holder.finish_icon.setVisibility(View.GONE);
			}
			holder.position = position;
			holder.undercarriage.setTag(holder);
			holder.bid_record.setTag(holder);
			holder.item_name.setTag(holder);
			holder.winner_phone.setTag(holder);
			holder.item_name.setText(item.getItem_name());
			holder.bid_record.setOnClickListener(ManagerBid.this);
			holder.undercarriage.setOnClickListener(ManagerBid.this);
			holder.item_name.setOnClickListener(ManagerBid.this);
			holder.winner_phone.setOnClickListener(ManagerBid.this);
			return view;
		}

	}

	class ViewHolder {
		ImageView bid_record;
		ImageView winner_phone;
		ImageView finish_icon;
		Button undercarriage;
		TextView item_name;
		int position;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		final ViewHolder holder = (ViewHolder) v.getTag();
		switch (id) {
		case R.id.item_name:
			ItemSpace.launchActivity(getActivity(), itemData.get(holder.position));
			break;
		case R.id.bid_record:
			// System.out.println((Integer) v.getTag());
			reqBidDate(itemData.get(holder.position).getItem_id(), 0);
			break;
		case R.id.winner_phone:
			GETUSERFLAG = ItemHttpProtocol.getUser(getActivity(), this, itemData.get(holder.position).getWiner_id());
			break;
		case R.id.undercarriage:

			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			dialog.setTitle("确定把拍卖品下架?");
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							reqBidDate(itemData.get(holder.position)
									.getItem_id(), 1);
							itemData.remove(holder.position);
							mHandler.sendEmptyMessage(2);
						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							arg0.dismiss();
						}
					});
			dialog.create().show();
			break;
		case R.id.title_add:

			AddItem.launchforResultActivity(getActivity(), map.get("user_id"));
			break;
		default:
			break;
		}
	}

	@Override
	public void onGeneralSuccess(String result, long flag) {
		// TODO Auto-generated method stub
		if (flag == GETITEMBIDFLAG) {
			itemBidData = JsonUtil.jsonToList(result);
			Intent intent = new Intent();
			intent.setClass(getActivity(), BidRecord.class);
			if (itemBidData != null) {
				BidRecord.appendList(itemBidData);
				startActivity(intent);
			}

		} else if (flag == DELITEM) {
			CommonFunction.Toast(getActivity(), "下架成功");
		} else if (flag == FINDUSERITEMFLAG) {
			itemData = CommonFunction.ToList(result, itemData);
			mHandler.sendEmptyMessageDelayed(1, 1000);
		}else if(flag == GETUSERFLAG){
			DialPhone.dialAlert(result, getActivity());
		}

	}

	@Override
	public void onGeneralError(String e, long flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0xf00) {
			System.out.println(resultCode);
			Gson gson = new Gson();
			Item item = gson.fromJson(data.getStringExtra("item"), Item.class);
			((LinkedList<Item>)itemData).addFirst(item);
			listAdapter.notifyDataSetChanged();
		}
	}

	public void showLoading(String message) {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(getActivity(), "", message,
					false, false);
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

	private void runOnUIThread(Runnable runnable) {

		if (runnable != null && mHandler != null) {
			mHandler.post(runnable);
		}
	}
}
