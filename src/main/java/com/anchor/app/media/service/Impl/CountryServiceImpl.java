package com.anchor.app.media.service.Impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.anchor.app.exception.CountryServiceException;
import com.anchor.app.media.service.CountryService;
import com.anchor.app.model.Country;
import com.anchor.app.repository.CountryRepository;

@Service
public class CountryServiceImpl implements CountryService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private CountryRepository countryRep;

	@Override
	public List<Country> getAllCountry() throws CountryServiceException {
		
		return countryRep.findAll();
	}
	
	
	

}
