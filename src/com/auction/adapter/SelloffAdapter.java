package com.auction.adapter;

import java.util.List;

import com.auction.client.R;
import com.auction.client.entity.Item;
import com.auction.client.util.CommonFunction;
import com.auction.client.util.ImageViewUtil;
import com.auction.config.Common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelloffAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Item> itemList;

	public SelloffAdapter(Context context, List<Item> list) {
		// TODO Auto-generated constructor stub
		inflater = LayoutInflater.from(context);
		itemList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemList.size();
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.selloff_item, null);
			holder = new ViewHolder();
			initView(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Item item = itemList.get(position);
		int timeTemp[] = CommonFunction.getRemaining(item.getEndtime() + "");
		holder.stime.setText(timeTemp[0] + "ÃÏ" + timeTemp[1] + " ±"
				+ timeTemp[2] + "∑÷" + timeTemp[3] + "√Î");
		holder.item_name.setText(item.getItem_name());
		holder.maxPrice.setText(item.getMax_price() + "");
		String[] imgurls = item.getItem_img().split(",");
		ImageViewUtil.getDefault().loadImage(
				Common.getInstance().BASE_UPLOAD + imgurls[0],
				holder.item_image, R.drawable.logo, R.drawable.logo);
		return convertView;
	}

	private void initView(ViewHolder holder, View view) {
		holder.item_name = (TextView) view.findViewById(R.id.item_name);
		holder.maxPrice = (TextView) view.findViewById(R.id.maxPrice);
		holder.stime = (TextView) view.findViewById(R.id.stime);
		holder.peoplenum = (TextView) view.findViewById(R.id.peoplenum);
		holder.item_image = (ImageView) view.findViewById(R.id.item_image);
	}

	class ViewHolder {
		public TextView item_name, stime, peoplenum;
		public TextView maxPrice;
		public ImageView item_image;
	}

	public void notifyData(List<Item> list) {
		itemList = list;
		this.notifyDataSetChanged();
	}

}
