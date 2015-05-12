package com.auction.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceUtil {
	/**保存设置了提示闹钟的item*/
	public final static String ALARM_ITEM="alarm_item";
	/**是否需要重新设置闹钟*/
	public final static String REINSTALL_ALARM_ITEM ="REINSTALL_ALARM_ITEM";
	
	private final static String KEY = "meyou_sharepreferences";
	private static SharedPreferenceUtil sharedPreferenceUtil;
	private SharedPreferences sharedPreferences;

	private SharedPreferenceUtil(Context context) {
		sharedPreferences = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
	}

	public static SharedPreferenceUtil getInstance(Context context) {
		if (sharedPreferenceUtil == null) {
			sharedPreferenceUtil = new SharedPreferenceUtil(context);
		}
		return sharedPreferenceUtil;
	}

	public void putString(String key, String value) {
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 获取key相对应的value，如果不设默认参数，默认值为""
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return getString(key, "");
	}

	public String getString(String key, String defaultValue) {
		return sharedPreferences.getString(key, defaultValue);
	}
	
	/**
	 * 设置Boolean类型值
	 * 
	 * @param key
	 * @param value
	 * @time 2011-5-30 上午09:30:56
	 * @author:linyg
	 */
	public void putBoolean( String key , boolean value )
	{
		Editor editor = sharedPreferences.edit( );
		editor.putBoolean( key , value );
		editor.commit( );
	}
	/**
	 * 获取key相对应的value，如果不设默认参数，默认值为false
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBoolean( String key )
	{
		return getBoolean( key , false );
	}
	
	/**
	 * 获取key相对应的value，如果不设默认参数，默认值为false
	 * 
	 * @param key
	 * @param defalutValue
	 * @return
	 */
	public boolean getBoolean( String key , boolean defaultValue )
	{
		return sharedPreferences.getBoolean( key , defaultValue );
	}
	
	/** 删除sharedPreferences文件中对应的Key和value */
	public boolean remove( String key )
	{
		Editor editor = sharedPreferences.edit( );
		editor.remove( key );
		return editor.commit( );
	}
}
