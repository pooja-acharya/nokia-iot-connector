package com.nokia.iot.connector.config.credentials;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.nokia.iot.connector.config.PropertyKeyConstants;

@Configuration
@PropertySource(value = { "classpath:credentials.properties" })
public class UserCredentials {
	
	private final Logger LOGGER = LoggerFactory
            .getLogger(UserCredentials.class);

	@Value("${impact.username}")
	private String pImpactUsername;
	
	@Value("${impact.password}")
	private String pImpactPassword;
	
	@Value("${impact.callback.username}")
	private String impactCallbackUsername;
	
	@Value("${impact.callback.password}")
	private String impactCallbackPassword;
	
	@Value("${api.key}")
	private String pApiKey;
	
	@Value("${api.token}")
	private String pApiToken;
	
	private static String authorization;
	
	private static String watsonAuthorization;
	
	private static String impactUsername;
	
	private static String impactPassword;
	
	private static String apiKey;
	
	private static String apiToken;
	
	public String getImpactCallbackUsername() {
		return impactCallbackUsername;
	}

	public String getImpactCallbackPassword() {
		return impactCallbackPassword;
	}

	public void setImpactCallbackPassword(String impactCallbackPassword) {
		this.impactCallbackPassword = impactCallbackPassword;
	}
	
	public void reloadPropertyPlaceHolders(Properties prop) {
		impactUsername = prop.getProperty(PropertyKeyConstants.IMPACT_USERNAME);
		impactPassword = prop.getProperty(PropertyKeyConstants.IMPACT_PASSWORD);
		apiKey = prop.getProperty(PropertyKeyConstants.API_KEY);
		apiToken = prop.getProperty(PropertyKeyConstants.API_TOKEN);
		getImpactAuthorization();
		getWatsonAuthorization();
	}
	
	@Bean
	public String initConf() {
		impactUsername = pImpactUsername;
		impactPassword = pImpactPassword;
		apiKey = pApiKey;
		apiToken = pApiToken;
		return impactUsername;
	}
	
	public String getImpactAuthorization() {
		String username = decodeBase64(impactUsername);
		String password = decodeBase64(impactPassword);
		authorization = encodeBase64(username+":"+password);
		return authorization;
	}
	
	public String getWatsonAuthorization() {
		String username = apiKey;
		String password = apiToken;
		watsonAuthorization = encodeBase64(username+":"+password);
		return watsonAuthorization;
	}
	
	public String decodeBase64(String value) {
		try {
			return new String(Base64.decodeBase64(value.getBytes()),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("decodeBase64 >> UnsupportedEncodingException "+e);
			return value;
		} catch (Exception e) {
			LOGGER.error("decodeBase64 >> Exception "+e);
			return value;
		}
	}
	
	public String encodeBase64(String value) {
		try {
			return new String(Base64.encodeBase64(value.getBytes()),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("encodeBase64 >> UnsupportedEncodingException "+e);
			return value;
		} catch (Exception e) {
			LOGGER.error("decodeBase64 >> Exception "+e);
			return value;
		}
	}
}
