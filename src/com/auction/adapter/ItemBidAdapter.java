package com.auction.adapter;

import java.util.List;
import java.util.Map;

import com.auction.client.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemBidAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Map> list;

	public ItemBidAdapter(LayoutInflater inflater, List list) {
		this.inflater = inflater;
		this.list = list;
	}

	// TODO Auto-generated constructor stub

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
	    View v = inflater.inflate(R.layout.item_bid_list_item, null);
		TextView userName = (TextView) v.findViewById(R.id.user_name);
		TextView bidPrice = (TextView) v.findViewById(R.id.bid_price);
		
		Map<String,String> map = list.get(position);
		userName.setText(map.get("user_name"));
		bidPrice.setText(map.get("bid_price"));
	    
	    return v;
	}

	
	
}
