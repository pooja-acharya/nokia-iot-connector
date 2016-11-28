package com.nokia.iot.connector.inbound.domain;


public class MessageOutDevice {

	private String deviceId;
	
	private String authToken = "12345_ASWss";
	
	private DeviceTypeDeviceInfo deviceInfo;
	
	private DeviceLocation location;
	
	private DeviceTypeMetadata metadata;
	
	public MessageOutDevice() {
		
	}
	
	public MessageOutDevice(Registration registration) {
		this.deviceId = getFormattedSerialNumber(registration.getSerialNumber());
		this.deviceInfo = new DeviceTypeDeviceInfo(registration);
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public DeviceTypeDeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceTypeDeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public DeviceLocation getLocation() {
		return location;
	}

	public void setLocation(DeviceLocation location) {
		this.location = location;
	}

	public DeviceTypeMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(DeviceTypeMetadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public String toString() {
		return "MessageOutDevice [deviceId=" + deviceId + ", authToken="
				+ authToken + ", deviceInfo=" + deviceInfo + ", location="
				+ location + ", metadata=" + metadata + "]";
	}
	
	public String getFormattedSerialNumber(String serialNum){
		String newSerial = serialNum.replaceAll(":", "-");
		return newSerial;
	}
}
