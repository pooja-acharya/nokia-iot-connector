package com.nokia.iot.connector.config.mqtt;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.net.ssl.SSLContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.nokia.iot.connector.utils.mqtt.MqttSubscriber;


@Configuration
@IntegrationComponentScan
@PropertySource(value = { "classpath:mqtt.properties" })
public class MqttConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MqttConfiguration.class);

	@Value("${mqtt.broker.url}")
	private String mqttBrokerUrl;

	@Value("${mqtt.subscriber.topic}")
	private String mqttSubscriberTopic;

	@Value("${api.key}")
	private String mqttApiKey;
	
	@Value("${api.token}")
	private String mqttApiToken;

	@Value("${mqtt.qos}")
	private Integer qos;
	
	@Value("${org.id}")
	private String orgId;
	
	@Value("${mqtt.publisher.default.topic}")
	private String mqttPublisherDefaultTopic;
	
	private boolean cleanSessionProp = true;
	
	public static final String APPLICATION_DEVICE_CLASS = "a";

	public Integer getQos() {
		return qos;
	}

	public void setQos(Integer qos) {
		this.qos = qos;
	}

	public boolean isCleanSessionProp() {
		return cleanSessionProp;
	}

	public void setCleanSessionProp(boolean cleanSessionProp) {
		this.cleanSessionProp = cleanSessionProp;
	}

	public String getMqttApiKey() {
		return mqttApiKey;
	}

	public void setMqttApiKey(String mqttApiKey) {
		this.mqttApiKey = mqttApiKey;
	}

	public String getMqttApiToken() {
		return mqttApiToken;
	}

	public void setMqttApiToken(String mqttApiToken) {
		this.mqttApiToken = mqttApiToken;
	}

	public String getMqttSubscriberTopic() {
		return mqttSubscriberTopic;
	}

	public void setMqttSubscriberTopic(String mqttSubscriberTopic) {
		this.mqttSubscriberTopic = mqttSubscriberTopic;
	}

	public String getMqttBrokerUrl() {
		return mqttBrokerUrl;
	}

	public void setMqttBrokerUrl(String mqttBrokerUrl) {
		this.mqttBrokerUrl = mqttBrokerUrl;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getMqttPublisherDefaultTopic() {
		return mqttPublisherDefaultTopic;
	}

	public void setMqttPublisherDefaultTopic(String mqttPublisherDefaultTopic) {
		this.mqttPublisherDefaultTopic = mqttPublisherDefaultTopic;
	}

	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}
	
	 /**
	  * * Subscribe to topic
	  * @return
	  */
	 
	@Bean
	public MessageProducer inbound() {
		String clientId = getClientId(this.orgId,"");
		LOGGER.debug("Subscribing to topic "+mqttSubscriberTopic+" broker url "+mqttBrokerUrl+" client Id "+clientId);
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, mqttClientFactory(), mqttSubscriberTopic);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setOutputChannel(mqttInputChannel());
		adapter.setCompletionTimeout(5000);
		adapter.setQos(qos);
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MqttSubscriber();
	}

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		LOGGER.debug("apikey as "+mqttApiKey+" token as "+mqttApiToken);
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setServerURIs(getMessagingUrl(orgId));
		factory.setUserName(mqttApiKey);
		factory.setPassword(mqttApiToken);
		
		SSLContext sslContext = null;
        //LoggerUtility.info(CLASS_NAME, METHOD, "Provider: " + sslContext.getProvider().getName());
        try {
        	sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(null, null, null);
		} catch (KeyManagementException e) {
			LOGGER.error("KeyManagementException e "+e);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("NoSuchAlgorithmException e "+e);
		}
        
		factory.setSocketFactory(sslContext.getSocketFactory());
		return factory;
	}

	public String getMessagingUrl(String orgId) {
		String finalURL = mqttBrokerUrl.replaceAll("<org_id>", orgId); ;
		return finalURL;
	}
	
	/**
	 * Mount clientId based on configuration and dynamic attributes
	 * CliendId is c:<org>:<deviceType>:<instanceId>:<variation>
	 * @param config
	 * @return
	 */
	public String getClientId(String orgId, String platformId){
		//Ensure temporary uniqueness for CliendId
		//InstanceId is not enough, many HTTP instances, coming from an external system can use the same connector instanceId
		//Using a timestamp for that
		Date currDate = new Date();
		long variation = currDate.getTime();
				
		StringBuilder clientId = new StringBuilder();
		
		clientId
			.append(APPLICATION_DEVICE_CLASS)
			.append(":")
			.append(orgId)
			.append(":")
			//.append(platformId)
			//.append(":")
			.append(String.valueOf(variation));
		return clientId.toString();
	}
}
