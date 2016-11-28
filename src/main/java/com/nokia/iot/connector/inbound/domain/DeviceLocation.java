package com.nokia.iot.connector.inbound.domain;

import java.util.Date;

public class DeviceLocation {

	private int longitude;
	
	private int latitude;
	
	private int elevation;
	
	private int accuracy;
	
	private Date measuredDateTime = new Date();

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getElevation() {
		return elevation;
	}

	public void setElevation(int elevation) {
		this.elevation = elevation;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public Date getMeasuredDateTime() {
		return measuredDateTime;
	}

	public void setMeasuredDateTime(Date measuredDateTime) {
		this.measuredDateTime = measuredDateTime;
	}

	@Override
	public String toString() {
		return "DeviceLocation [longitude=" + longitude + ", latitude="
				+ latitude + ", elevation=" + elevation + ", accuracy="
				+ accuracy + ", measuredDateTime=" + measuredDateTime + "]";
	}
}
