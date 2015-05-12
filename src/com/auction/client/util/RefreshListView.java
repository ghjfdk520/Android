package com.auction.client.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.auction.client.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RefreshListView extends ListView implements OnScrollListener {

	private float mDownY;
	private float mMoveY;

	private int mHeaderHeight;

	private int mCurrentScrollState;

	private final static int NONE_PULL_REFRESH = 0;
	// ����״̬
	private final static int ENTER_PULL_REFRESH = 1;
	// ��������ˢ��״̬
	private final static int OVER_PULL_REFRESH = 2;
	// ��������ˢ��״̬
	private final static int EXIT_PULL_REFRESH = 3;
	// ���ֺ󷴵��ͼ���״̬
	private int mPullRefreshState = 0;
	// ��¼ˢ��״̬

	private final static int REFRESH_BACKING = 0;
	// ������
	private final static int REFRESH_BACED = 1;
	// �ﵽˢ�½��ޣ�����������
	private final static int REFRESH_RETURN = 2;
	// û�дﵽˢ�½��ޣ�����
	private final static int REFRESH_DONE = 3;
	// �������ݽ���

	private LinearLayout mHeaderLinearLayout = null;
	private LinearLayout mFooterLinearLayout = null;
	private TextView mHeaderTextView = null;
	private TextView mHeaderUpdateText = null;

	private ProgressBar mHeaderProgressBar = null;
	private TextView mFooterTextView = null;
	private ProgressBar mFooterProgressBar = null;

	private SimpleDateFormat mSimpleDateFormat;

	private Object mRefreshObject = null;
	private RefreshListener mRefreshListener = null;

	public RefreshListView(Context context) {
		this(context, null);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	void init(final Context context) {
		mHeaderLinearLayout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.refresh_list_header, null);
		addHeaderView(mHeaderLinearLayout);
		mHeaderTextView = (TextView) findViewById(R.id.refresh_list_header_text);
		mHeaderUpdateText = (TextView) findViewById(R.id.refresh_list_header_last_update);
		mHeaderProgressBar = (ProgressBar) findViewById(R.id.refresh_list_header_progressbar);

		mFooterLinearLayout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.refresh_list_footer, null);
		addFooterView(mFooterLinearLayout);
		mFooterProgressBar = (ProgressBar) findViewById(R.id.refresh_list_footer_progressbar);
		mFooterTextView = (TextView) mFooterLinearLayout
				.findViewById(R.id.refresh_list_footer_text);

		setSelection(1);
		setOnScrollListener(this);
		measureView(mHeaderLinearLayout);
		mHeaderHeight = mHeaderLinearLayout.getMeasuredHeight();

		mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		mHeaderUpdateText.setText(mSimpleDateFormat.format(new Date()));
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			mMoveY = ev.getY();
			if (mPullRefreshState == OVER_PULL_REFRESH) {
				mHeaderLinearLayout.setPadding(
						mHeaderLinearLayout.getPaddingLeft(),
						(int) ((mMoveY - mDownY) / 3),
						mHeaderLinearLayout.getPaddingRight(),
						mHeaderLinearLayout.getPaddingBottom());
			}
			break;
		case MotionEvent.ACTION_UP:
			// when you action up, it will do these:
			// 1. roll back util header topPadding is 0
			// 2. hide the header by setSelection(1)
			if (mPullRefreshState == OVER_PULL_REFRESH
					|| mPullRefreshState == ENTER_PULL_REFRESH) {
				new Thread() {
					public void run() {
						Message msg;
						while (mHeaderLinearLayout.getPaddingTop() > 1) {
							msg = mHandler.obtainMessage();
							msg.what = REFRESH_BACKING;
							mHandler.sendMessage(msg);
							try {
								sleep(5);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						msg = mHandler.obtainMessage();
						if (mPullRefreshState == OVER_PULL_REFRESH) {
							msg.what = REFRESH_BACED;
						} else {
							msg.what = REFRESH_RETURN;
						}
						mHandler.sendMessage(msg);
					};
				}.start();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& firstVisibleItem == 0
				&& (mHeaderLinearLayout.getBottom() >= 0 && mHeaderLinearLayout
						.getBottom() < mHeaderHeight)) {
			// �����ҽ���������ˢ��״̬
			if (mPullRefreshState == NONE_PULL_REFRESH) {
				mPullRefreshState = ENTER_PULL_REFRESH;
			}
		} else if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& firstVisibleItem == 0
				&& (mHeaderLinearLayout.getBottom() >= mHeaderHeight)) {
			// �����ﵽ���ޣ���������ˢ��״̬
			if (mPullRefreshState == ENTER_PULL_REFRESH
					|| mPullRefreshState == NONE_PULL_REFRESH) {
				mPullRefreshState = OVER_PULL_REFRESH;
				mDownY = mMoveY; // Ϊ����1/3�ۿ�Ч����¼��ʼλ��
				mHeaderTextView.setText("����ˢ��");// ��ʾ����ˢ��
			}
		} else if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& firstVisibleItem != 0) {
			// ��ˢ����
			if (mPullRefreshState == ENTER_PULL_REFRESH) {
				mPullRefreshState = NONE_PULL_REFRESH;
			}
		} else if (mCurrentScrollState == SCROLL_STATE_FLING
				&& firstVisibleItem == 0) {
			// �ɻ�״̬��������ʾ��header��Ҳ����Ӱ�������ķɻ�
			// ֻ����������²ž���λ��
			if (mPullRefreshState == NONE_PULL_REFRESH) {
				setSelection(1);
			}
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		setSelection(1);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_BACKING:
				mHeaderLinearLayout.setPadding(
						mHeaderLinearLayout.getPaddingLeft(),
						(int) (mHeaderLinearLayout.getPaddingTop() * 0.75f),
						mHeaderLinearLayout.getPaddingRight(),
						mHeaderLinearLayout.getPaddingBottom());
				break;
			case REFRESH_BACED:
				mHeaderTextView.setText("���ڼ���...");
				mHeaderProgressBar.setVisibility(View.VISIBLE);
				mPullRefreshState = EXIT_PULL_REFRESH;
				new Thread() {
					public void run() {
						if (mRefreshListener != null) {
							mRefreshObject = mRefreshListener.refreshing();
						}
						Message msg = mHandler.obtainMessage();
						msg.what = REFRESH_DONE;
						mHandler.sendMessage(msg);
					};
				}.start();
				break;
			case REFRESH_RETURN:
				mHeaderTextView.setText("����ˢ��");
				mHeaderProgressBar.setVisibility(View.INVISIBLE);
				mHeaderLinearLayout.setPadding(
						mHeaderLinearLayout.getPaddingLeft(), 0,
						mHeaderLinearLayout.getPaddingRight(),
						mHeaderLinearLayout.getPaddingBottom());
				mPullRefreshState = NONE_PULL_REFRESH;
				setSelection(1);
				break;
			case REFRESH_DONE:
				mHeaderTextView.setText("����ˢ��");
				mHeaderProgressBar.setVisibility(View.INVISIBLE);
				mHeaderUpdateText.setText(mSimpleDateFormat.format(new Date()));
				mHeaderLinearLayout.setPadding(
						mHeaderLinearLayout.getPaddingLeft(), 0,
						mHeaderLinearLayout.getPaddingRight(),
						mHeaderLinearLayout.getPaddingBottom());
				mPullRefreshState = NONE_PULL_REFRESH;
				setSelection(1);
				if (mRefreshListener != null) {
					mRefreshListener.refreshed(mRefreshObject);
				}
				break;
			default:
				break;
			}
		}
	};

	public void finishFootView() {
		mFooterProgressBar.setVisibility(View.GONE);
		mFooterTextView.setText("ddd");
	}

	public void addFootView() {
		if (getFooterViewsCount() == 0) {
			addFooterView(mFooterLinearLayout);
		}
	}

	public void removeFootView() {
		removeFooterView(mFooterLinearLayout);
	}

	private void measureView(View child) {
		// TODO Auto-generated method stub
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public void setOnRefreshListener(RefreshListener refreshListener) {
		this.mRefreshListener = refreshListener;
	}

	public interface RefreshListener {
		Object refreshing();

		void refreshed(Object obj);

		void more();
	}
}
