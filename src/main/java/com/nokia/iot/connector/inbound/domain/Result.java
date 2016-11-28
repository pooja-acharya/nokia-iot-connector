package com.nokia.iot.connector.inbound.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result
{
    @JsonProperty("code")
    protected long code;

    @JsonProperty("subCode")
    protected String subCode;

    @JsonProperty("reason")
    protected String reason;

    public Result()
    {

    }

    public long getCode()
    {
        return code;
    }

    public void setCode(long code)
    {
        this.code = code;
    }

    public String getSubCode()
    {
        return subCode;
    }

    public void setSubCode(String subCode)
    {
        this.subCode = subCode;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

	@Override
	public String toString() {
		return "Result [code=" + code + ", subCode=" + subCode + ", reason="
				+ reason + "]";
	}
    
}
