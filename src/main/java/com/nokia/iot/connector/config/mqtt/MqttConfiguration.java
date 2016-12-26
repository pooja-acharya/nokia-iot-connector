package com.nokia.iot.connector.config.mqtt;

import java.util.Date;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

import com.nokia.iot.connector.config.PropertyKeyConstants;
import com.nokia.iot.connector.outbound.service.IOutboundImpactService;
import com.nokia.iot.connector.utils.mqtt.MqttSubscriber;


@Configuration
@PropertySource(value = { "classpath:mqtt.properties" })
public class MqttConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MqttConfiguration.class);

	@Value("${mqtt.broker.url}")
	private String mqttBrokerUrl;

	@Value("${mqtt.subscriber.topic}")
	private String mqttSubscriberTopic;

	@Value("${api.key}")
	private String pMqttApiKey;
	
	@Value("${api.token}")
	private String pMqttApiToken;

	@Value("${mqtt.qos}")
	private Integer qos;
	
	@Value("${org.id}")
	private String pOrgId;
	
	@Value("${mqtt.publisher.default.topic}")
	private String mqttPublisherDefaultTopic;
	
	@Autowired
	IOutboundImpactService outboundService;
	
	@Autowired
	MqttSubscriber subscriber;
	
	private boolean cleanSessionProp = true;
	
	private static String publisherClientId;
	
	public static final String SUBSCRIPTION_APPLICATION_DEVICE_CLASS = "A";
	
	public static final String PUBLISHER_USER_AUTH_TOKEN = "12345_ASWss";
	
	private static String orgId;
	
	private static String mqttApiKey;
	
	private static String mqttApiToken;

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
		MqttConfiguration.mqttApiKey = mqttApiKey;
	}

	public String getMqttApiToken() {
		return mqttApiToken;
	}

	public void setMqttApiToken(String mqttApiToken) {
		MqttConfiguration.mqttApiToken = mqttApiToken;
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
		MqttConfiguration.orgId = orgId;
	}

	public String getMqttPublisherDefaultTopic() {
		return mqttPublisherDefaultTopic;
	}

	public void setMqttPublisherDefaultTopic(String mqttPublisherDefaultTopic) {
		this.mqttPublisherDefaultTopic = mqttPublisherDefaultTopic;
	}

	public void reloadPropertyFilePlaceHolders(Properties prop) {
		mqttApiKey = prop.getProperty(PropertyKeyConstants.API_KEY);
		mqttApiToken = prop.getProperty(PropertyKeyConstants.API_TOKEN);
		orgId = prop.getProperty(PropertyKeyConstants.ORG_ID);
		initConnector();
		initSubscriber();
	}
	
	private void initSubscriber() {
		LOGGER.debug("Subscribing to client Id "+getClientId(
						orgId, "", SUBSCRIPTION_APPLICATION_DEVICE_CLASS));
		LOGGER.debug("and to topic "+mqttSubscriberTopic+" and url "+getMessagingUrl(orgId));
		 MqttClient sampleClient;
			try {
				sampleClient = new MqttClient(getMessagingUrl(orgId), getClientId(
						orgId, "", SUBSCRIPTION_APPLICATION_DEVICE_CLASS),
						new MemoryPersistence());
				MqttConnectOptions connOpts = new MqttConnectOptions();
				connOpts.setCleanSession(isCleanSessionProp());
				connOpts.setUserName(mqttApiKey);
				connOpts.setPassword(mqttApiToken.toCharArray());
				
				Properties sslClientProps = new Properties();
				sslClientProps.setProperty("com.ibm.ssl.protocol", "TLSv1.2");
				connOpts.setSSLProperties(sslClientProps);
				
				sampleClient.connect(connOpts);
				sampleClient.subscribe(mqttSubscriberTopic, qos);
				sampleClient.setCallback(subscriber);
			} catch (MqttException e) {
				LOGGER.error("Error while subscribing to url "+getMessagingUrl(orgId)+" exception "+e);
			}
	}

	@Bean
	public String initMqttConf() {
		orgId = this.pOrgId;
		mqttApiKey = this.pMqttApiKey;
		mqttApiToken = this.pMqttApiToken;
		return orgId;
	}
	
	 /**
	  * * Subscribe to topic
	  * @return
	  */
	 
	/*@Bean
	@Lazy(value = true)
	public MqttPahoMessageDrivenChannelAdapter inbound() {
		String clientId = getClientId(orgId,"",SUBSCRIPTION_APPLICATION_DEVICE_CLASS);
		LOGGER.debug("Subscribing to topic "+mqttSubscriberTopic+" broker url "+mqttBrokerUrl+" client Id "+clientId);
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, mqttClientFactory(), mqttSubscriberTopic);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setOutputChannel(mqttInputChannel());
		adapter.setCompletionTimeout(5000);
		adapter.setQos(qos);
		adapter.setRecoveryInterval(100);
		return adapter;
	}

	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
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
	}*/

	public String getMessagingUrl(String orgId) {
		String finalURL = mqttBrokerUrl.replaceAll("<org_id>", orgId); ;
		return finalURL;
	}
	
	@Bean
	@Lazy(value = true)
	public String initConnector() {
		LOGGER.debug("initConnector>> Registering connector to watson");
		try {
			publisherClientId = outboundService.registerConnectorToWatson();
			LOGGER.debug("publisherClientId :: "+publisherClientId);
		} catch(Exception e) {
			LOGGER.error("Registration of connector failed "+e);
		}
		return publisherClientId;
	}
	
	public String getPublisherClientId() {
		return publisherClientId;
	}
	/**
	 * Mount clientId based on configuration and dynamic attributes
	 * CliendId is c:<org>:<deviceType>:<instanceId>:<variation>
	 * @param config
	 * @return
	 */
	public String getClientId(String orgId, String platformId, String appDeviceClass){
		//Ensure temporary uniqueness for CliendId
		//InstanceId is not enough, many HTTP instances, coming from an external system can use the same connector instanceId
		//Using a timestamp for that
		Date currDate = new Date();
		long variation = currDate.getTime();
				
		StringBuilder clientId = new StringBuilder();
		
		clientId
			.append(appDeviceClass)
			.append(":")
			.append(orgId)
			.append(":")
			//.append(platformId)
			//.append(":")
			.append(String.valueOf(variation));
		return clientId.toString();
	}
}
