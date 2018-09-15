package com.apps.items;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemRestaurant implements Serializable{

 	private String id, name, image, address, monday, tuesday, wednesday, thursday, friday, saturday, sunday, cid, cimage, cname;
	private int total_rating;
	private float avgRatings;
	private ArrayList<ItemReview> arrayList_review;

	public ItemRestaurant(String id, String name, String image, String address, float avgRatings, int total_rating, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday, String cid, String cimage, String cname, ArrayList<ItemReview> arrayList_review) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.address = address;
		this.avgRatings = avgRatings;
		this.total_rating = total_rating;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
		this.cid = cid;
		this.cimage = cimage;
		this.cname = cname;
		this.arrayList_review = arrayList_review;
	}

	public ItemRestaurant(String id, String name, String image, String address, float avgRatings, int total_rating) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.address = address;
		this.total_rating = total_rating;
		this.avgRatings = avgRatings;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMonday() {
		return monday;
	}

	public void setMonday(String monday) {
		this.monday = monday;
	}

	public String getTuesday() {
		return tuesday;
	}

	public void setTuesday(String tuesday) {
		this.tuesday = tuesday;
	}

	public String getWednesday() {
		return wednesday;
	}

	public void setWednesday(String wednesday) {
		this.wednesday = wednesday;
	}

	public String getThursday() {
		return thursday;
	}

	public void setThursday(String thursday) {
		this.thursday = thursday;
	}

	public String getFriday() {
		return friday;
	}

	public void setFriday(String friday) {
		this.friday = friday;
	}

	public String getSaturday() {
		return saturday;
	}

	public void setSaturday(String saturday) {
		this.saturday = saturday;
	}

	public String getSunday() {
		return sunday;
	}

	public void setSunday(String sunday) {
		this.sunday = sunday;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCimage() {
		return cimage;
	}

	public void setCimage(String cimage) {
		this.cimage = cimage;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public int getTotalRating() {
		return total_rating;
	}

	public void setTotalRating(int total_rating) {
		this.total_rating = total_rating;
	}

	public float getAvgRatings() {
		return avgRatings;
	}

	public void setAvgRatings(float avgRatings) {
		this.avgRatings = avgRatings;
	}

	public ArrayList<ItemReview> getArrayListReview() {
		return arrayList_review;
	}

	public void setArrayListReview(ArrayList<ItemReview> arrayList_review) {
		this.arrayList_review = arrayList_review;
	}
}
