package com.anchor.app.oauth.service.impl;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.anchor.app.exception.AuthServiceException;
import com.anchor.app.msg.enums.UserRoleType;
import com.anchor.app.oauth.service.AuthService;
import com.anchor.app.oauth.vo.AuthReq;

@Service
public class AuthServiceImpl implements AuthService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Override
	public boolean hasPersmission(AuthReq authReq) throws AuthServiceException {
		boolean result = false;
		try {
			if(( null == authReq.getUserID())
			  //|| (null == authReq.getChnlID())
			  || (null == authReq.getReqPermission())
			 // || (null == authReq.getReqUserRoles())
			  //|| (authReq.getReqUserRoles().isEmpty())
				)
			{
				throw new AuthServiceException("Invalid AuthRequest");
			}
			
			// Perform Authorization
			result = performAuthorization(authReq);
			
			
		}catch(Exception e)
		{
			throw new AuthServiceException(e.getMessage(), e);
		}
		
		return result;
	}

	private boolean performAuthorization(AuthReq authReq)
	{
		boolean result = false;
		try {
			
			switch(authReq.getReqPermission())
			{
			case CnAdd:
					if((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Author))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Moderator))
						)
					{
						
						result = true;
					}
					
				break;
			
			case CnEdit:
				if((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Author))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Moderator))
						)
					{
						
						result = true;
					}
					
				break;
				
			case CnDelete:
				if((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Author))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Moderator))
						)
					{
						
						result = true;
					}
					
				break;
				
			case CnDeleteSelf:
				if(authReq.getReqUserID().equalsIgnoreCase(authReq.getUserID()))
				{
					
					result = true;
				}
				
				break;
				
			case CnView:
				if((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Author))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Moderator))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Viewer))
						)
					{
						
						result = true;
					}
					
				break;
			
			case UsrAdd:
				if((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Author))
						)
					{
						
						result = true;
					}
					
				break;
			
			case UsrEdit:
				if(authReq.getReqUserID().equalsIgnoreCase(authReq.getUserID()))
				{
					result = true;
				}
				else if((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Admin)))
				{
						
						result = true;
				}
					
				break;
			
			case UsrDelete:
				if((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))
						)
					{
						
						result = true;
					}
					
				break;
				
			case UsrDeleteSelf:
				if(authReq.getReqUserID().equalsIgnoreCase(authReq.getUserID()))
					{
						
						result = true;
					}
					
				break;
				
			case UsrView:
				if((authReq.getReqUserRoles().contains(UserRoleType.SuperAdmin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Admin))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Author))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Moderator))
						|| (authReq.getReqUserRoles().contains(UserRoleType.Viewer))
						)
					{
						
						result = true;
					}
					
				break;
				
				
			 default:
				 throw new AuthServiceException("Implementation not available for Permission:"+authReq.getReqPermission().name());
				 
			}
		}
		catch(Exception e)
		{
			logger.error("Invalid Auth Request for User:"+authReq.getReqUserID()+" , Req Permission:"+authReq.getReqPermission());
			
		}
		
		return result;
	}

	@Override
	public Map<String, Object> decodeToken(String token) throws AuthServiceException {

		/* 
		 try {
			    Jwt jwt = JwtHelper.decodeAndVerify(token, new MacSigner("123"));
			    String claimsStr = jwt.getClaims();
	            Map<String, Object> claims =  JsonParserFactory.create().parseMap(claimsStr);
	            
	            if(claims.containsKey("exp"))
	            {
	            	Integer expireAt = (Integer) claims.get("exp");
	            	long expireMills = expireAt.longValue()*1000l;
	            	
	            	Date expiryDate = new Date(expireMills);
	            	
	            	Date now = new Date();
	            	
	            	if(expiryDate.before(now))
	            	{
	            		claims.put("tokenExpire", true);
	            		
	            		logger.info("Token is Expire as Expire date is :"+expiryDate+" , and Current Date is:"+now);
	            	}
	            	else {
	            		claims.put("tokenExpire", false);
	            	}
	            	
	            }
	            
	            return  claims;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return  null;
	        }
		*/
		
		return null;
	}
}
