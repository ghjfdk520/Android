package com.auction.connector;

/**
 * �ϴ�ͼƬ�ص�
 * 
 */
public interface UploadFileCallback
{
	/**
	 * �ϴ�����
	 * 
	 * @param lengthOfUploaded
	 *            �Ѿ��ϴ��ĳ���
	 
	 * @param flag
	 */
	public void onUploadFileProgress( int lengthOfUploaded , long flag );
	
	/**
	 * �ϴ�����
	 * 
	 * @param flag
	 * @param result
	 */
	public void onUploadFileFinish( long flag , String result );
	
	/**
	 * �ϴ�����
	 * 
	 * @param e
	 * @param flag
	 */
	public void onUploadFileError( String e , long flag );
}