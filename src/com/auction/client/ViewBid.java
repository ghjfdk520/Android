package com.auction.client;

import com.auction.client.util.HttpUtil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;

public class ViewBid extends Activity{

	Button bnHome;
	ListView succList;
	TextView viewTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bnHome = (Button) findViewById(R.id.bn_home);
		succList = (ListView) findViewById(R.id.succList);
		viewTitle= (TextView) findViewById(R.id.view_title);
		
		bnHome.setOnClickListener(new FinishListener(this));
		String action = getIntent().getStringExtra("action");
	 
	}

}
