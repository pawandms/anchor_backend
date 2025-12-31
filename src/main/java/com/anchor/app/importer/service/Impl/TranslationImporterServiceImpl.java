package com.anchor.app.importer.service.Impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.anchor.app.exception.ImporterException;
import com.anchor.app.importer.model.MovieTranslationImport;
import com.anchor.app.importer.service.TranslationImporterService;
import com.anchor.app.model.Company;
import com.anchor.app.util.EnvProp;

@Service
public class TranslationImporterServiceImpl implements TranslationImporterService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EnvProp env;

	
	@Override
	@Async
	public MovieTranslationImport getMovieTranslationData(String id) throws ImporterException {
		MovieTranslationImport mt =null;
		 try {
			 final String url = env.getMovieDB_URL()+"/movie/{id}/translations";
			  HttpHeaders headers = new HttpHeaders();
			  headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			  Map<String, String> params = new HashMap<String, String>();
			    params.put("id",id); 
			    
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
		     mt = restTemplate.getForObject(uri, MovieTranslationImport.class);
		     
		 }
		 catch(Exception e)
		 {
			throw new ImporterException("Exception in getTranslation for Movie From MDB with ID :"+id+", Msg:"+e.getMessage(), e); 
		 }
		     
	     return mt;

	}

}
