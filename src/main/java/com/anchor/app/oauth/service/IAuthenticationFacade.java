package com.anchor.app.oauth.service;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
	
	Authentication getAuthentication();

}
