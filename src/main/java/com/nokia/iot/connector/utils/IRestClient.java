package com.nokia.iot.connector.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public interface IRestClient {
	
	String getImpactRestServerUrl();
	
	void setImpactRestServerUrl(String impactRestServerUrl);
	
	String getWatsonRestServerUrl();
	
	void setWatsonRestServerUrl(String watsonRestServerUrl);
	
	String getJsonFromObject(Object obj);
	
	String getGroupName();
	
	public RestTemplate getImpactRestClient();
	
	public RestTemplate getWatsonRestClient();

	ResponseEntity<String> doGet(String uri, String restServerUrl, String authorization, RestTemplate restTemplate);
	
	ResponseEntity<String> doPost(String uri, String payload, String restServerUrl, String authorization, RestTemplate restTemplate);
	
	ResponseEntity<String> doPut(String uri, String payload, String restServerUrl, String authorization, RestTemplate restTemplate);
	
	ResponseEntity<String> doDelete(String uri, String payload, String restServerUrl, String authorization, RestTemplate restTemplate);

}
