package com.nokia.iot.connector.inbound.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class Notification
{
	
	@JsonProperty("reports")
    protected List<Report> reports;

    @JsonProperty("registrations")
    protected List<Registration> registrations;

    @JsonProperty("deregistrations")
    protected List<Deregistration> deregistrations;

    @JsonProperty("updates")
    protected List<Update> updates;

    @JsonProperty("expirations")
    protected List<Expiration> expirations;

    @JsonProperty("responses")
    protected List<Response> responses;

    public Notification()
    {

    }

    public List<Response> getResponses()
    {
        return responses;
    }

    public void setResponses(List<Response> responses)
    {
        this.responses = responses;
    }

    public List<Report> getReports()
    {
        return reports;
    }

    public void setReports(List<Report> reports)
    {
        this.reports = reports;
    }

    public List<Registration> getRegistrations()
    {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations)
    {
        this.registrations = registrations;
    }

    public List<Deregistration> getDeregistrations()
    {
        return deregistrations;
    }

    public void setDeregistrations(List<Deregistration> deregistrations)
    {
        this.deregistrations = deregistrations;
    }

    public List<Update> getUpdates()
    {
        return updates;
    }

    public void setUpdates(List<Update> updates)
    {
        this.updates = updates;
    }

    public List<Expiration> getExpirations()
    {
        return expirations;
    }

    public void setExpirations(List<Expiration> expirations)
    {
        this.expirations = expirations;
    }

	@Override
	public String toString() {
		return "Notification [reports=" + reports + ", registrations="
				+ registrations + ", deregistrations=" + deregistrations
				+ ", updates=" + updates + ", expirations=" + expirations
				+ ", responses=" + responses + "]";
	}

}
