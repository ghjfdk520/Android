package com.auction.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.auction.client.entity.Item;
import com.auction.client.util.CommonFunction;
import com.auction.client.util.ImageViewUtil;
import com.auction.client.util.PhotoUtil;
import com.auction.common.Constants;
import com.auction.config.Common;
import com.auction.connector.BatchUploadManager;
import com.auction.connector.BatchUploadManager.BatchUploadCallBack;
import com.auction.connector.protocol.ItemHttpProtocol;
import com.auction.connector.HttpCallBack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Spinner;

@SuppressLint({ "ResourceAsColor", "NewApi" })
public class AddItem extends Activity implements HttpCallBack,
		BatchUploadCallBack,OnClickListener {
	private EditText item_name;
	private EditText init_price;
	private EditText item_desc;
	private Spinner time_spinner;
	private Spinner class_spinner;
	private LinearLayout llFirstRow;
	private BatchUploadManager bm;
	private TextView title_name;
	private boolean isSubmit = false;
	private final int UPDATE_IMAGE_LAYOUT_FALG = 0XFF;
	private PublishHandler mHandler = new PublishHandler();
	private int IMAGEVIEW_WIDTH = 0;// 每张图片View的宽度
	private String path = "";// 保存拍照返回的路径

	private final int MAX_IMAGE_COUNT = 4;// 最多可以添加多少张图片

	private long ADDITEMFLAG;
	// 保存每一个照片组件的List
	private ArrayList<View> imageLayoutList = new ArrayList<View>(
			MAX_IMAGE_COUNT);
	// 保存每一张照片的URL的List
	private ArrayList<String> imageUrlList = new ArrayList<String>(
			MAX_IMAGE_COUNT);

	private int marginLeft;// 图片的间距
	private int parentSize;// 图片的父View的大小
	private int imageSize;// 图片的大小

	protected long IMAGE_TASK_MASK = Long.MAX_VALUE ^ 7;// Image对应Task的Mask

	private String imgUrls = "";
	private Item item;
	private int kind_id;
	private int endtime;
	private ProgressDialog mProgressDialog;
	public static void launchActivity(Activity fromActivity,String user_id) {
		Intent i = new Intent(fromActivity, AddItem.class);
		i.putExtra("user_id", user_id);
		fromActivity.startActivity(i);
	}
	public static void launchforResultActivity(Activity fromActivity,String user_id) {
		Intent i = new Intent(fromActivity, AddItem.class);
		i.putExtra("user_id", user_id);
		fromActivity.startActivityForResult(i, 0x1111);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_additem);
		marginLeft = CommonFunction.dipToPx(this, 6);
		parentSize = (CommonFunction.getScreenPixWidth(this) - 8 * marginLeft) / 4;
		imageSize = (int) (parentSize - marginLeft * 1.2);
		init();
		initListener();
	}

	public void init() {
		item = new Item();
		time_spinner = (Spinner) findViewById(R.id.time_spinner);
		class_spinner = (Spinner) findViewById(R.id.class_spinner);
		title_name = (TextView) findViewById(R.id.title_name);
		title_name.setText("添加拍卖品");
		item_name = (EditText) findViewById(R.id.item_name);
		init_price = (EditText) findViewById(R.id.init_price);
		item_desc = (EditText) findViewById(R.id.item_desc);
		time_spinner = (Spinner) findViewById(R.id.time_spinner);
		class_spinner = (Spinner) findViewById(R.id.class_spinner);
		llFirstRow = (LinearLayout) findViewById(R.id.llFirstRow);
		findViewById(R.id.title_back).setOnClickListener(this);
		bm = new BatchUploadManager(this);
		initImageLayout();
	}

	public void initImageLayout() {
		llFirstRow.removeAllViews();
		imageLayoutList.clear();
		LinearLayout.LayoutParams paras = new LinearLayout.LayoutParams(
				parentSize, parentSize);
		paras.rightMargin = marginLeft;
		RelativeLayout.LayoutParams imageparas = new RelativeLayout.LayoutParams(
				imageSize, imageSize);
		imageparas.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
				RelativeLayout.TRUE);
		int count = imageUrlList.size();
		for (int i = 0; i < count; i++) {
			ViewGroup rootView = llFirstRow;
			View imageItemView = new View(this);
			imageItemView = View.inflate(this,
					R.layout.item_publish_image_item, null);
			imageItemView.setLayoutParams(paras);
			ImageView ivPic = (ImageView) imageItemView
					.findViewById(R.id.ivImage);
			ivPic.setLayoutParams(imageparas);
			ivPic.setTag(i);
			ivPic.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					System.out.println("添加照片");
				}
			});
			ivPic.setScaleType(ScaleType.CENTER_CROP);
			String url = "file://" + imageUrlList.get(i);

			ImageViewUtil.getDefault().loadImage(url, ivPic,
					R.drawable.ic_stub, R.drawable.ic_launcher);
			int wrap = LayoutParams.WRAP_CONTENT;
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
					wrap, wrap);
			p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			p.addRule(RelativeLayout.ALIGN_PARENT_TOP);

			ImageView ivDelete = (ImageView) imageItemView
					.findViewById(R.id.ivDelete);
			ivDelete.setTag(i);
			ivDelete.setLayoutParams(p);
			ivDelete.setOnClickListener(new DeleteBtnClickListener());

			imageLayoutList.add(imageItemView);

			rootView.addView(imageItemView);

		}

		int addViewPositon = imageUrlList.size();

		if (addViewPositon < MAX_IMAGE_COUNT) {
			ViewGroup rootView = llFirstRow;
			View imageAddView = new View(this);
			imageAddView.setBackgroundColor(R.color.white);
			imageAddView = View.inflate(this, R.layout.item_publish_image_item,
					null);

			imageAddView.setLayoutParams(paras);
			ImageView ivAdd = (ImageView) imageAddView
					.findViewById(R.id.ivImage);
			ivAdd.setLayoutParams(imageparas);

			ivAdd.setScaleType(ScaleType.CENTER);
			ivAdd.setBackgroundResource(R.drawable.dynamic_game_image_button_shape);
			ivAdd.setImageResource(R.drawable.dynamic_image_add);
			ivAdd.setOnClickListener(new AddBtnClickListener());

			ImageView ivDelete = (ImageView) imageAddView
					.findViewById(R.id.ivDelete);
			ivDelete.setVisibility(View.GONE);

			rootView.addView(imageAddView);
		}

	}

	public void initListener() {
		time_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				endtime = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		class_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				kind_id = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});
	}

	@Override
	public void onGeneralSuccess(String result, long flag) {
		// TODO Auto-generated method stub
		if(flag == ADDITEMFLAG){
			
			closeLoading();
			CommonFunction.Toast(this, "添加成功");
			Intent intent = new Intent();
			intent.putExtra("Refresh", true);
			intent.putExtra("item", result);
            setResult(0xf00, intent);
            isSubmit= false;
			finish();
		}
	}

	@Override
	public void onGeneralError(String e, long flag) {
		// TODO Auto-generated method stub

	}

	// 通过屏幕的宽度,计算图片的显示大小
	private int getWith() {
		if (IMAGEVIEW_WIDTH == 0) {
			DisplayMetrics dm = new DisplayMetrics();
			dm = this.getResources().getDisplayMetrics();
			IMAGEVIEW_WIDTH = (dm.widthPixels - 4) / 3;
		}

		return IMAGEVIEW_WIDTH;
	}

	class DeleteBtnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {

			int positon = (Integer) arg0.getTag();
			String url = imageUrlList.get(positon);
			if (!TextUtils.isEmpty(url)) {
				// 移除图片view + url
				imageUrlList.remove(positon);
				imageUrlList.trimToSize();

				initImageLayout();
			}
		}
	}

	class AddBtnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			// 跳转到选择图片的界面
			PhotoUtil.chooseImg(AddItem.this);
		}

	}

	public void submit(View v) {
		
		if(isSubmit) return;
		final long taskFlag = System.currentTimeMillis() & IMAGE_TASK_MASK;
		if (verify()) {
			isSubmit= true;
			showLoading("添加拍卖品...");
			if (imageUrlList.size() > 0) {
				bm.uploadImage(taskFlag, imageUrlList, 1, this);
			} else {
				item.setItem_img(imgUrls);
				ADDITEMFLAG = ItemHttpProtocol.addItem(this, this, item);
			}
		}
	}

	public void saveImage(Bitmap bitmap) {
		String sourcePath = CommonFunction.getSDPath() + "/proPics/"
				+ System.currentTimeMillis() + ".jpg";
		System.out.println(sourcePath);
		File uploadFile = new File(sourcePath);
		try {
			if (!uploadFile.exists()) {
               uploadFile.createNewFile();
			}
			// uploadFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(uploadFile);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bitmap.compress(CompressFormat.JPEG, 50, bos);
			bos.flush();
			fos.close();
			bos.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		imageUrlList.add(sourcePath);
	}

	private class PublishHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE_IMAGE_LAYOUT_FALG:
				initImageLayout();
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		PhotoUtil.dialog.dismiss();
		if (data != null && requestCode == Constants.REQUEST_CODE_PHOTOGRAPH) { // 拍照上传时
			Bundle extras = data.getExtras();
			if (extras != null) {
				saveImage((Bitmap) extras.get("data"));
				mHandler.sendEmptyMessage(UPDATE_IMAGE_LAYOUT_FALG);
			} else {
				Toast.makeText(this, "未找到图片", Toast.LENGTH_LONG).show();
			}
		} else if (data != null && requestCode == Constants.REQUEST_CODE_LOCPIC) {
			Uri uri = data.getData();
			try {
				String[] pojo = { MediaStore.Images.Media.DATA };

				Cursor cursor = this.getContentResolver().query(uri, pojo,
						null, null, null);
				if (cursor != null) {
					ContentResolver cr = this.getContentResolver();
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);
					imageUrlList.add(path);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mHandler.sendEmptyMessage(UPDATE_IMAGE_LAYOUT_FALG);
		}
	}

	@Override
	public void batchUploadSuccess(long taskFlag,
			ArrayList<String> serverUrlList) {
		// TODO Auto-generated method stub
		imgUrls = "";
		if (serverUrlList != null) {
			for (String s : serverUrlList) {
				imgUrls += s + ",";
			}
			item.setItem_img(imgUrls);
			ADDITEMFLAG = ItemHttpProtocol.addItem(this, this, item);
		}
	}

	@Override
	public void batchUploadFail(long taskFlag) {
		// TODO Auto-generated method stub

	}

	public boolean verify() {

		int id = 0;
		if (CommonFunction.isEmptyOrNullStr(item_name.getText().toString())) {
			id = R.id.item_name;
		} else if (CommonFunction.isEmptyOrNullStr(init_price.getText()
				.toString())) {
			id = R.id.init_price;
		} else if (CommonFunction.isEmptyOrNullStr(item_desc.getText()
				.toString())) {
			id = R.id.item_desc;
		}

		switch (id) {
		case R.id.item_name:
			CommonFunction.Toast(this, "名称不能为空");
			break;
		case R.id.init_price:
			CommonFunction.Toast(this, "价格不能为空");
			break;
		case R.id.item_desc:
			CommonFunction.Toast(this, "描述信息不能为空");
			break;
		default:
			item.setItem_name(item_name.getText().toString());
			item.setInit_price(Integer
					.parseInt(init_price.getText().toString()));
			item.setItem_desc(item_desc.getText().toString());
			item.setEndtime(endtime);
			item.setKind_id(kind_id);
			String user_id = getIntent().getStringExtra("user_id");
			item.setOwner_id(Integer.parseInt(user_id));
			item.setItem_img(imgUrls);
			return true;
		}
		return false;
	}
	
	public void showLoading(String message) {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(this, "", message, false,
					false);
		} else {
			mProgressDialog.setMessage(message);
			mProgressDialog.show();
		}
	}
	
	public void closeLoading() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.title_back:
			finish();
			break;

		default:
			break;
		}
	}
}
