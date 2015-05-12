package com.auction.adapter;

import java.util.List;
 





import com.auction.client.R;
import com.auction.client.fragment.login;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

public class GridAdapter extends  BaseAdapter{
    private LayoutInflater listContainer;
    private int selectedPosition=-1;
    private boolean shape;
    private List<Bitmap> bmp;
    public boolean isShape() {
		return shape;
	}
    public class ViewHolder{
    	public ImageView image;
    	public Button bt;
    }
      public GridAdapter(Context context,List<Bitmap> bmp) {
		// TODO Auto-generated constructor stub
    	  listContainer = LayoutInflater.from(context);
    	  this.bmp = bmp;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(bmp.size() <4){
			return bmp.size()+1;
		}else{
		return bmp.size();
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int sign = position;
		ViewHolder holder = null;
		if(convertView == null){
			holder =new ViewHolder();
			convertView = listContainer.inflate(R.layout.add_item_grida, null);
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			holder.bt = (Button) convertView
					.findViewById(R.id.item_grida_bt);
			convertView.setTag(holder);
		}else{
	     holder = (ViewHolder) convertView.getTag();
		}
		if(position == bmp.size()){
			holder.image.setImageBitmap(BitmapFactory.decodeResource(listContainer.getContext().getResources(),R.drawable.menu_add));
			holder.bt.setVisibility(View.GONE);
			if (position ==4) {
				holder.image.setVisibility(View.GONE);
			}
		}else{
			holder.image.setImageBitmap(bmp.get(position));
			holder.bt.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
				 Log.d("ddd", "dddd");
				}
			});
		}
		return null;
	}

}
