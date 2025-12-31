package com.anchor.app.media.service;

import com.anchor.app.exception.AppUserServiceException;
import com.anchor.app.media.model.AppUser;

@Deprecated
public interface AppUserService {
	
	
	/**
	 * Add or Update App User Details
	 * @param user
	 * @throws AppUserServiceException
	 */
	public void addUpdateAppUserDetails(AppUser user) throws AppUserServiceException; 
	
	/**
	 * Update IP Address Details of AppUser
	 * @param appUserID
	 * @param ipAddress
	 * @throws AppUserServiceException
	 */
	public void updateAppUserIpAddress(String appUserID, String ipAddress) throws AppUserServiceException;

}
