package com.auction.client.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.auction.client.R;
import com.auction.client.entity.UserInfo;
import com.auction.client.fragment.MenuFragment;
import com.auction.client.fragment.login;
import com.auction.common.Constants;
import com.auction.config.Common;
import com.auction.connector.FormFile;
import com.auction.connector.SocketHttpRequester;

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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 头像上传工具类
 * 
 * @author Administrator
 * 
 */
public class PhotoUtil {

	private Fragment mFragment;
	private Context mContext;
	public static Dialog dialog;
	private static ListView itemList;
	private Bitmap photo;
	private ImageView img;
	private UserInfo user = Common.getInstance().user;

	//
	public PhotoUtil(Fragment fragment, View v) {
		// TODO Auto-generated constructor stub
		this.mFragment = fragment;
		this.img = (ImageView) v;
		chooseType();
	}

	public static void chooseImg(final Activity activity) {
		dialog = new Dialog(activity, R.style.dialog);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		View dialogView = LayoutInflater.from(activity).inflate(
				R.layout.photo_dialog, null);
		itemList = (ListView) dialogView.findViewById(R.id.itemList);
		itemList.setAdapter(new ArrayAdapter<String>(activity,
				android.R.layout.simple_expandable_list_item_1, getData()));
		itemList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View conventView,
					int position, long id) {
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					if (position == 0) { // 拍照上传
						Intent intent = new Intent(
								"android.media.action.IMAGE_CAPTURE");
						activity.startActivityForResult(intent,
								Constants.REQUEST_CODE_PHOTOGRAPH);
					} else if (position == 1) { // 本地上传
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						activity.startActivityForResult(intent,
								Constants.REQUEST_CODE_LOCPIC);

					}
				} else {
					Toast.makeText(activity, "sd卡不存在", Toast.LENGTH_LONG)
							.show();
				}

			}
		});
		dialog.setContentView(dialogView);
		dialog.show();

	}

	private void chooseType() {
		dialog = new Dialog(mFragment.getActivity(), R.style.dialog);
		View dialogView = LayoutInflater.from(mFragment.getActivity()).inflate(
				R.layout.photo_dialog, null);
		itemList = (ListView) dialogView.findViewById(R.id.itemList);
		itemList.setAdapter(new ArrayAdapter<String>(mFragment.getActivity(),
				android.R.layout.simple_expandable_list_item_1, getData()));
		itemList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View conventView,
					int position, long id) {
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					if (position == 0) { // 拍照上传
						Intent intent = new Intent(
								"android.media.action.IMAGE_CAPTURE");
						mFragment.startActivityForResult(intent,
								Constants.REQUEST_CODE_PHOTOGRAPH);
					} else if (position == 1) { // 本地上传
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						mFragment.startActivityForResult(intent,
								Constants.REQUEST_CODE_LOCPIC);

					}
				} else {
					Toast.makeText(mFragment.getActivity(), "sd卡不存在",
							Toast.LENGTH_LONG).show();
				}

			}
		});
		dialog.setContentView(dialogView);
		dialog.show();
	}

	private static List<String> getData() {
		List<String> data = new ArrayList<String>();
		data.add("拍照上传");
		data.add("本地上传");
		return data;
	}

	public void getPhoto(int requestCode, int resultCode, Intent data) {
		dialog.dismiss();
		if (data != null && requestCode == Constants.REQUEST_CODE_PHOTOGRAPH) { // 拍照上传时
			Bundle extras = data.getExtras();

			if (extras != null) {
				photo = (Bitmap) extras.get("data");
				img.setImageBitmap(photo);
				// saveImage();
			} else {
				Toast.makeText(mFragment.getActivity(), "未找到图片",
						Toast.LENGTH_LONG).show();
			}
		}
		if (resultCode == Activity.RESULT_OK) {
			/**
			 * 当选择的图片不为空的话，在获取到图片的途径
			 */
			Uri uri = data.getData();
			try {
				String[] pojo = { MediaStore.Images.Media.DATA };

				Cursor cursor = mFragment.getActivity().getContentResolver()
						.query(uri, pojo, null, null, null);
				if (cursor != null) {
					ContentResolver cr = mFragment.getActivity()
							.getContentResolver();
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
		if (photo == null)
			return;
		changeMenuHeadImg();
		// upload();
		Map<String, String> map = new HashMap<String, String>();
		JSONObject json = new JSONObject();
		try {
			json.put("user_id", user.getUser_id());

		} catch (Exception e) {
			// TODO: handle exception
		}
		map.put("userJson", json.toString());
		String url = Common.getInstance().updateUserUrl;
		UploadImg up = new UploadImg(url, photo, map);
		up.start();
	}

	// 改变菜单栏头像
	private void changeMenuHeadImg() {
		int mRadius = Math.min(photo.getWidth(), photo.getHeight());
		Bitmap bitmap = CommonFunction.createCircleImage(photo,
				photo.getWidth(), photo.getHeight());
		Common.getInstance().user.setImgIcon(bitmap);
		MenuFragment.changeImg(bitmap);
		// Fragment menu
		// =mFragment.getActivity().getSupportFragmentManager().findFragmentById(R.id.frame_menu);
		// View v = menu.getView();
		// ImageView HeadImg = (ImageView) v.findViewById(R.id.headImg);
		// HeadImg.setImageBitmap(photo);
	}

	public void upload() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
		byte[] b = stream.toByteArray();
		String file = new String(Base64Coder.encodeLines(b));

		Map<String, String> map = new HashMap<String, String>();
		JSONObject json = new JSONObject();
		try {
			json.put("user_id", user.getUser_id());

		} catch (Exception e) {
			// TODO: handle exception
		}
		map.put("userJson", json.toString());

		try {

			String url = Common.getInstance().updateUserUrl;
			FormFile formfile = new FormFile("file", stream.toByteArray(),
					"file", "application/octet-stream");
			SocketHttpRequester.post(url, map, formfile);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
