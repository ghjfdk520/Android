package com.auction.connector.protocol;

import java.util.LinkedHashMap;

import org.json.JSONObject;

import com.auction.client.entity.UserInfo;
import com.auction.client.util.CommonFunction;
import com.auction.config.Common;
import com.auction.connector.ConnectorManage;
import com.auction.connector.HttpCallBack;
import com.google.gson.Gson;

import android.content.Context;

public class UserHttpProtocol {
	public static long UserPost(Context context, String url,
			LinkedHashMap<String, Object> map, HttpCallBack callback) {
		return ConnectorManage.getInstance(context).Post(url, map, callback);
	}

	public static long Login(Context context, HttpCallBack callback,
			String mobile, String userpass) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("mobile", mobile);
		map.put("userpass", userpass);
		String url = Common.getInstance().loginUrl;
		return UserPost(context, url, map, callback);
	}

	public static long register(Context context, HttpCallBack callback,
			String mobile, String userpass,String address) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("mobile", mobile);
		map.put("userpass", userpass);
        map.put("address", address);
		String url = Common.getInstance().registerUrl;
		CommonFunction.ToJsonStr(map);
		return UserPost(context, url, map, callback);
	}

	public static long updateUser(Context context, HttpCallBack callback) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Gson gson = new Gson();
		UserInfo user = Common.getInstance().user;
		JSONObject json =null;
		try {
			 json = new JSONObject(gson.toJson(user));
             json.remove("imgIcon");
		} catch (Exception e) {
			// TODO: handle exception
		}
		map.put("inputStr",json.toString());
		String url = Common.getInstance().updateUser;
		return UserPost(context, url, map, callback);

	}
}
