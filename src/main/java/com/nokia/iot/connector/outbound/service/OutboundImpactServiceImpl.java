package com.nokia.iot.connector.outbound.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.nokia.iot.connector.config.credentials.UserCredentials;
import com.nokia.iot.connector.inbound.domain.MessageOutDevice;
import com.nokia.iot.connector.inbound.domain.MessageOutDeviceType;
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
	
	Gson gson = new Gson();

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

	@Override
	public String registerConnectorToWatson() {
		LOGGER.debug("registerConnectorToWatson >>");
		ResponseEntity<String> deviceTypeResponse = null;
		String clientId = null;
		try {
			MessageOutDeviceType devType = new MessageOutDeviceType();
			devType = generateConnectorDeviceType();
			LOGGER.debug("Check if connector is already registered with watson for this instance");
			deviceTypeResponse = checkWatsonDeviceType(devType.getId());
			
			//404 means not found then silently create deviceType
			if(null != deviceTypeResponse && deviceTypeResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
				LOGGER.debug("Connector type is not created in Watson, hence creating one");
				String devInfoJson = restClient.getJsonFromObject(devType);
				ResponseEntity<String> createDevTypeResponse = restClient.doPost(WATSON_CREATE_DEVICE_TYPE_URI, devInfoJson, 
						restClient.getWatsonRestServerUrl(), userCred.getWatsonAuthorization(), restClient.getWatsonRestClient());
				
				if(null != createDevTypeResponse && createDevTypeResponse.getStatusCode() == HttpStatus.CREATED) {
					LOGGER.debug("Device Type is not created yet, hence creating one");
					MessageOutDevice connectorDevice = generateConnectorDevice();
					String deviceJson = restClient.getJsonFromObject(connectorDevice);
					String uri = WATSON_CREATE_DEVICE_URI;
					uri = uri.replaceAll("<device_type>", devType.getId());
					
					ResponseEntity<String> createdeviceResponse = restClient.doPost(uri, deviceJson , 
							restClient.getWatsonRestServerUrl(), userCred.getWatsonAuthorization()
							, restClient.getWatsonRestClient());
					LOGGER.debug("Create device response from watson {} "+createdeviceResponse.getBody());
					String response = createdeviceResponse.getBody();
					Map<String,Object> responseMap = gson.fromJson(response, Map.class);
					clientId = (String) responseMap.get("clientId");
					LOGGER.debug("clientId obtained from response "+clientId);
				} 
			} else {
				LOGGER.debug("DeviceType is already created..Checking if device already exist");
				
				String uri = WATSON_CREATE_DEVICE_URI;
				uri = uri.replaceAll("<device_type>", devType.getId())+"/"+CONNECTOR_DEVICE_ID;
				ResponseEntity<String> existingDeviceResp = restClient.doGet(uri ,restClient.getWatsonRestServerUrl(), userCred.getWatsonAuthorization()
						, restClient.getWatsonRestClient());
				String response = existingDeviceResp.getBody();
				Map<String,Object> responseMap = gson.fromJson(response, Map.class);
				clientId = (String) responseMap.get("clientId");
				LOGGER.debug("clientId obtained from response "+clientId);
			}
		} catch(Exception e) {
			LOGGER.error("Exception in registerConnectorToWatson "+e);
		}
		return clientId;
		
	}

	@Override
	public ResponseEntity<String> checkWatsonDeviceType(String devType)
			throws Exception {
		LOGGER.debug("checkWatsonDeviceType >> for device type "+devType);
		ResponseEntity<String> deviceTypeResponse = null;
		try {
			deviceTypeResponse = restClient.doGet(WATSON_CREATE_DEVICE_TYPE_URI+"/"+devType, 
					restClient.getWatsonRestServerUrl(), userCred.getWatsonAuthorization(), restClient.getWatsonRestClient());
		} catch(Exception e) {
			LOGGER.error("Exception in checkWatsonDeviceType for devType "+devType+" e "+e);
			throw new Exception(e.getMessage());
		}
		return deviceTypeResponse;
	}

	@Override
	public MessageOutDeviceType generateConnectorDeviceType() {
		LOGGER.debug("generateConnectorDeviceType >> ");
		MessageOutDeviceType outDeviceType = null;
		try {
			outDeviceType = new MessageOutDeviceType();
			outDeviceType.setClassId(CONNECTOR_CLASS_ID);
			outDeviceType.setId(CONNECTOR_DEVICE_TYPE_ID);
		} catch(Exception e) {
			LOGGER.error("Error in generateConnectorDeviceType");
		}
		return outDeviceType;
	}

	@Override
	public MessageOutDevice generateConnectorDevice() {
		LOGGER.debug("generateConnectorDeviceType >> ");
		MessageOutDevice outDevice = null;
		try {
			outDevice = new MessageOutDevice();
			outDevice.setDeviceId(CONNECTOR_DEVICE_ID);
			LOGGER.debug("Returning device object as "+outDevice.toString());
		} catch(Exception e) {
			LOGGER.error("Error in generateConnectorDeviceType");
		}
		return outDevice;
	}
	
}

