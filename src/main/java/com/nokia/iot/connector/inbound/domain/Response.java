package com.nokia.iot.connector.inbound.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by pachary on 5/16/2016.
 * Resource (request_id) related data
 */
public class Response
{
    @JsonProperty("serialNumber")
    protected String serialNumber;

    @JsonProperty("timestamp")
    protected long timestamp;

    @JsonProperty("requestId")
    public String requestId;

    @JsonProperty("creationDate")
    protected String creationDate;

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

    @JsonProperty("freeFormAddress")
    protected String freeformAddress;

    @JsonProperty("protocol")
    protected String protocol;

    @JsonProperty("resources")
    protected List<Resource> resources;

    @JsonProperty("result")
    protected Result result;


    public Response()
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

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public String getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(String creationDate)
    {
        this.creationDate = creationDate;
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

    public String getFreeformAddress()
    {
        return freeformAddress;
    }

    public void setFreeformAddress(String freeformAddress)
    {
        this.freeformAddress = freeformAddress;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public List<Resource> getResources()
    {
        return resources;
    }

    public void setResources(List<Resource> resources)
    {
        this.resources = resources;
    }

    public Result getResult()
    {
        return result;
    }

    public void setResult(Result result)
    {
        this.result = result;
    }
    
    public String getFormattedSerialNumber(String serialNum){
		String newSerial = serialNum.replaceAll(":", "-");
		return newSerial;
	}

	@Override
	public String toString() {
		return "Response [serialNumber=" + serialNumber + ", timestamp="
				+ timestamp + ", requestId=" + requestId + ", creationDate="
				+ creationDate + ", make=" + make + ", model=" + model
				+ ", firmwareVersion=" + firmwareVersion + ", groupName="
				+ groupName + ", imsi=" + imsi + ", address=" + address
				+ ", freeformAddress=" + freeformAddress + ", protocol="
				+ protocol + ", resources=" + resources + ", result=" + result
				+ "]";
	}
}
