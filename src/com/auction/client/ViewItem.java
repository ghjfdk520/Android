package com.auction.client;

import org.json.JSONArray;
import org.json.JSONObject;

import com.auction.client.util.DialogUtil;
import com.auction.client.util.HttpUtil;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class ViewItem extends Activity {
	Button bnHome;
	ListView succList;
	TextView viewTitle;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_item);
		bnHome = (Button) findViewById(R.id.bn_home);
		viewTitle = (TextView) findViewById(R.id.view_title);
		succList = (ListView) findViewById(R.id.succList);

		bnHome.setOnClickListener(new FinishListener(this));
		String action = getIntent().getStringExtra("action");
		 
		if (action.equals("viewFail.jsp")) {
			viewTitle.setText(R.string.view_fail); 
		}

		try {
			//
//			JSONArray jsonArray = new JSONArray(HttpUtil.getRequest(url));
//			JSONArrayAdapter adapter = new JSONArrayAdapter(this, jsonArray,
//					"name", true);
//			succList.setAdapter(adapter);
		} catch (Exception e) {
			// TODO: handle exception
			DialogUtil.showDialog(this, "服务器响应异常", false);
			e.printStackTrace();
		}
		succList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				 viewItemDetail(arg2);
			}
		});
	}

	private void viewItemDetail(int position) {
		// 加载detail.xml界面布局代表的视图
		View detailView = getLayoutInflater().inflate(R.layout.detail, null);
		EditText itemName = (EditText) detailView.findViewById(R.id.itemName);
		EditText itemKind = (EditText) detailView.findViewById(R.id.itemKind);
		EditText maxPrice = (EditText) detailView.findViewById(R.id.maxPrice);
		EditText itemRemark = (EditText) detailView
				.findViewById(R.id.itemRemark);
		JSONObject jsonObj = (JSONObject) succList.getAdapter().getItem(
				position);

		try {

			// 通过文本框显示物品详情
			itemName.setText(jsonObj.getString("name"));
			itemKind.setText(jsonObj.getString("kind"));
			maxPrice.setText(jsonObj.getString("maxPrice"));
			itemRemark.setText(jsonObj.getString("desc"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//DialogUtil.showDialog(ViewItem.this, detailView);
	}
}
