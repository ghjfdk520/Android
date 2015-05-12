package com.auction.client;

import java.util.*;

import org.json.JSONObject;

import com.auction.client.entity.UserInfo;
import com.auction.client.util.*;
import com.auction.common.Constants;
import com.auction.config.Common;
import com.auction.connector.HttpCallBack;
import com.auction.connector.protocol.UserHttpProtocol;
import com.auction.view.Register;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class Login extends Activity implements HttpCallBack, OnClickListener {

	private final String TAG = Login.class.getName();
	EditText etName, etPass;
	Button bnLogin, bnRegister, bnCancel;
	TextView tvRegister;
	Handler handler;
	private ProgressBar progressBar;
	private ProgressDialog mProgressDialog;
	static boolean isclick = false;
	private long LOGINFLAG;
	private Map<String, Object> mapData;
	private static long CLICKLOGINCURRENTTIME;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		LOGINFLAG = 0;
		handler = new Handler(Looper.getMainLooper());

		etName = (EditText) findViewById(R.id.username_edit);
		etPass = (EditText) findViewById(R.id.password_edit);
		bnLogin = (Button) findViewById(R.id.signin_button);
		tvRegister = (TextView) findViewById(R.id.register_link);

		// 自动登陆
		bnLogin.setOnClickListener(this);
		tvRegister.setOnClickListener(this);

	}

	private boolean validate() {
		String username = etName.getText().toString().trim();
		String pwd = etPass.getText().toString().trim();
		if (username.equals("")) {
			DialogUtil.showDialog(this, "用户账户是必填项！", false);
			return false;
		}
		if (pwd.equals("")) {
			DialogUtil.showDialog(this, "用户口令是必填项！", false);
			return false;
		}

		return true;

	}

	private boolean loginPro() {
		showLoading("登陆中。。。");
		// SetProgress(true);
		String username = etName.getText().toString();
		String pwd = etPass.getText().toString();
		isclick = true;
		JSONObject jsonObj;
		try {

			query(username, pwd);

			if (!CommonFunction.isEmptyOrNullStr(Common.getInstance().UserStr)) {
				jsonObj = new JSONObject(Common.getInstance().UserStr);

				if (jsonObj.getInt("user_id") != 0) {//
					JsonObjToUser(jsonObj);
					CommonFunction.log("user ", Common.getInstance().user);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return true;
	}

	private void changeToJson() {
		JSONObject jsonObj;
		try {

			jsonObj = new JSONObject(Common.getInstance().UserStr);

			if (jsonObj.getInt("user_id") != 0) {//
				JsonObjToUser(jsonObj);
				CommonFunction.log("user ", Common.getInstance().user);
				startNextActivity();
			} else {
				Toast.makeText(this, "输入正确账号密码", Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void JsonObjToUser(JSONObject jsonObj) {
		UserInfo user = Common.getInstance().user;
		try {
			user.setUser_id(jsonObj.optInt("user_id"));
			user.setAddress(jsonObj.optString("address"));
			user.setMobile(jsonObj.optString("mobile"));
			user.setUsername(jsonObj.optString("username"));
			user.setImg_url(jsonObj.optString("img_url"));
		} catch (Exception e) {
			// TODO: handle exception
			CommonFunction.log(e);
		}

	}

	private void query(final String username, final String pwd) {
		// if( Constants.DEBUG)
		// {
		// LightTimer lt = new LightTimer() {
		//
		// @Override
		// public void run(LightTimer timer) {
		// // TODO Auto-generated method stub
		// LOGINFLAG = UserHttpProtocol.Login(Login.this, Login.this, username,
		// pwd);
		// }
		// };
		// lt.startTimerDelay(3000);
		// }else{
		LOGINFLAG = UserHttpProtocol.Login(this, this, username, pwd);
		// }

	}

	public void startNextActivity() {
		Intent intent = new Intent();
		intent.setClass(Login.this, Main.class);
		startActivity(intent);
	}

	private void SetProgress(boolean isShowBar) {
		if (progressBar == null) {
			progressBar = (ProgressBar) findViewById(R.id.progressBar);
		}
		progressBar.setFocusable(true);
		progressBar.setVisibility(View.GONE);
		if (isShowBar) {
			progressBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onGeneralSuccess(String result, long flag) {
		// TODO Auto-generated method stub
		Log.d("TAG", result);
		closeLoading();
		mapData = JsonUtil.jsonToMap(result);
		Common.getInstance().UserStr = result;
		changeToJson();
	}

	@Override
	public void onGeneralError(String e, long flag) {
		// TODO Auto-generated method stub
		closeLoading();
		CommonFunction.Toast(getApplicationContext(), "登陆异常，请重新登陆...");
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.signin_button:
			long i1 = System.currentTimeMillis();
			int temp = (int) (System.currentTimeMillis() / 1000 - CLICKLOGINCURRENTTIME);
			System.out.println(i1 + " " + CLICKLOGINCURRENTTIME);
			if (temp > 10) {
				CLICKLOGINCURRENTTIME = System.currentTimeMillis() / 1000;
				if (validate()) { // 验证是否为空
					loginPro();
				}
			} else {
				CommonFunction.Toast(getApplicationContext(), "操作太过频繁，请稍后...");
			}
			break;
		case R.id.register_link:
			Intent intent = new Intent(Login.this, Register.class);
			startActivity(intent);
			break;
		default:
			break;
		}
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
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(),
						"再按一次退出程序", Toast.LENGTH_SHORT)
						.show();
				exitTime = System.currentTimeMillis();
			} else {
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
