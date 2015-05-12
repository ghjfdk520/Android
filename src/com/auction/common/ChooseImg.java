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
						if(position==0){      //�����ϴ�   
							Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
							fragment.startActivityForResult(intent, Constants.REQUEST_CODE_PHOTOGRAPH);
						}else if(position ==1){    //�����ϴ�
							Intent intent = new Intent();
							intent.setType("image/*");
							intent.setAction(Intent.ACTION_GET_CONTENT);
							fragment.startActivityForResult(intent, Constants.REQUEST_CODE_LOCPIC);
							
						}
					}else{
						Toast.makeText(mContext,"sd��������", Toast.LENGTH_LONG).show();
					}
					
				}
			});
	    	dialog.setContentView(dialogView);
	    	
	    	//dialog.setTitle("�ϴ���Ƭ");
	    	
	    	dialog.show();
	    	Log.d("tag", "succ");
	    }

	private List<String> getData() {
		List<String> data = new ArrayList<String>();
		data.add("�����ϴ�");
		data.add("�����ϴ�");
		return data;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		dialog.dismiss();
		Log.d("tag", "faile");
		if (data != null && requestCode == Constants.REQUEST_CODE_PHOTOGRAPH) { // �����ϴ�ʱ
			Bundle extras = data.getExtras();

			if (extras != null) {
				photo = (Bitmap) extras.get("data");
				img.setImageBitmap(photo);
				// saveImage();
			} else {
				Toast.makeText(mContext, "δ�ҵ�ͼƬ", Toast.LENGTH_LONG)
						.show();
			}
		}
		if (resultCode == Activity.RESULT_OK) {
			/**
			 * ��ѡ���ͼƬ��Ϊ�յĻ����ڻ�ȡ��ͼƬ��;��
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
					 * ���������һ���ж���Ҫ��Ϊ�˵����������ѡ�񣬱��磺ʹ�õ��������ļ��������Ļ�����ѡ����ļ��Ͳ�һ����ͼƬ�ˣ�
					 * �����Ļ��������ж��ļ��ĺ�׺�� �����ͼƬ��ʽ�Ļ�����ô�ſ���
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
	
	 //�ı�˵���ͷ��
	private void changeMenuHeadImg(){
		Fragment menu = fragment.getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_menu);
		View v = menu.getView();
		ImageView HeadImg = (ImageView) v.findViewById(R.id.userIconView);
		HeadImg.setImageBitmap(photo);


	}

}
