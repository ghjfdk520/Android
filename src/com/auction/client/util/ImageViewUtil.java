package com.auction.client.util;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImageViewUtil {
  
	private static ImageViewUtil sDefaultInstance;
	private static final String BITMAP_DISKCACHE_DIR = "/cacheimage_new/";
	private static ImageLoaderConfiguration config;
	private ImageLoadingListener animateFirstListener;
	private ImageViewUtil( Context context )
	{
		File cacheDir = StorageUtils.getCacheDirectory(context);  //�����ļ���·��
     	config = new ImageLoaderConfiguration.Builder(context)
		.memoryCacheExtraOptions(480, 800)
		.diskCacheExtraOptions(480, 800, null)
		.threadPoolSize(3) // default  �̳߳��ڼ��ص�����
        .threadPriority(Thread.NORM_PRIORITY - 2) // default ���õ�ǰ�̵߳����ȼ�
        .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //����ͨ���Լ����ڴ滺��ʵ��
		.diskCache(new UnlimitedDiscCache(cacheDir)) // default �����Զ��建��·��  
		.build();
     	
     	ImageLoader.getInstance().init(config);
	}
	
	public synchronized static ImageViewUtil initDefault( Context context )
	{
		if ( sDefaultInstance == null )
		{
			sDefaultInstance = new ImageViewUtil( context );
		}
		
		return sDefaultInstance;
	}
	
	public synchronized static ImageViewUtil getDefault( )
	{
		if ( sDefaultInstance == null )
		{
			throw new RuntimeException( "Must be call initDefault(Context) befor!" );
		}
		
		return sDefaultInstance;
	}
	
	//�������
	public void clearDefaultLoaderMemoryCache(){
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
	}
	
	//���浽˫�������ͼƬ
	public void loadImage(String imageUri , ImageView imageView , int stubImageRes ,
			int loadFailImageRes , ImageLoadingListener listener){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
	    .showImageOnLoading(stubImageRes) // ����ͼƬ�����ڼ���ʾ��ͼƬ
	    //.showImageForEmptyUri(R.drawable.ic_empty) // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
	    .showImageOnFail(loadFailImageRes)
		.cacheInMemory(true) // default  �������ص�ͼƬ�Ƿ񻺴����ڴ���
        .cacheOnDisk(true)
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
		.build();
		 displayImage(imageUri, imageView, options, listener, null);
	}
	/**
	 * ���浽˫�������ʽ����һ��ͼƬ��������ConvertView�У�������ImageView��
	 * 
	 * @param imageUri
	 * @param image
	 * @param stubImageRes
	 * @param loadFailImageRes
	 */
	public void loadImageInConvertView( String imageUri , ImageView imageView ,
			int stubImageRes , int loadFailImageRes , ImageLoadingListener listener )
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading(true)
		 .showImageOnLoading(stubImageRes) // ����ͼƬ�����ڼ���ʾ��ͼƬ
	    //.showImageForEmptyUri(R.drawable.ic_empty) // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
	    .showImageOnFail(loadFailImageRes)
		.cacheInMemory(true) // default  �������ص�ͼƬ�Ƿ񻺴����ڴ���
        .cacheOnDisk(true)
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
		.build();
		 displayImage(imageUri, imageView, options, listener, null);
	}
	
	/**
	 * Բ��ͼƬ
	 * @param uri
	 * @param imageView
	 * @param options
	 * @param listener
	 * @param progressListener
	 */
	public void fadeInRoundLoadImageInConvertView( String imageUri , ImageView imageView ,
			int stubImageRes , int loadFailImageRes , ImageLoadingListener listener ,
			int roundPix ){
		 
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading(true)
		 .showImageOnLoading(stubImageRes) // ����ͼƬ�����ڼ���ʾ��ͼƬ
	    //.showImageForEmptyUri(R.drawable.ic_empty) // ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
	    .showImageOnFail(loadFailImageRes)
		.cacheInMemory(true) // default  �������ص�ͼƬ�Ƿ񻺴����ڴ���
        .cacheOnDisk(true)
        .displayer(new RoundedBitmapDisplayer(roundPix))
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
		.build();
		
		 displayImage(imageUri, imageView, options, listener, null);
	}
	
	public void loadImage( String imageUri , ImageView imageView , int stubImageRes ,
			int loadFailImageRes )
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showImageOnLoading(stubImageRes) // ����ͼƬ�����ڼ���ʾ��ͼƬ
        .showImageOnFail(loadFailImageRes) // ����ͼƬ���ػ��������з���������ʾ��ͼƬ
        .resetViewBeforeLoading(true)  // default ����ͼƬ�ڼ���ǰ�Ƿ����á���λ
        .cacheInMemory(false) // default  �������ص�ͼƬ�Ƿ񻺴����ڴ���
        .cacheOnDisk(false) // default  �������ص�ͼƬ�Ƿ񻺴���SD����
        .considerExifParams(false) // default
        .build();	
		
	    displayImage(imageUri, imageView,options,null,null);
	}
	public void displayImage(String uri, ImageView imageView, DisplayImageOptions options,
			ImageLoadingListener listener, ImageLoadingProgressListener progressListener){
		ImageLoader.getInstance().displayImage(uri, imageView, options, listener, progressListener);
	}
	
	
	
	
	/**
	 * ͼƬ���ص�һ����ʾ������
	 * 
	 * @author Administrator
	 * 
	 */
	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());
		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// �Ƿ��һ����ʾ
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// ͼƬ����Ч��
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}

	

	
 
