package com.auction.connector;

/**
 * 网络通信回调接口
 * 
 * @author Heart
 * @date 2015年5月6日
 */
public interface CallBackNetwork {
	// socket方式
//	public void onReceiveMessage(TransportMessage message);

	public void onErrorMessage(int e);

	public void onSendCallBack(int e, long flag);

	public void onConnected();
}
