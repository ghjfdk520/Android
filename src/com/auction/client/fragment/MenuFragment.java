package com.auction.client.fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.auction.client.R;
import com.auction.client.entity.UserInfo;
import com.auction.client.util.CommonFunction;
import com.auction.common.Constants.State;
import com.auction.config.Common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuFragment extends Fragment implements OnClickListener {
	private RequestQueue rQueue;
	private View v;
	Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
                if (msg.what == 1) {
                	System.out.println("hhahah");
                	updateState();
                }
                super.handleMessage(msg);
        }
};
	private View menu_salesroom,menu_messages,menu_manager,menu_bid;
	private FragmentManager fragmentManager;
	private static TextView userNameView;
	private static ImageView userIconView;
    private static UserInfo user;
 

	
	private State state = State.SELLOFF;
	private Bundle bundle;
	 
	private ManagerBid mb;
	private UserInformation ui;
	private int oldClick = R.id.salesroom_ly;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (v == null) {
			v = inflater.inflate(R.layout.leftmenu, null);
		}
		fragmentManager = getActivity().getSupportFragmentManager();

		init();
		return v;

	}

	private void init() {
        user = Common.getInstance().user;
		ui = new UserInformation();
		mb = new ManagerBid();
		
		 
		userIconView = (ImageView) v.findViewById(R.id.userIconView);
		userNameView = (TextView) v.findViewById(R.id.userNameView);

		//headImg.setImageBitmap(Common.getInstance().user.getImgIcon());
		 
		downloadIcon(Common.getInstance().BASE_UPLOAD+Common.getInstance().user.getImg_url());
		
		
		userNameView.setText(user.getUsername());

		menu_salesroom = v.findViewById(R.id.salesroom_ly);
		menu_messages= v.findViewById(R.id.messages_ly);
		menu_manager= v.findViewById(R.id.manager_ly);
		menu_bid= v.findViewById(R.id.bid_ly);
		 
		menu_salesroom.setOnClickListener(this);
		menu_messages.setOnClickListener(this);
		menu_manager.setOnClickListener(this);
		menu_bid.setOnClickListener(this);
		userIconView.setOnClickListener(this);
	}
	private void downloadIcon(String url){
		rQueue =Volley.newRequestQueue(getActivity());
		ImageRequest imageRequest = new ImageRequest(url,  new Response.Listener<Bitmap>() {  
            @Override  
            public void onResponse(Bitmap response) { 
            	
            	int mRadius = Math.max(response.getWidth(),response.getHeight());  
            	Bitmap bitmap = CommonFunction.createCircleImage(response, response.getWidth(),response.getHeight());
            	setHeadImg(bitmap);
          

            	//userIconView.setBackgroundDrawable(new BitmapDrawable(bitmap));
//            	userIconView.setBackgroundDrawable(new BitmapDrawable(bitmap));
            	 
                 //userIconView.setImageBitmap(response);
                Common.getInstance().user.setImgIcon(response);
            }  
        }, 0, 0, Config.RGB_565,  new Response.ErrorListener() {  
            @Override  
            public void onErrorResponse(VolleyError error) {  
            	Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.bg_imageview_menu_heart_icon);
            	// bitmap =CommonFunction.createCircleImage(bit, bit.getWidth(),bit.getHeight());
            	setHeadImg(bit);
            	Common.getInstance().user.setImgIcon(bit);
              }
            } );
		rQueue.add(imageRequest);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == oldClick) {
			return;
		} else {
			oldClick = arg0.getId();
		}
		switch (arg0.getId()) {
		case R.id.salesroom_ly:
			state = State.SELLOFF;
			 
			break;
		case R.id.userIconView:
			state = State.INFORMATION;
			 
			break;
		case R.id.manager_ly:
			state = State.MANAGER;
			break;
		case R.id.bid_ly:
			state= State.BIDFRAGMENT;
			break;
		case R.id.messages_ly:
			state = State.ALARMFRAGMENT;
		default:
			break;
		}
		CommonFunction.TabFragment(fragmentManager, state);
	}

	public void showToast(int i) {
		Toast.makeText(getActivity(), i, Toast.LENGTH_SHORT).show();
	}

	private void OnTabSelected() {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		switch (state) {
		case SELLOFF:
 			transaction.replace(R.id.main, new Selloff()).commit();
			break;
		case INFORMATION:

			transaction.replace(R.id.main, ui).commit();
			ui.setArguments(bundle);
			break;
		case MANAGER:
			transaction.replace(R.id.main, mb).commit();
		default:
			break;
		}
	}

	public void setHeadImg(Bitmap bm) {
		userIconView.setImageBitmap(bm);
	}
	public static void changeImg(Bitmap bm){
		userIconView.setImageBitmap(bm);
	}
	public static void changName(String namestr){
		userNameView.setText(namestr);
	}
	public static void updateState(){
		//downloadIcon(Common.getInstance().BASE_UPLOAD+Common.getInstance().user.getImg_url());
		userNameView.setText(user.getUsername());
	}
}
