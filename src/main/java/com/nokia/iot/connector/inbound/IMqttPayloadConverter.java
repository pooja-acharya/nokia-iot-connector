package com.nokia.iot.connector.inbound;

public interface IMqttPayloadConverter {
	
	static String MQTT_EVENT_RESOURCE = "resource";
	
	static String MQTT_EVENT_SUBSCRIPTION = "subscription";
	
	static String READ_RESOURCE_OPERATION_METHOD = "GET";
	
	static String WRITE_RESOURCE_OPERATION_METHOD = "PUT";
	
	static String EXECUTE_RESOURCE_OPERATION_METHOD = "POST";
	
	static String DELETE_RESOURCE_OPERATION_METHOD = "DELETE";

	public void convertAndSend(String mqttPayload);
}
