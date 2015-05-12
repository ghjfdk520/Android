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
		File cacheDir = StorageUtils.getCacheDirectory(context);  //缓存文件夹路径
     	config = new ImageLoaderConfiguration.Builder(context)
		.memoryCacheExtraOptions(480, 800)
		.diskCacheExtraOptions(480, 800, null)
		.threadPoolSize(3) // default  线程池内加载的数量
        .threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级
        .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
		.diskCache(new UnlimitedDiscCache(cacheDir)) // default 可以自定义缓存路径  
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
	
	//清除缓存
	public void clearDefaultLoaderMemoryCache(){
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
	}
	
	//保存到双缓存加载图片
	public void loadImage(String imageUri , ImageView imageView , int stubImageRes ,
			int loadFailImageRes , ImageLoadingListener listener){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
	    .showImageOnLoading(stubImageRes) // 设置图片下载期间显示的图片
	    //.showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
	    .showImageOnFail(loadFailImageRes)
		.cacheInMemory(true) // default  设置下载的图片是否缓存在内存中
        .cacheOnDisk(true)
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
		.build();
		 displayImage(imageUri, imageView, options, listener, null);
	}
	/**
	 * 保存到双缓存的形式加载一张图片，且用于ConvertView中（重置了ImageView）
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
		 .showImageOnLoading(stubImageRes) // 设置图片下载期间显示的图片
	    //.showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
	    .showImageOnFail(loadFailImageRes)
		.cacheInMemory(true) // default  设置下载的图片是否缓存在内存中
        .cacheOnDisk(true)
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
		.build();
		 displayImage(imageUri, imageView, options, listener, null);
	}
	
	/**
	 * 圆角图片
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
		 .showImageOnLoading(stubImageRes) // 设置图片下载期间显示的图片
	    //.showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
	    .showImageOnFail(loadFailImageRes)
		.cacheInMemory(true) // default  设置下载的图片是否缓存在内存中
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
        .showImageOnLoading(stubImageRes) // 设置图片下载期间显示的图片
        .showImageOnFail(loadFailImageRes) // 设置图片加载或解码过程中发生错误显示的图片
        .resetViewBeforeLoading(true)  // default 设置图片在加载前是否重置、复位
        .cacheInMemory(false) // default  设置下载的图片是否缓存在内存中
        .cacheOnDisk(false) // default  设置下载的图片是否缓存在SD卡中
        .considerExifParams(false) // default
        .build();	
		
	    displayImage(imageUri, imageView,options,null,null);
	}
	public void displayImage(String uri, ImageView imageView, DisplayImageOptions options,
			ImageLoadingListener listener, ImageLoadingProgressListener progressListener){
		ImageLoader.getInstance().displayImage(uri, imageView, options, listener, progressListener);
	}
	
	
	
	
	/**
	 * 图片加载第一次显示监听器
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
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}

	

	
 
