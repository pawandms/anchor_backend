package com.anchor.app.importer.service;

import com.anchor.app.exception.ImporterException;

public interface MovieCreditImporterService {

	/**
	 * Import MovieCreditData for All Movies available in System
	 * @param localPath
	 * @throws ImporterException
	 */
	public int importMovieCreditData() throws ImporterException;
	
	

}
