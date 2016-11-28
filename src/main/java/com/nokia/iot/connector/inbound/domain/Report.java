package com.nokia.iot.connector.inbound.domain;

import java.util.Map;
/*
 * 
 * Subscription Callback Data
 */
public class Report
{
    protected String serialNumber;

    protected long timestamp;
    
    protected String subscriptionId;

    protected String resourcePath;

    protected String value;

    public Report()
    {

    }
    public Report(Map<String, Object> map) {
		//this.callbackReportId = map.get(key)
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

    public String getSubscriptionId()
    {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId)
    {
        this.subscriptionId = subscriptionId;
    }

    public String getResourcePath()
    {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath)
    {
        this.resourcePath = resourcePath;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

	public String getFormattedSerialNumber(String serialNum){
		String newSerial = serialNum.replaceAll(":", "-");
		return newSerial;
	}
	
	@Override
	public String toString() {
		return "Report [serial=" + serialNumber + ", timestamp=" + timestamp
				+ ", subscriptionId=" + subscriptionId + ", resourcePath="
				+ resourcePath + ", value=" + value + "]";
	}
}
