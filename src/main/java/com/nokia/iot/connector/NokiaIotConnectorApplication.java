package com.nokia.iot.connector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

@SuppressWarnings("deprecation")
@SpringBootApplication(exclude = MessageSourceAutoConfiguration.class)
@PropertySource(value = { "file:${CONNECTOR_HOME}/application.properties" })
public class NokiaIotConnectorApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(NokiaIotConnectorApplication.class, args);
	}
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	       return application.sources(nokiaIotConnectorApplication);
	}
	
	private static Class<NokiaIotConnectorApplication> nokiaIotConnectorApplication = NokiaIotConnectorApplication.class;
}
