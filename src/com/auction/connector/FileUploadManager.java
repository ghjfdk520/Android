package com.auction.connector;

import java.io.InputStream;
import java.util.Map;

import android.content.Context;

public class FileUploadManager {
	public enum FileProfix {
		JPG("file.jpg"), PNG("file.png"), MP3("file.mp3"), MP4("file.mp4"), GP3(
				"file.3gp");
		private String name;

		private FileProfix(String profixName) {
			this.name = profixName;
		}

		public String getName() {
			return name;
		}
	}

	public static Thread createUploadTask(Context context, InputStream is,
			FileProfix fileProfix, String url, Map<String, String> map,
			UploadFileCallback callback, long flag) {
		String filename = fileProfix.getName();
		return createUploadTask(context, is, filename, url, map, callback, flag);
	}

	/** 创建一个上传文件的线程 ,需要自己启动线程.start() */
	public static Thread createUploadTask(Context context, InputStream is,
			String filename, String url, Map<String, String> map,
			UploadFileCallback _callback, long flag) {
		Runnable uploadFileRun = null;
		try {
			uploadFileRun = new UploadFile(context, is, filename, url, map,
					_callback, flag);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new Thread(uploadFileRun);
	}

}
