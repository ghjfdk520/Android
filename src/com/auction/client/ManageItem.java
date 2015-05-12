package com.auction.client;

import com.auction.client.util.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class ManageItem extends Activity{
	Button bnHome, bnAdd;
	ListView itemList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_item);
		
		bnHome = (Button) findViewById(R.id.bn_home);
		bnAdd = (Button) findViewById(R.id.bnAdd);
		itemList = (ListView) findViewById(R.id.itemList);
		
		bnHome.setOnClickListener(new FinishListener(this));
		bnAdd.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View source)
			{
				// Æô¶¯AddItem Activity¡£
				Intent intent = new Intent(ManageItem.this, AddItem.class);
				startActivity(intent);
			}
		});
		
	 		
	}

	
}
