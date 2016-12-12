package com.nokia.iot.connector.outbound.service;

import org.springframework.http.ResponseEntity;

import com.nokia.iot.connector.inbound.domain.MessageOutDevice;
import com.nokia.iot.connector.inbound.domain.MessageOutDeviceType;
import com.nokia.iot.connector.outbound.resource.domain.ImpactResource;
import com.nokia.iot.connector.outbound.subscription.domain.ImpactSubscription;

public interface IOutboundImpactService {
	
	static final String CONNECTOR_CLASS_ID = "Connector";
	
	static final String CONNECTOR_DEVICE_TYPE_ID = "nokiaConnector";
	
	static final String CONNECTOR_DEVICE_ID = "Nokia-Connector";
	
	static String GET_ENDPOINTS_URI = "/m2m/endpoints/";
	
	static String WATSON_CREATE_DEVICE_TYPE_URI = "device/types";
	
	static String WATSON_CREATE_DEVICE_URI = "device/types/<device_type>/devices";
	
	ImpactSubscription getDefaultLceSubscriptionRequest(String groupName);

	ResponseEntity<String> sendResourceRequestToImpact(
			ImpactResource resourceReq) throws Exception;

	ResponseEntity<String> sendSubscriptionRequestToImpact(
			ImpactSubscription subscriptionReq) throws Exception;

	ResponseEntity<String> sendGetEndpointDetails(String serialNumber) throws Exception;

	String registerConnectorToWatson();
	
	ResponseEntity<String> checkWatsonDeviceType(String devType) throws Exception;
	
	MessageOutDeviceType generateConnectorDeviceType();
	
	MessageOutDevice generateConnectorDevice();
}
