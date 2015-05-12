package com.auction.client.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 
 * @author clown
 * ‘≤Ω«
 */
public class RoundPicture {

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmapl,float roundPx,int dWith,int dHeight){
	
		try {
			Bitmap bitmap = scalePicture(bitmapl, dWith, dHeight);
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas( output );
			
			final int color = 0xff424242;
			final Paint paint= new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF= new RectF(rect);
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			return output;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return bitmapl;
		
	}
	public static Bitmap scalePicture(Bitmap srcBitmap,int maxWidth,int maxHeight){
		try {
			Bitmap bitmap = null;
			//ºÙ«–Õº∆¨
			
			if(maxWidth == maxHeight){
				int width = srcBitmap.getWidth();
				int height = srcBitmap.getHeight();
				int side = width;
				if(width>height){
					side = height;
				}
				Bitmap newb = Bitmap.createBitmap(side, side, Config.ARGB_8888);
				Canvas cv = new Canvas(newb);
				cv.drawBitmap(srcBitmap, 0, 0,null);
			}
			int srcWidth = srcBitmap.getWidth();
			int srcHeight = srcBitmap.getHeight();
            //º∆À„Àı∑≈¬ 
		    float scaleWidth = ( ( float ) maxWidth ) / srcWidth;
			float scaleHeight = ( ( float ) maxHeight ) / srcHeight;
			
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, maxWidth, srcHeight,matrix, true);
			return bitmap;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace( );
			System.gc( );
		}
		
		return srcBitmap;
		
	}
}
