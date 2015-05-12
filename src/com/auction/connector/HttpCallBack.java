package com.auction.connector;

public interface HttpCallBack
{
	public void onGeneralSuccess( String result , long flag );
	
	public void onGeneralError( String e , long flag );
}

