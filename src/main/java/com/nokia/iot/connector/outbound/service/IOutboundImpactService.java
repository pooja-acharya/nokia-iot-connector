package com.nokia.iot.connector.outbound.service;

import org.springframework.http.ResponseEntity;

import com.nokia.iot.connector.outbound.resource.domain.ImpactResource;
import com.nokia.iot.connector.outbound.subscription.domain.ImpactSubscription;

public interface IOutboundImpactService {
	
	static String GET_ENDPOINTS_URI = "/m2m/endpoints/";
	
	ImpactSubscription getDefaultLceSubscriptionRequest(String groupName);

	ResponseEntity<String> sendResourceRequestToImpact(
			ImpactResource resourceReq) throws Exception;

	ResponseEntity<String> sendSubscriptionRequestToImpact(
			ImpactSubscription subscriptionReq) throws Exception;

	ResponseEntity<String> sendGetEndpointDetails(String serialNumber) throws Exception;
}
