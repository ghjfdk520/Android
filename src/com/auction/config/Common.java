package com.auction.config;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.auction.client.entity.Item;
import com.auction.client.entity.UserInfo;
import com.auction.client.entity.Additem_entity;

public class Common {
  
	private static class SingleInstance{
		public static Common INSTANCE = new Common();
	}
	
	public static Common getInstance(){
		return SingleInstance.INSTANCE;
	}
	public Additem_entity additem = new Additem_entity();
	public static UserInfo user = new UserInfo();
	public List<Item> itemList = new LinkedList<Item>();
	public String BASE_UPLOAD="http://192.168.223.1:8080/Auction/upload/";
	public String BASE_URL= "http://192.168.223.1:8080/Auction/auction/";
	public String loginUrl = BASE_URL+"login.action";
	public String updateUserUrl=BASE_URL+"updateUser.action";
	public String updateUser =BASE_URL+"update.action"; //´«user bean
    public String registerUrl = BASE_URL+"register.action";
    public String uploadImg = BASE_URL+"uploadImg.action";
    public String Imgupload = BASE_URL+"ImgUpload.action"; //ÐÂ
    public String uploadOther = BASE_URL+"uploadOther.action";
    public String itemUpdate = BASE_URL+"itemUpdate.action";
    public String PULLITEM = BASE_URL+"pullList.action";
    public String OTHERURL = BASE_URL+"OtherUrl.action";
    public String BIDITEM = BASE_URL+"BidItem.action";
    public String GETRECORD =BASE_URL+"getRecord.action";
    public String FINDUSERITEM = BASE_URL+"findUserItem.action";
    public String GETITEMBID = BASE_URL+"getItemBid.action";
    public String DELITEM = BASE_URL +"delItem.action";
    public String FINDITEM = BASE_URL +"findItem.action";
    public String ADDITEM = BASE_URL+"addItem.action";
    public String SELECTITEM = BASE_URL+"selectItem.action";
    public String GETUSERBID = BASE_URL+ "getUserBid.action";
    public String GETUSER = BASE_URL+ "getUser.action";
    
    public String UserStr = new String();
    public String tempStr;
}
