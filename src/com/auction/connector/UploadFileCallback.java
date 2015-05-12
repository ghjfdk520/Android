package com.auction.connector;

/**
 * 上传图片回调
 * 
 */
public interface UploadFileCallback
{
	/**
	 * 上传进度
	 * 
	 * @param lengthOfUploaded
	 *            已经上传的长度
	 
	 * @param flag
	 */
	public void onUploadFileProgress( int lengthOfUploaded , long flag );
	
	/**
	 * 上传结束
	 * 
	 * @param flag
	 * @param result
	 */
	public void onUploadFileFinish( long flag , String result );
	
	/**
	 * 上传错误
	 * 
	 * @param e
	 * @param flag
	 */
	public void onUploadFileError( String e , long flag );
}