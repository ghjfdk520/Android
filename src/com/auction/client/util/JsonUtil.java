package com.auction.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.auction.database.SharedPreferenceUtil;

/**
 * 处理json的工具类
 * 
 * @author linyg
 */
public class JsonUtil {
	/**
	 * 将json 转换为Map 对象
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map<String, Object> jsonToMap(String jsonString) {
		JSONObject jsonObject;
		Map<String, Object> valueMap = new HashMap<String, Object>();
		if (jsonString != null && !jsonString.equals("")) {
			try {
				jsonObject = new JSONObject(jsonString);
				@SuppressWarnings("unchecked")
				Iterator<String> keyIter = jsonObject.keys();
				String key;
				Object value;

				while (keyIter.hasNext()) {
					key = (String) keyIter.next();
					value = jsonObject.get(key);
					if (value instanceof JSONArray) { // array类型
						valueMap.put(key, array2Map((JSONArray) value));
					} else if (value instanceof JSONObject) { // object类型
						valueMap.put(key, object2Map((JSONObject) value));
					} else { // 简单类型
						valueMap.put(key, value);
					}
				}
				return valueMap;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return valueMap;
	}

	/**
	 * 将array转换为map
	 * 
	 * @param JSONArray
	 * @return
	 * @time 2011-6-14 上午09:16:25
	 * @author:linyg
	 * @throws JSONException
	 */
	private static LinkedHashMap<String, Object> array2Map(JSONArray array)
			throws JSONException {
		int count = array.length();
		LinkedHashMap<String, Object> valueMap = new LinkedHashMap<String, Object>();
		for (int i = 0; i < count; i++) { // 遍历数组
			Map<String, Object> map = new HashMap<String, Object>();
			JSONObject json = array.getJSONObject(i);
			@SuppressWarnings("unchecked")
			Iterator<String> keyIter = json.keys();
			String key;
			Object value;
			while (keyIter.hasNext()) {
				key = (String) keyIter.next();
				value = json.get(key);
				if (value instanceof JSONArray) { // array类型
					map.put(key, array2Map((JSONArray) value));
				} else if (value instanceof JSONObject) { // object类型
					map.put(key, object2Map((JSONObject) value));
				} else { // string 简单类型
					map.put(key, value);
				}
			}
			valueMap.put(String.valueOf(i), map);
		}
		return valueMap;
	}

	/**
	 * 将对象转换成object
	 * 
	 * @param JSONObject
	 * @return
	 * @time 2011-6-14 上午09:23:33
	 * @author:linyg
	 * @throws JSONException
	 */
	private static Map<String, Object> object2Map(JSONObject json)
			throws JSONException {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		Iterator<String> keyIter = json.keys();
		String key;
		Object value;
		while (keyIter.hasNext()) {
			key = (String) keyIter.next();
			value = json.get(key);
			if (value instanceof JSONArray) { // array类型
				objectMap.put(key, array2Map((JSONArray) value));
			} else if (value instanceof JSONObject) { // object类型
				objectMap.put(key, object2Map((JSONObject) value));
			} else { // string 简单类型
				objectMap.put(key, value);
			}
		}
		return objectMap;
	}

	// 删除闹钟数据
	public static void delAlarm(Context context,String itemId) {
		String jsonString =SharedPreferenceUtil.getInstance(context).getString(
				SharedPreferenceUtil.ALARM_ITEM);
		
		JSONObject jsonObject;
 		if (jsonString != null && !jsonString.equals("")) {
			try {
				jsonObject = new JSONObject(jsonString);
				jsonObject.remove(itemId);
				SharedPreferenceUtil.getInstance(context).putString(SharedPreferenceUtil.ALARM_ITEM, jsonObject.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	 
	}

	public static List<String[]> getStrPair(String jsonString) {
		JSONObject jsonObject;
		List<String[]> list = new ArrayList<String[]>();
		if (jsonString != null && !jsonString.equals("")) {
			try {
				jsonObject = new JSONObject(jsonString);
				@SuppressWarnings("unchecked")
				Iterator<String> keyIter = jsonObject.keys();
				String key;
				Object value;

				while (keyIter.hasNext()) {
					key = (String) keyIter.next();
					value = jsonObject.get(key);
					JSONObject tempJson = new JSONObject(value.toString());
					String tempStr[] = { tempJson.get("item_name").toString(),
							tempJson.get("endtime").toString(), key };
					list.add(tempStr);
				}
				return list;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 把json 转换为 ArrayList 形式
	 * 
	 * @param jsonString
	 * @return
	 */
	public static List<Map<String, Object>> jsonToList(String jsonString) {
		List<Map<String, Object>> list = null;
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			JSONObject jsonObject;
			list = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				list.add(jsonToMap(jsonObject.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 将map转换成json
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToJsonString(LinkedHashMap<String, Object> map) {
		JSONObject json = new JSONObject(map);
		return json.toString();
	}

}
