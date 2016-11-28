package com.nokia.iot.connector.outbound.resource.domain;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.nokia.iot.connector.inbound.domain.MqttPayload;
import com.nokia.iot.connector.inbound.domain.RequestJson;

public class ImpactResource {

	private ImpactResourcePayload resourceRequest;
		
	 static final String READ_RESOURCE_METHOD_TYPE = "GET";
	
	 static final String WRITE_RESOURCE_METHOD_TYPE = "PUT";
	
	 static final String DELETE_RESOURCE_METHOD_TYPE = "DELETE";
	
	 static final String EXEC_RESOURCE_METHOD_TYPE = "POST";
	
	public ImpactResource(){
		
	}
	
	public ImpactResource(MqttPayload payload, String resourceOperationType) {
		this.resourceRequest = new ImpactResourcePayload(payload, resourceOperationType);
	}

	public ImpactResourcePayload getResourceRequest() {
		return resourceRequest;
	}

	public void setResourceRequest(ImpactResourcePayload resourceRequest) {
		this.resourceRequest = resourceRequest;
	}

	@Override
	public String toString() {
		return "ImpactResource [resourceRequest=" + resourceRequest + "]";
	}

	public Map<String,String> contructResourcePayload(ImpactResource resourceReq) {
		Gson gson = new Gson();
		String operation = resourceReq.getResourceRequest().getOperation();
		String jsonPayload = "";
		Map<String,String> requestMap = new HashMap<String, String>();
		if("read".equalsIgnoreCase(operation)) {
			
			jsonPayload = constructReadResourceUri(resourceReq.getResourceRequest());
			requestMap.put("URI", jsonPayload);
			requestMap.put("METHOD_TYPE", ImpactResource.READ_RESOURCE_METHOD_TYPE);
			
		} else if("write".equalsIgnoreCase(operation)) {
			
			jsonPayload = constructWriteResourceUri(resourceReq.getResourceRequest());
			Map<String,String> writeReqBody = new HashMap<String, String>();
			writeReqBody.put("resourceValue", resourceReq.getResourceRequest().getResourceValue());
			
			requestMap.put("URI", jsonPayload);
			requestMap.put("BODY", gson.toJson(writeReqBody));
			requestMap.put("METHOD_TYPE", ImpactResource.WRITE_RESOURCE_METHOD_TYPE);
			
		} else if("execute".equalsIgnoreCase(operation)) {
			
			jsonPayload = constructExecResourceUri(resourceReq.getResourceRequest());
			requestMap.put("URI", jsonPayload);
			requestMap.put("METHOD_TYPE", ImpactResource.EXEC_RESOURCE_METHOD_TYPE);
			
		} else if("delete".equalsIgnoreCase(operation)) {
			
			jsonPayload = constructDeleteResourceUri(resourceReq.getResourceRequest());
			requestMap.put("URI", jsonPayload);
			requestMap.put("METHOD_TYPE", ImpactResource.DELETE_RESOURCE_METHOD_TYPE);
		}
		return requestMap;
	}
	
	public String constructDeleteResourceUri(
			ImpactResourcePayload deleteResourceReq) {
		String deleteResourcePath = "/m2m/endpoints/"+deleteResourceReq.getSerialNumber()+"/"+deleteResourceReq.getResourcePath();
		return deleteResourcePath;
	}

	public String constructExecResourceUri(
			ImpactResourcePayload execResourceReq) {
		String execResourcePath = "/m2m/endpoints/"+execResourceReq.getSerialNumber()+"/"+execResourceReq.getResourcePath();
		return execResourcePath;
	}

	public String constructWriteResourceUri(
			ImpactResourcePayload writeResourceReq) {
		String writeResourcePath = "/m2m/endpoints/"+writeResourceReq.getSerialNumber()+"/"+writeResourceReq.getResourcePath();
		return writeResourcePath;
	}

	public String constructReadResourceUri(
			ImpactResourcePayload readResourceReq) {
		String readResourcePath = "/m2m/endpoints/"+readResourceReq.getSerialNumber()+"/"+readResourceReq.getResourcePath();
		return readResourcePath;
	}
}
