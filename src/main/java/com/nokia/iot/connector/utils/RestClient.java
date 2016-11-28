package com.nokia.iot.connector.utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AbstractVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.nokia.iot.connector.config.credentials.UserCredentials;
/**
 * 
 * @author pacharya
 *
 */
@Service
public class RestClient implements IRestClient {

	@Value("${impact.server.url}")
	private String impactRestServerUrl;

	@Value("${watson.server.url}")
	private String watsonRestServerUrl;
	
	@Value("${org.id}")
	private String orgId;
	
	@Value("${impact.group.name}")
	private String groupName;
	
	//private static boolean isSSLContextSet = false;

	private static RestTemplate impactRestClient = new RestTemplate();
	
	private RestTemplate watsonRestClient = new RestTemplate();

	static HttpHeaders headers = new HttpHeaders();
	
	private static Gson gson = new Gson();

	@Autowired
	UserCredentials userCredentials;
	
	private static boolean isSSLContextSet = false;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RestClient.class);

	public RestClient() {
	}

	public RestTemplate getImpactRestClient() {
		return impactRestClient;
	}

	public RestTemplate getWatsonRestClient() {
		SSLContext sslContext = null;
		SSLConnectionSocketFactory sslConfactory = null;
		try {
			if(watsonRestServerUrl.startsWith("https") && !isSSLContextSet) {
				LOGGER.debug("Setting SSL context ");
				sslContext = SSLContexts.custom().useTLS().build();
				sslConfactory = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1.2" }, null,
						verifier());
				HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConfactory).build();

			    ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			    watsonRestClient = new RestTemplate(requestFactory);
			    isSSLContextSet = true;
			} else if(isSSLContextSet) {
				LOGGER.debug("SSL is already set hence returning the same");
			} else {
				LOGGER.debug("Returning default restclient");
				watsonRestClient = new RestTemplate();
			}
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			LOGGER.error("KeyManagementException/NoSuchAlgorithmException exception "+e);
		}
		return watsonRestClient;
	}

	public void setWatsonRestClient(RestTemplate watsonRestClient) {
		this.watsonRestClient = watsonRestClient;
	}

	public String getImpactRestServerUrl() {
		return impactRestServerUrl;
	}

	public void setImpactRestServerUrl(String impactRestServerUrl) {
		this.impactRestServerUrl = impactRestServerUrl;
	}
	private X509HostnameVerifier verifier() {
		X509HostnameVerifier verifier = new AbstractVerifier() {
			@Override
			public void verify(final String host, final String[] cns, final String[] subjectAlts) throws SSLException {
				verify(host, cns, subjectAlts, true);
			}
		};

		return verifier;
	}
	public String getWatsonRestServerUrl() {
		watsonRestServerUrl = watsonRestServerUrl.replaceAll("<org_id>",orgId );
		return watsonRestServerUrl;
	}

	public void setWatsonRestServerUrl(String watsonRestServerUrl) {
		this.watsonRestServerUrl = watsonRestServerUrl;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	@Override
	public ResponseEntity<String> doGet(String uri, String restServerUrl,
			String authorization, RestTemplate restTemplate) {
		LOGGER.debug("doGet with Parameters :: uri {} " + uri+" authorization {} "+authorization);
		headers.clear();
		headers.add("Authorization", "Basic " + authorization);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity entity = new HttpEntity(headers);
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(restServerUrl + uri,
					HttpMethod.GET, entity, String.class);
			
		} catch (HttpClientErrorException e) {
			LOGGER.error("doGet >> Exception " + e.getRawStatusCode()
					+ " and msg " + e.getResponseBodyAsString());
			return response.status(e.getRawStatusCode()).body(
					e.getResponseBodyAsString());
		} catch (Exception e) {
			LOGGER.error("doGet >> Exception " + e);
			return response.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"{\"msg\":\"Internal Server Error\"}");
		}
		LOGGER.debug("doGet >> Response :: status Code {} "
				+ response.getStatusCode() + " body {} " + response.getBody());
		return response;
	}

	@SuppressWarnings("static-access")
	@Override
	public ResponseEntity<String> doPost(String uri, String payload,
			String restServerUrl, String authorization, RestTemplate restTemplate) {
		LOGGER.debug("doPost with Parameters :: uri {} " + uri + " payload {} "
				+ payload+" authorization {} "+authorization);
		headers.clear();
		headers.add("Authorization", "Basic " + authorization);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> extRequest = new HttpEntity<String>(payload, headers);
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(restServerUrl + uri,
					HttpMethod.POST, extRequest, String.class);
			LOGGER.debug("doPost >> Response :: status Code {} "
					+ response.getStatusCode() + " body {} "
					+ response.getBody());
		} catch (HttpClientErrorException e) {
			LOGGER.error("doPost >> HttpClientErrorException " + e.getRawStatusCode()
					+ " and msg " + e.getResponseBodyAsString());
			return response.status(e.getRawStatusCode()).body(
					e.getResponseBodyAsString());
		} catch (Exception e) {
			LOGGER.error("doPost >> Exception " + e);
			return response.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"{\"msg\":\"Internal Server Error\"}");
		}

		return response;
	}

	@SuppressWarnings("static-access")
	@Override
	public ResponseEntity<String> doPut(String uri, String payload,
			String restServerUrl, String authorization, RestTemplate restTemplate) {
		LOGGER.error("doPut with Parameters :: uri {} " + uri + " payload {} "
				+ payload + " authorization {} " + authorization);
		headers.clear();
		headers.add("Authorization", "Basic " + authorization);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> extRequest = new HttpEntity<String>(payload, headers);
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(restServerUrl + uri,
					HttpMethod.PUT, extRequest, String.class);
		} catch (HttpClientErrorException e) {
			LOGGER.error("doPut >> Exception " + e.getRawStatusCode()
					+ " and msg " + e.getResponseBodyAsString());
			return response.status(e.getRawStatusCode()).body(
					e.getResponseBodyAsString());
		} catch (Exception e) {
			LOGGER.error("doPut >> Exception " + e);
			return response.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"{\"msg\":\"Internal Server Error\"}");
		}
		LOGGER.debug("doPut >> Response :: status Code {} "
				+ response.getStatusCode() + " body {} " + response.getBody());
		return response;
	}

	@SuppressWarnings("static-access")
	@Override
	public ResponseEntity<String> doDelete(String uri, String payload,
			String restServerUrl, String authorization, RestTemplate restTemplate) {
		LOGGER.debug("doDelete with Parameters :: uri {} " + uri
				+ " payload {} " + payload);
		headers.clear();
		headers.add("Authorization", "Basic " + authorization);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> extRequest = new HttpEntity<String>(payload, headers);
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(restServerUrl + uri,
					HttpMethod.DELETE, extRequest, String.class);
		} catch (HttpClientErrorException e) {
			LOGGER.error("doDelete >> Exception " + e.getRawStatusCode()
					+ " and msg " + e.getResponseBodyAsString());
			return response.status(e.getRawStatusCode()).body(
					e.getResponseBodyAsString());
		} catch (Exception e) {
			LOGGER.error("doDelete >> Exception " + e);
			return response.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					"{\"msg\":\"Internal Server Error\"}");
		}
		LOGGER.debug("doDelete >> Response :: status Code {} "
				+ response.getStatusCode() + " body {} " + response.getBody());
		return response;
	}

	@Override
	public String getJsonFromObject(Object obj) {
		LOGGER.debug("Converting to JSON from Object {} :"+obj.toString());
		String jsonStr = null;
		try {
			jsonStr = gson.toJson(obj);
		} catch(Exception e) {
			LOGGER.error("Error while converting Object to JSON "+e);
		}
		return jsonStr;
	}

}
