package com.anchor.app.oauth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.anchor.app.oauth.exceptions.AuthServiceException;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.service.IAuthenticationFacade;
import com.anchor.app.users.service.UserService;

@Service
public class IAuthenticationFacadeImpl implements IAuthenticationFacade {

	 private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;
	
	@Override
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@Override
	public User getApiAuthenticationDetails() throws AuthServiceException {
		
		User key = null;

	       try {
	           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	           
	           String principal = null;
	           if (authentication != null && authentication.getName() != null) {
	               principal = authentication.getName();
	           }

	           if( null != principal)
	           {
	        	 key=  userService.getUserDetails(principal);
	           }

	       }
	       catch(Exception e)
	       {
				logger.error(e.getMessage(), e);
	            throw new AuthServiceException(e.getMessage(), e);
	       }

	        return key;
	}

	
}

