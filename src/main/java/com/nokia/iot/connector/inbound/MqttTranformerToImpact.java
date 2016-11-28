package com.nokia.iot.connector.inbound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.nokia.iot.connector.inbound.domain.MqttPayload;
import com.nokia.iot.connector.outbound.resource.domain.ImpactResource;
import com.nokia.iot.connector.outbound.service.IOutboundImpactService;

@Component
public class MqttTranformerToImpact {
	
	@Autowired
	IOutboundImpactService outboundService;
	
	static final String READ_RESOURCE_METHOD_TYPE = "GET";
	
	static final String WRITE_RESOURCE_METHOD_TYPE = "PUT";
	
	static final String DELETE_RESOURCE_METHOD_TYPE = "DELETE";
	
	static final String EXEC_RESOURCE_METHOD_TYPE = "POST";
	
	static final String READ_RESOURCE_OPERATION_TYPE = "read";
	
	static final String WRITE_RESOURCE_OPERATION_TYPE = "write";
	
	static final String DELETE_RESOURCE_OPERATION_TYPE = "delete";
	
	static final String EXEC_RESOURCE_OPERATION_TYPE = "execute";
	
	Gson gson = new Gson();

	private static final Logger LOGGER = LoggerFactory.getLogger(MqttTranformerToImpact.class);
	
	public String transformAndSendResourceRequest(MqttPayload mqttPayload) {
		LOGGER.debug("MqttTranformerToImpact.transformAndSendResourceRequest >> with payload "+mqttPayload.toString());
		LOGGER.debug("Resource Request JSON "+mqttPayload);
		ResponseEntity<String> resourceResponse = null;
		String resourceResponseStr = null;
		ImpactResource resourceReq = null;
		switch (mqttPayload.getMethod()) {
		case READ_RESOURCE_METHOD_TYPE:
			LOGGER.debug("Converting payload for READ resource request ");
			resourceReq = new ImpactResource(mqttPayload,READ_RESOURCE_OPERATION_TYPE);
			break;
		case WRITE_RESOURCE_METHOD_TYPE:
			LOGGER.debug("Converting payload for WRITE resource request ");
			resourceReq = new ImpactResource(mqttPayload,WRITE_RESOURCE_OPERATION_TYPE);
			break;
		case EXEC_RESOURCE_METHOD_TYPE:
			LOGGER.debug("Converting payload for EXEC resource request ");
			resourceReq = new ImpactResource(mqttPayload,EXEC_RESOURCE_OPERATION_TYPE);
			break;
		case DELETE_RESOURCE_METHOD_TYPE:
			LOGGER.debug("Converting payload for DELETE resource request ");
			resourceReq = new ImpactResource(mqttPayload,DELETE_RESOURCE_OPERATION_TYPE);
			break;
		default:
			LOGGER.error("Invalid method type");
			resourceResponseStr = "{\"msg\":\"Internal Server Error\"}";
			return resourceResponseStr;
		}
		LOGGER.debug("Calling Resource Request to IMPACT using parameters "+resourceReq.toString());
		
		try {
			resourceResponse = outboundService.sendResourceRequestToImpact(resourceReq);
			resourceResponseStr = resourceResponse.getBody();
			LOGGER.debug("MqttTranformerToImpact.transformAndSendResourceRequest >> response from resourceRequest "+resourceResponseStr);
		} catch (Exception e) {
			LOGGER.debug("Exception in transformAndSendResourceRequest {} "+e.getMessage());
			resourceResponseStr = "{\"msg\":\"Internal Server Error\"}";
		}
		return resourceResponseStr;
	}
	
	public String transformAndSendSubscriptionRequest(MqttPayload mqttPayload) {
		LOGGER.debug("MqttTranformerToImpact.transformAndSendSubscriptionRequest >> with payload "+mqttPayload.getPayload());
		//ImpactSubscription subscriptionReq = new ImpactSubscription(mqttPayload.getPayload());
		
		//LOGGER.debug("Calling Subscription Request to IMPACT using parameters "+subscriptionReq.toString());
		ResponseEntity<String> subscriptionResponse = null;
		String subscriptionResponseStr = null;
		/*try {
			subscriptionResponse = outboundService.sendSubscriptionRequestToImpact(subscriptionReq);
			subscriptionResponseStr = subscriptionResponse.getBody();
			LOGGER.debug("MqttTranformerToImpact.transformAndSendSubscriptionRequest >> response from subscriptionRequest "+subscriptionResponseStr);
		} catch (Exception e) {
			LOGGER.debug("Exception in transformAndSendSubscriptionRequest {} "+e.getMessage());
			subscriptionResponseStr = "{\"msg\":\"Internal Server Error\"}";
		}*/
		return subscriptionResponseStr;
	}
}
