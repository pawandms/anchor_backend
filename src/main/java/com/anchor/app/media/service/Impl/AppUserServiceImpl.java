package com.anchor.app.media.service.Impl;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.exception.AppUserServiceException;
import com.anchor.app.media.model.AppUser;
import com.anchor.app.media.repository.AppUserRepository;
import com.anchor.app.media.service.AppUserService;

@Service
public class AppUserServiceImpl implements AppUserService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AppUserRepository appUserRep;

	@Override
	public void addUpdateAppUserDetails(AppUser user) throws AppUserServiceException {
		
		try {
			
			Date startT = new Date();
		
			if( null == user.getId())
			{
				throw new AppUserServiceException("UserID can not be null");
			}
			
			// Step 1 : Verify is User is Present in System
		Optional<AppUser> optAppUser = 	appUserRep.findById(user.getId());
		
		if(optAppUser.isPresent())
		{
			AppUser modUser = optAppUser.get();
			modUser.setModifiedOn(startT);
			
			appUserRep.save(modUser);
			
		}
		else {
			
			
			user.setCreatedBy("SYSTEM");
			user.setCreatedOn(startT);
			user.setModifiedBy("SYSTEM");
			user.setModifiedOn(startT);
			appUserRep.save(user);
		}
		
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new AppUserServiceException(e.getMessage(), e);
		}
		
	}

	@Override
	public void updateAppUserIpAddress(String appUserID, String ipAddress) throws AppUserServiceException {
		
		try {
			
			Date startT = new Date();
			// Step 1 : Verify is User is Present in System
		Optional<AppUser> optAppUser = 	appUserRep.findById(appUserID);
		
		if(optAppUser.isPresent())
		{
			AppUser modUser = optAppUser.get();
			modUser.setIpAddress(ipAddress);
			modUser.setModifiedOn(startT);
			appUserRep.save(modUser);
			
		}
		
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new AppUserServiceException(e.getMessage(), e);
		}

		
	}

}
