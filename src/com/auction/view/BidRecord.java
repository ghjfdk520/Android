package com.auction.view;

import java.util.List;
import java.util.Map;

import com.auction.client.R;
import com.auction.connector.HttpCallBack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BidRecord extends Activity {
    private ListView listView;
    private static List<Map<String,Object>> itemBidData;
    private MyAdapter adapter;
     
    public static void appendList(List<Map<String,Object>> list){
    	itemBidData =list;
    }
    public void init(){
    	listView=(ListView) findViewById(R.id.bidRecordList);
    	adapter = new MyAdapter();
    	listView.setAdapter(adapter);
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bid_record_list);
		init();
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemBidData.size();
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
			View v = getLayoutInflater().inflate(R.layout.bid_record_item, null);
			TextView name = (TextView) v.findViewById(R.id.name);
			TextView price = (TextView) v.findViewById(R.id.price);
			Map<String, Object> map = itemBidData.get(position);
			
			name.setText(map.get("user_name").toString());
			price.setText(map.get("bid_price").toString());
			return v;
		}
		
	}

}
