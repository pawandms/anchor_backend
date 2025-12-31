package com.anchor.app.media.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anchor.app.media.service.CountryService;
import com.anchor.app.model.Country;
import com.anchor.app.model.Genre;

@RestController
@RequestMapping(value= "/api/country")
public class CountryController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private CountryService countryService;
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<?> getAllGenre() 
	{
		ResponseEntity<?> response = null;
	    try {
      
        	List<Country> countryList =   countryService.getAllCountry();
    		
        	  response =  new ResponseEntity<>(countryList, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	logger.error(e.getMessage(), e);
        	response =  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;	
        
    }
}
