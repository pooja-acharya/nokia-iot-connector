package com.nokia.iot.connector.inbound.domain;

public class ImpactProperties {

	private String impactUsername;
	
	private String impactPassword;
	
	private String impactGroupName;
	
	private String msg;

	public String getImpactUsername() {
		return impactUsername;
	}

	public void setImpactUsername(String impactUsername) {
		this.impactUsername = impactUsername;
	}

	public String getImpactPassword() {
		return impactPassword;
	}

	public void setImpactPassword(String impactPassword) {
		this.impactPassword = impactPassword;
	}

	public String getImpactGroupName() {
		return impactGroupName;
	}

	public void setImpactGroupName(String impactGroupName) {
		this.impactGroupName = impactGroupName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ImpactProperties [impactUsername=" + impactUsername
				+ ", impactGroupName=" + impactGroupName + "]";
	}
}
