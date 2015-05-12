package com.auction.connector;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.auction.client.util.CommonFunction;
import com.auction.client.util.LightTimer;
import com.auction.config.Common;
import com.auction.config.ErrorCode;

import android.content.Context;
import android.util.Log;

public class ConnectorManage {
	private AtomicLong mHttpCount = new AtomicLong(0);
	private final String TAG = ConnectorManage.class.getName();
	private RequestQueue rQueue;
	private Context mContext;
	private static ConnectorManage sInstance;

	private ConnectorManage(Context context) {
		mContext = context;
		rQueue = Volley.newRequestQueue(context);
	}

	public static ConnectorManage getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new ConnectorManage(context);
		}
		return sInstance;
	}

	public long Post(final String url, final Map map,
		final HttpCallBack callback) {
		final long flag = mHttpCount.incrementAndGet();
		CommonFunction.log("request  "+flag, url+" "+map.toString());
		StringRequest stringRequest = new StringRequest(Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
                        CommonFunction.log("RESULT  "+flag, response);
						if (response == null) {
							if (callback != null) {
								callback.onGeneralError(response, flag);
							}
						} else {
							if (callback != null) {
								callback.onGeneralSuccess(response, flag);
							}
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if(error==null || !error.getMessage().equals("")){
							CommonFunction.Toast(mContext, error.getMessage());
						}
						CommonFunction.log("RESULT Error", error.getMessage());
						if (callback != null) {
							callback.onGeneralError(error.getMessage(), flag);
						}
					
						
					}
				}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// TODO Auto-generated method stub
				return map;
			}

		};
		
        final Request r = rQueue.add(stringRequest);
  		return flag;
	}

	public void asynPost(final String url, final Map map,
			final HttpCallBack callback) {
		final long flag = mHttpCount.incrementAndGet();
		StringRequest stringRequest = new StringRequest(Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", response);

						if (response == null) {
							if (callback != null) {
								callback.onGeneralError(response, flag);
							}
						} else {
							if (callback != null) {
								callback.onGeneralSuccess(response, flag);
							}
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
					}
				}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// TODO Auto-generated method stub
				return map;
			}

		};
		rQueue.add(stringRequest);
	}
}
