//package com.auction.connector;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FilterInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//
//
//
//import com.auction.client.util.CommonFunction;
//import com.auction.client.util.CryptoUtil;
//import com.auction.client.util.RoundPicture;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//
//public class DownLoadBitmap extends BaseHttp{
//
//	private String urlPath;
//	private int round;
//	
//	protected DownLoadBitmap(Context context, String url) {
//		this(context, url,null,0);
//		// TODO Auto-generated constructor stub
//	}
//
//    public DownLoadBitmap(Context context, String url,String imageType,int round) {
//		// TODO Auto-generated constructor stub
//    	super(context, url);
//    	this.urlPath = url;
//    	this.round = round;
//    	this.connectTimeout = 30000;
//    	this.connectTimeout = 5000;
//	}
//    
//    protected Bitmap getBitmap(String savePath,boolean saveSD)
//    {
//    	if ( urlPath == null && urlPath.length( ) <= 0 )
//		{
//			return null;
//		}
//    	if ( connection == null )
//		{
//			return null;
//		}
//    	
//    	try {
//			connection.connect();
//			InputStream is = connection.getInputStream();
//			PatchInputString pis = new PatchInputString(is);
//			Bitmap bitmap = BitmapFactory.decodeStream(pis);
//			connection.disconnect();
//			if ( bitmap == null || bitmap.isRecycled( ) )
//			{
//				return null;
//			}
//			if(round > 0 ){
//				 bitmap = RoundPicture.getRoundedCornerBitmap(bitmap, round, bitmap.getWidth(), bitmap.getHeight());
//				 
//			}
//			if(saveSD){
//				saveInSD(bitmap, savePath);
//			}
//			return bitmap;
//		} catch (Exception e) {
//			// TODO: handle exception
//			CommonFunction.log(e);
//		}
//    	return null;
//    }
//    
//    private void saveInSD(Bitmap bitmap, final String savePath){
//    	FileOutputStream fos = null;
//    	try {
//			File cacheDir = new File(savePath);
//			if(!cacheDir.exists()){
//				cacheDir.mkdir();
//			}else if( !cacheDir.isDirectory()){
//				cacheDir.delete();
//				cacheDir.mkdir();
//			}
//			 String fileName = ( round == 0 ) ? CryptoUtil.SHA1( urlPath ) : CryptoUtil
//					.SHA1( urlPath + round );
//			 File file = new File( cacheDir , fileName );
//			 if ( file.exists( ) )
//				{
//					file.delete( );
//				}
// 
//				
//				if ( file.createNewFile( ) )
//				{
//					fos = new FileOutputStream( file );
//					if ( round > 0 || urlPath.toLowerCase( ).endsWith( ".png" ) )
//					{ // 保存为png
//						bitmap.compress( Bitmap.CompressFormat.PNG , 60 , fos );
//					}
//					else
//					{
//						bitmap.compress( Bitmap.CompressFormat.JPEG , 60 , fos );
//					}
//					fos.flush( );
//					fos.close( );
//				}
//    	} catch (Exception e) {
//			// TODO: handle exception
//		}
//    }
//    
//    /**
//     * 读取数据
//     * @author clown
//     * 
//     */
//    public String getFileSave(String path){
//    	File file = new File(path);
//    	File folder = file.getParentFile();
//    	if(!folder.exists()){
//    		folder.mkdirs();
//    	}
//     
//    	InputStream is = null;
//    	FileOutputStream fos = null;
//    	try {
//    		
//    		connection.connect();
//    		is = connection.getInputStream();
//    		
//    		fos = new FileOutputStream(file);
//    		byte[] buffer = new byte[1024*4];
//    		int length = -1;
//    		while(( length = is.read( buffer ) ) > -1){
//    			fos.write(buffer,0,length);
//    		}
//    		fos.flush();
//    		connection.disconnect();
//		} catch (Exception e) {
//			// TODO: handle exception
//			CommonFunction.log(e);
//		}finally{
//			try {
//				if(is!=null) is.close();
//				if(fos!=null) fos.close();
//				
//			} catch (Exception e2) {
//				// TODO: handle exception
//			}
//		}
//    	
//    	return path;
//    }
//    public class PatchInputString extends FilterInputStream{
//
//    	InputStream in;
//    	
//		protected PatchInputString(InputStream in) {
//			super(in);
//			// TODO Auto-generated constructor stub
//			this.in = in;
//		}
//    	
//		public long skip(long n) throws IOException{
//			long m = 0;
//			while ( m < n )
//			{
//				long _m = in.skip( n - m );
//				if ( _m == 0 )
//				{
//					break;
//				}
//				m += _m;
//			}
//			return m;
//		}
//    }
//}
