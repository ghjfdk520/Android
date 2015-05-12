package com.auction.connector;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.auction.client.util.CommonFunction;

import android.content.Context;

public class UploadFile implements Runnable {

	private long flag;
	private InputStream is;
	private String filename;
	private String inputname ="file";
	private Map<String, String> map;
	private UploadFileCallback callback;
	private String url;

	public UploadFile(Context context, InputStream is, String filename,
			String url, Map<String, String> map, UploadFileCallback _callback,
			long flag) {
		// TODO Auto-generated constructor stub
		try {
			this.url = url;
			this.is = is;
			this.flag = flag;
			this.filename = filename;
			this.callback = _callback;
			this.map = map;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try
		{
		OpenHttpRequest httpRequest = OpenHttpRequest.post(url);
		for(Map.Entry<String,String> entry:map.entrySet()){
			httpRequest.part(entry.getKey(), entry.getValue());
		}
		httpRequest.part(inputname, filename,"image/jpeg",is);
		int statusCode = httpRequest.code();
		if(statusCode == 200){
			String result = httpRequest.body();
			callback.onUploadFileFinish(flag, result);
			CommonFunction.log( "***********" + flag , result );
		}else{
			callback.onUploadFileError( "" + statusCode , this.flag );
		}
		
	} catch ( Exception e )
	{
		e.printStackTrace( );
		// 回调上传出错信息
		callback.onUploadFileError( e.getMessage( ) , this.flag );
	}finally
	{
		if ( is != null )
		{
			try
			{
				is.close( );
			}
			catch ( IOException e )
			{
				e.printStackTrace( );
			}
		}
	}
	}
}
