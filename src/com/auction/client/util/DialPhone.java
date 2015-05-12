package com.auction.client.util;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
public class DialPhone {
	public static void dialAlert(final String dialNamber, final Context context) {

		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("电话号:"+ dialNamber);
		builder.setTitle("确定要拨打电话？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Uri uri = Uri.parse("tel:" + dialNamber);
				intent.setAction(intent.ACTION_CALL);
				intent.setData(uri);
				context.startActivity(intent);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				arg0.dismiss();
 
			}
		});
		builder.create().show();
	}
}