package com.anchor.app.msg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anchor.app.event.model.Event;
import com.anchor.app.event.service.EventService;
import com.anchor.app.model.response.ApiError;
import com.anchor.app.msg.enums.ChannelActionType;
import com.anchor.app.msg.enums.ChannelType;
import com.anchor.app.msg.exceptions.ChannelServiceException;
import com.anchor.app.msg.exceptions.MsgServiceException;
import com.anchor.app.msg.model.Channel;
import com.anchor.app.msg.service.ChannelService;
import com.anchor.app.msg.service.NatsService;
import com.anchor.app.msg.vo.ChannelParticipantResponse;
import com.anchor.app.msg.vo.ChannelResponse;
import com.anchor.app.msg.vo.ChnlUserActionVo;
import com.anchor.app.msg.vo.CreateChannelVo;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.service.IAuthenticationFacade;
import com.anchor.app.util.EnvProp;

@RestController
@RequestMapping(value= "/api/{version}/channel", version = "v1")
public class ChannelController {

	@Autowired
	private EnvProp  env;
	
	@Autowired
	private IAuthenticationFacade authfacade;
	
	@Autowired
	private ChannelService channelService;

	@Autowired
	private NatsService ns;
	
	@Autowired
	private EventService eventService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> createChannel( @RequestBody CreateChannelVo request) 
	{
		ResponseEntity<?> response = null;
	    try {
        		
        	if( null == request)
        	{
        		throw new ChannelServiceException("Request can not be null");
        	}
        	
        	User user = authfacade.getApiAuthenticationDetails();
        	if( null == user)
        	{
        		throw new ChannelServiceException("Invalid authenticated user");
        	}
        	request.setUserID(user.getUid());
        	
        	// Create channel along with participants details
        	Channel result = channelService.createChannel(request);
        	
        	response =  new ResponseEntity<>(result, HttpStatus.OK);
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
        				"Unable to create channel" );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
        	
        	
        }
        
        return response;	
        
    }
	
	@RequestMapping(value = "user/{userId}/{chnlType}/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserChannels( 
    		@PathVariable(required = false,name = "userId") String userID ,
    		@PathVariable ("chnlType") ChannelType chnlType)
    		
	{
		ResponseEntity<?> response = null;
	    
		try {
        	
	       if(env.isAuthEnabled())
        	{
        		User user = authfacade.getApiAuthenticationDetails();
            	if( null == user)
            	{
            		throw new ChannelServiceException("Invalid authenticated user");
            	}
            	
            	userID = user.getUid();
        	}
        	else {
        		if( null == userID)
        		{
        			throw new MsgServiceException("Invalid request UserID");
        		}
        	}
        	
			
        	ChannelResponse chnlResp = channelService.getUserChannels(userID, chnlType);
        	
        	response =  new ResponseEntity<>(chnlResp, HttpStatus.OK);
        }
        catch (Exception e)
        {
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				"Unable to fetch channel list" );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	
        	
        	
        }
        
        return response;	
        
    }
	
	@RequestMapping(value = "user/{userId}/{chnlID}/users", method = RequestMethod.GET)
    public ResponseEntity<?> getChannelParticipents( 
    		@PathVariable(required = false,name = "userId") String userID ,
    		@PathVariable ("chnlID") String chnlID)
    		
	{
		ResponseEntity<?> response = null;
		ChannelParticipantResponse resp = new ChannelParticipantResponse();
		try {
        	
	       if(env.isAuthEnabled())
        	{
        		User user = authfacade.getApiAuthenticationDetails();
            	if( null == user)
            	{
            		throw new ChannelServiceException("Invalid authenticated user");
            	}
            	
            	userID = user.getUid();
        	}
        	else {
        		if( null == userID)
        		{
        			throw new MsgServiceException("Invalid request UserID");
        		}
        	}
        	
	       resp.setUserID(userID);
	       resp.setChnlId(chnlID);
			
        	channelService.getChannelParticipantForChannel(resp);
        	
        	response =  new ResponseEntity<>(resp, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	if(!resp.getErrors().isEmpty())
        	{
        		
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				resp.getErrorMessage() , resp.getErrors());
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        	else {
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				"Unable to fetch channel list" );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	
        	}
        	
        }
        
        return response;	
        
    }
	
	
	
	/**
	 * Add User to Channel
	 * @param chnlID
	 * @param userID
	 * @return
	 */
	@RequestMapping(value = "/{chnlID}/user/{userID}/add", method = RequestMethod.POST)
    public ResponseEntity<?> addUserToChannel( @PathVariable ("chnlID") String chnlID,
    											@PathVariable ("userID") String userID) 
	{
		ChnlUserActionVo request = null;
		ResponseEntity<?> response = null;
	    try {
        	
        	User user = authfacade.getApiAuthenticationDetails();
        	if( null == user)
        	{
        		throw new ChannelServiceException("Invalid authenticated user");
        	}
        	
        	request = new ChnlUserActionVo();
        	request.setReqUserID(user.getUid());
        	request.setUserID(userID);
        	request.setChannelID(chnlID);
        	request.setActionType(ChannelActionType.AddUser);
        	// Create channel along with participants details
        	channelService.addUserToChannel(request);
        	
        	response =  new ResponseEntity<>(request.getResponse(), HttpStatus.OK);
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
        				"Unable to create channel" );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
        	
        	
        }
        
        return response;	
        
    }
	

	/**
	 * Add User to Channel
	 * @param chnlID
	 * @param userID
	 * @return
	 */
	@RequestMapping(value = "/{chnlID}/user/{userID}/remove", method = RequestMethod.POST)
    public ResponseEntity<?> removeUserFromChannel( @PathVariable ("chnlID") String chnlID,
    											@PathVariable ("userID") String userID) 
	{
		ChnlUserActionVo request = null;
		ResponseEntity<?> response = null;
	    try {
        	
        	User user = authfacade.getApiAuthenticationDetails();
        	if( null == user)
        	{
        		throw new ChannelServiceException("Invalid authenticated user");
        	}
        	
        	request = new ChnlUserActionVo();
        	request.setReqUserID(user.getUid());
        	request.setUserID(userID);
        	request.setChannelID(chnlID);
        	request.setActionType(ChannelActionType.RemoveUser);
        	// Create channel along with participants details
        	channelService.removeUserFromChannel(request);
        	
        	response =  new ResponseEntity<>(request.getResponse(), HttpStatus.OK);
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
        				"Unable to create channel" );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
        	
        	
        }
        
        return response;	
        
    }
	


	@RequestMapping(value = "/{chnlID}/user/{userID}/nats/msg", method = RequestMethod.POST)
    public ResponseEntity<?> sendNatsMessageToChannel( @PathVariable ("chnlID") String chnlID,
    											@PathVariable ("userID") String userID,
    											@RequestBody Event request) 
	{
		ResponseEntity<?> response = null;
	    try {
        	
        	User user = authfacade.getApiAuthenticationDetails();
        	if( null == user)
        	{
        		throw new ChannelServiceException("Invalid authenticated user");
        	}
        	
        	eventService.addEvent(request);
        	
        	response =  new ResponseEntity<>(request, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				"Unable to create channel" );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	
        }
        
        return response;	
        
    }
	


}
