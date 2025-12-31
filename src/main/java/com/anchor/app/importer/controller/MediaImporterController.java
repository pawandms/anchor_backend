package com.anchor.app.importer.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.anchor.app.enums.MediaType;
import com.anchor.app.importer.model.YouTubeImportRequest;
import com.anchor.app.importer.service.ImportMediaService;
import com.anchor.app.importer.service.YouTubeImporterService;
import com.anchor.app.media.model.Media;

@RestController
@RequestMapping("/api/import")
public class MediaImporterController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private YouTubeImporterService ytiService;

	@Autowired
	private ImportMediaService importMediaService;
	

	
	@RequestMapping(value = "/youtube", method = RequestMethod.POST)
    public ResponseEntity<?> importYouTubeMedia(@RequestBody YouTubeImportRequest yimpReq) 
	{
		ResponseEntity<?> response = null;
	    try {
	    	yimpReq = ytiService.importYouTubeMedia(yimpReq);	
	    	response =  new ResponseEntity<>(yimpReq, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }

	
	@RequestMapping(value = "/youtubeRequest/pending", method = RequestMethod.GET)
    public ResponseEntity<?> processPendingYouTubeImportRequest() 
	{
		ResponseEntity<?> response = null;
	    try {
	    	
	    	int count = ytiService.processYouTubeImportRequest();	
	    	response =  new ResponseEntity<>("YouTube Media Process Count:"+count, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }
	
	@RequestMapping(value = "/youtube/pending", method = RequestMethod.GET)
    public ResponseEntity<?> processPendingYouTubeMedia() 
	{
		ResponseEntity<?> response = null;
	    try {
	    	
	    	int count = ytiService.processImportedYouTubeMedia();	
	    	response =  new ResponseEntity<>("YouTube Media Process Count:"+count, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }
	
	

	@RequestMapping(value = "/media", method = RequestMethod.POST, consumes = "multipart/form-data")
	public ResponseEntity<?> addVideoFile(
			@RequestParam("title") String title,
			@RequestParam("mediaType") MediaType mediaType,
			@RequestParam("createdBy") String createdBy,
			@RequestParam(name = "mediaFile", required = false ) MultipartFile file,
			@RequestParam(name = "mediaFilePath", required = false) String mediaFileServerPath,
			@RequestParam(name = "isServerFile", required = false ) boolean isServerFile
			) throws IOException {
       
    	ResponseEntity<?> response = null;
	    try {
	    	
	    	logger.info("..................Import Media Started with Title :"+title);
	    	Media mediaRequest = importMediaService.importMedia(title, title, mediaType, createdBy, file, mediaFileServerPath,isServerFile);
	    	response =  new ResponseEntity<>(mediaRequest, HttpStatus.OK);
	    	
	    	logger.info("..................Import Media Completed with Title :"+title);
		    
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	

    }
	
	
	@RequestMapping(value = "/media/pending", method = RequestMethod.GET)
    public ResponseEntity<?> processUploadedMediaFile() 
	{
		ResponseEntity<?> response = null;
	    try {
	    	
	    	int count = importMediaService.processImportedMedia();	
	    	response =  new ResponseEntity<>("YouTube Media Process Count:"+count, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }
	



}
