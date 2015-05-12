package com.auction.client;

import org.json.*;

import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class KindArrayAdapter extends BaseAdapter{
    private JSONArray kindArray;
    private Context ctx;
    
    
    public KindArrayAdapter(JSONArray kindArray,Context ctx) {
		// TODO Auto-generated constructor stub
    	this.kindArray=kindArray;
    	this.ctx = ctx;
    			
	}
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return kindArray.length();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return kindArray.optJSONArray(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		try {
			return ((JSONObject)getItem(arg0)).getInt("id");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LinearLayout container = new LinearLayout(ctx);
		container.setOrientation(1);
		LinearLayout linear = new LinearLayout(ctx);
		linear.setOrientation(0);
		ImageView iv = new ImageView(ctx);
		iv.setPadding(10, 0, 20, 0);
		iv.setImageResource(R.drawable.item);
		
		linear.addView(iv);
		TextView tv = new TextView(ctx);
		try {
			String kindName = ((JSONObject)getItem(arg0)).getString("kindName");
			tv.setText(kindName);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		tv.setTextSize(20);
		linear.addView(tv);
		container.addView(linear);
		
		TextView descView = new TextView(ctx);
		descView.setPadding(30, 0, 0, 0);
		try {
			String kindDesc = ((JSONObject)getItem(arg0)).getString("kindDesc");
			descView.setText(kindDesc);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		descView.setTextSize(16);
		container.addView(descView);
		
		return container;
	}

}
