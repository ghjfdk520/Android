package com.auction.connector;

/**
 * ����ͨ�Żص��ӿ�
 * 
 * @author Heart
 * @date 2015��5��6��
 */
public interface CallBackNetwork {
	// socket��ʽ
//	public void onReceiveMessage(TransportMessage message);

	public void onErrorMessage(int e);

	public void onSendCallBack(int e, long flag);

	public void onConnected();
}
