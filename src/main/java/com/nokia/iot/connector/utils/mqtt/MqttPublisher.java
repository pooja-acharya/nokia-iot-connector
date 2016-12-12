package com.nokia.iot.connector.utils.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokia.iot.connector.config.mqtt.MqttConfiguration;

@Service
public class MqttPublisher {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(MqttPublisher.class);

    @Autowired
    MqttConfiguration mqttClient;
    
	MqttClient pubClient;

	public void resetConnection() {
		disconnect();
	}
	public String getTopicName(String deviceType, String deviceId, String eventType, String format) {
		String topic = null;
		LOGGER.debug("Framing topic name for deviceType "+deviceType+" deviceId "+deviceId+" eventType "+eventType+" format "+format );
		try {
			topic = mqttClient.getMqttPublisherDefaultTopic();
			topic = topic.replaceAll("<device_type>", deviceType);
			topic = topic.replaceAll("<device_id>", deviceId);
			topic = topic.replaceAll("<event_type>", eventType);
			topic = topic.replaceAll("<format>", format);
		} catch(Exception e) {
			LOGGER.error("Error while framing topic name for deviceType "+deviceType+" deviceId "+deviceId+" eventType "+eventType+" format "+format );
			LOGGER.error("Exception "+e);
		}
		return topic;
	}
	
	public void initMqttClientForPublish() {
		LOGGER.debug("Setting mqttClient ");
		try {
			pubClient = getInstance(mqttClient.getOrgId(), "", false);
		} catch(Exception e) {
			LOGGER.error("Exception while setting mqttClient "+e);
		}
	}

	/**
	 * Publish events to IoTP
	 * @param topic
	 * @param payload
	 * @return
	 * @throws Exception 
	 */
	public boolean publishEvents(String topic, byte[] payload) throws Exception{
		LOGGER.debug("Publishing payload "+payload.toString()+" to topic "+topic);
		MqttConnectOptions options = new MqttConnectOptions();
		
		//options = this.setSSLContext(JKS_FILE_PATH, JKS_PASSWORD, options);
		
		options.setCleanSession(mqttClient.isCleanSessionProp());
		options.setUserName(mqttClient.getMqttApiKey());
		options.setPassword(mqttClient.getMqttApiToken().toCharArray());
		//Define SSL as default
		java.util.Properties sslClientProps = new java.util.Properties();
		sslClientProps.setProperty("com.ibm.ssl.protocol", "TLSv1.2");
		options.setSSLProperties(sslClientProps);		
		
		//Connect to IoTP
		try {
			if(!pubClient.isConnected()){
				LOGGER.debug("Mqtt connection is not connected, creating new instance ");
				pubClient.connect(options);
			}else{
				//already connected
				LOGGER.debug("Mqtt connection is already connected, using the same ");
			}
		} catch (Exception e) {
			LOGGER.error("Error while creating connection to MQTT");
			throw new Exception(e.getMessage());
		}
			
		//After connect, publish events
		try {
			pubClient.publish(topic, payload, mqttClient.getQos(), false);
			
		} catch (Exception e) {
			LOGGER.error("Exception while publishing "+e);
			return false;
		}
		//Try disconnect from IoTP
		//disconnect();
		return true;		
		
	}
	
	
	public MqttClient getInstance(String orgId, String platformId, boolean forceNew) throws Exception{
		LOGGER.debug("getInstance >> parameters orgId "+orgId+" platformId "+platformId+" forceNew "+forceNew);
		String finalURL = mqttClient.getMessagingUrl(orgId);
		LOGGER.debug("Messaging URL "+finalURL);
		try {
			//Only get a new client if the existent reference is null or forceNew is true
			if(pubClient == null || forceNew) {
				MemoryPersistence persistence = new MemoryPersistence();
				/*
				 * Commenting below line for testing
				 */
				//pubClient = new MqttClient(finalURL, getClientId(orgId, platformId), persistence);	
				LOGGER.debug("Connectiong to URL "+finalURL);
				String publisherClientId = mqttClient.getPublisherClientId();
				
				LOGGER.debug("and publisherClientId as "+publisherClientId);
				pubClient = new MqttClient(finalURL, publisherClientId, persistence);	
			}
		} catch (Exception e) {
			LOGGER.error("Exception getInstance "+e);
		}	
		
		return pubClient;
	}
	
	public boolean disconnect(){
		try {
			if(pubClient.isConnected()) {
				LOGGER.debug("Publish done, disconnecting..");
				pubClient.disconnect();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	} 
}
