package com.auction.client.entity;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Serializable{
	private int user_id;
    private String img_url;
    private String username;
    private String address;
    private  String mobile;
    private Bitmap imgIcon;
 
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Bitmap getImgIcon() {
		return imgIcon;
	}
	public void setImgIcon(Bitmap imgIcon) {
		this.imgIcon = imgIcon;
	}
	
	 
	public String toString(){
		return "user_id:"+" "+user_id+";img_url:"+img_url+";username:"+username+";address:"
		+address+";mobile:"+mobile;
	}
}
