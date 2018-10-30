package com.techelevator.model;

import java.time.Month;

public class Campground {

	private long campgroundId;
	private int parkId;
	private String name;
	private String openFromMonth;
	private String openToMonth;
	private double dailyFee;
	
	
	public long getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(long campgroundId) {
		this.campgroundId = campgroundId;
	}
	public int getParkId() {
		return parkId;
	}
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpenFromMonth() {
		return openFromMonth;
	}
	public void setOpenFromMonth(String openFromMonth) {
		this.openFromMonth = openFromMonth;
	}
	public String getOpenToMonth() {
		return openToMonth;
	}
	public void setOpenToMonth(String openToMonth) {
		this.openToMonth = openToMonth;
	}
	public double getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(double dailyFee) {
		this.dailyFee = dailyFee;
	}
	public String openMonthToString() {
		int openFromMonth = Integer.parseInt(this.openFromMonth);
		String om = Month.of(openFromMonth).toString();
		return om.substring(0,1).toUpperCase() + om.substring(1).toLowerCase();
	}
	public String closeMonthToString() {
		int openToMonth = Integer.parseInt(this.openToMonth);
		String cm = Month.of(openToMonth).toString();
		return cm.substring(0,1).toUpperCase() + cm.substring(1).toLowerCase();
	}
	public String toString() {
		String df = String.format("%.2f", this.dailyFee);
		return String.format("%-32s%-10s%-13s%-20s", this.name, openMonthToString(), closeMonthToString(), "$"+df);
	}
}
