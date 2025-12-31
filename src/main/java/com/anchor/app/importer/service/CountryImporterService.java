package com.anchor.app.importer.service;

import com.anchor.app.exception.ImporterException;

public interface CountryImporterService {
	
	/**
	 * Import Country Data from Temp MongoDB Table import_country
	 * @return
	 * @throws ImporterException
	 */
	public int importCountryData() throws ImporterException;

}
