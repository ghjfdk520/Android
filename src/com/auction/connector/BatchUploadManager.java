package com.auction.connector;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.auction.client.util.CommonFunction;
import com.auction.config.Common;
import com.auction.connector.FileUploadManager.FileProfix;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.LongSparseArray;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class BatchUploadManager implements UploadFileCallback {

	private Context mContext;
	private BatchUploadCallBack callback;

	// 记录图片任务flag以及对应的图片的list
	protected LongSparseArray<ArrayList<String>> taskArray = new LongSparseArray<ArrayList<String>>();
	// 记录传进来任务的falg和对应图片任务flagflag
	protected LongSparseArray<Long> imageFlagArray = new LongSparseArray<Long>();

	// 因为最多只能发6张图片,所以用3位就足够,[0111] 0~7,第一张图片为0；
	protected long IMAGE_TASK_MASK = Long.MAX_VALUE ^ 7;// 值为[111111......000]
	protected long IMAGE_POSITION_MASK = 7;// Image对应的位置的Mask
											// 值为[000000......111]

	public BatchUploadManager(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public void uploadImage(long flag, ArrayList<String> photoList,
			int uploadType, BatchUploadCallBack callback) {
		try {
			this.callback = callback;
			long imageTaskFlag = flag & IMAGE_TASK_MASK;
			imageFlagArray.put(flag, imageTaskFlag);
			int imageCount = photoList.size();
			ArrayList<String> imageList = new ArrayList<String>(imageCount);
			for (int i = 0; i < imageCount; i++) {
				String imageUrl = photoList.get(i);
				imageList.add(imageUrl);
			}
			taskArray.put(imageTaskFlag, imageList);
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", String.valueOf(uploadType));
			for (int i = 0; i < imageCount; i++) {
				long imagePostionFlag = imageTaskFlag + i;
				String imageUrl = photoList.get(i);
				map.put("imageFileName", imageUrl);
				File uploadFile = new File(imageUrl);
				
				String compressPath = compressImage(imageUrl);
				if (!TextUtils.isEmpty(compressPath)) {
					uploadFile = new File(compressPath);
				}
				FileInputStream fis = new FileInputStream(uploadFile);
				if (!uploadFile.exists()) {
					fis.close();
					callback.batchUploadFail(flag);
					return;
				}
				FileUploadManager.createUploadTask(mContext, fis,
						FileProfix.JPG, Common.getInstance().Imgupload, map,
						this, imagePostionFlag).start();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	protected String compressImage(String sourcePath) {
		File uploadFile = new File(sourcePath);
		String tmpFilePath = "";
		try {
			if (uploadFile.exists()) {
				tmpFilePath = CommonFunction.getSDPath() + "/proPics/"
						+ CommonFunction.md5(sourcePath);
				File tmpFile = new File(tmpFilePath);
				Bitmap bitmap = BitmapFactory.decodeFile(uploadFile
						.getAbsolutePath());
				FileOutputStream fos = new FileOutputStream(tmpFile);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				bitmap.compress(CompressFormat.JPEG, 50, bos);
				bitmap.recycle();
				bos.flush();
				fos.close();
				bos.close();
			} else {
				return tmpFilePath;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return tmpFilePath;

	}

	@Override
	public void onUploadFileProgress(int lengthOfUploaded, long flag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUploadFileFinish(long flag, String result) {
		// TODO Auto-generated method stub

		long imageTaskFlag = flag & IMAGE_TASK_MASK;
		int imagePosition = (int) (flag & IMAGE_POSITION_MASK);
		ArrayList<String> imageList = taskArray.get(imageTaskFlag);

		if (imageList != null) {
			int count = imageList.size();
			if (imagePosition < count) {
				imageList.set(imagePosition, result);
			}
		}
		int j= 0;
		for (String s : imageList) {
			System.out.println("imgurl:" + s);
			if (s.contains("/")) {
				j = 1;
			}
		}
		if (j == 0) {
			int taskFlagValueIndex = imageFlagArray.indexOfValue(imageTaskFlag);

			for (int i = 0; i < imageFlagArray.size(); i++) {
				if (imageFlagArray.valueAt(i).longValue() == imageTaskFlag) {
					taskFlagValueIndex = i;
					break;
				}
			}
			if (taskFlagValueIndex < 0) {
				throw new ArrayIndexOutOfBoundsException("找不到对应的图片任务");
			} else {
				long taskFlag = imageFlagArray.keyAt(taskFlagValueIndex);// 通过图片任务的flag,得到传进来的flag
				callback.batchUploadSuccess(taskFlag,
						taskArray.get(imageTaskFlag));
			}
		}
	}

	@Override
	public void onUploadFileError(String e, long flag) {
		// TODO Auto-generated method stub

	}

	public interface BatchUploadCallBack {
		/**
		 * 批量上传成功 serverUrlList: 服务端下发的url
		 * */
		public void batchUploadSuccess(long taskFlag,
				ArrayList<String> serverUrlList);

		/**
		 * 批量上传失败 taskFlag上传任务的flag
		 * */
		public void batchUploadFail(long taskFlag);
	}

}
