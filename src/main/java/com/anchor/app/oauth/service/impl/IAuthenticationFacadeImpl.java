package com.anchor.app.oauth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import com.anchor.app.exception.AuthServiceException;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.service.IAuthenticationFacade;
import com.anchor.app.oauth.service.UserService;

@Service
public class IAuthenticationFacadeImpl implements IAuthenticationFacade {

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
	           Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
	           
	           String principal = (String) authentication.getPrincipal();

	           if( null != principal)
	           {
	        	 key=  userService.getUserDetails(principal);
	           }

	       }
	       catch(Exception e)
	       {
	            throw new AuthServiceException(e.getMessage(), e);
	       }

	        return key;
	}

	@Override
	public String getJti() throws AuthServiceException {
		
		String result = null;
	
		
		return result;
		
	}	
	

	
}
