package com.nokia.iot.connector.inbound.domain;


/**
 * example:
 * 		{"event":"resource","payload":"{"resourceRequest": {"serialNumber":"value","resourcePath":"value","operation":"read"}}"}
 * @author pacharya
 *
 */
public class MqttPayload {
	
	
	private String method;
	
	private String deviceId;
	
	private String resourceId;
	
	private String resourceValue;
	
	private String payload;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceValue() {
		return resourceValue;
	}

	public void setResourceValue(String resourceValue) {
		this.resourceValue = resourceValue;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "MqttPayload [method=" + method + ", deviceId=" + deviceId
				+ ", resourceId=" + resourceId + ", resourceValue="
				+ resourceValue + ", payload=" + payload + "]";
	}
}
