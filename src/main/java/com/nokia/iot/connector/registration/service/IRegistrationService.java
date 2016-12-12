package com.nokia.iot.connector.registration.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.nokia.iot.connector.inbound.domain.ImpactProperties;
import com.nokia.iot.connector.inbound.domain.Notification;
import com.nokia.iot.connector.inbound.domain.WatsonProperties;

public interface IRegistrationService {
	
	static String IMPACT_REGISTER_URI="/m2m/applications/registration";
	
	static String IMPACT_CALLBACK_URI="/inbound/callback";
	
	static String WATSON_CREATE_DEVICE_URI = "device/types/<device_type>/devices";
	
	static String WATSON_CREATE_DEVICE_TYPE_URI = "device/types";
	
	static String NOTIFICATION_DEVICE_TYPE = "nokiaImpact";
	
	static String OBSERVATION_EVENT_TYPE = "notify";
	
	static String RESPONSE_EVENT_TYPE = "response";
	
	static String ENDPOINT_DETAILS_EVENT_TYPE = "resource";
	
	static String NOTIFICATION_FORMAT = "json";

	ResponseEntity<String> registerToImpact(HttpServletRequest request) throws Exception;
	
	void publishCallbackData(Notification callbackPayload) throws Exception;

	void saveImpactProperty(ImpactProperties impactProp) throws Exception;
	
	void saveWatsonProperty(WatsonProperties watsonProp) throws Exception;
	
}
