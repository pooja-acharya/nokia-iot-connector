package com.nokia.iot.connector.outbound.subscription.domain;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.nokia.iot.connector.inbound.domain.RequestJson;
import com.nokia.iot.connector.inbound.domain.Response;

public class ImpactSubscription {

	private ImpactSubscriptionPayload subscriptionRequest;

	private static String RESOURCE_SUBSCRIPTION_URI = "/m2m/subscriptions?type=resources";

	private static String LCE_SUBSCRIPTION_URI = "/m2m/subscriptions?type=lifecycleEvents";

	public ImpactSubscription() {

	}
	
	public ImpactSubscription(RequestJson request) {
		this.subscriptionRequest = request.getSubscriptionRequest();
	}

	public ImpactSubscription(Response response, String groupName, List<Map<String, String>> resourceDetails) {
		this.subscriptionRequest = new ImpactSubscriptionPayload(response, groupName, resourceDetails);
	}

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ImpactSubscription.class);

	public ImpactSubscriptionPayload getSubscriptionRequest() {
		return subscriptionRequest;
	}

	public void setSubscriptionRequest(
			ImpactSubscriptionPayload subscriptionRequest) {
		this.subscriptionRequest = subscriptionRequest;
	}

	@Override
	public String toString() {
		return "ImpactSubscription [subscriptionRequest=" + subscriptionRequest
				+ "]";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> constructSubscriptionPayload(
			ImpactSubscription subscriptionReq) {
		ImpactSubscriptionPayload subscriptionPayload = subscriptionReq
				.getSubscriptionRequest();
		LOGGER.debug("payload received as " + subscriptionPayload.toString());
		Map requestJsonMap = new HashMap<>();
		try {
			if ("resources".equals(subscriptionPayload.getType())) {
				String requestJson = constructRequestJson(subscriptionPayload,
						"resources");
				requestJsonMap.put("BODY", requestJson);
				requestJsonMap.put("URI", RESOURCE_SUBSCRIPTION_URI);
			} else if ("lifecycleEvents".equals(subscriptionPayload.getType())) {
				String requestJson = constructRequestJson(subscriptionPayload,
						"lifecycleEvents");
				requestJsonMap.put("BODY", requestJson);
				requestJsonMap.put("URI", LCE_SUBSCRIPTION_URI);
			}
		} catch (Exception e) {
			LOGGER.error("Exception " + e);
		}

		return requestJsonMap;
	}

	public Map<String, String> constructResourceDetailsPayload(
			List<Map<String, String>> resourceDetails) {
		LOGGER.debug("Received List as " + resourceDetails.toString());
		Map<String, String> resourceMap = new HashMap<String, String>();
		Map<String, String> resourceIndexMap = null;
		int i = 0;
		for (Map<String, String> rmap : resourceDetails) {
			resourceIndexMap = new HashMap<String, String>();
			if (rmap.containsKey("resourcePath")) {
				resourceIndexMap.put("resourcePath" + i,
						rmap.get("resourcePath"));
			}
			if (rmap.containsKey("condition")) {
				resourceIndexMap.put("condition" + i, rmap.get("condition"));
			} else if (!rmap.containsKey("condition")) {
				resourceIndexMap.put("condition" + i, "");
			}
			resourceMap.putAll(resourceIndexMap);
			i = i + 1;
		}
		LOGGER.debug("Resource Map returning as " + resourceMap.toString());
		return resourceMap;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String constructRequestJson(
			ImpactSubscriptionPayload subscriptionPayload, String type) {
		List resourceDetails = new ArrayList();
		Map<String, Object> resource = null;
		Gson gson = new Gson();
		int mod = 1;
		String requestJson = "";
		try {
			if ("resources".equals(type)) {
				Map<String, String> resourceMap = constructResourceDetailsPayload(subscriptionPayload
						.getResourceDetails());
				if (resourceMap != null) {
					LOGGER.debug("received resourceMap size as "
							+ resourceMap.size());
					mod = ((resourceMap.size() % 2) == 0 ? (resourceMap.size() / 2)
							: (resourceMap.size() - 1));

					for (int i = 0; i < (resourceMap.size() - mod); i++) {
						resource = new HashMap<String, Object>();
						// if(resourceMap.containsKey(("resourcePath"+i))) {
						if (!resourceMap.get(("resourcePath" + i)).isEmpty()) {
							resource.put("resourcePath",
									resourceMap.get(("resourcePath" + i)));
						}
						if (!resourceMap.get(("condition" + i)).isEmpty()) {
							String propertiesFormat = resourceMap.get(
									("condition" + i)).replaceAll(",", "\n");
							Properties properties = new Properties();
							properties.load(new StringReader(propertiesFormat));

							resource.put("conditions", new HashMap(properties));
						}
						resourceDetails.add(resource);
					}
				}
			}
			Map subscriptionJson = new HashMap<>();
			Map manuDataJSON = new HashMap<>();
			Map criteriaJSON = new HashMap<>();

			Map<String, Object> subscriptionData = new HashMap<String, Object>();
			subscriptionData.put("groupName",
					subscriptionPayload.getGroupName());

			if (!(StringUtils.isBlank(subscriptionPayload.getMake()))) {
				manuDataJSON.put("make", subscriptionPayload.getMake());
			}
			if (!(StringUtils.isBlank(subscriptionPayload.getModel()))) {
				manuDataJSON.put("model", subscriptionPayload.getModel());
			}
			if (!(resourceDetails.isEmpty())) {
				subscriptionData.put("resources", resourceDetails);
			}

			if (!(StringUtils.isBlank(subscriptionPayload.getFirmwareVersion()))) {
				manuDataJSON.put("firmwareVersion",
						subscriptionPayload.getFirmwareVersion());
			}
			if (!"resources".equals(type)) {
				if (subscriptionPayload.getEvents() != null) {
					subscriptionData.put("events",
							subscriptionPayload.getEvents());
				}
			}

			if (subscriptionPayload.getSerialNumber() != null) {
				criteriaJSON.put("serialNumbers",
						subscriptionPayload.getSerialNumber());
			}
			if (!manuDataJSON.isEmpty()) {
				criteriaJSON.put("manufacturerData", manuDataJSON);
			}
			if (!criteriaJSON.isEmpty()) {
				subscriptionData.put("criteria", criteriaJSON);
			}

			subscriptionData.put("deletionPolicy",
					subscriptionPayload.getDeletionPolicy());
			subscriptionJson.putAll(subscriptionData);
			requestJson = gson.toJson(subscriptionJson);
		} catch (Exception e) {
			LOGGER.error("Error in constructRequestJson "+e);
		}
		return requestJson;
	}
}
