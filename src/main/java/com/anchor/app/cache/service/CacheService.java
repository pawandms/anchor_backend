package com.anchor.app.cache.service;

import com.anchor.app.exception.CacheServiceException;
import com.anchor.app.oauth.model.UserVerifyToken;

public interface CacheService {
	
	/**
	 * Send Confirmation Email Message to User
	 * @param confirmationMsg
	 */
	public void sendSingupVerificationEmailMsg(UserVerifyToken confirmationMsg) throws CacheServiceException;

}
