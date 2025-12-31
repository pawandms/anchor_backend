package com.anchor.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.anchor.app.exception.UserException;
import com.anchor.app.minio.MinioService;
import com.anchor.app.oauth.service.UserService;
import com.anchor.app.oauth.vo.UserVo;
import com.anchor.app.util.EnvProp;


@Controller
@RequestMapping(value= "/api/{version}/public", version = "v1")
public class PublicController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EnvProp env;

	@Autowired
	private UserService userService;
	
	@Autowired
	private MinioService minioService; 
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody UserVo request) 
	{
		ResponseEntity<?> response = null;
	    
        try {
        		
        	if( null == request)
        	{
        		throw new UserException("Request can not be null");
        	}
        	
        	UserVo result = userService.createUser(request);
        	
        	response =  new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	if(!request.getErrors().isEmpty())
        	{
        		request.setErrorCode("SIGNUP_ERROR");
        		response =  new ResponseEntity<>(request, HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        	else {
        		response =  new ResponseEntity<>("User creation Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
        	
        	
        }
        
        return response;	
        
    }
	
	/*
	@RequestMapping(value = "/email/activation/{token}/{uid}", method = RequestMethod.POST)
    public ResponseEntity<?> activationEmail(
    		@PathVariable("token") String token,
    		@PathVariable("uid") String uid
    		) 
	{
		ResponseEntity<?> response = null;
	    
		UserVo result = null;
        try {
        		
        	
        	result = userService.activateUser(token, uid);
        	if(result.isValid())
        	{
        		response =  new ResponseEntity<>(result, HttpStatus.OK);	
        	}
        	else {
        		throw new ValidationException("Invalid Request");
        	}
        	
        	
        }
        catch (Exception e)
        {
        	if(!result.getErrors().isEmpty())
        	{
        		response =  new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        	else {
        		response =  new ResponseEntity<>("User validation Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
        	
        	
        }
        
        return response;	
        
    }
	
	 */

	

}