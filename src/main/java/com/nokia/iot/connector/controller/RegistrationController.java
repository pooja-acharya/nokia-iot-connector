package com.nokia.iot.connector.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.iot.connector.inbound.domain.Notification;
import com.nokia.iot.connector.registration.service.IRegistrationService;

@RestController
@RequestMapping("/*")
public class RegistrationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);
	
	@Autowired
	IRegistrationService registrationService;
	
	@RequestMapping(value = "/impact/register", method = RequestMethod.POST, headers = "Accept=application/json")
	public String setCallbackServer(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.debug("RegistrationController.setCallbackServer >> ");
		ResponseEntity<String> registrationResponse = null;
		String registrationRespStr = null;
		try {
			registrationResponse = registrationService.registerToImpact(request);
			response.setStatus(registrationResponse.getStatusCodeValue());
			LOGGER.debug("RegistrationController.setCallbackServer << response {} "+registrationResponse.getBody());
			registrationRespStr = registrationResponse.getBody();
		} catch(Exception e) {
			LOGGER.debug("Exception in setCallbackServer {} "+e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			registrationRespStr = "{\"msg\":\"Internal Server Error\"}";
		}
		return registrationRespStr;
	}
	
	@RequestMapping(value = "/inbound/callback", method = RequestMethod.PUT)
	public void pingCallbackUrl(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.debug(">> Checking if callback URL active from server:: "+request.getServerName()+request.getServerPort()+request.getRequestURI() );
		response.setStatus(HttpStatus.OK.value());
	}
	
	@RequestMapping(value = "/inbound/callback", method = RequestMethod.POST, headers = "Accept=application/json") 
	public String callbackNotificationData(@RequestBody(required = false) Notification notification,HttpServletRequest request, HttpServletResponse response) {
		LOGGER.debug("Callback url is called ");
		try {
			if(null == notification) {
				LOGGER.debug("Callback ping is success ");
				response.setStatus(HttpStatus.OK.value());
				return "Success";
			} else {
				LOGGER.debug("Received following data :: "+notification.toString());
				registrationService.publishCallbackData(notification);
				LOGGER.debug("Notification Data published successfully");
				response.setStatus(HttpStatus.OK.value());
				return "Success";
			}
		}catch(Exception e) {
			LOGGER.debug("Error while publishing Notification data "+e);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return "Internal Server Error";
	}
	@RequestMapping(value = "/test", method = RequestMethod.GET, headers = "Accept=application/json") 
	public String test(HttpServletRequest request, HttpServletResponse response) {
		return "Hi test";
	}
}
