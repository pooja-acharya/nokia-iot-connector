package com.nokia.iot.connector.inbound.domain;

public class DeviceTypeDeviceInfo {

	private String serialNumber;
	
	private String manufacturer;
	
	private String model;
	
	private String deviceClass;
	
	private String description;
	
	private String fwVersion;
	
	private String hwVersion;
	
	private String descriptiveLocation;
	
	public DeviceTypeDeviceInfo() {
		
	}

	public DeviceTypeDeviceInfo(Registration registration) {
		this.description = registration.model;
		this.fwVersion = registration.firmwareVersion;
		this.manufacturer = registration.make;
		this.model = registration.model;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDeviceClass() {
		return deviceClass;
	}

	public void setDeviceClass(String deviceClass) {
		this.deviceClass = deviceClass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFwVersion() {
		return fwVersion;
	}

	public void setFwVersion(String fwVersion) {
		this.fwVersion = fwVersion;
	}

	public String getHwVersion() {
		return hwVersion;
	}

	public void setHwVersion(String hwVersion) {
		this.hwVersion = hwVersion;
	}

	public String getDescriptiveLocation() {
		return descriptiveLocation;
	}

	public void setDescriptiveLocation(String descriptiveLocation) {
		this.descriptiveLocation = descriptiveLocation;
	}

	@Override
	public String toString() {
		return "DeviceTypeDeviceInfo [serialNumber=" + serialNumber
				+ ", manufacturer=" + manufacturer + ", model=" + model
				+ ", deviceClass=" + deviceClass + ", description="
				+ description + ", fwVersion=" + fwVersion + ", hwVersion="
				+ hwVersion + ", descriptiveLocation=" + descriptiveLocation
				+ "]";
	}
}
