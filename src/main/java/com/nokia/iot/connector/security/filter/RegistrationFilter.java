/**
 * THIS PROGRAM IS THE CONFIDENTIAL AND PROPRIETARY PRODUCT OF MFORMATION
 * SOFTWARE TECHNOLOGIES LLC. ANY UNAUTHORIZED USE, REPRODUCTION OR TRANSFER OF
 * THIS PROGRAM IS STRICTLY PROHIBITED. COPYRIGHT 2015 BY MFORMATION SOFTWARE
 * TECHNOLOGIES LLC. (SUBJECT TO LIMITED DISTRIBUTION AND RESTRICTED DISCLOSURE
 * ONLY). ALL RIGHTS RESERVED. $Id: $ $Log: $
 */

package com.nokia.iot.connector.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import com.google.gson.Gson;
import com.nokia.iot.connector.security.Util.AuthenticationUtil;
/**
 * <PRE>
 * Module:
 * 
 * Purpose:
 * 
 * Steps and Logic:
 *
 * </PRE>
 *
 * @author pachary
 * @version $Revision$
 */
public class RegistrationFilter extends GenericFilterBean {

	@Autowired
	private AuthenticationUtil authenticationUtil;
	
	protected Logger LOGGER = LoggerFactory.getLogger(RegistrationFilter.class);


	public RegistrationFilter(AuthenticationUtil authenticationUtil) {
		super();
		this.authenticationUtil = authenticationUtil;
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String url = httpRequest.getRequestURI().trim();
		Gson gson = new Gson();
		//LOGGER.debug("RegistrationFilter: servlet path " + url + "http method called is " + httpRequest.getMethod());

		try {
			if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
				LOGGER.debug("OPTIONS http method");
			} else if (url.contains("/inbound/callback")) {
				LOGGER.debug("this url requires authentication :: "+url);
				try {
					String authorizationHeaderValue = httpRequest.getHeader("Authorization");
					
					if(null == authorizationHeaderValue) {
						LOGGER.debug("authorizationHeaderValue is null ");
					}
					if(authenticationUtil.isCallbackUserAuthenticated(authorizationHeaderValue)) {
						LOGGER.debug("User is authenticated to POST");
					} else {
						httpResponse.getWriter().write(gson.toJson("Access denied"));
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
				} catch(Exception e) {
					LOGGER.error("Exception in RegistrationFilter "+e);
					httpResponse.getWriter().write(gson.toJson("Access denied"));
					httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
			} else {
				LOGGER.debug("this url "+url+" doesn't require authentication");
				//LOGGER.debug("Allow other URL");
				/*Map<String, String> userCredMap = null;
				String username = null;
				String groupid = null;
				String groupname = null;

				if (null != url) {

					logger.debug("RegistrationFilter: registration or callback " + restServerUrl);

					final String authorization = httpRequest.getHeader("Authorization");

					if (authorization != null && authorization.startsWith("Basic")) {

						HttpHeaders headers = new HttpHeaders();

						headers.add("Authorization", httpRequest.getHeader("Authorization"));
						headers.setContentType(MediaType.APPLICATION_JSON);

						String groupName = null;

						if (httpRequest.getParameter("groupName") != null) {
							groupName = httpRequest.getParameter("groupName");
							logger.debug("RegistrationFilter: groupName from query Parameter is " + groupName);
						} else {

							logger.debug("payload is " + authenticationRequestWrapper.getJsonPayload());
							try {
								jsonObject = gson.fromJson(authenticationRequestWrapper.getJsonPayload(),
										JsonObject.class);
							} catch (Exception e) {
								logger.debug("RegistrationFilter:  Malformed json received {}", e);
								authResponseMessage.setMsg(ResponseMessage.BAD_REQUEST.getKey());
								httpResponse.getWriter().write(gson.toJson(authResponseMessage));
								httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
								return;
							}

							logger.debug("RegistrationFilter: json object received " + jsonObject);

							if (jsonObject != null && jsonObject.get("groupName") != null) {
								groupName = jsonObject.get("groupName").getAsString();
							}
						}

						if (groupName != null && groupName.equalsIgnoreCase("DM")) {

							logger.error("403:Groupname is DM, You cannot perform action in requested group");
							authResponseMessage.setMsg(ResponseMessage.YOU_CANNOT_PERFORM_ACTION_IN_REQUESTED_GROUP.getKey());
							httpResponse.getWriter().write(gson.toJson(authResponseMessage));
							httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
							return;

						}

						GroupRequest groupRequest = new GroupRequest();
						groupRequest.setGroupFullName(groupName);
						HttpEntity<GroupRequest> httpEntity = new HttpEntity<GroupRequest>(groupRequest, headers);

						ResponseEntity<GroupResponse> authResponse = null;
						try {
							logger.debug("RegistrationFilter: validateGroup starts");

							authResponse = apiUtilService.validateGroup(httpEntity);

							logger.debug("RegistrationFilter: validateGroup ends");

							if (authResponse.getStatusCode().value() == HttpServletResponse.SC_OK) {
								
								GroupResponse groupResponse = (GroupResponse) authResponse.getBody();
								groupid = groupResponse.getGroupId();
								groupname = groupResponse.getGroupFullName();
							
							}
						} catch(HttpClientErrorException hcEE){
							logger.error(
									"401:Authentication call returned error code, username/pwd/groupname is not valid");
							authResponseMessage.setMsg(ResponseMessage.INCORRECT_CREDENTIALS.getKey());
							httpResponse.getWriter().write(gson.toJson(authResponseMessage));
							httpResponse.setStatus(hcEE.getStatusCode().value());
							return;
							
						} 
						catch (Exception e) {
							logger.error("503:Authentication call to authprovider failed {}", e.getMessage());
							authResponseMessage.setMsg(ResponseMessage.SERVICE_UNAVAILABLE.getKey());
							httpResponse.getWriter().write(gson.toJson(authResponseMessage));
							httpResponse.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
							return;
						}

					} else {
						logger.error("401:Authentication call is not basic" + httpRequest.getHeader("Authorization"));
						try {
							authResponseMessage.setMsg(ResponseMessage.AUTHENTICATION_CALL_IS_NOT_BASIC.getKey());
							httpResponse.getWriter().write(gson.toJson(authResponseMessage));
							httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							return;
						} catch (Exception e2) {
							logger.error("Unauthorized");
						}
					}

					try {

						userCredMap = apiUtilService.getUserCredentials(httpRequest);

						username = userCredMap.get("username");

					} catch (Exception e3) {
						logger.error("403:Exception in obtaining username");
						authResponseMessage.setMsg(ResponseMessage.EXCEPTION_IN_OBTAINING_USERNAME.getKey());
						httpResponse.getWriter().write(gson.toJson(authResponseMessage));
						httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
						return;
					}

				}

				authenticationRequestWrapper.setAttribute("USERNAME", username);
				authenticationRequestWrapper.setAttribute("GROUPID", new Long(groupid));
				authenticationRequestWrapper.setAttribute("GROUPNAME", groupname);

				if (null != url && !url.equalsIgnoreCase("/m2m/applications/registration")
						&& !url.equalsIgnoreCase("/m2m/applications/callback")) {

					ApiRegistration apiRegistration = registrationService.getApiRegistration(username);
					if (apiRegistration == null) {
						logger.error("403:Authentication apiregistration is null");
						authResponseMessage.setMsg(ResponseMessage.CLIENT_IS_NOT_REGISTERED.getKey());
						httpResponse.getWriter().write(gson.toJson(authResponseMessage));
						httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
						return;
					} else {
						logger.error("apregistration is not null" + apiRegistration.getApiRegistrationId());
						authenticationRequestWrapper.setAttribute("APIREGID", apiRegistration.getApiRegistrationId());
					}
				}*/

			}
		} catch (Exception e) {

			LOGGER.warn("Exception in doFilter " + e);
			httpResponse.getWriter().write(gson.toJson("Access denied"));
			httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		LOGGER.debug("RegistrationFilter: Ends");
		chain.doFilter(request, response);
	}

}
