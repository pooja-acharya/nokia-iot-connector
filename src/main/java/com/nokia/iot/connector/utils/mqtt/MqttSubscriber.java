package com.nokia.iot.connector.utils.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import com.nokia.iot.connector.inbound.IMqttPayloadConverter;

@Service
public class MqttSubscriber implements MessageHandler{
	
	@Autowired
	IMqttPayloadConverter mqttConverter;

	private static final Logger LOGGER = LoggerFactory.getLogger(MqttSubscriber.class);
	
	@Override
	public void handleMessage(Message<?> msg) throws MessagingException {
		LOGGER.debug("Message Recieved "+msg.getPayload());
		LOGGER.debug("Calling converter to send payload to IMPACT");
		mqttConverter.convertAndSend(msg.getPayload()+"");
	}

}
