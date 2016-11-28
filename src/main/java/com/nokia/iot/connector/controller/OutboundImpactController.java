package com.nokia.iot.connector.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.iot.connector.outbound.resource.domain.ImpactResource;
import com.nokia.iot.connector.outbound.service.IOutboundImpactService;
import com.nokia.iot.connector.outbound.subscription.domain.ImpactSubscription;

@RestController
@RequestMapping("/outbound/impact")
public class OutboundImpactController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OutboundImpactController.class);
	
	@Autowired
	IOutboundImpactService outboundImpactService;
	
	@RequestMapping(value = "/resource", method = RequestMethod.POST, headers = "Accept=application/json")
	public String sendResourceRequest(@RequestBody ImpactResource resourceReq, HttpServletRequest request, HttpServletResponse response) {
		LOGGER.debug("OutboundImpactController.sendResourceRequest >> "+resourceReq.toString());
		ResponseEntity<String> resourceResponse = null;
		String resourceRespStr = null;
		try {
			resourceResponse = outboundImpactService.sendResourceRequestToImpact(resourceReq);
			resourceRespStr = resourceResponse.getBody();
			response.setStatus(resourceResponse.getStatusCodeValue());
			
		} catch(Exception e) {
			LOGGER.debug("Exception in sendResourceRequest {} "+e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			resourceRespStr = "{\"msg\":\"Internal Server Error\"}";
		}
		return resourceRespStr;
	}
	
	@RequestMapping(value = "/subscription/create", method = RequestMethod.POST, headers = "Accept=application/json")
	public String sendSubscriptionRequest(@RequestBody ImpactSubscription subscriptionReq, HttpServletRequest request, HttpServletResponse response) {
		LOGGER.debug("OutboundImpactController.sendSubscriptionRequest >> "+subscriptionReq.toString());
		ResponseEntity<String> resourceResponse = null;
		String resourceRespStr = null;
		try {
			resourceResponse = outboundImpactService.sendSubscriptionRequestToImpact(subscriptionReq);
			resourceRespStr = resourceResponse.getBody();
			response.setStatus(resourceResponse.getStatusCodeValue());
			
		} catch(Exception e) {
			LOGGER.debug("Exception in sendResourceRequest {} "+e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			resourceRespStr = "{\"msg\":\"Internal Server Error\"}";
		}
		return resourceRespStr;
	}
	
	@RequestMapping(value = "/endpoints/{serialNumber}", method = RequestMethod.GET, headers = "Accept=application/json")
	public String sendGetEndpointDetails(@PathVariable("serialNumber") String serialNumber, HttpServletRequest request, HttpServletResponse response) {
		LOGGER.debug("OutboundImpactController.sendGetEndpointDetails >> "+serialNumber);
		ResponseEntity<String> getEndpointDetailsResponse = null;
		String getEndpointDetailsStr = null;
		try {
			getEndpointDetailsResponse = outboundImpactService.sendGetEndpointDetails(serialNumber);
			getEndpointDetailsStr = getEndpointDetailsResponse.getBody();
			response.setStatus(getEndpointDetailsResponse.getStatusCodeValue());
			
		} catch(Exception e) {
			LOGGER.debug("Exception in sendResourceRequest {} "+e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			getEndpointDetailsStr = "{\"msg\":\"Internal Server Error\"}";
		}
		return getEndpointDetailsStr;
	}
	
}
