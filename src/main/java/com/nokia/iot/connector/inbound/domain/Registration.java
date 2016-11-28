package com.nokia.iot.connector.inbound.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Registration
{
    @JsonProperty("serialNumber")
    protected String serialNumber;

    @JsonProperty("timestamp")
    protected long timestamp;

    @JsonProperty("make")
    protected String make;

    @JsonProperty("model")
    protected String model;

    @JsonProperty("firmwareVersion")
    protected String firmwareVersion;

    @JsonProperty("groupName")
    protected String groupName;

    @JsonProperty("imsi")
    protected String imsi;

    @JsonProperty("address")
    protected String address;

    @JsonProperty("protocol")
    protected String protocol;

    @JsonProperty("tags")
    protected String tags;
    
    @JsonProperty("subscriptionId")
    protected String subscriptionId;

    public Registration()
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

    public String getMake()
    {
        return make;
    }

    public void setMake(String make)
    {
        this.make = make;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public String getFirmwareVersion()
    {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion)
    {
        this.firmwareVersion = firmwareVersion;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getImsi()
    {
        return imsi;
    }

    public void setImsi(String imsi)
    {
        this.imsi = imsi;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	@Override
	public String toString() {
		return "Registration [serialNumber=" + serialNumber + ", timestamp=" + timestamp
				+ ", make=" + make + ", model=" + model + ", firmwareVersion="
				+ firmwareVersion + ", groupName=" + groupName + ", imsi="
				+ imsi + ", address=" + address + ", protocol=" + protocol
				+ ", tags=" + tags + ", subscriptionId=" + subscriptionId + "]";
	}
	
}
