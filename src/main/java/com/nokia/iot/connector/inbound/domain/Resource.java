package com.nokia.iot.connector.inbound.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Resource
{
    @JsonProperty("resourcePath")
    protected String resource;

    @JsonProperty("value")
    protected String value;

    public Resource()
    {

    }

    public String getResource()
    {
        return resource;
    }

    public void setResource(String resource)
    {
        this.resource = resource;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

	@Override
	public String toString() {
		return "Resource [resource=" + resource + ", value=" + value + "]";
	}
    
}
