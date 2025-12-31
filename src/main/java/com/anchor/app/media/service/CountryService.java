package com.anchor.app.media.service;

import java.util.List;

import com.anchor.app.exception.CountryServiceException;
import com.anchor.app.model.Country;

public interface CountryService {
	
	public List<Country> getAllCountry() throws CountryServiceException;

}
