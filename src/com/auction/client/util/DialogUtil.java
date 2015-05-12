package com.auction.client.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import com.auction.adapter.ItemBidAdapter;
import com.auction.client.R;
import com.auction.client.entity.UserInfo;
import com.auction.client.fragment.MenuFragment;
import com.auction.common.Constants;
import com.auction.config.Common;
import com.auction.view.ItemSpace;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DialogUtil {
	private static String tempStr;
	private static EditText infoEdit;

	public static void showDialog(final Context ctx, String msg,
			boolean closeSelf) {
		Builder builder = new Builder(ctx).setMessage(msg).setCancelable(false);
		if (closeSelf) {
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					((Activity) ctx).finish();
				}

			});
		} else {
			builder.setPositiveButton("确定", null);
		}
		builder.create().show();
	}

	public static void showDialog(final Fragment mFragment, final View view) {
		showDialog(mFragment, view, null);
	}

	public static void showBidListDialog(LayoutInflater inflater,
			final List list) {
		final View dialogView = inflater.inflate(R.layout.item_bid_list, null);
		ItemBidAdapter iba = new ItemBidAdapter(inflater, list);
		ListView listView = (ListView) dialogView
				.findViewById(R.id.itemBidList);
		listView.setAdapter(iba);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				inflater.getContext());
		builder.setView(dialogView);
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		builder.create().show();
	}

	public static Dialog showBIDialog(Activity activity, final int price) {
        int price_et = price;
		final View dialogView = activity.getLayoutInflater().inflate(
				R.layout.bid_item, null);
		final EditText now_price = (EditText) dialogView.findViewById(R.id.now_price);
		now_price.setText(price + "");
		ImageView left_arrows = (ImageView) dialogView
				.findViewById(R.id.left_arrows);
 		ImageView right_arrows = (ImageView) dialogView
				.findViewById(R.id.right_arrows);
		Button submit_bt = (Button) dialogView.findViewById(R.id.submit_bt);
		Button cancle_bt = (Button) dialogView.findViewById(R.id.cancle_bt);

		final Dialog dialog = new Dialog(activity);
		cancle_bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		right_arrows.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
				 int tempInt =Integer.parseInt(now_price.getText().toString());
				 now_price.setText(++tempInt+"");
			}
		});
		dialog.setTitle("出价");

		dialog.setContentView(dialogView);
		dialog.show();

		left_arrows.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
				 int tempInt =Integer.parseInt(now_price.getText().toString());
				 if(tempInt> price)
				 now_price.setText(--tempInt+"");
			}
		});
		submit_bt.setTag(now_price);
		return dialog;
	 
	}

	@SuppressLint("NewApi") 
	public static void showDialog(Fragment mFragment, final TextView tv,
			final int updateFlag) {
		final View dialogView = LayoutInflater.from(mFragment.getActivity())
				.inflate(R.layout.userinformation_dialog, null);
		EditText et = (EditText) dialogView.findViewById(R.id.user_edit);
		et.setText(tv.getText());
		AlertDialog.Builder builder = new AlertDialog.Builder(
				mFragment.getActivity(),R.style.dialog);
		if (updateFlag == 1) {
			builder.setTitle("用户名");
		} else if (updateFlag == 2) {
			builder.setTitle("手机");
		} else if (updateFlag == 3) {
			builder.setTitle("地址");
		}
		
		builder.setView(dialogView);
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				EditText et = (EditText) dialogView
						.findViewById(R.id.user_edit);
				String temp = et.getText().toString();
				if (!tv.getText().equals(temp)) {
					tv.setText(temp);
					updateUser(updateFlag, temp);
					if (updateFlag == 1) {
						MenuFragment.changName(temp);
						Common.getInstance().user.setUsername(temp);
					} else if (updateFlag == 2) {
						Common.getInstance().user.setMobile(temp);
					} else if (updateFlag == 3) {
						Common.getInstance().user.setAddress(temp);
					}
				}

			}
		});
		builder.create().show();
	}

	public static void showDialog(final Fragment mFragment, final View view,
			final Boolean isName) {
		infoEdit = (EditText) view.findViewById(R.id.user_edit);
		tempStr = infoEdit.getText().toString();
		AlertDialog.Builder builder = new AlertDialog.Builder(
				mFragment.getActivity()).setView(view).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						TextView tv = (TextView) view.getTag();
						EditText et = (EditText) view
								.findViewById(R.id.user_edit);
						String temp = et.getText().toString();
						tv.setText(temp);
						Log.d("dialog", "xixixi");
						if (isName != null && isName == true) {
							MenuFragment.changName(temp);
							Common.getInstance().user.setUsername(temp);
						}

					}
				});
		builder.create().show();
	}

	public static void updateUser(int updateFlag, String updateStr) {
		UserInfo user = Common.getInstance().user;
		JSONObject json = new JSONObject();
		try {
			json.put("user_id", user.getUser_id());
			json.put("updateFlag", updateFlag);
			json.put("updateStr", updateStr);
		} catch (Exception e) {
			// TODO: handle exception
			CommonFunction.log(e);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("userJson", json.toString());
		String url = Common.getInstance().updateUserUrl;
		HttpUtil.postRequest(url, map);
	}

}
