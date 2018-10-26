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
	public String toString() {
		int openFromMonth = Integer.parseInt(this.openFromMonth);
		int openToMonth = Integer.parseInt(this.openToMonth);
		String om = Month.of(openFromMonth).toString();
		String omConversion = om.substring(0,1).toUpperCase() + om.substring(1).toLowerCase();
		String cm = Month.of(openToMonth).toString();
		String cmConversion = cm.substring(0,1).toUpperCase() + cm.substring(1).toLowerCase();
		
		String df = String.format("%.2f", this.dailyFee);
		String name = String.format("%-32s", this.name);
		String open = String.format("%-10s", omConversion);
		String close = String.format("%-13s", cmConversion);
		String fee = String.format("%-20s", "$"+df);
		return name+open+close+fee;
	}
}
