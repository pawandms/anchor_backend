package com.anchor.app.msg.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import com.anchor.app.enums.SequenceType;
import com.anchor.app.event.model.Event;
import com.anchor.app.event.model.EventLog;
import com.anchor.app.event.model.EventLogReq;
import com.anchor.app.event.service.EventLogService;
import com.anchor.app.event.service.EventNotificationService;
import com.anchor.app.event.service.FireBaseService;
import com.anchor.app.exception.UserServiceException;
import com.anchor.app.geolation.resolver.entity.GeoLocation;
import com.anchor.app.geolation.service.GeoLocationManager;
import com.anchor.app.model.response.ApiError;
import com.anchor.app.msg.enums.NatsSubjectType;
import com.anchor.app.msg.enums.UserActionStatusType;
import com.anchor.app.msg.enums.UserActionType;
import com.anchor.app.msg.enums.VisibilityType;
import com.anchor.app.msg.service.AppUserService;
import com.anchor.app.msg.service.NatsService;
import com.anchor.app.msg.vo.SearchResp;
import com.anchor.app.msg.vo.UserActionVo;
import com.anchor.app.msg.vo.UserNotificationResp;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.service.IAuthenticationFacade;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;
import com.anchor.app.util.WebUtil;
import com.github.f4b6a3.uuid.UuidCreator;

@RestController
@RequestMapping(value= "/api/user")
public class AppUserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp  env;

	@Autowired
	private IAuthenticationFacade authfacade;

	@Autowired
	private AppUserService appUserService;
	
	@Autowired
	private NatsService natsService;
	
	@Autowired
	private EventLogService eventLogService;

	@Autowired
	private EventNotificationService eventNotificationService;
	
	@Autowired
	private WebUtil webUtil;
	
	@Autowired
	private HelperBean helper;
	
	@Autowired
	private GeoLocationManager geoLocationManager;
	
	@Autowired
	private FireBaseService fireBaseService;
	
	@PostMapping(value = "/{userID}/profile/type/{profileType}" )
    public ResponseEntity<?> ChangeUserProfileType(
    		@PathVariable ("userID") String userID,
    		@PathVariable ("profileType") VisibilityType profileType) 
	{
		UserActionVo request = new UserActionVo();
    	ResponseEntity<?> response = null;
		String reqUseID = null;
        try {
        	
        	request.setActionType(UserActionType.Change_ProfileType);
        	
        	if(( null == userID) || (null == profileType))
        	{
        		throw new UserServiceException("Invalid request");	
        	}
        	
        	request.setSrcUserID(userID);
        	request.setProfileType(profileType);
        	if(env.isAuthEnabled())
        	{
        		User user = authfacade.getApiAuthenticationDetails();
            	if( null == user)
            	{
            		throw new UserServiceException("Invalid authenticated user");
            	}
            	reqUseID = user.getUid();
            	
            	request.setReqUserID(reqUseID);
            	request.setSrcUserID(reqUseID);
        	}
        	else {
        		request.setReqUserID(userID);
        	}
        	
        	appUserService.performUserAction(request);
        	response =  new ResponseEntity<>(request, HttpStatus.OK);
        	
        }
        catch (Exception e)
        {
        	if(!request.getErrors().isEmpty())
        	{
        		
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				request.getErrorMessage() , request.getErrors());
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        	else {
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				"Unable to perform user Action with Error:"+e.getMessage() );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
        	
        	
        	
        }
        
        return response;	
        
    }
	
	@PostMapping(value = "/{srcUserID}/add/friend/{trgUserID}" )
    public ResponseEntity<?> addFriendRequest(
    		@PathVariable ("srcUserID") String userID,
    		@PathVariable ("trgUserID") String addFriendID) 
	{
		UserActionVo request = new UserActionVo();
		ResponseEntity<?> response = null;
		String reqUseID = null;
        try {
        	
        	request.setActionType(UserActionType.Add_Friend_Request);
        	
        	if(( null == userID) || (null == addFriendID))
        	{
        		throw new UserServiceException("Invalid request");	
        	}
        	
        	request.setSrcUserID(userID);
        	request.setTrgUserID(addFriendID);
        	
        	if(env.isAuthEnabled())
        	{
        		User user = authfacade.getApiAuthenticationDetails();
            	if( null == user)
            	{
            		throw new UserServiceException("Invalid authenticated user");
            	}
            	reqUseID = user.getUid();
            	
            	request.setReqUserID(reqUseID);
            	request.setSrcUserID(reqUseID);
        	}
        	else {
        		request.setReqUserID(userID);
        	}
        	
        	appUserService.performUserAction(request);
        	response =  new ResponseEntity<>(request, HttpStatus.OK);
        	
        }
        catch (Exception e)
        {
         	if(!request.getErrors().isEmpty())
        	{
        		
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				request.getErrorMessage() , request.getErrors());
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        	else {
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				"Unable to perform user Action with Error:"+e.getMessage() );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
        	
        }
        
        return response;	
        
    }
	
	@PostMapping(value = "/{srcUserID}/remove/friend/{trgUserID}" )
    public ResponseEntity<?> removeFriendRequest(
    		@PathVariable ("srcUserID") String userID,
    		@PathVariable ("trgUserID") String addFriendID) 
	{
		UserActionVo request = new UserActionVo();
		ResponseEntity<?> response = null;
		String reqUseID = null;
        try {
        	
        	request.setActionType(UserActionType.Remove_Friend_Request);
        	
        	if(( null == userID) || (null == addFriendID))
        	{
        		throw new UserServiceException("Invalid request");	
        	}
        	
        	request.setSrcUserID(userID);
        	request.setTrgUserID(addFriendID);
        	
        	if(env.isAuthEnabled())
        	{
        		User user = authfacade.getApiAuthenticationDetails();
            	if( null == user)
            	{
            		throw new UserServiceException("Invalid authenticated user");
            	}
            	reqUseID = user.getUid();
            	
            	request.setReqUserID(reqUseID);
            	request.setSrcUserID(reqUseID);
        	}
        	else {
        		request.setReqUserID(userID);
        	}
        	
        	appUserService.performUserAction(request);
        	response =  new ResponseEntity<>(request, HttpStatus.OK);
        	
        }
        catch (Exception e)
        {
         	if(!request.getErrors().isEmpty())
        	{
        		
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				request.getErrorMessage() , request.getErrors());
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        	else {
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				"Unable to perform user Action with Error:"+e.getMessage() );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
       
        	
        	
        }
        
        return response;	
        
    }
	
	@PostMapping(value = "/{srcUserID}/action/{actionID}/status/{actionStatus}" )
    public ResponseEntity<?> processEventAction(
    		@PathVariable ("srcUserID") String userID,
    		@PathVariable ("actionID") String actionID,
    		@PathVariable ("actionStatus") UserActionStatusType actStatus
    		) 
	{
		UserActionVo request = new UserActionVo();
		ResponseEntity<?> response = null;
		String reqUseID = null;
        try {
        	
        	request.setActionType(UserActionType.Event_Action_Response);
        	
        	if(( null == userID) || (null == actionID) || (null == actStatus))
        	{
        		throw new UserServiceException("Invalid request");	
        	}
        	
        	request.setSrcUserID(userID);
        	request.setActionId(actionID);
        	request.setActionStatus(actStatus);
        	
        	if(env.isAuthEnabled())
        	{
        		User user = authfacade.getApiAuthenticationDetails();
            	if( null == user)
            	{
            		throw new UserServiceException("Invalid authenticated user");
            	}
            	reqUseID = user.getUid();
            	
            	request.setReqUserID(reqUseID);
            	request.setSrcUserID(reqUseID);
        	}
        	else {
        		request.setReqUserID(userID);
        	}
        	
        	appUserService.performUserAction(request);
        	response =  new ResponseEntity<>(request, HttpStatus.OK);
        	
        }
        catch (Exception e)
        {
         	if(!request.getErrors().isEmpty())
        	{
        		
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				request.getErrorMessage() , request.getErrors());
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        	else {
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				"Unable to perform user Action with Error:"+e.getMessage() );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
       
        	
        	
        }
        
        return response;	
        
    }
	

    @GetMapping(value = "/request-reply/{subject}/{message}")
    public String requestReply(
    		@PathVariable String subject,
    		@PathVariable String message)  
    {
    	String result = null;

    	try {
    		 logger.info("Publishing Request-Reply message:" + message);
 	        
    		 NatsSubjectType.getList().forEach(nc -> {
    			
    			 logger.info("Event Type:"+nc.eventType.name());
    			 logger.info("Event Subject:"+nc.subject);
    		 });
    		 result = message;

    	}
    	catch(Exception e)
    	{
    		 logger.error(e.getMessage(), e);
    	}
       	        
        return result;
        
        
    }


	@PostMapping(value = "/event" )
    public ResponseEntity<?> testNatsEventAction(
    		@RequestBody Event event
    		) 
	{
		ResponseEntity<?> response = null;
		try {
        	
			natsService.sendEvent(event);
			
        	response =  new ResponseEntity<>(event, HttpStatus.OK);
        	
        }
        catch (Exception e)
        {
       		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				"Unable to perform user Action with Error:"+e.getMessage() );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
       
        	
        	
        }
        
        return response;	
        
    }
	
    @GetMapping(value = "/{userID}/search/{searchKey}")
    public ResponseEntity<?> searchUsersAndMsgChannels(
    		@PathVariable ("userID") String userID,
    		@PathVariable("searchKey") String searchKey,
    		@RequestParam(value = "page", required = false, defaultValue = "0" ) int page, 
    		@RequestParam(value = "size", required = false, defaultValue = "10") int size )
	
    {
    	//UserActionVo request = new UserActionVo();
    	ResponseEntity<?> response = null;
    	String reqUseID = null;
		
    	try {
    		
    		//request.setActionType(UserActionType.Search_Users);
        	
        	if(( null == userID) || (null == searchKey))
        	{
        		throw new UserServiceException("Invalid request");	
        	}
        	
        	if(env.isAuthEnabled())
        	{
        		User user = authfacade.getApiAuthenticationDetails();
            	if( null == user)
            	{
            		throw new UserServiceException("Invalid authenticated user");
            	}
            	reqUseID = user.getUid();
            	
        	}
        	else {
        		//request.setReqUserID(userID);
        		reqUseID = userID;
        
        	}
        	
        	//Page<SearchUserVo> searchResponse = appUserService.searchUsersAndMsgChnls(reqUseID, searchKey, page, size );
        	//response =  new ResponseEntity<>(searchResponse, HttpStatus.OK);
        	SearchResp resp = appUserService.getSearchResponSearchByUsersAndMsgChnls(reqUseID, searchKey, page, size);
        	response =  new ResponseEntity<>(resp, HttpStatus.OK);
      		
    	}
    	catch(Exception e)
    	{
    		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
    				"Unable to perform user Action with Error:"+e.getMessage() );
    		
    		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    		 
    	}
       	        
        return response;
        
        
    }
    
    
	@PostMapping(value = "/eventlog" )
    public ResponseEntity<?> testEventLgAction(
    		@RequestBody EventLog event
    		) 
	{
		ResponseEntity<?> response = null;
		try {
			
			EventLogReq req = new EventLogReq();
			req.setActionType(UserActionType.Create_Msg_Chnl);
			req.setReqUserID("Test");
			req.getLogs().add(event);
        	
			eventLogService.sendEventLogToQueue(req);
			
        	response =  new ResponseEntity<>(event, HttpStatus.OK);
        	
        }
        catch (Exception e)
        {
       		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				"Unable to perform user Action with Error:"+e.getMessage() );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
       
        	
        	
        }
        
        return response;	
        
    }
	
	@GetMapping(value = "/details" )
    public ResponseEntity<?> getAppUserDetails(
    		@RequestParam(name="search") String search) 
	{
		ResponseEntity<?> response = null;
		try {
			
			//String uuidSequence = helper.getSequanceNo(SequenceType.UUID);

    		User user = authfacade.getApiAuthenticationDetails();

			
			String uuidSequence = "502_2024";
			
			UUID uuid = UuidCreator.getNameBasedMd5(uuidSequence);
		    GeoLocation location = geoLocationManager.getGeoLocation(search);	
			location.setGeoLocationId(uuid);
			String clientIp = webUtil.getClientIp();
			String result = "Request Client IP is :"+clientIp;
			response =  new ResponseEntity<>(location, HttpStatus.OK);
        	
        }
        catch (Exception e)
        {
       		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				"Unable to perform user Action with Error:"+e.getMessage() );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        }
        
        return response;	
        
    }
	
	
	
	 @GetMapping(value = "/{userID}/notification")
	    public ResponseEntity<?> getUserNotification(
	    		@PathVariable ("userID") String userID,
	    		@RequestParam(value = "page", required = false, defaultValue = "0" ) int page, 
	    		@RequestParam(value = "size", required = false, defaultValue = "10") int size )
		
	    {
	    	UserActionVo request = new UserActionVo();
	    	ResponseEntity<?> response = null;
	    	
	    	try {
	    		
	    		request.setActionType(UserActionType.Get_User_Notification);
	        	
	        	if(( null == userID))
	        	{
	        		throw new UserServiceException("Invalid request");	
	        	}
	        	request.setSrcUserID(userID);
	        	
	        	if(env.isAuthEnabled())
	        	{
	        		User user = authfacade.getApiAuthenticationDetails();
	            	if( null == user)
	            	{
	            		throw new UserServiceException("Invalid authenticated user");
	            	}
	            	request.setReqUserID(user.getUid());
	            	
	            	
	        	}
	        	else {
	        		request.setReqUserID(userID);
	        	}
	        	
	        	UserNotificationResp resp = eventNotificationService.getUserNotification(request.getReqUserID(), request.getSrcUserID(),  page, size);
	        	response =  new ResponseEntity<>(resp, HttpStatus.OK);
	      		
	    	}
	    	catch(Exception e)
	    	{
	    		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
	    				"Unable to perform user Action with Error:"+e.getMessage() );
	    		
	    		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	    		 
	    	}
	       	        
	        return response;
	        
	        
	    }
	 
	 
	 
		@PostMapping(value = "/fcmMsg/{fcmToken}/{fcmMsg}" )
	    public ResponseEntity<?> testFireBaseFcmMsgSening(
	    		@PathVariable ("fcmToken") String fcmToken,
	    		@PathVariable ("fcmMsg") String fcmMsg) 
		{
			ResponseEntity<?> response = null;
			try {
				
				String result = fireBaseService.sendFcnNotification(fcmToken, fcmMsg);
				 
	        	response =  new ResponseEntity<>(result, HttpStatus.OK);
	        	
	        }
	        catch (Exception e)
	        {
	       		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
	        				"Unable to perform user Action with Error:"+e.getMessage() );
	        		
	        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
	       
	        	
	        	
	        }
	        
	        return response;	
	        
	    }
		

}
