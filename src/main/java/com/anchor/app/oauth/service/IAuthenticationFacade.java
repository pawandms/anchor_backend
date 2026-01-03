package com.anchor.app.oauth.service;

import org.springframework.security.core.Authentication;

import com.anchor.app.oauth.exceptions.AuthServiceException;
import com.anchor.app.oauth.model.User;

public interface IAuthenticationFacade {
	
	Authentication getAuthentication();

	public User getApiAuthenticationDetails() throws AuthServiceException;
}
