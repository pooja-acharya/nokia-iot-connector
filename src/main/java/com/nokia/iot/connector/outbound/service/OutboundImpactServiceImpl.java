package com.nokia.iot.connector.outbound.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nokia.iot.connector.config.credentials.UserCredentials;
import com.nokia.iot.connector.outbound.resource.domain.ImpactResource;
import com.nokia.iot.connector.outbound.subscription.domain.ImpactSubscription;
import com.nokia.iot.connector.outbound.subscription.domain.ImpactSubscriptionPayload;
import com.nokia.iot.connector.utils.IRestClient;
/**
 * 
 * @author pacharya
 *
 */
@Service
public class OutboundImpactServiceImpl implements IOutboundImpactService {

	@Autowired
	IRestClient restClient;

	@Autowired
	UserCredentials userCred;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OutboundImpactServiceImpl.class);

	@Override
	public ResponseEntity<String> sendResourceRequestToImpact(
			ImpactResource resourceReq) throws Exception {
		LOGGER.debug("OutboundImpactServiceImpl.sendResourceRequestToImpact >> with parameters "
				+ resourceReq.toString());
		ResponseEntity<String> resourceResponse = null;
		try {
			Map<String, String> requestPayload = resourceReq
					.contructResourcePayload(resourceReq);
			String methodType = requestPayload.get("METHOD_TYPE");

			/*
			 * if("GET".equalsIgnoreCase(methodType)) {
			 * 
			 * } else if("PUT".equalsIgnoreCase(methodType)) {
			 * 
			 * } else if("POST".equalsIgnoreCase(methodType)) {
			 * 
			 * } else if("DELETE".equalsIgnoreCase(methodType)) { }
			 */
			switch (methodType) {
			case "GET":
				LOGGER.debug("Request method is GET. Passing uri as "
						+ requestPayload.get("URI"));
				try {
					resourceResponse = restClient.doGet(
							requestPayload.get("URI"),
							restClient.getImpactRestServerUrl(),
							userCred.getImpactAuthorization(), restClient.getImpactRestClient());
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
				break;
			case "POST":
				LOGGER.debug("Request method is POST. Passing uri as "
						+ requestPayload.get("URI") + " and payload as "
						+ requestPayload.get("BODY"));
				try {
					resourceResponse = restClient.doPost(
							requestPayload.get("URI"),
							requestPayload.get("BODY"),
							restClient.getImpactRestServerUrl(),
							userCred.getImpactAuthorization(), restClient.getImpactRestClient());
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
				break;
			case "PUT":
				LOGGER.debug("Request method is PUT. Passing uri as "
						+ requestPayload.get("URI") + " and payload as "
						+ requestPayload.get("BODY"));
				try {
					resourceResponse = restClient.doPut(
							requestPayload.get("URI"),
							requestPayload.get("BODY"),
							restClient.getImpactRestServerUrl(),
							userCred.getImpactAuthorization(), restClient.getImpactRestClient());
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
				break;
			case "DELETE":
				LOGGER.debug("Request method is DELETE. Passing uri as "
						+ requestPayload.get("URI") + " and payload as "
						+ requestPayload.get("BODY"));
				try {
					resourceResponse = restClient.doDelete(
							requestPayload.get("URI"),
							requestPayload.get("BODY"),
							restClient.getImpactRestServerUrl(),
							userCred.getImpactAuthorization(), restClient.getImpactRestClient());
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
				break;
			default:
				LOGGER.debug("Invalid operation, hence doing nothing");
				break;
			}
		} catch (Exception e) {
			LOGGER.error("Exception in sendResourceRequestToImpact " + e);
			throw new Exception(e.getMessage());
		}
		return resourceResponse;
	}

	@Override
	public ResponseEntity<String> sendSubscriptionRequestToImpact(
			ImpactSubscription subscriptionReq) throws Exception {
		LOGGER.debug("OutboundImpactServiceImpl.sendSubscriptionRequestToImpact >> with parameters "
				+ subscriptionReq.toString());
		ResponseEntity<String> subscriptionResponse = null;
		try {
			Map<String, String> requestPayload = subscriptionReq
					.constructSubscriptionPayload(subscriptionReq);
			subscriptionResponse = restClient.doPost(requestPayload.get("URI"),
					requestPayload.get("BODY"),
					restClient.getImpactRestServerUrl(),
					userCred.getImpactAuthorization(), restClient.getImpactRestClient());
		} catch (Exception e) {
			LOGGER.error("Exception in sendSubscriptionRequestToImpact " + e);
			throw new Exception(e.getMessage());
		}
		return subscriptionResponse;
	}

	@Override
	public ImpactSubscription getDefaultLceSubscriptionRequest(String groupName) {
		LOGGER.debug("getDefaultLceSubscriptionRequest for groupName "+groupName);
		ImpactSubscription subscriptionDefault = new ImpactSubscription();
		try {
			ImpactSubscriptionPayload subscriptionPayload = new ImpactSubscriptionPayload();
			subscriptionPayload.setGroupName(groupName);
			subscriptionPayload.setDeletionPolicy(1);
			subscriptionPayload.setType("lifecycleEvents");
			subscriptionPayload.setEvents(new ArrayList<String>(Arrays.asList("Registration")));
			subscriptionDefault.setSubscriptionRequest(subscriptionPayload);
		} catch(Exception e) {
			LOGGER.error("getDefaultLceSubscriptionRequest Exception "+e);
		}
		return subscriptionDefault;
	}

	@Override
	public ResponseEntity<String> sendGetEndpointDetails(String serialNumber) throws Exception{
		LOGGER.debug("OutboundImpactServiceImpl.sendGetEndpointDetails >> with parameters "
				+ serialNumber);
		ResponseEntity<String> getEndpointDetailsResponse = null;
		try {
			String uri = GET_ENDPOINTS_URI+serialNumber;
			getEndpointDetailsResponse = restClient.doGet(uri,
					restClient.getImpactRestServerUrl(),
					userCred.getImpactAuthorization(), restClient.getImpactRestClient());
		} catch (Exception e) {
			LOGGER.error("Exception in sendGetEndpointDetails " + e);
			throw new Exception(e.getMessage());
		}
		return getEndpointDetailsResponse;
	}
}

