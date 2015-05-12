//package com.auction.album;
//
//import java.util.ArrayList;
//
//import com.auction.client.R;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.LoaderManager.LoaderCallbacks;
//import android.content.Loader;
//import android.database.Cursor;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.util.SparseArray;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.CheckBox;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.ImageView.ScaleType;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//@SuppressLint("NewApi")
//public class PictureMultiSelectActivity extends Activity implements
//		LoaderCallbacks<Cursor>, OnClickListener {
//
//	private int IMAGEVIEW_WIDTH = 0;// 每张图片View的宽度
//	private String ALBUM_ALL;
//	private TextView tvAlbum;// 相册按钮
//	private TextView tvDisplay;// 预览按钮
//	private RelativeLayout rlAlbum;// 相册背景布局
//	private ListView lvAlbum;// 相册listView
//	private GridView mGridView;// 图片gridView
//
//	private ArrayList<String> pathList = new ArrayList<String>();// 照片路径的list
//	private SparseArray<String> buckSpareseArray = new SparseArray<String>();// 相册名字SparseArray
//	private SparseArray<String> buckPageSpareseArray = new SparseArray<String>();// 相册封面SparseArray
//
//	private ArrayList<String> selectedPathList = new ArrayList<String>();
//	private AlbumGridAdapter picGridAdapter;
//
//	int defRes = R.drawable.default_face_small;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//	}
//
//	private void initView() {
//		// mGridView = (GridView) findViewById(R.id.gvPic);
//		// mGridView.setAdapter(picGridAdapter);
//		// mGridView.setOnItemClickListener(pictureClickListener);
//		//
//		// tvDisplay = (TextView) findViewById(R.id.tvDisplay);
//		// tvDisplay.setOnClickListener(this);
//		//
//		// tvAlbum = (TextView) findViewById(R.id.tvAlbum);
//		// tvAlbum.setOnClickListener(this);
//		// rlAlbum = (RelativeLayout) findViewById(R.id.rlAlbum);
//		// lvAlbum = (ListView) findViewById(R.id.lvAlbum);
//		// lvAlbum.setAdapter(albumAdapter);
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onLoaderReset(Loader<Cursor> arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	// 通过屏幕的宽度,计算图片的显示大小
//	private int getWith() {
//		if (IMAGEVIEW_WIDTH == 0) {
//			DisplayMetrics dm = new DisplayMetrics();
//			dm = this.getResources().getDisplayMetrics();
//			IMAGEVIEW_WIDTH = (dm.widthPixels - 4) / 3;
//		}
//
//		return IMAGEVIEW_WIDTH;
//	}
//
//	// 相册Grid的适配器
//	private class AlbumGridAdapter extends BaseAdapter {
//
//		private ArrayList<String> albumList;
//		RelativeLayout.LayoutParams params;
//
//		public AlbumGridAdapter(ArrayList<String> list) {
//			this.albumList = list;
//			params = new RelativeLayout.LayoutParams(getWith(), getWith());
//		}
//
//		@Override
//		public int getCount() {
//			return albumList == null ? 0 : albumList.size();
//		}
//
//		@Override
//		public Object getItem(int arg0) {
//
//			return albumList == null ? null : albumList.get(arg0);
//		}
//
//		@Override
//		public long getItemId(int arg0) {
//
//			return arg0;
//		}
//
//		@Override
//		public View getView(int arg0, View arg1, ViewGroup arg2) {
//			if (arg1 == null) {
//				arg1 = View.inflate(PictureMultiSelectActivity.this,
//						R.layout.multi_select_item, null);
//			}
//
//			ViewHolder holder = new ViewHolder();
//			initViewHolder(holder, arg1, arg0);
//
//			return arg1;
//		}
//
//		private void initViewHolder(ViewHolder holder, View arg1, int position) {
//			String path = pathList.get(position);
//			holder.rlContent = (RelativeLayout) arg1
//					.findViewById(R.id.rlContent);
//			holder.rlContent.setLayoutParams(params);
//			holder.mCheckBox = (CheckBox) arg1.findViewById(R.id.cbSelect);
//			holder.mImageView = (ImageView) arg1.findViewById(R.id.ivImage);
//			holder.mImageView.setLayoutParams(params);
//
//			if (path.equals(ALBUM_ALL)) {// 照相
//
//				holder.mCheckBox.setVisibility(View.GONE);
//				holder.mImageView.setScaleType(ScaleType.CENTER);
//
//				ImageViewUtil.getDefault().loadImage(
//						"drawable://" + R.drawable.album_take_photo_icon,
//						holder.mImageView, defRes, defRes);
//			} else {
//				holder.mImageView.setScaleType(ScaleType.CENTER_CROP);
//
//				holder.mImageView.setTag(position);
//				ImageViewUtil.getDefault().loadImage(PathUtil.getFILEPrefix() + path,
//						holder.mImageView, defRes, defRes);
//
//				int colorId = selectedPathList.contains(path) ? R.color.c_33000000
//						: R.color.transparent;
//				holder.rlContent.setBackgroundColor(getResources().getColor(
//						colorId));
//
//				if (IS_CROP || IS_ONE_CHOICE) {
//					holder.mCheckBox.setVisibility(View.GONE);
//				} else {
//					holder.mCheckBox.setVisibility(View.VISIBLE);
//					holder.mCheckBox.setTag(VIEW_TAG, holder);
//					holder.mCheckBox.setTag(PATH_TAG, path);
//
//					holder.mCheckBox.setChecked(selectedPathList.contains(path));
//					holder.mCheckBox
//							.setOnCheckedChangeListener(mCheckedChangeListener);
//					holder.mCheckBox.setOnTouchListener(mTouchListener);
//				}
//
//			}
//
//		}
//		private class ViewHolder {
//			public RelativeLayout rlContent;
//			public ImageView mImageView;
//			public CheckBox mCheckBox;
//		}
//	}
//}
