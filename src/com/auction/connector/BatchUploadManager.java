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

	// ��¼ͼƬ����flag�Լ���Ӧ��ͼƬ��list
	protected LongSparseArray<ArrayList<String>> taskArray = new LongSparseArray<ArrayList<String>>();
	// ��¼�����������falg�Ͷ�ӦͼƬ����flagflag
	protected LongSparseArray<Long> imageFlagArray = new LongSparseArray<Long>();

	// ��Ϊ���ֻ�ܷ�6��ͼƬ,������3λ���㹻,[0111] 0~7,��һ��ͼƬΪ0��
	protected long IMAGE_TASK_MASK = Long.MAX_VALUE ^ 7;// ֵΪ[111111......000]
	protected long IMAGE_POSITION_MASK = 7;// Image��Ӧ��λ�õ�Mask
											// ֵΪ[000000......111]

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
				throw new ArrayIndexOutOfBoundsException("�Ҳ�����Ӧ��ͼƬ����");
			} else {
				long taskFlag = imageFlagArray.keyAt(taskFlagValueIndex);// ͨ��ͼƬ�����flag,�õ���������flag
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
		 * �����ϴ��ɹ� serverUrlList: ������·���url
		 * */
		public void batchUploadSuccess(long taskFlag,
				ArrayList<String> serverUrlList);

		/**
		 * �����ϴ�ʧ�� taskFlag�ϴ������flag
		 * */
		public void batchUploadFail(long taskFlag);
	}

}
