package com.nokia.iot.connector.outbound.subscription.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.nokia.iot.connector.inbound.domain.Response;

public class ImpactSubscriptionPayload {

    private String make;

    private String model;

    private String firmwareVersion;

    private String groupName;

    private List<String> serialNumber;

    private List<Map<String, String>> resourceDetails;

    private String type;

    private List<String> events;

    private Integer deletionPolicy;

	public ImpactSubscriptionPayload(Response response, String groupName, List<Map<String, String>> resourceDetails) {
		this.groupName = groupName;
		this.serialNumber = new ArrayList<String>(Arrays.asList(response.getSerialNumber()));
		this.deletionPolicy = 1;
		this.resourceDetails = resourceDetails;
		this.type = "resources";
	}
	
	public ImpactSubscriptionPayload() {
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<String> getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(List<String> serialNumber) {
		this.serialNumber = serialNumber;
	}

	public List<Map<String, String>> getResourceDetails() {
		return resourceDetails;
	}

	public void setResourceDetails(List<Map<String, String>> resourceDetails) {
		this.resourceDetails = resourceDetails;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getEvents() {
		return events;
	}

	public void setEvents(List<String> events) {
		this.events = events;
	}

	public Integer getDeletionPolicy() {
		return deletionPolicy;
	}

	public void setDeletionPolicy(Integer deletionPolicy) {
		this.deletionPolicy = deletionPolicy;
	}

	@Override
	public String toString() {
		return "ImpactSubscriptionPayload [make=" + make + ", model=" + model
				+ ", firmwareVersion=" + firmwareVersion + ", groupName="
				+ groupName + ", serialNumber=" + serialNumber
				+ ", resourceDetails=" + resourceDetails + ", type=" + type
				+ ", events=" + events + ", deletionPolicy=" + deletionPolicy
				+ "]";
	}
}
