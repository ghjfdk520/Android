package com.auction.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.auction.client.R;
import com.auction.client.entity.Item;
import com.auction.client.fragment.BidFragment;
import com.auction.client.util.CommonFunction;
import com.auction.client.util.ImageViewUtil;
import com.auction.client.util.JsonUtil;
import com.auction.common.Constants;
import com.auction.config.Common;
import com.auction.connector.ConnectorManage;
import com.auction.connector.HttpCallBack;
import com.auction.connector.protocol.ItemHttpProtocol;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Bid_win extends Fragment implements HttpCallBack {
	private ListView bidList;
	private TextView warn;
	private RecordAdapter rAdater;
	
	private long GETITEMFLAG;
	private List<Map<String, Object>> listMap;
	
	
	private ProgressDialog mProgressDialog;
	static int i ;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.RECORD_NULL:
				warn.setVisibility(warn.VISIBLE);
				break;

			case Constants.RECORD_SUCCESS:
				warn.setVisibility(warn.GONE);
				rAdater.notifyDataSetChanged();
 				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		listMap= new LinkedList<Map<String,Object>>();
		reqRecordData();

	 
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.bid_tab_list, null);
		init(v);
		return v;
	}

	private void reqRecordData() {
		ItemHttpProtocol.getUserBid(getActivity(), this, 0);
		 
	}

	public void init(View v) {
		warn = (TextView) v.findViewById(R.id.warn);
		bidList = (ListView) v.findViewById(R.id.bid_list);
		rAdater = new RecordAdapter();
		bidList.setAdapter(rAdater);
		bidList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				showLoading("加载中...");
				int item_id = (Integer) listMap.get(arg2).get("item_id");
				GETITEMFLAG = ItemHttpProtocol.getItem(getActivity(), Bid_win.this, item_id);
			}
		});
	}

	class RecordAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		// private ImageLoadingListener animateFirstListener;
		public RecordAdapter() {
			// TODO Auto-generated constructor stub
			inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listMap.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.bid_tab_00, null);
				holder.item_image = (ImageView) convertView
						.findViewById(R.id.item_image);
				holder.item_name = (TextView) convertView
						.findViewById(R.id.item_name);
				holder.max_price = (TextView) convertView
						.findViewById(R.id.max_price);
				holder.win_time = (TextView) convertView
						.findViewById(R.id.win_time);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
            Map<String, Object> map = listMap.get(position);
			
			holder.item_name.setText(map.get("item_name").toString());
			holder.max_price.setText(map.get("max_price").toString());
			holder.win_time.setText(map.get("endtime").toString());
			String[] imgurls = map.get("item_img").toString().split(",");
			ImageViewUtil.getDefault().loadImage(
					Common.getInstance().BASE_UPLOAD + imgurls[0],
					holder.item_image, R.drawable.logo, R.drawable.logo);
			return convertView;
		}

	}

	class ViewHolder {
		ImageView item_image;
		TextView item_name;
		TextView max_price;
		TextView win_time;
	}

	 

	@Override
	public void onGeneralSuccess(String result, long flag) {
		// TODO Auto-generated method stub
		closeLoading();
		if(flag == GETITEMFLAG){
			Item item = CommonFunction.toItem(result);
			ItemSpace.launchActivity(getActivity(), item);
			return;
		}
		
		if (result.equals("[]")) {
			mHandler.sendEmptyMessage(Constants.RECORD_NULL);
		} else {
			
 			listMap = JsonUtil.jsonToList(result);
  			mHandler.sendEmptyMessage(Constants.RECORD_SUCCESS);
		}
	}

	@Override
	public void onGeneralError(String e, long flag) {
		// TODO Auto-generated method stub
		closeLoading();
		CommonFunction.Toast(getActivity(),e);
	}
	
	public void Refresh(){
		showLoading("加载中...");
		reqRecordData();
	}
	
	public void showLoading(String message) {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(getActivity(), "", message, false,
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
