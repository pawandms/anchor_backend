package com.anchor.app.media.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anchor.app.enums.SequenceType;
import com.anchor.app.exception.GenreException;
import com.anchor.app.exception.MediaServiceException;
import com.anchor.app.exception.MetaDataServiceException;
import com.anchor.app.exception.SequencerException;
import com.anchor.app.importer.model.vo.MetaDataVo;
import com.anchor.app.media.model.Media;
import com.anchor.app.media.model.MediaMetaData;
import com.anchor.app.media.model.MetaData;
import com.anchor.app.media.service.MediaService;
import com.anchor.app.media.service.MetaDataService;
import com.anchor.app.model.Genre;
import com.anchor.app.oauth.model.User;
import com.anchor.app.util.HelperBean;

@RestController
@RequestMapping(value= "/api/metadata")
public class MetaDataController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MetaDataService metaDataService;
	
	@Autowired
	private MediaService mediaService;
	/*
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addMetaData(@RequestBody MetaDataVo request) 
	{
		ResponseEntity<?> response = null;
	    try {
	    		
	    	metaDataService.saveMetaData(request);
        	String message = "Ok";
        	  response =  new ResponseEntity<>(message, HttpStatus.OK);
        }
        catch (MetaDataServiceException e)
        {
        	if(!request.getErrorMap().isEmpty())
        	{
        		response =  new ResponseEntity<>(request.getErrorMap(), HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        	else {
        		response =  new ResponseEntity<>("MetaData creation Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);	
        	}

        }
        
        return response;	
    
	}

	*/
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addMediaMetaDataList(@RequestBody MetaData request) 
	{
		ResponseEntity<?> response = null;
	    try {
	    		
	    	mediaService.saveMediaMetaData(request);
        	String message = "Ok";
        	  response =  new ResponseEntity<>(message, HttpStatus.OK);
        }
        catch ( MediaServiceException e)
        {
        	response =  new ResponseEntity<>("MetaData creation Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        	

        }
        
        return response;	
    
	}

	
	@Deprecated
	@RequestMapping(value = "/search", method = RequestMethod.GET )
    public ResponseEntity<?> getDummyUserDetails(@RequestParam("SearchKey") String searchKey,
    		@Param(value = "page") int page, 
			@Param(value = "size") int size )  
	{	ResponseEntity<?> response = null;
		try {
			 
	        	if( searchKey != null )
	        	{
	        		Pageable pageable = PageRequest.of(page, size);
	    			
	        		Page<Media> mediaPage = metaDataService.searchMediabyText(searchKey, pageable);
	        		
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
	


}
