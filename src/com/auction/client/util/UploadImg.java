package com.auction.client.util;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import com.auction.connector.FormFile;
import com.auction.connector.SocketHttpRequester;

import android.graphics.Bitmap;

public class UploadImg extends Thread {
	private String url;
	private Bitmap bitmap;
	private Map<String,String> map;
	public UploadImg(String url, Bitmap bit,Map<String,String> map) {
		// TODO Auto-generated constructor stub
		this.url =url;
		this.bitmap = bit;
		this.map = map;
	}

	public static void upload(String url, Bitmap bit) {

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte [] b = stream.toByteArray();
		 
			try {
				FormFile formfile = new FormFile("file", stream.toByteArray(), "file", "application/octet-stream");
				SocketHttpRequester.post(url,map,formfile);
				
			} catch (Exception e) {
				// TODO: handle exception
				CommonFunction.log(e);
			}finally{
				if (stream != null) {
					try {
						stream.close();
						stream = null;
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
		
		}
		
	}
	
}
