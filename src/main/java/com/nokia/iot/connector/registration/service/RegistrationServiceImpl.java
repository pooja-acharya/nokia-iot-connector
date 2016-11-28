package com.nokia.iot.connector.registration.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.nokia.iot.connector.inbound.domain.Notification;
import com.nokia.iot.connector.inbound.domain.Registration;
import com.nokia.iot.connector.inbound.domain.Report;
import com.nokia.iot.connector.inbound.domain.Resource;
import com.nokia.iot.connector.inbound.domain.Response;
import com.nokia.iot.connector.outbound.service.IOutboundImpactService;
import com.nokia.iot.connector.outbound.subscription.domain.ImpactSubscription;
import com.nokia.iot.connector.utils.IRestClient;
import com.nokia.iot.connector.utils.mqtt.MqttPublisher;
/**
 * 
 * @author pacharya
 *
 */
@Service
public class RegistrationServiceImpl implements IRegistrationService {

	@Autowired
	IRestClient restClient;

	@Autowired
	UserCredentials userCred;

	@Autowired
	MqttPublisher publisher;
	
	@Autowired
	IOutboundImpactService outboundApi;

	Gson gson = new Gson();

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RegistrationServiceImpl.class);

	@Override
	public ResponseEntity<String> registerToImpact(HttpServletRequest request)
			throws Exception {
		LOGGER.debug("RegistrationServiceImpl.registerToImpact >> ");
		ResponseEntity<String> registrationResponse = null;
		ResponseEntity<String> subscriptionResponse = null;
		try {
			String username = userCred.decodeBase64(userCred
					.getImpactCallbackUsername());
			String password = userCred.decodeBase64(userCred
					.getImpactCallbackPassword());
			String callbackAuth = userCred.encodeBase64(username + ":"
					+ password);
			String callbackUrl = request.getScheme() + "://" +
					request.getServerName() + ":" + request.getServerPort() +
					request.getContextPath();
			callbackUrl+= IMPACT_CALLBACK_URI;
			LOGGER.debug("Callback server URL as "+callbackUrl);
			Map<String, Object> registration = new HashMap<String, Object>();
			Map<String, String> authorizationMap = new HashMap<String, String>();

			authorizationMap.put("authorization", "Basic " + callbackAuth);
			authorizationMap.put("content-type", "application/json");
			/*registration.put("url",
					"http://mfusdev15.mformation.com:7001/m2m/impact/callback");*/
			registration.put("url",callbackUrl);
			registration.put("headers", authorizationMap);
			String registrationPayload = restClient.getJsonFromObject(registration);
			LOGGER.debug("RegistrationServiceImpl.registerToImpact >> sending {} "
					+ registrationPayload);

			registrationResponse = restClient.doPut(IMPACT_REGISTER_URI,
					registrationPayload, restClient.getImpactRestServerUrl(), 
					userCred.getImpactAuthorization(), restClient.getImpactRestClient());
			
			if(registrationResponse.getStatusCode().equals(HttpStatus.OK)) {
				LOGGER.debug("Registration is success, hence sending Lifecycle event subscription ");
				
				//Now send Lifecycle event subscription for group name mentioned in property file
				ImpactSubscription subscriptionReq = outboundApi.getDefaultLceSubscriptionRequest(restClient.getGroupName());
				
				LOGGER.debug("Creating Subscription for group "+restClient.getGroupName());
				subscriptionResponse = outboundApi.sendSubscriptionRequestToImpact(subscriptionReq);
				
				LOGGER.debug("Response from IMPACT "+subscriptionResponse.getBody());
			}
			
		} catch (Exception e) {
			LOGGER.error("Exception while registering to IMPACT "
					+ userCred.getImpactCallbackUsername() + " " + e);
			throw new Exception(e.getMessage());
		}
		return registrationResponse;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void publishCallbackData(Notification callbackPayload)
			throws Exception {
		LOGGER.debug("RegistrationServiceImpl.publishCallbackData callback data received "
				+ callbackPayload.toString());
		try {
			if (callbackPayload.getReports() != null) {
				if (!callbackPayload.getReports().isEmpty()) {
					publisher.initMqttClientForPublish();
					
					List<Report> reportList = callbackPayload.getReports();
					for(Report report : reportList) {
						String reportPayload = restClient.getJsonFromObject(report);
						LOGGER.debug("publishing REPORT(observation) payload as "
								+ reportPayload);
						publisher.publishEvents(publisher.getTopicName(NOTIFICATION_DEVICE_TYPE, 
								report.getFormattedSerialNumber(report.getSerialNumber()), 
								OBSERVATION_EVENT_TYPE, NOTIFICATION_FORMAT),reportPayload.getBytes());
					}
				}
			}
			if (callbackPayload.getResponses() != null) {
				if (!(callbackPayload.getResponses().isEmpty())) {
					publisher.initMqttClientForPublish();
					
					List<Response> responseList = callbackPayload
							.getResponses();
					for(Response response : responseList) {
						String responsePayload = restClient.getJsonFromObject(response);
						LOGGER.debug("publishing REPORT(observation) payload as "
								+ responsePayload);
						for(Resource resource : response.getResources()) {
							if(StringUtils.isBlank(resource.getResource())) {
								ResponseEntity<String> subscriptionResponse = null;
								
								LOGGER.debug("Response came for Get End point details, hence collecting all resources and sending subscription request for all resources");
								Map<String,String> resourceValueMap = gson.fromJson(resource.getValue(), Map.class);
								LOGGER.debug("Obtained values from response "+resourceValueMap.toString());
								
								List<String> resourcePathMap = new ArrayList<String>(resourceValueMap.keySet());

								LOGGER.debug("Sending subscription request for resourcePaths "+resourcePathMap.toString());
								List<Map<String, String>> resourceDetails = new ArrayList<Map<String,String>>();
								
								
								for(String resourcePath : resourcePathMap) {
									resourceValueMap = new HashMap<String, String>();
									int occurance = StringUtils.countMatches(resourcePath, "/");
									if(occurance > 2) {
										String rp = resourcePath;
										resourcePath = StringUtils.substring(resourcePath, 0, StringUtils.ordinalIndexOf(resourcePath, "/", 3));
										LOGGER.debug("this resourcePath "+rp+" will be truncated as "+resourcePath);
									}
									resourceValueMap.put("resourcePath", resourcePath);
									resourceDetails.add(resourceValueMap);
								}
								/*resourceValueMap.put("resourcePath", "device/0/batteryLevel");
								resourceDetails.add(resourceValueMap);*/
								
								LOGGER.debug("ResourceDetails map is ready for Subscription "+resourceDetails.toString());
								ImpactSubscription subscriptionReq = new ImpactSubscription(response, restClient.getGroupName(), resourceDetails);
								
								LOGGER.debug("Subscription Request for Server "+subscriptionReq.toString());
								subscriptionResponse = outboundApi.sendSubscriptionRequestToImpact(subscriptionReq);
								LOGGER.debug("Response from resource Subscription "+subscriptionResponse.getBody());
							}
						}
						//publisher.publish(responsePayload);
						publisher.publishEvents(publisher.getTopicName(NOTIFICATION_DEVICE_TYPE,
								response.getFormattedSerialNumber(response.getSerialNumber()), 
								RESPONSE_EVENT_TYPE, NOTIFICATION_FORMAT),responsePayload.getBytes());
					}
					
				}
			}
			if (callbackPayload.getRegistrations() != null) {
				if (!(callbackPayload.getRegistrations().isEmpty())) {
					LOGGER.debug("Received Registration Notification from IMPACT {} "+callbackPayload.getRegistrations().toString());
					ResponseEntity<String> deviceResponse = null;
					ResponseEntity<String> getEndpointResponse = null;
					String uri = null;
					String deviceJson = null;
					
					//According to watson code, first create deviceType 
					Registration reg = callbackPayload.getRegistrations().get(0);
					MessageOutDeviceType devType = new MessageOutDeviceType(reg);
					
					//Before creating deviceType, check if this particular deviceType exist
					ResponseEntity<String> existingDevTypeResponse = restClient.doGet(WATSON_CREATE_DEVICE_TYPE_URI+"/"+devType.getId(), 
							restClient.getWatsonRestServerUrl(), userCred.getWatsonAuthorization(), restClient.getWatsonRestClient());
					
					//404 means not found then silently create deviceType
					if(existingDevTypeResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
						String devInfoJson = restClient.getJsonFromObject(devType); 
						ResponseEntity<String> deviceTypeResponse = restClient.doPost(WATSON_CREATE_DEVICE_TYPE_URI, devInfoJson, 
								restClient.getWatsonRestServerUrl(), userCred.getWatsonAuthorization(), restClient.getWatsonRestClient());
							
						LOGGER.debug("Response from device Type "+deviceTypeResponse.getBody());
					}
					//MessageOutDeviceType devType = null;
					for(Registration registration : callbackPayload.getRegistrations()) {
						/*LOGGER.debug("Creating deviceType for make "+registration.getMake()+" if it doesnt exist");
						devType = new MessageOutDeviceType(registration);
						
						//Before creating deviceType, check if this particular deviceType exist
						ResponseEntity<String> existingDevTypeResponse = restClient.doGet(WATSON_CREATE_DEVICE_TYPE_URI+"/"+devType.getId(), 
								restClient.getWatsonRestServerUrl(), userCred.getWatsonAuthorization(), restClient.getWatsonRestClient());
						
						//404 means not found then silently create deviceType
						if(existingDevTypeResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
							String devInfoJson = restClient.getJsonFromObject(devType); 
							ResponseEntity<String> deviceTypeResponse = restClient.doPost(WATSON_CREATE_DEVICE_TYPE_URI, devInfoJson, 
									restClient.getWatsonRestServerUrl(), userCred.getWatsonAuthorization(), restClient.getWatsonRestClient());
								
							LOGGER.debug("Response from device Type "+deviceTypeResponse.getBody());
						}*/
						
						LOGGER.debug("Creating device for serial Number "+registration.getSerialNumber());
						deviceJson = null;
						
						MessageOutDevice device = new MessageOutDevice(registration);
						deviceJson = restClient.getJsonFromObject(device);
						uri = WATSON_CREATE_DEVICE_URI;
						uri = uri.replaceAll("<device_type>", devType.getId());
						
						deviceResponse = restClient.doPost(uri, deviceJson , 
								restClient.getWatsonRestServerUrl(), userCred.getWatsonAuthorization()
								, restClient.getWatsonRestClient());
						LOGGER.debug("Create device response from watson {} "+deviceResponse.getBody());
						
						//For this serial Number, send getEndpoint Details to IMPACT
						LOGGER.debug("Sending Get Endpoint Details for serial number "+registration.getSerialNumber());
						getEndpointResponse = outboundApi.sendGetEndpointDetails(registration.getSerialNumber());
						LOGGER.debug("Response from getEndpointDetails "+getEndpointResponse.getBody());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Exception in service " + e);
		}
		LOGGER.debug("Publishing callback data is done ");
	}

}
