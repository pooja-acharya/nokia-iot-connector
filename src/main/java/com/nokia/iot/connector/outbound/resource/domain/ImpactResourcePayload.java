package com.nokia.iot.connector.outbound.resource.domain;

import com.nokia.iot.connector.inbound.domain.MqttPayload;

/**
 * 
 * @author pacharya
 * Example: 
 * "serialNumber":"value",
	"resourcePath":"value",
	"operation":"read"
 */
public class ImpactResourcePayload {
	
	private String serialNumber;
	
	private String resourcePath;
	
	private String operation;
	
	private String resourceValue;

	public ImpactResourcePayload(MqttPayload payload, String resourceOperationType) {
		this.serialNumber = getFormattedSerialNumber(payload.getDeviceId());
		this.resourcePath = payload.getResourceId();
		this.operation = resourceOperationType;
		this.resourceValue = payload.getResourceValue();
	}
	
	public ImpactResourcePayload() {
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getResourceValue() {
		return resourceValue;
	}

	public void setResourceValue(String resourceValue) {
		this.resourceValue = resourceValue;
	}

	@Override
	public String toString() {
		return "ImpactResourcePayload [serialNumber=" + serialNumber
				+ ", resourcePath=" + resourcePath + ", operation=" + operation
				+ ", resourceValue=" + resourceValue + "]";
	}
	
	public String getFormattedSerialNumber(String serialNum){
		String newSerial = serialNum.replaceAll("-", ":");
		return newSerial;
	}
}
