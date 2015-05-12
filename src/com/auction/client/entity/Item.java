package com.auction.client.entity;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Serializable {
	private int item_id;
	private String item_name;
	private String item_remark;
	private String item_desc;
	private int kind_id;
	private int init_price;
	private int max_price;
	private int owner_id;
	private int winer_id;
	private String item_img;
	private int fiexd_price;
	private int state_id;
    private int other_img;
    private int endtime;
	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_remark() {
		return item_remark;
	}

	public void setItem_remark(String item_remark) {
		this.item_remark = item_remark;
	}

	public String getItem_desc() {
		return item_desc;
	}

	public void setItem_desc(String item_desc) {
		this.item_desc = item_desc;
	}

	public int getKind_id() {
		return kind_id;
	}

	public void setKind_id(int kind_id) {
		this.kind_id = kind_id;
	}

	public int getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}

	public int getWiner_id() {
		return winer_id;
	}

	public void setWiner_id(int winer_id) {
		this.winer_id = winer_id;
	}

	public String getItem_img() {
		return item_img;
	}

	public void setItem_img(String item_img) {
		this.item_img = item_img;
	}

	public int getState_id() {
		return state_id;
	}

	public void setState_id(int state_id) {
		this.state_id = state_id;
	}

	public int getInit_price() {
		return init_price;
	}

	public void setInit_price(int init_price) {
		this.init_price = init_price;
	}

	public int getMax_price() {
		return max_price;
	}

	public void setMax_price(int max_price) {
		this.max_price = max_price;
	}

	public int getFiexd_price() {
		return fiexd_price;
	}

	public void setFiexd_price() {
		this.fiexd_price = fiexd_price;
	}
    
	public int getOther_img() {
		return other_img;
	}

	public void setOther_img(int other_img) {
		this.other_img = other_img;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	public void setFiexd_price(int fiexd_price) {
		this.fiexd_price = fiexd_price;
	}

	public String toString() {
		return "item_id" + item_id + "item_desc" + item_desc + "item_img"
				+ item_img;
	}
}
