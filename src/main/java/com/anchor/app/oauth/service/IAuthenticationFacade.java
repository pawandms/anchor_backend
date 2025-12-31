package com.anchor.app.oauth.service;

import org.springframework.security.core.Authentication;

import com.anchor.app.exception.AuthServiceException;
import com.anchor.app.oauth.model.User;

public interface IAuthenticationFacade {
	
	Authentication getAuthentication();
	
	/**
     * Get AuthenticationDetails from Authentication Token
     * @return
     */
    public User getApiAuthenticationDetails() throws AuthServiceException;
    
    public String getJti()throws AuthServiceException;

}
