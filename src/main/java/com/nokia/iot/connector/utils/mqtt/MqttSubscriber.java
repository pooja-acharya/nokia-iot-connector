package com.nokia.iot.connector.utils.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessagingException;

import com.nokia.iot.connector.inbound.IMqttPayloadConverter;

public class MqttSubscriber implements MqttCallback {
	
	IMqttPayloadConverter mqttConverter;

	private static final Logger LOGGER = LoggerFactory.getLogger(MqttSubscriber.class);
	
	public MqttSubscriber(IMqttPayloadConverter mqttConverter) {
		this.mqttConverter = mqttConverter;
	}
	public void handleMessage(MqttMessage msg) throws MessagingException {
		LOGGER.debug("Message Recieved "+msg.toString());
		LOGGER.debug("Calling converter to send payload to IMPACT");
		try {
			mqttConverter.convertAndSend(msg.toString());
		} catch(Exception e) {
			LOGGER.error("Exception in handleMessage "+e);
		}
		
	}

	@Override
	public void connectionLost(Throwable cause) {
		LOGGER.error("Connection lost .. reason "+cause.getMessage());
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message)
			throws Exception {
		LOGGER.debug("Message arrived as "+message.toString());
		handleMessage(message);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		
	}

}
