package com.nokia.iot.connector.inbound.domain;

public class DeviceTypeMetadata {

	private String customField1;
	
	private String customField2;

	public String getCustomField1() {
		return customField1;
	}

	public void setCustomField1(String customField1) {
		this.customField1 = customField1;
	}

	public String getCustomField2() {
		return customField2;
	}

	public void setCustomField2(String customField2) {
		this.customField2 = customField2;
	}

	@Override
	public String toString() {
		return "DeviceTypeMetadata [customField1=" + customField1
				+ ", customField2=" + customField2 + "]";
	}
}
