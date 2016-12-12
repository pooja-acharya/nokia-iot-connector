package com.nokia.iot.connector.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.stereotype.Service;

import com.nokia.iot.connector.config.credentials.UserCredentials;
import com.nokia.iot.connector.config.mqtt.MqttConfiguration;
import com.nokia.iot.connector.inbound.domain.ImpactProperties;
import com.nokia.iot.connector.inbound.domain.WatsonProperties;
import com.nokia.iot.connector.utils.IRestClient;
import com.nokia.iot.connector.utils.mqtt.MqttPublisher;

@Service
public class RefreshConfiguration {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RefreshConfiguration.class);
	
	@Autowired
	UserCredentials credentials;
	
	@Autowired
	MqttConfiguration mqttConf;
	
	@Autowired
	MqttPublisher publisherConf;
	
	@Autowired @Lazy
	MqttPahoMessageDrivenChannelAdapter subscriber;
	
	@Autowired
	IRestClient restConf;
	
	public void reloadImpactConfigurations() {
		LOGGER.debug("reloadImpactConfigurations >>");
		Properties externalProp = new Properties();
		try {
			LOGGER.debug("Reading from property file");
			externalProp = loadPropertyFile();
			LOGGER.debug("All properties are read as "+externalProp);
			
			//Set User credentials
			credentials.reloadPropertyPlaceHolders(externalProp);
			
			//Set Rest Client Configurations
			restConf.reloadPropertyFilePlaceHolders(externalProp);
		} catch(Exception e) {
			LOGGER.error("Exception in reloadAllConfigurations "+e);
		}
	}
	
	public void reloadWatsonConfigurations() {
		LOGGER.debug("reloadWatsonConfigurations >>");
		Properties externalProp = new Properties();
		try {
			LOGGER.debug("Reading from property file");
			externalProp = loadPropertyFile();
			LOGGER.debug("All properties are read as "+externalProp);
			
			//Set Rest Client Configurations
			restConf.reloadPropertyFilePlaceHolders(externalProp);
			
			//Set Mqtt Configuration
			mqttConf.reloadPropertyFilePlaceHolders(externalProp);
			/*MqttPahoMessageDrivenChannelAdapter subs = mqttConf.inbound();
			subs.start();*/
			subscriber.start();
			
			//Reset publisher connection if already connected
			publisherConf.resetConnection();
			
		} catch(Exception e) {
			LOGGER.error("Exception in reloadAllConfigurations "+e);
		}
	}
	
	public void reloadAllConfigurations() {
		LOGGER.debug("reloadAllConfigurations >>");
		Properties externalProp = new Properties();
		try {
			LOGGER.debug("Reading from property file");
			externalProp = loadPropertyFile();
			LOGGER.debug("All properties are read as "+externalProp);
			
			//Set User credentials
			credentials.reloadPropertyPlaceHolders(externalProp);
			
			//Set Mqtt Configuration
			mqttConf.reloadPropertyFilePlaceHolders(externalProp);
			/*MqttPahoMessageDrivenChannelAdapter subs = mqttConf.inbound();
			subs.start();*/
			subscriber.start();
			
			//Reset publisher connection if already connected
			publisherConf.resetConnection();
			
			//Set Rest Client Configurations
			restConf.reloadPropertyFilePlaceHolders(externalProp);
		} catch(Exception e) {
			LOGGER.error("Exception in reloadAllConfigurations "+e);
		}
	}
	
	public void writeWatsonProperties(WatsonProperties prop) {
		LOGGER.debug("writeImpactProperties >> "+prop.toString());
		Properties externalProp = new Properties();
		try {
			externalProp = loadPropertyFile();
			if(externalProp.containsKey(PropertyKeyConstants.API_KEY)) {
				LOGGER.debug("API_KEY key is found");
				externalProp.setProperty(PropertyKeyConstants.API_KEY, prop.getWatsonApiKey());
			}
			if(externalProp.containsKey(PropertyKeyConstants.API_TOKEN)) {
				LOGGER.debug("API_TOKEN key is found");
				externalProp.setProperty(PropertyKeyConstants.API_TOKEN, prop.getWatsonApiToken());
			}
			if(externalProp.containsKey(PropertyKeyConstants.ORG_ID)) {
				LOGGER.debug("ORG_ID key is found");
				externalProp.setProperty(PropertyKeyConstants.ORG_ID, prop.getWatsonOrgId());
			}
			writeToPropertyFile(externalProp); 
			reloadWatsonConfigurations();
		} catch(Exception e) {
			LOGGER.error("Exception in writeImpactProperties "+e);
		}
	}
	
	public void writeImpactProperties(ImpactProperties prop) {
		LOGGER.debug("writeImpactProperties >> "+prop.toString());
		Properties externalProp = new Properties();
		try {
			externalProp = loadPropertyFile();
			if(externalProp.containsKey(PropertyKeyConstants.IMPACT_USERNAME)) {
				LOGGER.debug("IMPACT_USERNAME key is found");
				externalProp.setProperty(PropertyKeyConstants.IMPACT_USERNAME, credentials.encodeBase64(prop.getImpactUsername()));
			}
			if(externalProp.containsKey(PropertyKeyConstants.IMPACT_PASSWORD)) {
				LOGGER.debug("IMPACT_PASSWORD key is found");
				externalProp.setProperty(PropertyKeyConstants.IMPACT_PASSWORD, prop.getImpactPassword());
			}
			if(externalProp.containsKey(PropertyKeyConstants.IMPACT_GROUP_NAME)) {
				LOGGER.debug("IMPACT_GROUP_NAME key is found");
				externalProp.setProperty(PropertyKeyConstants.IMPACT_GROUP_NAME, prop.getImpactGroupName());
			}
			writeToPropertyFile(externalProp); 
			reloadImpactConfigurations();
		} catch(Exception e) {
			LOGGER.error("Exception in writeImpactProperties "+e);
		}
	}
	public void writeToPropertyFile(Properties property) {
		LOGGER.debug("writeToPropertyFile >> "+property);
		File input = null;
		String connectorPath = System.getenv("CONNECTOR_HOME");
		if (!(connectorPath != null)) {
			LOGGER.debug("Please set the environment variable automation_home to automation.cfg file");
			// System.exit(-1);
		}
		try {
			input = new File(connectorPath + File.separator+"application.properties");
			OutputStream out = new FileOutputStream( input);
			property.store(out, null);
		} catch(Exception e) {
			LOGGER.error("Error in writeToPropertyFile "+e);
		}
	}
	public Properties loadPropertyFile() {
		LOGGER.debug("Loading external property file ");
		Properties prop = new Properties();
		FileInputStream input = null;
		String connectorPath = System.getenv("CONNECTOR_HOME");
		if (!(connectorPath != null)) {
			LOGGER.debug("Please set the environment variable automation_home to automation.cfg file");
			// System.exit(-1);
		}
		try {
			input = new FileInputStream(connectorPath + File.separator+"application.properties");

			// load a properties file
			prop.load(input);
		} catch (IOException ex) {
			LOGGER.error("Error in loadPropertyFile "+ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.error("Error in loadPropertyFile "+e);
				}
			}
		}
		return prop;
	}
}
