package com.anchor.app.media.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anchor.app.enums.MediaGenreType;
import com.anchor.app.enums.MediaOrderType;
import com.anchor.app.enums.MediaType;
import com.anchor.app.exception.MediaServiceException;
import com.anchor.app.media.model.AppUser;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.model.MetaData;
import com.anchor.app.media.repository.MediaRepository;
import com.anchor.app.media.service.AppUserService;
import com.anchor.app.media.service.MediaService;
import com.anchor.app.oauth.enums.UserRoleType;
import com.anchor.app.oauth.model.UserAuth;
import com.anchor.app.oauth.service.UserService;

@RestController
@RequestMapping(value= "/api/media/user")
public class MediaUserController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private MediaRepository mediaRep;

	@Autowired
	private UserService userService;

	@Autowired
	private MediaService mediaService;
	
	
	@GetMapping("{userName}/media")
	public ResponseEntity<?> getMediaByUser(
							@PathVariable ("userName") String userName,
							@Param(value = "page") int page, 
							@Param(value = "size") int size)
	{
		ResponseEntity<?> response = null;
	
		try {
			Sort sort = null;
			sort = Sort.by(Sort.Direction.DESC, "createdOn");
			
			Pageable pageable = PageRequest.of(page, size, sort);
			
			// Get UserAuth Details
			
			UserAuth uauth = userService.getUserAuthDetails(userName);
			boolean adminUserFlag = false;
			if(null != uauth)
			{
				if(uauth.getRoles().contains(UserRoleType.ADMIN_USER.getValue()))
				{
					adminUserFlag = true;
				}
			}
			
			Page<Media> mediaPage = null;
			
			if(adminUserFlag)
			{
				mediaPage = mediaRep.findAll(pageable);
			}
			else {
				// Find for Respective UserID
				mediaPage = mediaRep.findByCreatedBy(userName, pageable);
			}
			
			 Map<String, Object> Pepresponse = new HashMap<>();
		   	 Pepresponse.put("currentPage", mediaPage.getNumber());
		   	 Pepresponse.put("currentItems", mediaPage.getNumberOfElements());
		   	 Pepresponse.put("totalItems", mediaPage.getTotalElements());
		   	 Pepresponse.put("totalPages", mediaPage.getTotalPages());
			 Pepresponse.put("media", mediaPage.getContent());
				
		   	 response =  new ResponseEntity<>(Pepresponse, HttpStatus.OK);
		
		}
		catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
		
		return response;	
	}
	
	@RequestMapping(value = "{userName}/media", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMediaDetails(
    		@PathVariable ("userName") String userName,
			@RequestBody Media media) 
	{
		ResponseEntity<?> response = null;
	    try {
	    	
	    	
	    	
	    	Media result = mediaService.updateMedia(media, userName);
	    		
        	  response =  new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch ( Exception e)
        {
        	response =  new ResponseEntity<>("Media Update Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        	

        }
        
        return response;	
    
	}


}
