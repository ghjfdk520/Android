//package com.auction.client.fragment;
//
//import java.util.Arrays;
//import java.util.LinkedList;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.widget.AbsListView.OnScrollListener;
//import android.widget.AdapterView.OnItemClickListener;
//
//import com.auction.client.R;
////import com.handmark.pulltorefresh.library.PullToRefreshListView;
////import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
//
//public class selloff_text extends Fragment{
//
//	static final int MENU_MANUAL_REFRESH = 0;
//	static final int MENU_DISABLE_SCROLL = 1;
//
//	private LinkedList<String> mListItems;
////	private PullToRefreshListView mPullRefreshListView;
//	private ArrayAdapter<String> mAdapter;
//	private String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
//			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
//			"Allgauer Emmentaler", "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
//			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
//			"Allgauer Emmentaler" };
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		
//		View v =inflater.inflate(R.layout.selloff, null);
//
//		
//		
//		mPullRefreshListView = (PullToRefreshListView) v.findViewById(R.id.selloff_list);
//		// Set a listener to be invoked when the list should be refreshed.
//		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
//			@Override
//			public void onRefresh() {
//				// Do work to refresh the list here.
//				new GetDataTask().execute();
//			}
//		});
//		
//		
//		
//		ListView actualListView = mPullRefreshListView.getRefreshableView();
// 
//		actualListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				Toast.makeText(getActivity(), arg3+"", Toast.LENGTH_SHORT).show();
//			}
//		});
//		
//		
//           actualListView.setOnScrollListener(new OnScrollListener() {
//			
//			@Override
//			public void onScrollStateChanged(AbsListView arg0, int arg1) {
//				// TODO Auto-generated method stub
//				Log.d("gudong", arg1+"");
//				//Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT).show();
//			}
//			
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//				// TODO Auto-generated method stub
//				final int totalCount = firstVisibleItem + visibleItemCount;
//				int currentPage = totalCount / 20;
//				int nextPage = currentPage + 1;
//				
//				if(totalCount == totalItemCount && nextPage <= 6){
//					//Toast.makeText(getActivity(), "到尾部", Toast.LENGTH_SHORT).show();;
//					Log.d("gudong", "滚到尾部");
//				}
//			}
//		});
//		
//		mListItems = new LinkedList<String>();
//		mListItems.addAll(Arrays.asList(mStrings));
//
//		mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mListItems);
//		
//		// You can also just use setListAdapter(mAdapter)
//		actualListView.setAdapter(mAdapter);
//		
//		
//		return v;
////	}
////	
//	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
//
//		@Override
//		protected String[] doInBackground(Void... params) {
//			// Simulates a background job.
//			try {
//				Thread.sleep(4000);
//			} catch (InterruptedException e) {
//			}
//			return mStrings;
//		}
//
//		@Override
//		protected void onPostExecute(String[] result) {
//			mListItems.addFirst("Added after refresh...");
//			mAdapter.notifyDataSetChanged();
//
//			// Call onRefreshComplete when the list has been refreshed.
//			mPullRefreshListView.onRefreshComplete();
//
//			super.onPostExecute(result);
//		}
//	}
//	
//	
//	}
//	
//	
//}
