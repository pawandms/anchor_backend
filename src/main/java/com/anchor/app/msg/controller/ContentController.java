package com.anchor.app.msg.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.exception.AuthServiceException;
import com.anchor.app.exception.ValidationException;
import com.anchor.app.msg.enums.MsgAttachmentType;
import com.anchor.app.msg.enums.PermissionType;
import com.anchor.app.msg.exceptions.MsgServiceException;
import com.anchor.app.msg.service.ContentService;
import com.anchor.app.msg.service.VideoStreamService;
import com.anchor.app.oauth.model.User;
import com.anchor.app.oauth.service.AuthService;
import com.anchor.app.oauth.service.IAuthenticationFacade;
import com.anchor.app.oauth.service.UserService;
import com.anchor.app.oauth.vo.AuthReq;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value= "/content")
public class ContentController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private IAuthenticationFacade authfacade;

	@Autowired
	private VideoStreamService videoStreamService;

	
	@RequestMapping(value = "/attachment/{type}/{contentID:.+}", method = RequestMethod.GET, produces = "video/mp4")
    public Mono<ResponseEntity<?>> getAttachment(
    		//@Valid @NotBlank @PathVariable String msgId,
    		@Valid @NotBlank @PathVariable MsgAttachmentType type,
    		@Valid @NotBlank @PathVariable String contentID,
    		@RequestParam(required = false, name = "cntLength") long cntLength,
    		@RequestParam (required = false, name = "extension")String extension,
    		 @RequestHeader(value = "Range", required = false) String httpRangeList,
    		 @RequestParam String token
    		 ) 
	{
		
		Mono<ResponseEntity<?>> response = null;
	    try {
	    	
	    		  //response =  new ResponseEntity<>(inputStreamResource, HttpStatus.OK);
	    		if(type.equals(MsgAttachmentType.Image))
	    		{
	    			InputStream stream = contentService.getContent(contentID);
	    			InputStreamResource  streamResource = new InputStreamResource(stream);
		        	
	    	    	
	    			response = Mono.just(ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(streamResource));	
	    		}
	    		else if (type.equals(MsgAttachmentType.Video))
	    		{
	    			logger.info("Getting VIDEO ContentID :"+contentID+" With type:"+type.name()+" ,With Range:"+httpRangeList);
	    			
	    			response = Mono.just(videoStreamService.getVideoContent(contentID, extension, cntLength, httpRangeList));
	    			
	    			//response = contentService.getVideoContent(contentID, extension, cntLength, httpRangeList);
	    			
	    			
	    			// Streaming Response Body Test API
	    			//response = contentService.getVideoContentStreaming(msgId, contentID, httpRangeList);
	    		}
		    	
	    }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
        
        return response;	
        
    }
	
	 @GetMapping("/stream/{fileType}/{fileName}")
	    public Mono<ResponseEntity<byte[]>> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList,
	                                                    @PathVariable("fileType") String fileType,
	                                                    @PathVariable("fileName") String fileName) {
			logger.info("Get MONO VIDEO ContentID :"+fileName+" With type:"+fileType+" ,With Range:"+httpRangeList);
	        return Mono.just(videoStreamService.prepareContent(fileName, fileType, httpRangeList));
	    }
	
	
	
	@RequestMapping(value = "/profile/{contentID:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> getProfileImage(
    		@Valid @NotBlank @PathVariable String contentID,
    		@RequestParam String token) 
	{
		ResponseEntity<?> response = null;
	    try {
	    	
	    	Map<String, Object> tokenMap = authService.decodeToken(token);
	    	logger.info(tokenMap.toString());
	    	
	    	if((boolean) tokenMap.get("tokenExpire"))
	    	{
	    		throw new AuthServiceException("Token Expire");
	    	}
	    	
	    	InputStream stream = contentService.getProfileImage(contentID);
	    	if( null != stream)
	    	{
	    		InputStreamResource  streamResource = new InputStreamResource(stream);
	        		response = ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(streamResource);	
	    		
	    	}
	    	else {
	    		throw new ValidationException("Invalid content ID:"+contentID);
	    	}
	    
        }
		catch(AuthServiceException e)
		{
			 response =  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }
	
	
	@PostMapping(value = "user/{userID}/profile/add" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<?> addUpdateUserProfileImage(
    		@PathVariable ("userID") String userID,
    		@RequestPart(value = "attachment", required = true) MultipartFile attachment)
    {
		ResponseEntity<?> response = null;
		try {
			
			User user = authfacade.getApiAuthenticationDetails();
        	if( null == user)
        	{
        		throw new MsgServiceException("Invalid authenticated user");
        	}
        	
        	// perform Authorization
			AuthReq authReq = new AuthReq(user.getUid(),userID, PermissionType.UsrEdit);
			
		 boolean hasPermission = authService.hasPersmission(authReq);
		
		 if(!hasPermission)
		 {
			
			 throw new AuthServiceException("Invalid Perssion");
			 
		 }
			
			userService.addUpdateUserProfileImage(userID, user.getUid(), attachment);	
			response =  new ResponseEntity<>(HttpStatus.OK);
		}
		catch(AuthServiceException e)
		{
			 response =  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		
		return response;
    }
    		



}
