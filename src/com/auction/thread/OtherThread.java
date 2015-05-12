package com.auction.thread;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

import com.auction.client.util.CommonFunction;
import com.auction.connector.FormFile;
import com.auction.connector.SocketHttpRequester;

/**
 * @author asus
 *
 */
public class OtherThread extends Thread {

	private String url;
	private static LinkedList<Bitmap> bitmapList;
	private Map<String, String> map;

	public OtherThread(String url, LinkedList<Bitmap> bitmapList,
			Map<String, String> map) {
		// TODO Auto-generated constructor stub
		this.url = url;
		this.bitmapList = bitmapList;
		this.map = map;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(bitmapList.size() + "   linklist");
		synchronized (bitmapList) {
			for (Bitmap bitmap : bitmapList) {
				System.out.println(bitmapList.size() + "   linklist");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				byte[] b = stream.toByteArray();

				try {
					FormFile formfile = new FormFile("file",
							stream.toByteArray(), "file",
							"application/octet-stream");
					SocketHttpRequester.post(url, map, formfile);

				} catch (Exception e) {
					// TODO: handle exception
					CommonFunction.log(e);
				} finally {
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

	}
}
