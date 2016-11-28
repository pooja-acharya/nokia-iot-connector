package com.nokia.iot.connector.inbound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.nokia.iot.connector.inbound.domain.MqttPayload;

@Service
public class MqttPayloadConverterImpl implements IMqttPayloadConverter {
	
	@Autowired
	MqttTranformerToImpact mqttTransformer;

	private static final Logger LOGGER = LoggerFactory.getLogger(MqttPayloadConverterImpl.class);
	
	Gson gson = new Gson();
	
	@Override
	public void convertAndSend(String mqttPayload) {
		LOGGER.debug("MqttPayloadConverterImpl.convertAndSend >> with payload "+mqttPayload);
		MqttPayload mqttData = gson.fromJson(mqttPayload, MqttPayload.class);
		String response = null;
		
		response = mqttTransformer.transformAndSendResourceRequest(mqttData);
		LOGGER.debug("Response from IMPACT "+response);
		/*
		 * Considering only RESOURCE request as of now
		 */
		/*if(MQTT_EVENT_RESOURCE.equalsIgnoreCase(mqttData.getEvent())) {
			LOGGER.debug("Transforming and Sending resource request to IMPACT");
			response = mqttTransformer.transformAndSendResourceRequest(mqttData);
			LOGGER.debug("Response from IMPACT "+response);
			
		} else if(MQTT_EVENT_SUBSCRIPTION.equalsIgnoreCase(mqttData.getEvent())) {
			LOGGER.debug("Transforming and Sending resource request to IMPACT");
			response = mqttTransformer.transformAndSendSubscriptionRequest(mqttData);
			LOGGER.debug("Response from IMPACT "+response);
		}*/
		
	}
}
