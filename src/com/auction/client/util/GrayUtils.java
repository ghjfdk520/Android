package com.auction.client.util;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GrayUtils{

	/**
	 * 递归设置变灰
	 */
	public static void setGray(View base) {
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		ColorMatrixColorFilter grayFilter = new ColorMatrixColorFilter(
				colorMatrix);
		setFilter(base, grayFilter);
	}

	/**
	 * 变灰之后，总要有个还原的方法吧
	 */
	public static void setFilterNull(View base) {
		setFilter(base, null);
	}

	/**
	 * 将某个View设置颜色滤镜
	 */
	public static void setFilter(View base, ColorFilter filter) {
		if (base.getBackground() != null)
			base.getBackground().setColorFilter(filter);
		if (base instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) base;
			int count = parent.getChildCount();
			for (int i = 0; i < count; i++) {
				setFilter(parent.getChildAt(i), filter);
			}
		} else if (base instanceof ImageView) {
			ImageView view = (ImageView) base;
			view.setColorFilter(filter);
		} else if (base instanceof TextView) {
			TextView view = (TextView) base;
			if (view.getPaint() != null)
				view.getPaint().setColorFilter(filter);
		}
		if (base.getBackground() != null)
			base.getBackground().setColorFilter(filter);
	}
}