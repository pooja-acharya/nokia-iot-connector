package com.nokia.iot.connector.inbound.domain;

public class WatsonProperties {

	private String watsonApiKey;
	
	private String watsonApiToken;
	
	private String watsonOrgId;
	
	private String msg;

	public String getWatsonApiKey() {
		return watsonApiKey;
	}

	public void setWatsonApiKey(String watsonApiKey) {
		this.watsonApiKey = watsonApiKey;
	}

	public String getWatsonApiToken() {
		return watsonApiToken;
	}

	public void setWatsonApiToken(String watsonApiToken) {
		this.watsonApiToken = watsonApiToken;
	}

	public String getWatsonOrgId() {
		return watsonOrgId;
	}

	public void setWatsonOrgId(String watsonOrgId) {
		this.watsonOrgId = watsonOrgId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "WatsonProperties [watsonApiKey=" + watsonApiKey
				+ ", watsonOrgId=" + watsonOrgId + "]";
	}
}
