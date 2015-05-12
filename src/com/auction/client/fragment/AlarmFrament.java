package com.auction.client.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.auction.client.R;
import com.auction.client.entity.Item;
import com.auction.client.util.CommonFunction;
import com.auction.client.util.JsonUtil;
import com.auction.client.util.LightTimer;
import com.auction.database.SharedPreferenceUtil;
import com.auction.view.ItemSpace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AlarmFrament extends Fragment {
	private String alarmStr;
	private List<Item> list = new ArrayList<Item>();
	Map<String, Object> map = null;
	private List<String[]> listData = new ArrayList<String[]>();
	// private List<Item> list;
	private boolean isReinstall;
	private ListView alarm_list;
	private View rootView;
	private TextView title_name;
	private MyAdapter adapter;
	private Intent m_Intent;
	private Handler mMainHandler;
	private PendingIntent m_PendingIntent;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	public void init() {
		title_name = (TextView) rootView.findViewById(R.id.title_name);
		title_name.setText("闹钟");
		isReinstall = SharedPreferenceUtil.getInstance(getActivity())
				.getBoolean(SharedPreferenceUtil.REINSTALL_ALARM_ITEM);
		alarmStr = SharedPreferenceUtil.getInstance(getActivity()).getString(
				SharedPreferenceUtil.ALARM_ITEM);
		listData = JsonUtil.getStrPair(alarmStr);
		alarm_list = (ListView) rootView.findViewById(R.id.alarm_list);
		adapter = new MyAdapter();
		alarm_list.setAdapter(adapter);
		mMainHandler = new Handler(Looper.getMainLooper());
		m_Intent = new Intent(getActivity(), ItemSpace.class);
		// 主要是设置点击通知时显示内容的类

		m_PendingIntent = PendingIntent.getActivity(getActivity(), 0, m_Intent,
				0);

		LightTimer lt = new LightTimer() {

			@Override
			public void run(LightTimer timer) {
				// TODO Auto-generated method stub
				for (String[] s : listData) {
					int tempStame = Integer.parseInt(s[1]);

					if ((tempStame - System.currentTimeMillis() / 1000000) < 600) {
						m_Intent.putExtra("item_id", s[2]);

						NotificationMethod(s);
					}
				}
			}
		};
		lt.startTimer(10 * 60 * 1000, (int) System.currentTimeMillis() / 10000);
	}

	public void initlistener() {

		alarm_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				showDialog(arg2);
				return false;
			}
		});

		alarm_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				SharedPreferenceUtil sharedpreference = SharedPreferenceUtil
						.getInstance(getActivity());
				String temp = sharedpreference
						.getString(SharedPreferenceUtil.ALARM_ITEM);
				Item item = CommonFunction.toItem(temp, listData.get(arg2)[2]);

				ItemSpace.launchActivity(getActivity(), item);

			}

		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {

			isReinstall = SharedPreferenceUtil.getInstance(getActivity())
					.getBoolean(SharedPreferenceUtil.REINSTALL_ALARM_ITEM);
		} else {
			//CommonFunction.stopTimer();
		}
		if (isReinstall) {
			alarmStr = SharedPreferenceUtil.getInstance(getActivity())
					.getString(SharedPreferenceUtil.ALARM_ITEM);
			listData = JsonUtil.getStrPair(alarmStr);
			SharedPreferenceUtil.getInstance(getActivity()).putBoolean(
					SharedPreferenceUtil.REINSTALL_ALARM_ITEM, false);
			upadateAlarmList();
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.alarm_fragment, null);

		// setAlarm();
		init();
		initlistener();

		return rootView;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.alarm_item, null);
				holder.time_ly = (LinearLayout) convertView
						.findViewById(R.id.time_ly);
				holder.item_name = (TextView) convertView
						.findViewById(R.id.item_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String[] temp = listData.get(position);
			holder.item_name.setText(temp[0]);

			final int[] time = CommonFunction.getRemaining(temp[1]);
			CommonFunction.Timer(time,
					(LinearLayout) convertView.findViewById(R.id.time_ly));

			return convertView;
		}

	}

	class ViewHolder {
		TextView item_name;
		LinearLayout time_ly;
	}

	@SuppressLint("NewApi")
	private void NotificationMethod(String[] s) {
		// 主要是设置点击通知时显示内容的类

		m_PendingIntent = PendingIntent.getActivity(getActivity(), 0, m_Intent,
				0);
		try {
			NotificationManager m_NotificationManager = (NotificationManager) getActivity()
					.getSystemService(Context.NOTIFICATION_SERVICE);

			Notification notify = new Notification.Builder(getActivity())
					// 设置打开该通知，该通知自动消失
					.setAutoCancel(true)
					// 设置显示在状态栏的通知提示信息
					.setTicker("有新消息")
					.setContentIntent(m_PendingIntent)
					// 设置通知的图标
					.setSmallIcon(R.drawable.ic_launcher)
					// 设置通知内容的标题
					.setContentTitle("s[0]接近拍卖结束")
					// 设置通知内容
					.setContentText("刚快出手，别让机会从手中溜走")
					// // 设置使用系统默认的声音、默认LED灯
					.setDefaults(Notification.DEFAULT_SOUND)
					// .setDefaults(Notification.DEFAULT_SOUND
					// |Notification.DEFAULT_LIGHTS)
					// 设置通知的自定义声音
					.setDefaults(Notification.DEFAULT_SOUND)
					.setWhen(System.currentTimeMillis())
					// 设改通知将要启动程序的Intent
					.build();
			m_NotificationManager.notify(4, notify);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void upadateAlarmList() {
		runOnUIThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void runOnUIThread(Runnable runnable) {

		if (runnable != null && mMainHandler != null) {
			mMainHandler.post(runnable);
		}
	}

	private void showDialog(final int position) {
		Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("确定取消？");
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				JsonUtil.delAlarm(getActivity(), listData.get(position)[2]);
				listData.remove(position);
				upadateAlarmList();
			}
		});
		builder.show();
	}

}