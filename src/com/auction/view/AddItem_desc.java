package com.auction.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.auction.client.Main;
import com.auction.client.R;
import com.auction.client.entity.Additem_entity;
import com.auction.client.fragment.MenuFragment;
import com.auction.client.util.CommonFunction;
import com.auction.config.Common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItem_desc extends Activity{
	private RequestQueue rQueue;
    private EditText item_name,item_desc,init_price,fiexd_price;
    private Button submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additem_desc);
		init();
		CommonFunction.log("sdfd", item_name.getText().toString());
	}

	
	private void init(){
		item_name = (EditText) findViewById(R.id.item_name);
		item_desc = (EditText) findViewById(R.id.item_desc);
		init_price = (EditText) findViewById(R.id.init_price);
		fiexd_price = (EditText) findViewById(R.id.fiexd_price);	
		rQueue = Volley.newRequestQueue(this);
	}
	public void submit(View view){
		Additem_entity additem = Common.getInstance().additem;
		additem.setOwner_id(Common.getInstance().user.getUser_id());
		additem.setItem_name(item_name.getText().toString()!=null?item_name.getText().toString():null);
		additem.setItem_desc(item_desc.getText().toString()!=null?item_desc.getText().toString():null);
		if(!init_price.getText().toString().equals("")){
		additem.setInit_price(Integer.parseInt(init_price.getText().toString()));
        additem.setMax_price(Integer.parseInt(init_price.getText().toString()));
		}else{
			Toast.makeText(this, "«Î ‰»Îº€∏Ò", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!fiexd_price.getText().toString().equals(""))
		additem.setFiexd_price(Integer.parseInt(fiexd_price.getText().toString()));
		final String s = CommonFunction.ItemToJson(additem);
		CommonFunction.log("sdf", s);
		new Thread(
		 new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				update(s);
			}
		}		
		).start();
		
		 
		 Intent it = new Intent();  
         setResult(Activity.RESULT_OK, it);  
         finish();
	}
	
	private void update(final String inputStr) {
		 
		String url = Common.getInstance().itemUpdate;
		final ArrayList<String> arr = new ArrayList<String>();
		StringRequest stringRequest = new StringRequest(Method.POST,url,  
                new Response.Listener<String>() {  
                    @Override  
                    public void onResponse(String response) {  
                        Log.d("TAG", response); 
                    }  
                }, new Response.ErrorListener() {  
                    @Override  
                    public void onErrorResponse(VolleyError error) {  
                        Log.e("TAG", error.getMessage(), error);  
                    }  
                }){

					@Override
					protected Map<String, String> getParams()
							throws AuthFailureError {
						// TODO Auto-generated method stub
						Map<String, String> map = new HashMap<String, String>();
						map.put("inputStr", inputStr);
					    return map;  
					}
			
		};  
		rQueue.add(stringRequest);
	}
}
