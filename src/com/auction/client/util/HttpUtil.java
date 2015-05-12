package com.auction.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpUtil {

	public static HttpClient httpClient = new DefaultHttpClient();
	
	public static String getRequest(String url){
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(get);
			if(httpResponse.getStatusLine().getStatusCode()==200){
				String result = EntityUtils.toString(httpResponse.getEntity());
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
			
	public static String postRequest(String url,Map<String,String> rawParams){
	 
		HttpPost post = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for(String key:rawParams.keySet()){
			params.add(new BasicNameValuePair(key, rawParams.get(key)));
		}
		try {
		post.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
		
			HttpResponse httpResponse = httpClient.execute(post);
			if(httpResponse.getStatusLine().getStatusCode()==200){
				String result = EntityUtils.toString(httpResponse.getEntity());
			    return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		 
		return null;	
	}

}
