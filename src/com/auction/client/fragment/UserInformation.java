package com.auction.client.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.auction.client.R;
import com.auction.client.entity.UserInfo;
import com.auction.client.util.CommonFunction;
import com.auction.client.util.DialogUtil;
import com.auction.client.util.PhotoUtil;
import com.auction.config.Common;
import com.auction.connector.HttpCallBack;
import com.auction.connector.protocol.UserHttpProtocol;
import com.auction.view.ImagePagerActivity;

public class UserInformation extends Fragment implements OnClickListener,
		HttpCallBack {

	private Dialog dialog;
	// private View dialogView;

	private long UPDATENAMEFLAG;
	private long UPDATEMOBILEFLAG;
	private long UPDATEADDRESSFLAG;
	private int updateFlag;
	private ImageView head_image;
	private TextView username;
	private TextView mobile;
	private TextView address;
	private RelativeLayout rl_user_img, rl_user_name, rl_user_mobile,
			rl_user_address;
	private View contentView;
	private Bundle bundle;
	private UserInfo user;
	private TextView title_name;
	private View dialogView;
	private EditText et;
	private PhotoUtil pu;
	private ProgressDialog mProgressDialog;
	String tempStr;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		contentView = inflater.inflate(R.layout.user_information, null);
		// dialogView =inflater.inflate(R.layout.photo_dialog, null );
		initDate();
		initInformation();

		return contentView;
	}

	private void initDate() {
		// bundle = getArguments();
		user = Common.getInstance().user;

	}

	private void initInformation() {
		title_name = (TextView) contentView.findViewById(R.id.title_name);
		title_name.setText("个人资料");

		rl_user_img = (RelativeLayout) contentView
				.findViewById(R.id.rl_user_img);
		rl_user_name = (RelativeLayout) contentView
				.findViewById(R.id.rl_user_name);
		rl_user_mobile = (RelativeLayout) contentView
				.findViewById(R.id.rl_user_mobile);
		rl_user_address = (RelativeLayout) contentView
				.findViewById(R.id.rl_user_address);

		head_image = (ImageView) contentView.findViewById(R.id.head_image);
		username = (TextView) contentView.findViewById(R.id.username);
		mobile = (TextView) contentView.findViewById(R.id.mobile);
		address = (TextView) contentView.findViewById(R.id.address);

		username.setText(user.getUsername());
		mobile.setText(user.getMobile());
		address.setText(user.getAddress());

		head_image.setImageBitmap(Common.getInstance().user.getImgIcon());
		head_image.setOnClickListener(this);

		rl_user_img.setOnClickListener(this);
		rl_user_name.setOnClickListener(this);
		rl_user_mobile.setOnClickListener(this);
		rl_user_address.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.head_image:
			ImagePagerActivity.appendStr(Common.getInstance().BASE_UPLOAD
					+ Common.getInstance().user.getImg_url());
			Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_user_img:
			head_image.setTag(user.getUser_id());
			pu = new PhotoUtil(UserInformation.this, head_image);
			break;
		case R.id.rl_user_name:
			updateFlag = 1;
			showDialog("修改姓名", username.getText().toString());
			break;
		case R.id.rl_user_mobile:
			updateFlag = 2;
			showDialog("修改手机", mobile.getText().toString());
			break;
		case R.id.rl_user_address:
			updateFlag = 3;
			showDialog("修改地址", address.getText().toString());
			break;
		default:
			break;
		}
	}

	public void update(String changeStr) {
		
		UserInfo user = Common.getInstance().user;

		if (updateFlag == 1) {
			user.setUsername(changeStr);
			username.setText(changeStr);
			MenuFragment.changName(changeStr);
			// UPDATENAMEFLAG;
		} else if (updateFlag == 2) {
			user.setMobile(changeStr);
			mobile.setText(changeStr);
			// UPDATEMOBILEFLAG;
		} else if (updateFlag == 3) {
			user.setAddress(changeStr);
			address.setText(changeStr);
			// UPDATEADDRESSFLAG
		}
		UserHttpProtocol.updateUser(getActivity(), this);
	}

	@SuppressLint("NewApi")
	public void showDialog(String title, final String msg) {
		final View dialogView = LayoutInflater.from(getActivity()).inflate(
				R.layout.userinformation_dialog, null);
		EditText et = (EditText) dialogView.findViewById(R.id.user_edit);
		et.setText(msg);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// if (updateFlag == 1) {
		// builder.setTitle("用户名");
		// } else if (updateFlag == 2) {
		// builder.setTitle("手机");
		// } else if (updateFlag == 3) {
		builder.setTitle(title);

		builder.setView(dialogView);
		builder.setCancelable(true);
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				showLoading("请稍后...");
				EditText et = (EditText) dialogView
						.findViewById(R.id.user_edit);
				tempStr = et.getText().toString();
				if (!msg.equals(tempStr)) {
					update(tempStr);
				}else{
					closeLoading();
					arg0.dismiss();
				}
			}
		});

		builder.create().show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		pu.getPhoto(requestCode, resultCode, data);
	}

	@Override
	public void onGeneralSuccess(String result, long flag) {
		// TODO Auto-generated method stub

		closeLoading();
		if(result.equals("success")){
			CommonFunction.Toast(getActivity(), "修改成功");
		}else{
			CommonFunction.Toast(getActivity(), "修改失败");
		}
	}

	@Override
	public void onGeneralError(String e, long flag) {
		// TODO Auto-generated method stub
		closeLoading();	
		CommonFunction.Toast(getActivity(), "修改失败");
			
	}
	public void showLoading(String message) {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(getActivity(), "", message, false,
					false);
		} else {
			mProgressDialog.setMessage(message);
			mProgressDialog.show();
		}
	}

	public void closeLoading() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
}
