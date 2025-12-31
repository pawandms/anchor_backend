package com.anchor.app.msg.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.exception.AuthServiceException;
import com.anchor.app.model.response.ApiError;
import com.anchor.app.msg.exceptions.ChannelServiceException;
import com.anchor.app.msg.exceptions.MsgServiceException;
import com.anchor.app.msg.model.Message;
import com.anchor.app.msg.model.UserMsgView;
import com.anchor.app.msg.service.MsgService;
import com.anchor.app.msg.vo.MessageActionVo;
import com.anchor.app.msg.vo.MessageVo;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.service.IAuthenticationFacade;
import com.anchor.app.util.EnvProp;
import com.anchor.app.util.HelperBean;

@RestController
@RequestMapping(value= "/api/msg")
public class MessageController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IAuthenticationFacade authfacade;
	
	@Autowired
	private MsgService msgService;

	@Autowired
	private EnvProp  env;
	
	@Autowired
	private HelperBean helperBean;
	
	@PostMapping(value = "/add" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<?> addMsgToChannel(
    		@RequestPart(value = "attachment", required = false) List<MultipartFile> attachment,
    		@RequestPart(value = "request", required = false) String requestText) 
	{
		logger.info("Msg Add Started...... ");
		ResponseEntity<?> response = null;
		MessageActionVo request = null;
	    
		Date startT = new Date();
        try {
        
        	if( null == requestText)	
        	{
        		throw new MsgServiceException("Request can not be null");
        	}
        
        	 request = helperBean.stringToMsgActionObject(requestText);
        	 if(!attachment.isEmpty())
        	 {
        		 request.setAttachments(attachment);
        	 }
        	
        	if(env.isAuthEnabled())
        	{
        		User user = authfacade.getApiAuthenticationDetails();
            	if( null == user)
            	{
            		throw new MsgServiceException("Invalid authenticated user");
            	}
            	request.setUserID(user.getUid());
        	}
        	else {
        		if( null == request.getUserID())
        		{
        			throw new MsgServiceException("Invalid request UserID");
        		}
        	}
        	
        	Message result = msgService.addMessage(request);
        	//String result = "Ok";
        	response =  new ResponseEntity<>(result, HttpStatus.OK);
        	logger.info("Msg Add Completed...... ");
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
        				"Unable to add message to channel" );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
        	
        	
        }
        
        return response;	
        
    }
	

	@RequestMapping(value = "/{chnlID}/{msgID}/reply", method = RequestMethod.POST)
    public ResponseEntity<?> replayMsg( 
    		@PathVariable ("chnlID") String chnlID,
    		@PathVariable ("msgID") String msgID,
    		@RequestBody MessageActionVo request) 
	{
		ResponseEntity<?> response = null;
	    
		try {
        		
        	if (( null == request) || (null == chnlID) || (null == msgID))
        	{
        		throw new MsgServiceException("Request can not be null");
        	}
        	
        	
        	User user = authfacade.getApiAuthenticationDetails();
        	if( null == user)
        	{
        		throw new AuthServiceException("Invalid authenticated user");
        	}
        	request.setUserID(user.getUid());
        	request.setChnlID(chnlID);
        	request.setParentMsgID(msgID);
        	
        	Message result = msgService.replyMessage(request);
        	
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
        				"Unable to add message to channel" );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
        	
        	
        }
        
        return response;	
        
    }

	
	@RequestMapping(value = "user/{userId}/{channelId}/list", method = RequestMethod.GET )
    public ResponseEntity<?> GetMessagesForChannel(
    		@PathVariable ("userId") String userID,
    		@PathVariable ("channelId") String channelID,
    		@RequestParam(value = "page" ) int page, 
    		@RequestParam(value = "size") int size,
    		@RequestParam(value = "sort" ,defaultValue = "createdOn.desc") List<String> sort)  
	{
		logger.info("get ChMsg for User:"+userID+" , chnl:"+channelID+" , Page:"+page);
		ResponseEntity<?> response = null;
		try {
    		if( null == channelID)
    		{
    		 throw new MsgServiceException("Invalid Request");	
    		}
        	
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
    		
    		 List<Order> orders = new ArrayList<Order>();
    		 if (sort.size() > 0) 
    		 {
    		        // will sort more than 2 fields
    		        // sortOrder="field, direction"
    		        for (String sortOrder : sort) {
    		          String[] _sort = sortOrder.split(Pattern.quote("."));
    		          String sortField = _sort[0];
    		      	  Sort.Direction direction = Sort.Direction.fromString(_sort[1]);
    		          orders.add(new Order(direction, sortField));
    		        }
    		      }
        	//Sort sort = Sort.by(Sort.Direction.DESC, "createdOn");

        	Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        	
        	Page<UserMsgView>  msgResposne = msgService.getMessageForChannel(userID, channelID, pageable);
    	    
        	response =  new ResponseEntity<>(msgResposne, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				"Unable to fetch user message for channel" );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	
        	
        	
        }
		
		return response;
	}


	

	@Deprecated
	@RequestMapping(value = "/channel/{channelId}/list", method = RequestMethod.GET )
    public ResponseEntity<?> getUserMessagesForChannel(
    		@PathVariable ("channelId") String channelID,
    		@Param(value = "page") int page, 
			@Param(value = "size") int size )  
	{
		ResponseEntity<?> response = null;
		MessageActionVo request = null;
	    try {
    		if( null == channelID)
    		{
    		 throw new MsgServiceException("Invalid Request");	
    		}
        	
    		
        	User user = authfacade.getApiAuthenticationDetails();
        	if( null == user)
        	{
        		throw new MsgServiceException("Invalid authenticated user");
        	}
        	request = new MessageActionVo();
        	request.setUserID(user.getUid());
        	request.setChnlID(channelID);
        	
        	Sort sort = Sort.by(Sort.Direction.ASC, "msgID");

        	Pageable pageable = PageRequest.of(page, size, sort);
        	
        	Page<MessageVo> msgResposne = msgService.getChannelMessageForUser(request, pageable);
    	    
        	response =  new ResponseEntity<>(msgResposne, HttpStatus.OK);
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
        				"Unable to add message to channel" );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
        	
        	
        }
		
		return response;
	}
	

	@PostMapping("/user/{userID}/{chnlID}/{msgID}/edit")
	//@RequestMapping(value = "/user/{userID}/{chnlID}/{msgID}/edit", method = RequestMethod.POST)
	public ResponseEntity<?> UpdateMsgAttribute(
    		@PathVariable (name = "userID") String userID,
    		@PathVariable ( name = "chnlID", required = true ) String chnlID,
    		@PathVariable (name = "msgID",  required = false, value = "" ) String msgID,
    		@RequestBody MessageActionVo request) 
	{
		ResponseEntity<?> response = null;
	    
		try {
        		
        	if (( null == request) || (null == chnlID))
        	{
        		throw new MsgServiceException("Request can not be null");
        	}
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
        	
        	request.setUserID(userID);
        	request.setChnlID(chnlID);
        	if(!msgID.isEmpty())
        	{
        		request.setMsgID(msgID);	
        	}
        	
        	msgService.performMsgAction(request);
        	String result = "Ok";
        	response =  new ResponseEntity<>(result,HttpStatus.OK);
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
        				"Unable to add message to channel" );
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
        	
        	
        }
        
        return response;	
        
    }

	
}
