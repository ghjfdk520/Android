package com.auction.common;
public class Constants {
	public static boolean DEBUG=true;
	public static int TOTAL_COUNT = 4; // 添加物品照片数量
	public static final String FRAGMENT_INDEX = "FRAGMENT_INDEX";
	public static final String FRAGMENT_REGISTER = "FRAGMENT_REGISTER";
	public static final int REQUEST_CODE_PHOTOGRAPH = 11;
	public static final int REQUEST_CODE_LOCPIC = 12;
    public static final int REFRESH_COMPLETE = 15;
	public static final int REQUEST_CODE_RETURN = 13;
    public static final int REFRESH_IMAGEVIEWPAGER=16;
    public static final int RECORD_NULL= 17;
    public static final int RECORD_SUCCESS=18;
    public static final int UPDATE_PRICE=17;
	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public enum auctionKind {
		JPG("电脑类"), PNG("手机类"), MP3("兴趣爱好"), MP4("服饰鞋品"), GP3(
				"file.3gp");
		private String name;

		private auctionKind(String profixName) {
			this.name = profixName;
		}

		public String getName() {
			return name;
		}
	}
	public static enum State {
		SELLOFF, MANAGER, INFORMATION,ITEMSPACE,BIDFRAGMENT,ALARMFRAGMENT
	}
}
