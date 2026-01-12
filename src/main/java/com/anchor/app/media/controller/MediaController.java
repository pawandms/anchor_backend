package com.anchor.app.media.controller;

import com.anchor.app.exceptions.ValidationException;
import com.anchor.app.media.dto.StreamMediaInfo;
import com.anchor.app.media.service.MediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/{version}/media")
public class MediaController {
    
    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);
    
    @Autowired
    private MediaService mediaService;
    
    /**
     * Get user profile image by user ID
     * 
     * @param userId User ID
     * @return Profile image details with presigned URL
     */
    @GetMapping("/profile/{mediaId:.+}")
    public ResponseEntity<?> getUserProfileImage(@PathVariable String mediaId) {
          ResponseEntity<?> response = null;
          StreamMediaInfo req = null;
        
        try {
            
            req = new StreamMediaInfo();
            req.setMediaId(mediaId);

          mediaService.getUserProfileByMediaId(req);
          	if( req.isValid())
	    	{
	    		InputStreamResource  streamResource = new InputStreamResource(req.getMediaStream());
	        	response = ResponseEntity.ok().contentType(MediaType.valueOf(req.getMediaType().getContentType())).body(streamResource);	
	    		
	    	}
	    	else {
	    		throw new ValidationException("Invalid content ID:"+mediaId);
	    	}
            
        } catch (Exception e) {

                if(!req.getErrors().isEmpty())
            {
               response =  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "message", "Validation failed",
                "errors", req.getErrors()
            ));
            }
            else {
                response =  new ResponseEntity<>("get User Profile Error Msg:"+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);	
            }
            
        }

        return response;
    }
    
    
}
