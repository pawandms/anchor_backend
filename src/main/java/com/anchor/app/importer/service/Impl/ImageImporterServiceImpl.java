package com.anchor.app.importer.service.Impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.anchor.app.enums.EntityType;
import com.anchor.app.enums.ImageType;
import com.anchor.app.exception.ImageException;
import com.anchor.app.exception.ImageServiceException;
import com.anchor.app.importer.model.YouTubeMediaImage;
import com.anchor.app.importer.service.ImageImporterService;
import com.anchor.app.media.model.MediaImage;
import com.anchor.app.service.ImageService;
import com.anchor.app.util.EnvProp;

@Service
public class ImageImporterServiceImpl implements ImageImporterService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;

	@Autowired
	private ImageService imgService;

	
	@Override
	@Async
	public InputStream getImageStreamFromMovieDB(String id) {
		InputStream input =null;
		try {
			final String url = env.getMovieDB_Image_URL()+"{id}";
			  HttpHeaders headers = new HttpHeaders();
			  headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			  Map<String, String> params = new HashMap<String, String>();
			    params.put("id", id); 
			    
			  URI uri = UriComponentsBuilder.fromUriString(url)
				        .buildAndExpand(params)
				        .toUri();
				uri = UriComponentsBuilder
				        .fromUri(uri)
				        .queryParam("api_key", env.getMovieDB_apiKey())
				        .build()
				        .toUri();
			  
		    RestTemplate restTemplate = new RestTemplate();
		    
		     HttpEntity<String> entity = new HttpEntity<String>(headers);
		    
		     
		     ResponseEntity<Resource> responseEntity  = restTemplate.exchange(
		    	        uri, 
		    	        HttpMethod.GET, 
		    	        entity, 
		    	        Resource.class);
		     
		     input = responseEntity.getBody().getInputStream();
		    
			
		}
		catch(Exception e)
		{
			// Swolling exception as if Image is not available then mark it as null
			//throw new ImporterException("Exception in getImage From MDB with ID :"+id+", Msg:"+e.getMessage(), e);
		}
		 
	    return input;

	}


	@Override
	@Async
	public InputStream getImageStreamFromUrl(String url) {
		InputStream input =null;
		try {
			  HttpHeaders headers = new HttpHeaders();
			  //headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			  Map<String, String> params = new HashMap<String, String>();
			  //  params.put("id", id); 
			    
			  URI uri = UriComponentsBuilder.fromUriString(url)
				       .buildAndExpand(params)
				        .toUri();
				
			  /*
			  uri = UriComponentsBuilder
				        .fromUri(uri)
				        .queryParam("api_key", env.getMovieDB_apiKey())
				        .build()
				        .toUri();
			  */
		    RestTemplate restTemplate = new RestTemplate();
		    
		     HttpEntity<String> entity = new HttpEntity<String>(headers);
		    
		     
		     ResponseEntity<Resource> responseEntity  = restTemplate.exchange(
		    	        uri, 
		    	        HttpMethod.GET, 
		    	        entity, 
		    	        Resource.class);
		     
		     input = responseEntity.getBody().getInputStream();
		    
			
		}
		catch(Exception e)
		{
			// Swolling exception as if Image is not available then mark it as null
			//throw new ImporterException("Exception in getImage From MDB with ID :"+id+", Msg:"+e.getMessage(), e);
		}
		 
	    return input;
	}


	@Override
	public MediaImage getMediaImageFromUrl(EntityType entityType, ImageType imageType, String entityId, String name,String url) {
			
		MediaImage mi = null;
		try {
			
			InputStream stream = getImageStreamFromUrl(url);
			if( null != stream)
			{
				mi = imgService.addImage(entityType, imageType, entityId, name, stream);
				
			}
		}
		catch(Exception e)
		{
		
		}
		
		return mi;
	}


	@Override
	public MediaImage getMediaImageFromUrl(EntityType entityType, ImageType imageType, String entityId, String name, Path imgPath) 
	{
		MediaImage mi = null;
		
	
		try {
			 if(!Files.exists(imgPath, LinkOption.NOFOLLOW_LINKS))
				{
					 throw new ImageException("Unable to load Image from  Path"+imgPath);
						
				}
			 
			 InputStream stream = Files.newInputStream(imgPath);
				if( null != stream)
				{
					mi = imgService.addImage(entityType, imageType, entityId, name, stream);
					
				}
			
		}
		catch(ImageServiceException | IOException | ImageException e )
		{
			// Swolow exception if unable to parase Image file 
		}
		
		
		return mi;
	}


	@Override
	public List<MediaImage> getMediaImageFromList(EntityType entityType, ImageType imageType, String entityId,
			List<YouTubeMediaImage> thumbnails) {

		List<MediaImage> miList = new ArrayList<>();
		
		if(!thumbnails.isEmpty())
		{
			String imageName = "thumbnail.jpg";
			thumbnails.stream().forEach(ytmi -> {
				try {
			
					InputStream stream = getImageStreamFromUrl(ytmi.getUrl());
					if( null != stream)
					{
						MediaImage mi = imgService.addImage(entityType, imageType, entityId, imageName,ytmi.getWidth(), ytmi.getHeight(),  stream);
						
						if( null != mi)
						{
							miList.add(mi);
						}
					}

				
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Image error Error for YouTube Media :"+entityId, e);
					throw new RuntimeException("Import Image Errro");
				}
			});

		}
		
		return miList;
	}
	
	
	
}
