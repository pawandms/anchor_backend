package com.anchor.app.oauth.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.cache.service.CacheService;
import com.anchor.app.enums.GenderType;
import com.anchor.app.enums.SequenceType;
import com.anchor.app.enums.ValidationErrorType;
import com.anchor.app.event.service.EventLogService;
import com.anchor.app.exception.UserServiceException;
import com.anchor.app.exception.ValidationException;
import com.anchor.app.msg.enums.VisibilityType;
import com.anchor.app.msg.model.Attachment;
import com.anchor.app.msg.service.ContentService;
import com.anchor.app.msg.vo.SearchUserVo;
import com.anchor.app.oauth.dao.UserAuthDao;
import com.anchor.app.oauth.dao.UserDao;
import com.anchor.app.oauth.enums.AuthorityType;
import com.anchor.app.oauth.enums.GrantType;
import com.anchor.app.oauth.enums.UserIdentifyType;
import com.anchor.app.oauth.enums.UserStatusType;
import com.anchor.app.oauth.model.OauthClientDetails;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.model.UserAuth;
import com.anchor.app.oauth.model.UserVerifyToken;
import com.anchor.app.oauth.repository.OauthClientDetailsRepository;
import com.anchor.app.oauth.repository.UserVerifyTokenRepository;
import com.anchor.app.oauth.service.AuthService;
import com.anchor.app.oauth.service.IAuthenticationFacade;
import com.anchor.app.oauth.service.UserService;
import com.anchor.app.oauth.viewmodel.ClientDetailsVo;
import com.anchor.app.oauth.vo.UserVo;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;
import com.anchor.app.vo.ErrorMsg;

@Service
public class UserServiceImpl implements UserService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//@Autowired
//	private UserRepository userRepository;
	
	@Autowired
	private UserDao userDao;
	
//	@Autowired
//	private UserAuthRepository userAuthRepository;

	@Autowired
	private UserAuthDao userAuthDao;
	
	@Autowired
	private UserVerifyTokenRepository userVerifyTokenRep;
	
	@Autowired
    private OauthClientDetailsRepository oauthClientDetailsRepository;

	
	@Autowired
	private HelperBean helper;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private EnvProp envProp;

	@Autowired
	private IAuthenticationFacade authfacade;

	@Autowired
	private AuthService authServce;
	
	@Autowired
	private ContentService contentService;


	@Autowired
	private EventLogService eventLogService;

	
	
	@Override
	public UserVo createUser(UserVo request) throws UserServiceException {
		
		
		try {
		
			validateUserRequest(request);
			// Create User in DB
			
			if(!request.isValid())
			{
				throw new UserServiceException("Invalid User");
				
			}
			
			persistUser(request);
			
			// Push Message to Email Qeu for Sending Email for Verification
			
			if( null != request.getVerificationToken())
			{
				cacheService.sendSingupVerificationEmailMsg(request.getVerificationToken());
			}
			
			eventLogService.createCreateUserEvent(request.getId(), request.getId(), request.getFirstName(), request.getLastName(), request.getProfileType());
			
			
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
		return request;
	}
	
	
	@Transactional
	public void persistUser(UserVo request) throws UserServiceException
	{
		Date crDate = new Date();
		String crBy = "Admin";
		try {
			User user = new User();
			user.setUid(helper.getSequanceNo(SequenceType.USER).toString());
			user.setId(user.getUid());
			user.setUserName(request.getEmail());
			user.setAdmin(false);
			user.setBirthday(null);
			user.setEmail(request.getEmail());
			user.setEmailBindTime(crDate.getTime());
			user.setFace(null);
			user.setFace200(null);
			user.setFirstName(request.getFirstName());
			if( null != request.getGender())
			{
				user.setGender(request.getGender().name());	
			}
			else {
				user.setGender(GenderType.Not_Specify.name());
			}
			
			user.setLastName(request.getLastName());
			user.setProfileType(request.getProfileType());
			user.setMobile(null);
			user.setMobileBindTime(null);
			user.setNickName(null);
			user.setSignature(null);
			user.setSrcface(null);
			user.setStatus(UserStatusType.Unverfieid.getValue());
			user.setCreatedBy(crBy);
			user.setCreatedDate(crDate);
			user.setModifiedBy(crBy);
			user.setModifiedDate(crDate);
			
			
			UserAuth uauth = new UserAuth();
			uauth.setId(user.getId());
			uauth.setUid(user.getUid());
			
			uauth.setIdentifier(request.getEmail());
			uauth.setCertificate(helper.encriptPassword(request.getPassword()));
			uauth.setIdentityType(UserIdentifyType.EMAIL.getValue());
			uauth.getRoles().add(request.getRole().name());
			uauth.setAccountNonExpired(true);
			uauth.setCredentialsNonExpired(true);
			uauth.setEnabled(true);
			uauth.setCreatedBy(crBy);
			uauth.setCreatedDate(crDate);
			uauth.setModifiedBy(crBy);
			uauth.setModifiedDate(crDate);
			
			
			// Generate Email Verification Token to verify from Email
			
			UserVerifyToken uvt = new UserVerifyToken();
			uvt.setId(helper.getSequanceNo(SequenceType.UserVerifyToken).toString());
			uvt.setUserName(request.getEmail());
			String token = helper.getUUIDFromString(request.getEmail()).toString();
			uvt.setVerifyToken(token);
			uvt.setFirstName(user.getFirstName());
			uvt.setLastName(user.getLastName());
			uvt.setEmailAddress(request.getEmail());
			uvt.setValid(true);
			uvt.setCreatedBy(crBy);
			uvt.setCreatedDate(crDate);
			uvt.setModifiedBy(crBy);
			uvt.setModifiedDate(crDate);
			userDao.saveUser(user);
			userAuthDao.saveUserAuth(uauth);
			userVerifyTokenRep.insert(uvt);	
			
			request.setId(user.getUid());
			request.setVerificationToken(uvt);
			
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}

	}
	

	
	private void validateUserRequest(UserVo request) throws ValidationException
	{
		userRequestStructuralValidation(request);
		userRequestDbValidation(request);
		
	}
	
	private void userRequestStructuralValidation(UserVo request) throws ValidationException
	{
		
		if(helper.isEmptyString(request.getEmail()))
		{
			request.setValid(false);
			request.setErrorMessage(ValidationErrorType.Invalid_Email.getValue());
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Email.name(), ValidationErrorType.Invalid_Email.getValue()));
			
		}
		
		// Validate Email Address
		
		boolean isValidEmail = helper.validateEmailAddress(request.getEmail());
		
		if(!isValidEmail)
		{
			request.setValid(false);
			request.setErrorMessage(ValidationErrorType.Invalid_Email.getValue());
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Email.name(), ValidationErrorType.Invalid_Email.getValue()));
		}
		
		
		if(helper.isEmptyString(request.getPassword()))
		{
			request.setValid(false);
			request.setErrorMessage(ValidationErrorType.Invalid_Password.getValue());
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Password.name(), ValidationErrorType.Invalid_Password.getValue()));
			
		}
		
		
		if(helper.isEmptyString(request.getFirstName()))
		{
			request.setValid(false);
			request.setErrorMessage(ValidationErrorType.Invalid_FName.getValue());
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_FName.name(), ValidationErrorType.Invalid_FName.getValue()));
			
		}
		if(helper.isEmptyString(request.getLastName()))
		{
			request.setValid(false);
			request.setErrorMessage(ValidationErrorType.Invalid_LName.getValue());
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_LName.name(), ValidationErrorType.Invalid_LName.getValue()));
			
		}
		if( null == request.getProfileType())
		{
			request.setProfileType(VisibilityType.Public);
		}
		
		if(!request.isValid())
		{
			throw new ValidationException("Invalid Request");
		}
		
		
	}
	
	private void userRequestDbValidation(UserVo request) throws ValidationException
	{
		try {
	
		boolean isEmailPresent = isEmailPresent(request.getEmail()); 
		
		if(isEmailPresent)
		{
			request.setValid(false);
			request.setErrorMessage(ValidationErrorType.Email_Already_Present.getValue());
			request.getErrors().add(new ErrorMsg(ValidationErrorType.Email_Already_Present.name(), ValidationErrorType.Email_Already_Present.getValue()));
			
		}

		}
		catch(Exception e)
		{
			throw new ValidationException(e.getMessage(), e); 
		}
				
		
	}


	@Override
	public boolean isEmailPresent(String email) throws UserServiceException {
		boolean result = true;
		
		try {
			result = userDao.isEmailPresent(email);
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
		return result;
	}
	

	@Override
    public OauthClientDetails saveClientDetails(ClientDetailsVo request) throws UserServiceException
    {
    	OauthClientDetails result = null;
    	
    	try {
    	
    		// Create ClientDetails object from Request;
        	
        	result = new OauthClientDetails();
        	result.setId(helper.getSequanceNo(SequenceType.CLIENT));
        	result.setClientId(request.getClientID());
        	result.setClientName(request.getClientName());
        	result.setResourceIds("resource-server-rest-api");
        	result.setClientSecret(helper.encriptPassword(request.getClientPassword()));
        	
        	/*
        	List<String> authorities = request.getAuthorities().stream()
        			   .map(AuthorityType:: name)
        			   .collect(Collectors.toList());
        	
        	result.getAuthorities().addAll(authorities);
        	
        	
        	List<String> grants = request.getGrants().stream()
     			   .map(GrantType:: name)
     			   .collect(Collectors.toList());
     	
        	result.getAuthorizedGrantTypes().addAll(grants);
        	
        	List<String> scops = request.getScope().stream()
      			   .map(AuthorityType:: name)
      			   .collect(Collectors.toList());
        	
        	result.getScope().addAll(scops);
        	result.setAccessTokenValidity(request.getAccessTokenValiditySeconds());
        	result.setRefreshTokenValidity(request.getRefreshTokenValiditySeconds());
        	result.getAdditionalInformation().putAll(request.getAdditionalInformation());
        	result.setAutoapprove(request.isAutoApprove());
        	result.setCreated(new Date());
        	result.setEnabled(true);

        	*/
        	String delim = ",";
            
        /*
        	
        	List<String> authorities = request.getAuthorities().stream()
     			   .map(AuthorityType:: name)
     			   .collect(Collectors.toList());
     	
        String authoritiesStr = String.join(delim, authorities);
        result.setAuthorities(authoritiesStr);
     	
     	*/
     	
     	List<String> grants = request.getGrants().stream()
  			   .map(GrantType:: getValue)
  			   .collect(Collectors.toList());
  	
     	String grantsStr = String.join(delim, grants);
        result.setAuthorizedGrantTypes(grantsStr);
     	
     	List<String> scops = request.getScope().stream()
   			   .map(AuthorityType:: name)
   			   .collect(Collectors.toList());
     
    	 
        
     	String scopsStr = String.join(delim, scops);
        result.setScope(scopsStr);
     	
     	result.setAccessTokenValiditySeconds(request.getAccessTokenValiditySeconds());
     	result.setRefreshTokenValiditySeconds(request.getRefreshTokenValiditySeconds());
     	result.setAutoapprove(request.getAutoApprove());
     	result.setCreated(new Date());
     	result.setEnabled(true);
     	result.setRegisteredRedirectUris(request.getRegisteredRedirectUri());	
     	
        	oauthClientDetailsRepository.save(result);

    	}
    	catch(Exception e)
    	{
    		throw new UserServiceException(e.getMessage(), e);
    	}
    	
    	    	
    	return result;
    }


	@Override
	public User getUserDetails(String userName) throws UserServiceException {
			
		User user = null;
		try {
			UserAuth userAuth = getUserAuthDetails(userName);
			
			if(null != userAuth)
			{
				String uid = userAuth.getUid();
				user = userDao.getUserByUserID(uid);
				
				
			}
		
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
		return user;
	}


	@Override
	public UserAuth getUserAuthDetails(String userName) throws UserServiceException {
		
		UserAuth uauth = null;
		
		try {
			
			if( null != userName)
			{
				uauth = userAuthDao.getUserAuthByUserName(userName);
				
			}
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
		return uauth;
	}


	@Override
	public UserVo  activateUser(String verifyToken, String id) throws UserServiceException {

		UserVo result = null;
		
		try {
			Date modT = new Date();
			result = new UserVo();
			result.setValid(true);
			// Step 1 get Activation Token Details
			
			List<UserVerifyToken> tokenList = userVerifyTokenRep.findByIdAndVerifyToken(id, verifyToken);
			
			if(tokenList.isEmpty())
			{
				result.setValid(false);
				result.setErrorMessage(ValidationErrorType.Invalid_Verificaiton_Token.getValue());
				result.getErrors().add(new ErrorMsg(ValidationErrorType.Invalid_Verificaiton_Token.name(), ValidationErrorType.Invalid_Verificaiton_Token.getValue()));
				
			}
			else {
				if(tokenList.size()> 1)
				{
					
					logger.error("Duplicate Email Activation Record Found For ID:"+id);	
				}
				
				UserVerifyToken token = tokenList.get(0);
				
				// Get User Object From DB
				User user = getUserDetails(token.getUserName());
				
				// Populate UserDetails to UserVo
				 helper.populateUserDetailsToUserVo(user, result);
				
				if(token.isValid())
				{
					
					// Step 2 : Activate User and Invalidate Actiivation Token
					activateUserAccount(token.getId(), token.getUserName(), modT);
				}
				
			}
			
			
			
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
		return result;

	}
	
	/**
	 * Activate UserAccount 
	 * @param uid
	 * @param userName
	 * @throws UserServiceException 
	 */
	
	@Transactional
	private void activateUserAccount(String uid, String userName, Date modT) throws UserServiceException
	{
		try {
			
			//Step 1: Mark userVerifyToken as Validated
			markuserVerifyTokenAsValidated(uid, userName, modT);
			
			// Step 2: Mark UserAuth as Enabled
			enabledUserAuthForUser(uid, userName, modT);
			
			// Step 3 : Mark User status as 2 i.e NormalUser
			userDao.activateUserId(uid, userName, modT);
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		

	}


	private void markuserVerifyTokenAsValidated(String uid, String userName, Date modT) throws UserServiceException {
		
		try {
			
			if( (null == uid) || (null == userName))
			{
				throw new UserServiceException("Invalid uid or userName");
			}
			
			Query query = new Query();
			
			Criteria idCrt = Criteria.where("_id").is(uid);
			Criteria userNameCrt = Criteria.where("userName").is(userName);
		
			query.addCriteria(new Criteria().andOperator(idCrt, userNameCrt));
			
			Update update = new Update().set("valid", false)
					.set("modifiedBy", userName)
					.set("modifiedOn", modT);
			
			
			mongoOperations.updateFirst(query, update, UserVerifyToken.class);
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage(), e);
		}
		

		
	}


	private void enabledUserAuthForUser(String uid, String userName, Date modT) throws UserServiceException {
		
		try {

			
			if( (null == uid) || (null == userName))
			{
				throw new UserServiceException("Invalid uid or userName");
			}
			
			Query query = new Query();
			
			Criteria idCrt = Criteria.where("uid").is(uid);
			Criteria userNameCrt = Criteria.where("identifier").is(userName);
		
			query.addCriteria(new Criteria().andOperator(idCrt, userNameCrt));
			
			Update update = new Update().set("enabled", true)
					.set("modifiedBy", userName)
					.set("modifiedOn", modT);
			
			
			mongoOperations.updateFirst(query, update, UserAuth.class);
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage(), e);
		}
		

		
	}

	
	private void activateUserId(String uid, String userName, Date modT) throws UserServiceException {
		
		try {

			
			if( (null == uid) || (null == userName))
			{
				throw new UserServiceException("Invalid uid or userName");
			}
			
			Query query = new Query();
			
			Criteria idCrt = Criteria.where("uid").is(uid);
			Criteria userNameCrt = Criteria.where("userName").is(userName);
		
			query.addCriteria(new Criteria().andOperator(idCrt, userNameCrt));
			
			Update update = new Update().set("status", UserStatusType.Normal.getValue())
					.set("modifiedBy", userName)
					.set("modifiedOn", modT);
			
			
			mongoOperations.updateFirst(query, update, User.class);
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage(), e);
		}
		

		
	}


	@Override
	public List<User> getUserForUserIds(Collection userIds) throws UserServiceException {
		List<User> users = null;
		try {
			
			users = userDao.getUserForUserIds(userIds);
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
		return users;
	}


	@Override
	public User getUserForUserID(String userID) throws UserServiceException {
		
		User user = null;
		try {
			user = userDao.getUserByUserID(userID);
			
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
	
		return user;
		
	}


	@Override
	public void addUpdateUserProfileImage(String reqUserID, String userID, MultipartFile profileImage) throws UserServiceException {
		
		try {
			
		Date modDate = new Date();
		 
		 User profileUser = getUserForUserID(userID);
		 
		 Attachment profileAttachment = contentService.saveUserProfile(userID, profileUser.getSrcface(), modDate, profileImage);
		 
		 profileUser.setSrcface(profileAttachment.getContentID());
		 
		 // Update User Into DB
		 userDao.updateUserProfileContentId(userID, reqUserID, profileUser.getSrcface(), modDate);
		 

		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
	}


	@Override
	public void updateUserProfileType(String reqUserID, String userID, VisibilityType profileType, Date modDate)
			throws UserServiceException {
	
		try {
			userDao.updateUserProfileType(reqUserID, userID, profileType, modDate);
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
	
	}


	@Override
	public void updateUserLoginStatus(String userID) throws UserServiceException {
		
		try {
			Date now = new Date();
			userDao.updateUserLoginStatus(userID, now);
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
	}


	@Override
	public Page<SearchUserVo> getUserBySearchString(String searchKey, Pageable pageable) throws UserServiceException {
		Page<SearchUserVo> result = null;
		
		try {
			
			result = userDao.getUserBySearchString(searchKey, pageable);
		}
		catch(Exception e)
		{
			throw new UserServiceException(e.getMessage(), e);
		}
		
		return result;
	}


	

	
}
