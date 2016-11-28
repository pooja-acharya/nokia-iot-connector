package com.nokia.iot.connector.inbound.domain;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Deregistration
{
    @JsonProperty("serialNumber")
    protected String serialNumber;

    @JsonProperty("timestamp")
    protected long timestamp;

	protected String subscriptionId;


    public Deregistration()
    {

    }

    public String getSerialNumber() {
		return serialNumber;
	}


	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}


	public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }
    public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	@Override
	public String toString() {
		return "Deregistration [serialNumber=" + serialNumber + ", timestamp=" + timestamp
				+ ", subscriptionId=" + subscriptionId + "]";
	}
	
}
