package com.nokia.iot.connector.inbound.domain;

import com.google.gson.annotations.SerializedName;

public class MessageOutDeviceType {

	private String id = "nokiaImp";
	
	private String description;
	
	private String classId;
	
	@SerializedName("deviceInfo")
	private DeviceTypeDeviceInfo deviceInfo;
	
	@SerializedName("metadata")
	private DeviceTypeMetadata metadata;

	public MessageOutDeviceType(Registration reg) {
		//this.id+= "_"+getFormattedId(reg.getMake());
		this.deviceInfo = new DeviceTypeDeviceInfo();
		this.description = "Nokia IMPACT devices";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public DeviceTypeDeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceTypeDeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public DeviceTypeMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(DeviceTypeMetadata metadata) {
		this.metadata = metadata;
	}
	public String getFormattedId(String make) {
		return make.replaceAll(" ", "-");
	}
}
