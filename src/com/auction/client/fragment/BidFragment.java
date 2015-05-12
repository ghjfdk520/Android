package com.auction.client.fragment;

import java.util.ArrayList;
import java.util.List;

import com.auction.client.R;
import com.auction.view.Bid_fail;
import com.auction.view.Bid_tender;
import com.auction.view.Bid_win;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BidFragment extends Fragment implements OnClickListener {
	private View mainView;

	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private FragmentTransaction transaction;
	/**
	 * 顶部四个按钮
	 */
	private LinearLayout mTabBidwin;
	private LinearLayout mTabBidtender;
	private LinearLayout mTabBidfail;

	private Bid_fail tab03;
	private Bid_tender tab02;
	private Bid_win tab01;

	private TextView title_name;
	private TextView mBidwin;
	private TextView mBidtender;
	private TextView mBidfail;
	boolean[] fragmentsUpdateFlag = { false, false, false, false };
	private ImageView mTabLine;

	private int currentIndex;
	private int screenWidth;

	@Override
	public void onHiddenChanged(boolean hidden) {
		transaction = getActivity().getSupportFragmentManager()
				.beginTransaction();
		if (hidden) {
			System.out.println("hidsdfen");
			transaction.hide(tab01);
			transaction.hide(tab02);
			transaction.hide(tab03);
			transaction.commit();
		} else {
			transaction.show(tab01);
			transaction.show(tab02);
			transaction.show(tab03);
			transaction.commit();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mainView = inflater.inflate(R.layout.bid_fragment, null);
		mViewPager = (ViewPager) mainView.findViewById(R.id.id_viewpager);

		initView();
		initListener();
		initTabLine();

		mAdapter = new FragmentPagerAdapter(getActivity()
				.getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return mFragments.get(arg0);
			}
		};

		mViewPager.setAdapter(mAdapter);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				resetTextView(position);

				currentIndex = position;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				if (currentIndex == 0 && position == 0)// 0->1
				{
					LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine
							.getLayoutParams();
					lp.leftMargin = (int) (positionOffset
							* (screenWidth * 1.0 / 3) + currentIndex
							* (screenWidth / 3));
					mTabLine.setLayoutParams(lp);
				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine
							.getLayoutParams();
					lp.leftMargin = (int) (-(1 - positionOffset)
							* (screenWidth * 1.0 / 3) + currentIndex
							* (screenWidth / 3));
					mTabLine.setLayoutParams(lp);

				} else if (currentIndex == 1 && position == 1) // 1->2
				{
					LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine
							.getLayoutParams();
					lp.leftMargin = (int) (positionOffset
							* (screenWidth * 1.0 / 3) + currentIndex
							* (screenWidth / 3));
					mTabLine.setLayoutParams(lp);
				} else if (currentIndex == 2 && position == 1) // 2->1
				{
					LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine
							.getLayoutParams();
					lp.leftMargin = (int) (-(1 - positionOffset)
							* (screenWidth * 1.0 / 3) + currentIndex
							* (screenWidth / 3));
					mTabLine.setLayoutParams(lp);

				}

			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		mViewPager.setCurrentItem(1);

		return mainView;
	}

	private void initTabLine() {
		mTabLine = (ImageView) mainView.findViewById(R.id.id_tab_line);
		DisplayMetrics outMetrics = new DisplayMetrics();
		getActivity().getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(outMetrics);
		screenWidth = outMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine
				.getLayoutParams();
		lp.width = screenWidth / 3;
		lp.leftMargin = screenWidth / 3;
		mTabLine.setLayoutParams(lp);
	}

	/**
	 * 重置颜色
	 */
	protected void resetTextView(int position) {
		mBidwin.setTextColor(getResources().getColor(R.color.black));
		mBidtender.setTextColor(getResources().getColor(R.color.black));
		mBidfail.setTextColor(getResources().getColor(R.color.black));

		LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabLine
				.getLayoutParams();
		lp.leftMargin = (int) position * (screenWidth / 3);
		mTabLine.setLayoutParams(lp);
		
		switch (position) {
		case 0:
			mBidwin.setTextColor(getResources().getColor(R.color.m_FF72CAE1));
			break;
		case 1:
			mBidtender
					.setTextColor(getResources().getColor(R.color.m_FF72CAE1));
			break;
		case 2:
			mBidfail.setTextColor(getResources().getColor(R.color.m_FF72CAE1));
			break;
		}
	}

	private void initView() {
		title_name = (TextView) mainView.findViewById(R.id.title_name);
		title_name.setText("竞标管理");
		mTabBidwin = (LinearLayout) mainView.findViewById(R.id.bid_win_ly);
		mTabBidtender = (LinearLayout) mainView
				.findViewById(R.id.bid_tender_ly);
		mTabBidfail = (LinearLayout) mainView.findViewById(R.id.bid_fail_ly);

		mBidwin = (TextView) mainView.findViewById(R.id.bid_win);
		mBidtender = (TextView) mainView.findViewById(R.id.bid_tender);
		mBidfail = (TextView) mainView.findViewById(R.id.bid_fail);

		tab03 = new Bid_fail();
		tab02 = new Bid_tender();
		tab01 = new Bid_win();
		mFragments.add(tab01);
		mFragments.add(tab02);
		mFragments.add(tab03);

	}

	private void initListener() {
		mTabBidwin.setOnClickListener(this);
		mTabBidtender.setOnClickListener(this);
		mTabBidfail.setOnClickListener(this);
	}
 
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bid_win_ly:
			tab01.Refresh();
			resetTextView(0);
			mViewPager.setCurrentItem(0);
			break;
		case R.id.bid_tender_ly:
			tab02.Refresh();
			resetTextView(1);
			mViewPager.setCurrentItem(1);
			break;
		case R.id.bid_fail_ly:
			tab03.Refresh();
			resetTextView(2);
			mViewPager.setCurrentItem(2);
			break;

		default:
			break;
		}
	}

}
