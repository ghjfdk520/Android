package com.auction.client;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JSONArrayAdapter extends BaseAdapter{

	private Context ctx;
	private JSONArray jsonArray;
	private String property;
	private boolean hasIcon;
	
	public JSONArrayAdapter(Context ctx,JSONArray jsonArray,String property,boolean hasIcon){
		this.ctx =ctx;
		this.jsonArray = jsonArray;
		this.property = property;
		this.hasIcon = hasIcon;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jsonArray.length();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return jsonArray.optJSONObject(arg0);
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
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LinearLayout linear = new LinearLayout(ctx);
		linear.setOrientation(0);
		ImageView iv = new ImageView(ctx);
		iv.setPadding(10, 0, 20, 0);
		iv.setImageResource(R.drawable.item);
		linear.addView(iv);
		TextView tv =new TextView(ctx);
		try {
			String itemName= ((JSONObject)getItem(arg0)).getString(property);
			tv.setText(itemName);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		tv.setTextSize(20);
		if(hasIcon){
			linear.addView(tv);
			return linear;
		}else{
			tv.setTextColor(Color.BLACK);
			return tv;
		}
		
	}

}
