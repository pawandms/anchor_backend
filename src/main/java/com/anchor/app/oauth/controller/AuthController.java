package com.anchor.app.oauth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anchor.app.exception.UserException;
import com.anchor.app.model.ApiError;
import com.anchor.app.oauth.model.OauthClientDetails;
import com.anchor.app.oauth.service.UserService;
import com.anchor.app.oauth.viewmodel.ClientDetailsVo;
import com.anchor.app.oauth.vo.UserVo;


@RestController
@RequestMapping(value= "/api/{version}/auth", version = "1.0")
public class AuthController {
	
	@Autowired
	private UserService userService;

	/* 
	@RequestMapping(value = "/client", method = RequestMethod.GET)
    public ResponseEntity<?> getAllClients() 
	{
		ResponseEntity<?> response = null;
	    
        try {
        	List<OauthClientDetails> clients = userService.getAllClients();
        	response = new ResponseEntity<>(clients, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        			"Unable to retrieve clients: " + e.getMessage());
        	response = new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
    }
		*/

	@RequestMapping(value = "/client", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthClient(@RequestBody ClientDetailsVo request) 
	{
		ResponseEntity<?> response = null;
	    
        try {
        		
        	if( null == request)
        	{
        		throw new UserException("Request can not be null");
        	}
        	
        	OauthClientDetails result = userService.saveClientDetails(request);
        	
        	response =  new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	if(!request.getErrors().isEmpty())
        	{
        		response =  new ResponseEntity<>(request.getErrors(), HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        	else {
        		response =  new ResponseEntity<>("User creation Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
        	
        	
        }
        
        return response;	
        
    }
	

	
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
        		
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				request.getErrorMessage() , request.getErrors());
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        	else {
        		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
        				"Unable to create User");
        		
        		response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
        	
        	
        }
        
        return response;	
        
    }
	
	



}
