package com.auction.client.util;

import java.util.List;

import com.auction.client.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private Context mContext;
	private List<String> mDatas;
	
	
	public MyAdapter(Context context,List<String> mDatas) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mDatas = mDatas;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mDatas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int  position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = ViewHolder.get(mContext, convertView, parent, R.layout.item, position);
		    convertView = mInflater.inflate(R.layout.item,parent,false);
		  
		}
		return viewHolder.getConvertView();
	}
	
}