package com.science.strangertofriend.bean;

import java.io.Serializable;

/**
 * @description
 * 
 * @author ÐÒÔËScience ³ÂÍÁŸö
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-5-3
 * 
 */

public class LocationMenList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -477707387657371625L;

	private String userObjectId;
	private String username;
	private double latitude;
	private double longtitude;

	public LocationMenList() {
	}

	public LocationMenList(String userObjectId, String username,
			double latitude, double longtitude) {
		this.userObjectId = userObjectId;
		this.username = username;
		this.latitude = latitude;
		this.longtitude = longtitude;
	}

	public String getUserObjectId() {
		return userObjectId;
	}

	public void setUserObjectId(String userObjectId) {
		this.userObjectId = userObjectId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

}
