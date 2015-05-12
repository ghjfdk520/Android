package com.auction.receiver;

import java.util.ArrayList;
import java.util.List;

import com.auction.client.entity.Item;
import com.auction.client.util.CommonFunction;
import com.auction.database.SharedPreferenceUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{
	private String alarmStr;
    private List<Item> list = new ArrayList<Item>();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 System.out.println("miyou ");
		alarmStr = SharedPreferenceUtil.getInstance(context).getString(SharedPreferenceUtil.ALARM_ITEM);
	    CommonFunction.ToList(alarmStr, list);
       
        Toast.makeText(context, "sdflkj", Toast.LENGTH_SHORT);
	}

}
