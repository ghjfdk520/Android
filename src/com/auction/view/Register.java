package com.auction.view;

import com.auction.client.R;
import com.auction.client.util.CommonFunction;
import com.auction.client.util.LightTimer;
import com.auction.connector.HttpCallBack;
import com.auction.connector.protocol.UserHttpProtocol;
import com.auction.map.LocationBaiduMapActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity implements HttpCallBack, OnClickListener {

	private long GETCODEFLAG;
	private long SUBMITFALG;
	private ProgressDialog mProgressDialog;
	private int rockon = 10;
	private EditText phone;
	private EditText setpasswd;
	private EditText confrimpasswd;
	private EditText codeEdit;
	private TextView get_code_bt;
	private TextView submit;
	private LightTimer lt;

	private String addressCode;
	private TextView titlename;
	private EditText address;
	private ImageView title_back;
	private boolean isGetCode = false;// 是否点击获取验证码按钮
    private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		init();
	}

	public void init() {
		titlename = (TextView) findViewById(R.id.title_name);
		title_back = (ImageView) findViewById(R.id.title_back);
		titlename.setText("注册用户");

		phone = (EditText) findViewById(R.id.phone);
		setpasswd = (EditText) findViewById(R.id.setpasswd);
		confrimpasswd = (EditText) findViewById(R.id.confrimpasswd);
		codeEdit = (EditText) findViewById(R.id.codeEdit);
		get_code_bt = (TextView) findViewById(R.id.get_code_bt);
		submit = (TextView) findViewById(R.id.submit);
		address = (EditText) findViewById(R.id.address);
		get_code_bt.setOnClickListener(this);
		submit.setOnClickListener(this);

		findViewById(R.id.title_back).setOnClickListener(this);

		findViewById(R.id.map_bt).setOnClickListener(this);
		lt = new LightTimer() {

			@Override
			public void run(LightTimer timer) {
				// TODO Auto-generated method stub

				if (timer.getRunCount() == rockon) {
					isGetCode = false;
					get_code_bt
							.setBackgroundResource(R.drawable.bg_button_register_one);
					get_code_bt.setText("获取验证码");
					timer.stop();
					return;
				}

				get_code_bt
						.setBackgroundResource(R.drawable.bg_button_register_two);
				get_code_bt.setText(rockon - timer.getRunCount() + "");

			}
		};

	}

	public boolean verify(int key) {
		if (CommonFunction.isEmptyOrNullStr(phone.getText().toString())) {
			Toast.makeText(this, "手机不能为空", Toast.LENGTH_LONG).show();
		}

		switch (key) {
		
		case 1:
			if (CommonFunction.isEmptyOrNullStr(phone.getText().toString())) {
				Toast.makeText(this, "手机不能为空", Toast.LENGTH_LONG).show();
				return false;
			}
			if(isGetCode) return true;
		case 2:
			if (!CommonFunction
					.isEmptyOrNullStr(setpasswd.getText().toString())) {
				if (!CommonFunction.isEmptyOrNullStr(confrimpasswd.getText()
						.toString())) {
					if (!confrimpasswd.getText().toString()
							.equals(setpasswd.getText().toString())) {
						Toast.makeText(this, "密码不相同", Toast.LENGTH_LONG).show();
						return false;
					}
				} else {
					Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
					return false;
				}

			} else {
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
				return false;
			}
		case 3:
			 if (CommonFunction
					.isEmptyOrNullStr(address.getText().toString())) {
				 Toast.makeText(this, "地址不能为空", Toast.LENGTH_LONG).show();
					return false;
			 }
			 
		default:
			break;
		}
		return true;
	}

	public void query() {
		if (verify(1)) {
			showLoading("注册中...");
			SUBMITFALG = UserHttpProtocol.register(this, this, phone.getText()
					.toString(), setpasswd.getText().toString(),addressCode+"/"+address.getText().toString());
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.submit:
			query();
			break;
		case R.id.get_code_bt:
			if (!isGetCode) {
				isGetCode = true;
				codeEdit.setText((int)(Math.random()*1000000)+"");
				lt.startTimer(1000, rockon);
			}
			break;
		case R.id.title_back:
			finish();
			break;
		case R.id.map_bt:
			LocationBaiduMapActivity.actionActivity(this);
			break;
		default:
			break;
		}
	}

	@Override
	public void onGeneralSuccess(String result, long flag) {
		// TODO Auto-generated method stub
        if(flag == SUBMITFALG){
        	closeLoading();
        	if(!result.equals("0") && !CommonFunction.isEmptyOrNullStr(result)){
        		Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
        		finish();
        	}else{
        		Toast.makeText(this, "号码已注册", Toast.LENGTH_LONG).show();
        	}
        }else if(flag == GETCODEFLAG){
        	
        }
	}

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 0x00f)
		addressCode = data.getStringExtra("address");
	}

	@Override
	public void onGeneralError(String e, long flag) {
		// TODO Auto-generated method stub
		closeLoading();
	}
 
	
	public void showLoading(String message) {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(this, "", message, false,
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
