package com.auction.client.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.auction.client.R;
import com.auction.client.entity.Additem_entity;
import com.auction.client.entity.Item;
import com.auction.client.entity.UserInfo;
import com.auction.client.fragment.AlarmFrament;
import com.auction.client.fragment.BidFragment;
import com.auction.client.fragment.ItemSpace;
import com.auction.client.fragment.ManagerBid;
import com.auction.client.fragment.Selloff;
import com.auction.client.fragment.UserInformation;
import com.auction.common.Constants.State;
import com.auction.config.Common;
import com.auction.connector.FormFile;
import com.auction.connector.SocketHttpRequester;
import com.auction.database.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommonFunction {

	final static boolean DEBUG = true;
	// 是否需要重新获取
	public static boolean isCheckAlarm = true;
	public static List<String> itemAlarm = new ArrayList<String>();

	public static Gson gson = new Gson();
	public static List<String> itemImg = new ArrayList<String>();
	public static LinkedList<Activity> ActivityStack = new LinkedList<Activity>();
	public static LinkedList<Bitmap> BitmapStack = new LinkedList<Bitmap>();
	public static FragmentManager fragmentManager;
	public static Selloff selloff;
	public static ManagerBid mb;
	public static UserInformation ui;
	public static ItemSpace itemSpace;
	public static BidFragment bidFragment;
	public static AlarmFrament alarmFragment;

	public static LinkedList<LightTimer> timerList = new LinkedList<LightTimer>();
	public static int flagFragment;
	private static float density;

	// 切换
	public static void TabFragment(FragmentManager fragmentManager, State state) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideFragments(transaction);
		switch (state) {
		case SELLOFF:

			if (selloff == null) {
				selloff = new Selloff();
				transaction.add(R.id.main, selloff);
			} else {
				transaction.show(selloff);
			}
			break;
		case INFORMATION:
			if (ui == null) {
				ui = new UserInformation();
				transaction.add(R.id.main, ui);
			} else {
				transaction.show(ui);
			}
			break;
		case MANAGER:
			if (mb == null) {
				mb = new ManagerBid();
				transaction.add(R.id.main, mb);
			} else {
				transaction.show(mb);
			}
			break;
		case ITEMSPACE:
			if (itemSpace == null) {
				itemSpace = new ItemSpace();
				transaction.add(R.id.main, itemSpace);
			} else {
				transaction.show(itemSpace);
			}
			flagFragment = 1;
			break;
		case BIDFRAGMENT:
			if (bidFragment == null) {
				bidFragment = new BidFragment();
				transaction.add(R.id.main, bidFragment);
			} else {
				transaction.show(bidFragment);
			}
			// transaction.replace(R.id.main, bidFragment)
			break;
		case ALARMFRAGMENT:
			if (alarmFragment == null) {
				alarmFragment = new AlarmFrament();
				transaction.add(R.id.main, alarmFragment);
			} else {
				transaction.show(alarmFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}

	// 隐藏所有fragment
	public static void hideFragments(FragmentTransaction ft) {
		int i = 0;
		if (itemSpace != null) {
			ft.hide(itemSpace);
			flagFragment = 0;
		}
		if (selloff != null) {
			ft.hide(selloff);
		}
		if (ui != null) {
			ft.hide(ui);
		}
		if (mb != null) {
			ft.hide(mb);
		}
		if (bidFragment != null) {
			ft.hide(bidFragment);
		}
		if (alarmFragment != null) {
			ft.hide(alarmFragment);
		}

	}

	public static void stopFragment() {
		if (itemSpace != null) {
			itemSpace = null;
		}
		if (selloff != null) {
			selloff = null;
			;
		}
		if (ui != null) {
			ui = null;
			;
		}
		if (mb != null) {
			mb = null;
		}
		if (bidFragment != null) {
			bidFragment = null;
		}
		if (alarmFragment != null) {
			alarmFragment = null;
		}
	}

	/**
	 * 计算当前距离剩余多少时间
	 */

	public static int[] getRemaining(String endStamp) {
		Long l = System.currentTimeMillis() / 1000;
		int currentStamp = l.intValue();

		int remainningTiem = Integer.parseInt(endStamp) - currentStamp;
		int second = remainningTiem % 60;
		remainningTiem /= 60;
		int minute = remainningTiem % 60;
		remainningTiem /= 60;
		int hour = remainningTiem % 24;
		int day = remainningTiem / 24;
		int[] time = { day, hour, minute, second };
		return time;
	}

	public static void Timer(int endStamp, LinearLayout time_ly) {
		Timer(getRemaining(endStamp + ""), time_ly);
	}

	public static void Timer(final int[] time, LinearLayout time_ly) {
		final LinearLayout day_ly = (LinearLayout) time_ly
				.findViewById(R.id.day_ly);
		final LinearLayout hour_ly = (LinearLayout) time_ly
				.findViewById(R.id.hour_ly);
		final LinearLayout minute_ly = (LinearLayout) time_ly
				.findViewById(R.id.minute_ly);
		final LinearLayout second_ly = (LinearLayout) time_ly
				.findViewById(R.id.second_ly);

		final TextView remaining_day = (TextView) time_ly
				.findViewById(R.id.remaining_day);
		final TextView remaining_hour = (TextView) time_ly
				.findViewById(R.id.remaining_hour);
		final TextView remaining_minute = (TextView) time_ly
				.findViewById(R.id.remaining_minute);
		final TextView remaining_second = (TextView) time_ly
				.findViewById(R.id.remaining_second);

		LightTimer lt = new LightTimer() {
			int day = time[0];
			int hour = time[1];
			int minute = time[2];
			int second = time[3];

			@Override
			public void run(LightTimer timer) {
				// TODO Auto-generated method stub
				if (second == 0) {
					if (minute == 0) {
						if (hour == 0) {
							if (day == 0) {

							} else {
								day--;
								hour = 23;
								minute = 59;
								second = 59;
							}
						} else {
							hour--;
							minute = 59;
							second = 59;
						}
					} else {
						minute--;
						second = 59;
					}
				} else {
					second--;
				}

				if (day < 0 || hour < 0 || minute < 0 || second < 0) {
					timer.stop();
					remaining_day.setText("0");
					remaining_hour.setText("0");
					remaining_minute.setText("0");
					remaining_second.setText("0");
					return;
				}
				if (day == 0) {
					if (day_ly.isShown())
						day_ly.setVisibility(View.GONE);
					if (hour == 0) {
						hour_ly.setVisibility(View.INVISIBLE);
						if (minute == 0) {
							minute_ly.setVisibility(View.INVISIBLE);
							if (second == 0) {
								remaining_second.setText(second + "");
								timer.stop();
							}
						} else {
							remaining_minute.setText(minute + "");
							remaining_second.setText(second + "");
						}
					} else {
						remaining_hour.setText(hour + "");
						remaining_minute.setText(minute + "");
						remaining_second.setText(second + "");
					} 
				} else {
					remaining_day.setText(day + "");
					remaining_hour.setText(hour + "");
					remaining_minute.setText(minute + "");
					remaining_second.setText(second + "");
				}

			}

		};
		lt.startTimer(1000, (int) System.currentTimeMillis());

		if (!timerList.contains(lt)) {
			timerList.add(lt);
		}
	}

	public static void stopTimer() {
		for (LightTimer lt : timerList) {
			lt.stop();
		}
	}

	/**
	 * 获取sd卡路径
	 * 
	 * @param tag
	 * @param msg
	 */
	public static String getSDPath() {
		String sdDir = "";
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在)
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory() + "/auction";// 获取跟目录
		} else {
			sdDir = "/data/data/com.auction/";
		}
		File file = new File(sdDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return sdDir;
	}

	public static void log(String tag, Object... msg) {
		if (DEBUG && msg != null) {
			StringBuilder sb = new StringBuilder();
			int i = 0;
			for (Object o : msg) {
				if (i > 0) {
					sb.append(',');
				}
				sb.append(o == null ? "null" : o.toString());
				i++;
			}

			int logStrLength = sb.length();
			int maxLogSize = 1000;
			for (i = 0; i <= logStrLength / maxLogSize; i++) {
				int start = i * maxLogSize;
				int end = (i + 1) * maxLogSize;
				end = end > logStrLength ? logStrLength : end;
				if (tag.equals("sherlock")) {
					Log.i(tag, sb.substring(start, end));
				} else {
					Log.v("" + tag, sb.substring(start, end));
				}
			}
		}
	}

	public static void log(Throwable e) {
		if (DEBUG && e != null) {
			e.printStackTrace();
		}
	}

	public static Bitmap createCircleImage(Bitmap bitmap, int wight, int height1) {
		// 圆形图片宽高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 正方形的边长
		int r = 0;
		// 取最短边做边长
		if (width > height) {
			r = height;
		} else {
			r = width;
		}
		// 构建一个bitmap 正方形
		Bitmap backgroundBmp = Bitmap.createBitmap(r, r, Config.ARGB_8888);
		// new一个Canvas，在backgroundBmp上画图
		Canvas canvas = new Canvas(backgroundBmp);
		Paint paint = new Paint();
		// 设置边缘光滑，去掉锯齿
		paint.setAntiAlias(true);
		// 宽高相等，即正方形
		RectF rect = new RectF(0, 0, r, r);
		// 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
		// 且都等于r/2时，画出来的圆角矩形就是圆形
		canvas.drawRoundRect(rect, r / 2, r / 2, paint);
		// 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas将bitmap画在backgroundBmp上
		canvas.drawBitmap(bitmap, null, rect, paint);
		// 返回已经绘画好的backgroundBmp
		return backgroundBmp;

	}

	public static String UserToJson(UserInfo user) {
		JSONObject userJson = new JSONObject();

		return null;
	}

	public static void ToJsonStr(Map map) {
		JSONObject json = new JSONObject();
		try {
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = entry.getKey().toString();
				String val = entry.getValue().toString();
				json.put(key, val);
			}
			map.clear();
			map.put("inputStr", json.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static String ItemToJson(Additem_entity additem) {

		JSONObject itemJson = new JSONObject();
		try {
			itemJson.put("max_price", additem.getMax_price());
			itemJson.put("item_name", additem.getItem_name());
			itemJson.put("item_desc", additem.getItem_desc());
			itemJson.put("kind_id", additem.getKind_id());
			itemJson.put("init_price", additem.getInit_price());
			itemJson.put("owner_id", additem.getOwner_id());
			itemJson.put("endtime", additem.getEndtime());
			itemJson.put("fiexd_price", additem.getFiexd_price());
			itemJson.put("item_id", additem.getItem_id());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return itemJson.toString();

	}

	public int upload(Bitmap bitmap, String url, String inputStr) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
		byte[] b = stream.toByteArray();
		String file = new String(Base64Coder.encodeLines(b));

		Map<String, String> map = new HashMap<String, String>();

		map.put("inputStr", inputStr);

		try {

			FormFile formfile = new FormFile("file", stream.toByteArray(),
					"file", "application/octet-stream");
			SocketHttpRequester.post(url, map, formfile);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return 0;

	}

	/**
	 * jsonArray to list
	 * 
	 * @param <T>
	 */

	public static List<String> ToListStr(String jsonarrayStr, List<String> list) {
		try {

			if (!jsonarrayStr.equals("[]")) {

				JSONArray jsonarray = new JSONArray(jsonarrayStr);
				for (int i = 0; i < jsonarray.length(); i++) {

					String temp = Common.getInstance().BASE_UPLOAD
							+ jsonarray.getString(i);
					list.add(temp);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	public static List<Map> ToMap(String jsonarrayStr, List list) {

		try {
			list.clear();
			JSONArray jsonArray = new JSONArray(jsonarrayStr);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
				Map map = new HashMap<String, String>();
				map.put("user_name", jsonObject.getString("user_name"));
				map.put("bid_price", jsonObject.getString("bid_price"));
				list.add(map);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;

	}

	private HashMap<String, Object> fromJson(JSONObject json) {
		return null;

	}

	public static List JsonObjectToList(String str, List list) {
		try {
			JSONObject json = new JSONObject(str);
			Iterator<String> iterator = json.keys();
			list.clear();
			while (iterator.hasNext())
				list.add(gson.fromJson((String) json.get(iterator.next()),
						Item.class));
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return list;
	}

	public static List ToList(String jsonarrayStr, List list) {
		try {
			// Type listType=new TypeToken<ArrayList<Item>>(){}.getType();
			// list = gson.fromJson(jsonarrayStr, listType);
			list.clear();
			JSONArray jsonarray = new JSONArray(jsonarrayStr);
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jo = (JSONObject) jsonarray.opt(i);

				Item item = gson.fromJson(jo.toString(), Item.class);
				list.add(item);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return list;
	}

	// 取消闹钟功能
	public static void cancleAlarm(Context context, Item item) {
		String alarmStr = SharedPreferenceUtil.getInstance(context).getString(
				SharedPreferenceUtil.ALARM_ITEM);
		try {
			JSONObject json = new JSONObject(alarmStr);
			json.remove(item.getItem_id() + "");
			SharedPreferenceUtil.getInstance(context).putString(
					SharedPreferenceUtil.ALARM_ITEM, json.toString());
			isCheckAlarm = true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// 检查item是否已经添加了提醒功能
	public static boolean checkItemInAlarm(Context context, Item item) {
		if (isCheckAlarm) {
			itemAlarm.clear();
			String alarmStr = SharedPreferenceUtil.getInstance(context)
					.getString(SharedPreferenceUtil.ALARM_ITEM);
			try {
				System.out.println(alarmStr);
				JSONObject json = null;
				if (!alarmStr.equals("")) {
					json = new JSONObject(alarmStr);
				} else {
					json = new JSONObject();
				}
				Iterator<String> iterator = json.keys();
				while (iterator.hasNext()) {
					itemAlarm.add(iterator.next().toString());
				}
				isCheckAlarm = false;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		boolean isExists = itemAlarm.contains(item.getItem_id() + "");
		return isExists;
	}

	// 把item保存到sharedpreference
	public static void saveItemAlarm(Context context, Item item) {

		try {
			if (!checkItemInAlarm(context, item)) {
				String alarmStr = SharedPreferenceUtil.getInstance(context)
						.getString(SharedPreferenceUtil.ALARM_ITEM);
				JSONObject json = null;
				if (alarmStr.equals("")) {
					json = new JSONObject();

				} else {
					json = new JSONObject(alarmStr);
				}
				String temp = gson.toJson(item);
				json.put(item.getItem_id() + "", temp);
				SharedPreferenceUtil.getInstance(context).putString(
						SharedPreferenceUtil.ALARM_ITEM, json.toString());
				isCheckAlarm = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static Item toItem(String jsonStr) {
		Item item = null;
		try {
			if (!jsonStr.equals("")) {
				item = gson.fromJson(jsonStr, Item.class);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return item;

	}

	public static Item toItem(String jsonStr, String item_id) {
		Item item = null;
		try {
			if (!jsonStr.equals("")) {
				JSONObject json = new JSONObject(jsonStr);
				item = gson.fromJson(json.getString(item_id), Item.class);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return item;
	}

	/**
	 * 判断字符串是否为空字符串、null或“null”字符串包括所有大小写情况
	 * 
	 * @param str
	 * @return 是否为空
	 */
	public static boolean isEmptyOrNullStr(String str) {
		return TextUtils.isEmpty(str) || "".equals(str);
	}

	/**
	 * 使用MD5产生消息摘要
	 * 
	 * @param string
	 *            soures 待产生消息摘要的字符串
	 * 
	 * @return 返回十六进制字符串
	 */
	public static String md5(String data) {
		if (data == null) {
			return null;
		}

		String s = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data.getBytes());
			byte[] tmp = md.digest();
			s = tmp.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/** dip转px */
	public static int dipToPx(Context context, int dip) {
		if (density <= 0) {
			density = context.getResources().getDisplayMetrics().density;
		}
		return (int) (dip * density + 0.5f);
	}

	/** px转dip */
	public static int pxToDip(Context context, int px) {
		if (density <= 0) {
			density = context.getResources().getDisplayMetrics().density;
		}
		return (int) ((px - 0.5f) / density);
	}

	/**
	 * 获取屏幕分辨率：宽
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenPixWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		if (!(context instanceof Activity)) {
			dm = context.getResources().getDisplayMetrics();
			return dm.widthPixels;
		}

		WindowManager wm = ((Activity) context).getWindowManager();
		if (wm == null) {
			dm = context.getResources().getDisplayMetrics();
			return dm.widthPixels;
		}

		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static void Toast(Context context, String msg) {
		android.widget.Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

}
