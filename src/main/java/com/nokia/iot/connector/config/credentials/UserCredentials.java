package com.nokia.iot.connector.config.credentials;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = { "classpath:credentials.properties" })
public class UserCredentials {
	
	private static final Logger LOGGER = LoggerFactory
            .getLogger(UserCredentials.class);

	@Value("${impact.username}")
	private String impactUsername;
	
	@Value("${impact.password}")
	private String impactPassword;
	
	@Value("${impact.callback.username}")
	private String impactCallbackUsername;
	
	@Value("${impact.callback.password}")
	private String impactCallbackPassword;
	
	@Value("${api.key}")
	private String apiKey;
	
	@Value("${api.token}")
	private String apiToken;
	
	private String authorization;
	
	private String watsonAuthorization;

	public String getImpactUsername() {
		return impactUsername;
	}

	public void setImpactUsername(String impactUsername) {
		this.impactUsername = impactUsername;
	}

	public String getImpactPassword() {
		return impactPassword;
	}

	public void setImpactPassword(String impactPassword) {
		this.impactPassword = impactPassword;
	}
	
	public String getImpactCallbackUsername() {
		return impactCallbackUsername;
	}

	public void setImpactCallbackUsername(String impactCallbackUsername) {
		this.impactCallbackUsername = impactCallbackUsername;
	}

	public String getImpactCallbackPassword() {
		return impactCallbackPassword;
	}

	public void setImpactCallbackPassword(String impactCallbackPassword) {
		this.impactCallbackPassword = impactCallbackPassword;
	}
	
	@Bean
	public String setImpactAuthorization() {
		String username = decodeBase64(impactUsername);
		String password = decodeBase64(impactPassword);
		this.authorization = encodeBase64(username+":"+password);
		return this.authorization;
	}
	
	@Bean
	public String getImpactAuthorization() {
		return this.authorization;
	}
	
	@Bean
	public String setWatsonAuthorization() {
		String username = apiKey;
		String password = apiToken;
		this.watsonAuthorization = encodeBase64(username+":"+password);
		return this.watsonAuthorization;
	}
	
	@Bean
	public String getWatsonAuthorization() {
		return this.watsonAuthorization;
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
	/*@Bean
	public ErrorPageFilter errorPageFilter() {
	    return new ErrorPageFilter();
	}

	@Bean
	public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
	    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.setEnabled(false);
	    return filterRegistrationBean;
	}*/
}
