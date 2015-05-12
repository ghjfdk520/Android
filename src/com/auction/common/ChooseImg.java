package com.auction.common;

import java.util.ArrayList;
import java.util.List;

import com.auction.client.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.auction.common.*;
public class ChooseImg {
	private Context mContext;
	private ImageView img;
	private Dialog dialog;
	private ListView itemList;
	private Bitmap photo;
	private Fragment fragment;

	public ChooseImg(Fragment fragment, View v) {
		// TODO Auto-generated constructor stub
		this.mContext = fragment.getActivity();
		this.img = (ImageView) v;
	    this.fragment = fragment;
		chooseType();
	}

	

	private void chooseType(){
	    	dialog = new Dialog(mContext,R.style.dialog);
	    	View dialogView = LayoutInflater.from(mContext).inflate(R.layout.photo_dialog, null);
	    	itemList = (ListView)dialogView.findViewById(R.id.itemList);
	    	itemList.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_expandable_list_item_1,getData()));
	    	itemList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View conventView, int position,
						long id) {
					String state = Environment.getExternalStorageState();
					if(state.equals(Environment.MEDIA_MOUNTED)){
						if(position==0){      //拍照上传   
							Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
							fragment.startActivityForResult(intent, Constants.REQUEST_CODE_PHOTOGRAPH);
						}else if(position ==1){    //本地上传
							Intent intent = new Intent();
							intent.setType("image/*");
							intent.setAction(Intent.ACTION_GET_CONTENT);
							fragment.startActivityForResult(intent, Constants.REQUEST_CODE_LOCPIC);
							
						}
					}else{
						Toast.makeText(mContext,"sd卡不存在", Toast.LENGTH_LONG).show();
					}
					
				}
			});
	    	dialog.setContentView(dialogView);
	    	
	    	//dialog.setTitle("上传照片");
	    	
	    	dialog.show();
	    	Log.d("tag", "succ");
	    }

	private List<String> getData() {
		List<String> data = new ArrayList<String>();
		data.add("拍照上传");
		data.add("本地上传");
		return data;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		dialog.dismiss();
		Log.d("tag", "faile");
		if (data != null && requestCode == Constants.REQUEST_CODE_PHOTOGRAPH) { // 拍照上传时
			Bundle extras = data.getExtras();

			if (extras != null) {
				photo = (Bitmap) extras.get("data");
				img.setImageBitmap(photo);
				// saveImage();
			} else {
				Toast.makeText(mContext, "未找到图片", Toast.LENGTH_LONG)
						.show();
			}
		}
		if (resultCode == Activity.RESULT_OK) {
			/**
			 * 当选择的图片不为空的话，在获取到图片的途径
			 */
			Uri uri = data.getData();
			try {
				String[] pojo = { MediaStore.Images.Media.DATA };

				Cursor cursor = mContext.getContentResolver().query(uri, pojo, null,
						null, null);
				if (cursor != null) {
					ContentResolver cr = mContext.getContentResolver();
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);
					Log.d("path", path);
					/***
					 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
					 * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
					 */
					if (path.endsWith("jpg") || path.endsWith("png")) {
						// picPath = path;
						photo = BitmapFactory.decodeStream(cr
								.openInputStream(uri));
						img.setImageBitmap(photo);
					} else {

					}
				} else {

				}

			} catch (Exception e) {

			}
		}
		changeMenuHeadImg();

	}
	
	 //改变菜单栏头像
	private void changeMenuHeadImg(){
		Fragment menu = fragment.getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_menu);
		View v = menu.getView();
		ImageView HeadImg = (ImageView) v.findViewById(R.id.userIconView);
		HeadImg.setImageBitmap(photo);


	}

}
