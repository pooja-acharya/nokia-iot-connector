package com.nokia.iot.connector.security.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import com.nokia.iot.connector.config.credentials.UserCredentials;

@Service
public class AuthenticationUtilImpl implements AuthenticationUtil {
	
	@Autowired
	UserCredentials userCred;

	protected Logger LOGGER = LoggerFactory.getLogger(AuthenticationUtilImpl.class);
	
	
	public boolean isCallbackUserAuthenticated(String authorization) throws Exception {
		LOGGER.debug(">> Checking if callback user authenticated");
		
		String base64Credentials = authorization.substring(
                "Basic".length()).trim();
        String credentials = new String(Base64.decode(base64Credentials
                .getBytes()));
        

        // credentials = username:password
        final String[] values = credentials.split(":", 2);

        if (values.length < 2) {
            LOGGER.debug("AuthenticationUtilImpl isCallbackUserAuthenticated called  value length less than 2 "
                    + values.length);
            throw new Exception("User credentials are not proper");
        }
        
        String username = values[0];
        String password = values[1];
        boolean isAuthenticated = false;
        isAuthenticated = checkCallbackUserValidity(username, password);
        
        LOGGER.debug("Is user authenticated ? "+isAuthenticated);
        return isAuthenticated;
	}


	private boolean checkCallbackUserValidity(String username, String password) {
		LOGGER.debug("Checking user : "+username+" as callback user ");
		try {
			String callbackFileUsrname = userCred.decodeBase64(userCred.getImpactCallbackUsername());
			String callbackFilePassword = userCred.decodeBase64(userCred.getImpactCallbackPassword());
				if(username.equals(callbackFileUsrname) && password.equals(callbackFilePassword)) {
					LOGGER.debug("User is authenticated ");
					return true;
			} else {
				LOGGER.debug("User is not authenticated");
			}
		} catch(Exception e) {
			LOGGER.error("Error while checking callback user validity ");
		}
		return false;
	}
}
