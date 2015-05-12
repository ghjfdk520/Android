package com.auction.client;

import org.json.JSONArray;

import com.auction.client.util.DialogUtil;
import com.auction.client.util.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.*;
import android.widget.*;

public class ManageKind extends Activity{
	Button bnHome , bnAdd;
	ListView kindList;
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_kind);
		bnHome= (Button) findViewById(R.id.bn_home);
	    bnAdd = (Button) findViewById(R.id.bnAdd);
	    kindList = (ListView) findViewById(R.id.kindList);
	    
	    bnHome.setOnClickListener(new FinishListener(this));
		bnAdd.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View source)
			{
				// 启动AddKind Activity。
				Intent intent = new Intent(ManageKind.this, AddKind.class);
				startActivity(intent);
				
			}
		});
		
	 		try {
//			final JSONArray jsonArray =new JSONArray(HttpUtil.getRequest(com.));
//	        kindList.setAdapter(new KindArrayAdapter(jsonArray, ManageKind.this));
			
		} catch (Exception e) {
			// TODO: handle exception
			DialogUtil.showDialog(this , "服务器响应异常，请稍后再试！" ,false);
			e.printStackTrace();
		}
	}

}