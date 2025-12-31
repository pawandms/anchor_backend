package com.anchor.app.media.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ImageType;
import com.anchor.app.enums.MediaGenreType;
import com.anchor.app.enums.MediaOrderType;
import com.anchor.app.enums.MediaType;
import com.anchor.app.exception.ImageServiceException;
import com.anchor.app.exception.MediaServiceException;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.media.model.MediaMetaData;
import com.anchor.app.media.repository.MediaRepository;
import com.anchor.app.media.service.MediaService;
import com.anchor.app.media.service.MetaDataService;
import com.anchor.app.oauth.enums.UserRoleType;
import com.anchor.app.oauth.model.UserAuth;
import com.anchor.app.oauth.service.UserService;
import com.anchor.app.util.HelperBean;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(value= "/api/media")
public class MediaController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MediaService mediaService;

	@Autowired
	private MediaRepository mediaRep;

	@Autowired
	private MetaDataService metaDataService;

	@Autowired
	private UserService userService;

	@Autowired
	private HelperBean helper;
	
	@Autowired 
	private HttpServletRequest request;

	@GetMapping("/{mediaType}/{genre}/{mediaOrder}")
	public ResponseEntity<?> getMediaByCategory(
							@PathVariable ("mediaType") MediaType mediaType,
							@PathVariable ("genre") MediaGenreType genreType,
							@PathVariable ("mediaOrder") MediaOrderType mediaOrder,
							@Param(value = "page") int page, 
							@Param(value = "size") int size)
	{
		ResponseEntity<?> response = null;
	
		try {
			Sort sort = null;
			if( null != mediaOrder)
			{
				if(mediaOrder.equals(MediaOrderType.LATEST))
				{
					sort = Sort.by(Sort.Direction.DESC, "createdOn");
					
				}
				else if(mediaOrder.equals(MediaOrderType.POPULAR))
				{
					sort = Sort.by(Sort.Direction.DESC, "view_count");
					
				}
				else if(mediaOrder.equals(MediaOrderType.TRENDING))
				{
					sort = Sort.by(Sort.Direction.DESC, "view_count");
					
				}
			}
			
			
			Pageable pageable = PageRequest.of(page, size, sort);
			
			Page<Media> mediaPage = null;
			
			if(genreType == MediaGenreType.ALL)
			{
				mediaPage = mediaRep.findByMediaType(mediaType, pageable);
				
			}
			else {
				mediaPage = mediaRep.findByMediaTypeAndGenerList(mediaType, genreType, pageable);	
			}
			
			 Map<String, Object> Pepresponse = new HashMap<>();
		   	 Pepresponse.put("currentPage", mediaPage.getNumber());
		   	 Pepresponse.put("currentItems", mediaPage.getNumberOfElements());
		   	 Pepresponse.put("totalItems", mediaPage.getTotalElements());
		   	 Pepresponse.put("totalPages", mediaPage.getTotalPages());
			 Pepresponse.put("media", mediaPage.getContent());
				
		   	   	
			
			/*
			
			Page<Media> mediaPage;
			
			
			mediaPage = mediaService.getPage(mediaType, genreType, mediaOrder, pageable);
			mediaList = mediaPage.getContent();
		    	
			
	   	 Map<String, Object> Pepresponse = new HashMap<>();
	   	 Pepresponse.put("media", mediaList);
	   	 Pepresponse.put("currentPage", mediaPage.getNumber());
	   	 Pepresponse.put("totalItems", mediaPage.getTotalElements());
	   	 Pepresponse.put("totalPages", mediaPage.getTotalPages());
	   	
	   	*/
	   	  response =  new ResponseEntity<>(Pepresponse, HttpStatus.OK);
			
			 
		
		}
		catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
		
		return response;	
	}

	
	@GetMapping("/{mediaId}")
	public ResponseEntity<?> getMediaDetails(
							@PathVariable ("mediaId") String mediaId)
	{
		ResponseEntity<?> response = null;
	
		try {
	
			Media media = mediaService.getMedia(mediaId);
			
			Map<String, Object> responseMap = new HashMap<>();
		   	responseMap.put("media", media);
			response =  new ResponseEntity<>(responseMap, HttpStatus.OK);
			 
		
		}
		catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
		
		return response;	
	}

	
	
	@RequestMapping(value = "/getMediaTypeByUser", method = RequestMethod.GET)
    public ResponseEntity<?> getAllGenre() 
	{
		ResponseEntity<?> response = null;
	    try {
      
        	List<Media> mediaList =   mediaService.getMediaAggregationByCreatedUser();
    		
        	  response =  new ResponseEntity<>(mediaList, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }
	
	
	@PreAuthorize("hasAuthority('ADMIN_USER')")
	@RequestMapping(value = "/user/{userName}/media", method = RequestMethod.POST)
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

	@PreAuthorize("hasAuthority('ADMIN_USER')")
	@RequestMapping(value = "delete/{mediaID}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteMedia(
    		@PathVariable ("mediaID") String mediaID) 
	{
		ResponseEntity<?> response = null;
	    try {
	    	mediaService.deleteMedia(mediaID);
	    	String result = "Media Deleted with ID:"+mediaID;	
        	 response =  new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch ( Exception e)
        {
        	response =  new ResponseEntity<>("Media Delete Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        	

        }
        
        return response;	
    
	}


	
	@RequestMapping(value = "/search", method = RequestMethod.GET )
    public ResponseEntity<?> searchMedia(@RequestParam("SearchKey") String searchKey,
    		@Param(value = "page") int page, 
			@Param(value = "size") int size )  
	{
		ResponseEntity<?> response = null;
		try {
			 
	        	if( searchKey != null )
	        	{
	        		Sort sort = Sort.by(Sort.Direction.ASC, "score");

	        		Pageable pageable = PageRequest.of(page, size, sort);
	    			
	        		//Page<Media> mediaPage = metaDataService.searchMediabyText(searchKey, pageable);
	        		Page<Media> mediaPage = mediaService.getMediaBySearchString(searchKey, pageable);
	        		
	        		Map<String, Object> Pepresponse = new HashMap<>();
	    		   	 
	        		if( null != mediaPage)
	        		{
	        			 Pepresponse.put("currentPage", mediaPage.getNumber());
		    		   	 Pepresponse.put("currentItems", mediaPage.getNumberOfElements());
		    		   	 Pepresponse.put("totalItems", mediaPage.getTotalElements());
		    		   	 Pepresponse.put("totalPages", mediaPage.getTotalPages());
		    			 Pepresponse.put("media", mediaPage.getContent());
		    				
	        		}
	        		else {
	        			 Pepresponse.put("currentPage", 0);
		    		   	 Pepresponse.put("currentItems", 0);
		    		   	 Pepresponse.put("totalItems", 0);
		    		   	 Pepresponse.put("totalPages", 0);
		    			 Pepresponse.put("media", new ArrayList<Media>());
		    			
	        		}
	        			
	    		
	        		
	    			 response =  new ResponseEntity<>(Pepresponse, HttpStatus.OK);
	        	}
		}
		catch (Exception e)
		{
			response =  new ResponseEntity<>("MetaData search Error:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
       
		return response;
    }
	

	@RequestMapping(value = "/related/{mediaId}", method = RequestMethod.GET )
    public ResponseEntity<?> getRelatedMedia(
    		@PathVariable ("mediaId") String mediaId,
    		@Param(value = "page") int page, 
			@Param(value = "size") int size )  
	{
		ResponseEntity<?> response = null;
		try {
			
			String searchKey = null;
			if( null != mediaId)
			{
				Media media = mediaService.getMedia(mediaId);
				
				
				if ( null != media)
				{
					if(!media.getMetaData().getMetaDataList().isEmpty())
					{
						searchKey = String.join(" ", media.getMetaData().getMetaDataList()); 
					}
					else {
						StringBuilder sb = new StringBuilder();
						if (!helper.isEmptyString(media.getTitle()))
						{
							sb.append(media.getTitle());	
						}
						
						if (!helper.isEmptyString(media.getOverview()))
						{
							sb.append(" ");
							sb.append(media.getOverview());	
						}
					
						searchKey = sb.toString();
						
					}
					
				}
				
			}
			 
	        	if( searchKey != null )
	        	{
	        		Sort sort = Sort.by(Sort.Direction.ASC, "score");

	        		Pageable pageable = PageRequest.of(page, size, sort);
	    			
	        		Page<Media> mediaPage = mediaService.getMediaBySearchString(searchKey, pageable);
	        		//Page<Media> mediaPage = mediaService.getRelatedMedia(mediaId, searchKey, pageable);
	        		
	        		Map<String, Object> Pepresponse = new HashMap<>();
	        		
	        		
	    		   	 
	        		if(( null != mediaPage) && (mediaPage.hasContent()))
	        		{
	        			List<Media> mediaList = new ArrayList<>();
	        			
	        			mediaList.addAll(mediaPage.getContent());
	        			int actualSize = mediaList.size();
	        			
	        			Predicate<Media> condition = media -> media.getId().equalsIgnoreCase(mediaId);
	        			mediaList.removeIf(condition);	
	        			
	        			int newSize = mediaList.size();
	        			int totoalItems = (int) mediaPage.getTotalElements();
	        			if( newSize < actualSize)
	        			{
	        				totoalItems = totoalItems - 1;	
	        			}
	        			 Pepresponse.put("currentPage", mediaPage.getNumber());
		    		   	 Pepresponse.put("currentItems", newSize);
		    		   	 Pepresponse.put("totalItems", totoalItems);
		    		   	 Pepresponse.put("totalPages", mediaPage.getTotalPages());
		    			 Pepresponse.put("media", mediaList);
	 	    		
	        		}
	        		else {
	        			 Pepresponse.put("currentPage", 0);
		    		   	 Pepresponse.put("currentItems", 0);
		    		   	 Pepresponse.put("totalItems", 0);
		    		   	 Pepresponse.put("totalPages", 0);
		    			 Pepresponse.put("media", new ArrayList<Media>());
		    			
	        		}
	        			
	    		
	        		
	    			 response =  new ResponseEntity<>(Pepresponse, HttpStatus.OK);
	        	}
		}
		catch (Exception e)
		{
			response =  new ResponseEntity<>("MetaData search Error:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
       
		return response;
    }
	

	
	@GetMapping("/user/{userName}")
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


	/**
	 * Update Image Against Media
	 * @param image
	 * @param entityType
	 * @param entityID
	 * @param imageType
	 * @return
	 */
	
	@RequestMapping(value = "image/add", method = RequestMethod.POST)
    public ResponseEntity<?> addImage(@RequestParam("image") MultipartFile image,
    		@Valid @NotNull @RequestParam("entityType") EntityType entityType,
    		@Valid @NotNull @RequestParam("entityId") String entityID,
    		@Valid @NotNull @RequestParam("imageType") ImageType imageType) 
	{
		ResponseEntity<?> response = null;
		
		try {
			MediaImage img = mediaService.addMediaImage(entityType, imageType, entityID, image);	
			response =  new ResponseEntity<>(img, HttpStatus.OK);	
		}
		catch(  MediaServiceException e)
		{
			logger.error(e.getMessage(), e);
			response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
        return response;	
        
    }
	

	@RequestMapping(value = "/update/contentid", method = RequestMethod.POST)
    public ResponseEntity<?> updateMediaContentID() 
	{
		ResponseEntity<?> response = null;
	    try {
	    	
	    		    	
	    	int result = mediaService.updateMediaContentID();
	    	String msg = "ContentID updated for Total Media:"+result;	
        	  response =  new ResponseEntity<>(msg, HttpStatus.OK);
        }
        catch ( Exception e)
        {
        	response =  new ResponseEntity<>("Media Update Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        	

        }
        
        return response;	
    
	}

/**
 * Update ViewCount of Respective MediaID
 * @param mediaId
 * @return
 */
	
	@RequestMapping(value = "/update/viewcount/{appUserID}/{mediaId}", method = RequestMethod.POST)
    public ResponseEntity<?> updateViewCount(
    		@PathVariable ("appUserID") String apiUserID,
    		@PathVariable ("mediaId") String mediaId) 
	{
		ResponseEntity<?> response = null;
	    try {
	    	
	    		    	
	    	 mediaService.updateMediaViewCount(apiUserID,mediaId);
	    	String msg = "ViewCount updated for Media:"+mediaId;	
        	  response =  new ResponseEntity<>(msg, HttpStatus.OK);
        }
        catch ( Exception e)
        {
        	response =  new ResponseEntity<>("Media Update Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        	

        }
        
        return response;	
    
	}



	
}
