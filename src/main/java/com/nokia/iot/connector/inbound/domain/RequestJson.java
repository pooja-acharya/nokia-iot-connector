package com.nokia.iot.connector.inbound.domain;

import com.nokia.iot.connector.outbound.resource.domain.ImpactResourcePayload;
import com.nokia.iot.connector.outbound.subscription.domain.ImpactSubscriptionPayload;

public class RequestJson {

	private ImpactResourcePayload resourceRequest;
	
	private ImpactSubscriptionPayload subscriptionRequest;

	public ImpactResourcePayload getResourceRequest() {
		return resourceRequest;
	}

	public void setResourceRequest(ImpactResourcePayload resourceRequest) {
		this.resourceRequest = resourceRequest;
	}

	public ImpactSubscriptionPayload getSubscriptionRequest() {
		return subscriptionRequest;
	}

	public void setSubscriptionRequest(ImpactSubscriptionPayload subscriptionRequest) {
		this.subscriptionRequest = subscriptionRequest;
	}

	@Override
	public String toString() {
		return "RequestJson [resourceRequest=" + resourceRequest
				+ ", subscriptionRequest=" + subscriptionRequest + "]";
	}

}
