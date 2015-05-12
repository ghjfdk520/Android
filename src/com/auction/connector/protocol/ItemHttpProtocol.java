package com.auction.connector.protocol;

import java.util.LinkedHashMap;

import android.content.Context;
import android.provider.SyncStateContract.Constants;

import com.auction.client.entity.Additem_entity;
import com.auction.client.entity.Item;
import com.auction.client.util.CommonFunction;
import com.auction.config.Common;
import com.auction.connector.ConnectorManage;
import com.auction.connector.HttpCallBack;

public class ItemHttpProtocol{
	public static long itemPost( Context context , String url ,
			LinkedHashMap< String , Object > map , HttpCallBack callback )
	{
		return ConnectorManage.getInstance( context ).Post( url , map , callback );
	}
	
	//��ȡ��������Ʒ�б�
	public static long getItemList(Context context,HttpCallBack callback,int kind_id){
		LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
		entity.put( "kind_id" , kind_id);
		CommonFunction.ToJsonStr(entity); 
		return itemPost(context, Common.getInstance().PULLITEM,entity, callback);
	}
	
	//��������Ʒ
	public static long selectItem(Context context,HttpCallBack callback,String keyword){
		LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
		entity.put( "keyword" , keyword);
		CommonFunction.ToJsonStr(entity); 
		return itemPost(context, Common.getInstance().SELECTITEM,entity, callback);
	}
	//��ȡ��Ʒ�б�
	public static long getItem(Context context,HttpCallBack callback,int item_id){
		LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
		entity.put( "item_id" , item_id);
		CommonFunction.ToJsonStr(entity); 
		return itemPost(context, Common.getInstance().FINDITEM,entity, callback);
	}
	//��ȡ����ƷͼƬ��Ϣ
	public static long getOtherUrl(Context context,HttpCallBack callback,int item_id){
		LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
		entity.put( "item_id" , item_id);
		CommonFunction.ToJsonStr(entity); 
		return itemPost(context, Common.getInstance().OTHERURL,entity, callback);
	}
	
	//������Ʒ
	public static long bidItem(Context context,HttpCallBack callback,int item_id,int winer_id,int price){
		LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
		entity.put( "item_id" , item_id);
		entity.put("max_price", price);
		entity.put("winer_id",winer_id);
		CommonFunction.ToJsonStr(entity); 
		return itemPost(context, Common.getInstance().BIDITEM,entity, callback);
	}
	
	//���ļ�¼
	public static long bidRecord(Context context,HttpCallBack callback,int item_id ){
		LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
		entity.put( "item_id" , item_id);
		CommonFunction.ToJsonStr(entity); 
		return itemPost(context, Common.getInstance().GETITEMBID,entity, callback);
	}
	
	//�������Ʒ
	public static long addItem(Context context,HttpCallBack callback,Item item){
		LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
		entity.put( "item_name" , item.getItem_name());
		entity.put("init_price", item.getInit_price());
		entity.put("item_desc",item.getItem_desc());
		entity.put("item_img",item.getItem_img());
        entity.put("endtime", item.getEndtime());
        entity.put("kind_id", item.getKind_id());
        entity.put("owner_id", item.getOwner_id());
		CommonFunction.ToJsonStr(entity); 
		return itemPost(context, Common.getInstance().ADDITEM,entity, callback);
	}
	
	//��ȡ�û�����Ʒ
	public static long getUserItem(Context context,HttpCallBack callback){
		LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
		entity.put( "user_id" , Common.getInstance().user.getUser_id());
		CommonFunction.ToJsonStr(entity); 
		return itemPost(context, Common.getInstance().FINDUSERITEM,entity, callback);
	}
	
	//��ȡ�û�������¼
	public static long getUserBid(Context context,HttpCallBack callback,int record){
		LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
		entity.put( "user_id" , Common.getInstance().user.getUser_id());
		entity.put("record", record);
		CommonFunction.ToJsonStr(entity); 
		return itemPost(context, Common.getInstance().GETUSERBID,entity, callback);
	}
	
	
	//��ȡ�û���ϵ��ʽ
	public static long getUser(Context context,HttpCallBack callback,int user_id){
		LinkedHashMap< String , Object > entity = new LinkedHashMap< String , Object >( );
		entity.put( "user_id" , user_id);
		CommonFunction.ToJsonStr(entity); 
		return itemPost(context, Common.getInstance().GETUSER,entity, callback);
	}
}
